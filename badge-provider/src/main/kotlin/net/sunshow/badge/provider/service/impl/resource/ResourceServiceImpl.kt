package net.sunshow.badge.provider.service.impl.resource

import net.sunshow.badge.domain.service.resource.ResourceService
import net.sunshow.badge.provider.component.key.RedisKeyManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.stereotype.Service

@Service
class ResourceServiceImpl : ResourceService {

    @Autowired
    private lateinit var redisKeyManager: RedisKeyManager

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    @Value("classpath:scripts/create_unread_resource.lua")
    private lateinit var createUnreadResourceScriptFile: Resource

    private val createUnreadResourceScript: RedisScript<Void> by lazy {
        RedisScript.of(createUnreadResourceScriptFile, Void::class.java)
    }

    @Value("classpath:scripts/delete_unread_resource.lua")
    private lateinit var deleteUnreadResourceScriptFile: Resource

    private val deleteUnreadResourceScript: RedisScript<Void> by lazy {
        RedisScript.of(deleteUnreadResourceScriptFile, Void::class.java)
    }

    private val hashOperations: HashOperations<String, String, String> by lazy {
        stringRedisTemplate.opsForHash()
    }

    private val setOperations: SetOperations<String, String> by lazy {
        stringRedisTemplate.opsForSet()
    }

    override fun createUnreadResource(store: String, path: String, resource: String) {
        // 1. add resource to node set
        // 2. maintain unread count from bottom to top
        // do this by lua script

        // store key
        val storeKey = redisKeyManager.getStoreKey(store)

        val keyList = listOf(storeKey, redisKeyManager.getNodeKey(store, path))

        val argList = mutableListOf(resource)
        var reducePath = path
        do {
            argList.add(reducePath)
            reducePath = reducePath.substringBeforeLast("/")
        } while (reducePath.isNotEmpty())
        argList.add("/")

        stringRedisTemplate.execute(createUnreadResourceScript, keyList, *argList.toTypedArray())
    }

    override fun deleteUnreadResource(store: String, path: String, resource: String) {
        val storeKey = redisKeyManager.getStoreKey(store)

        val keyList = listOf(storeKey, redisKeyManager.getNodeKey(store, path))

        val argList = mutableListOf(resource)
        var reducePath = path
        do {
            argList.add(reducePath)
            reducePath = reducePath.substringBeforeLast("/")
        } while (reducePath.isNotEmpty())
        argList.add("/")

        stringRedisTemplate.execute(deleteUnreadResourceScript, keyList, *argList.toTypedArray())
    }

    override fun countUnreadResource(store: String, path: String): Int {
        val storeKey = redisKeyManager.getStoreKey(store)

        return hashOperations.get(storeKey, path)?.toIntOrNull() ?: 0
    }
}