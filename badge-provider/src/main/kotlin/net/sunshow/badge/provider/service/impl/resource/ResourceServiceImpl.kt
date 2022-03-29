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

    private val createUnreadResourceScript: RedisScript<Long> by lazy {
        RedisScript.of(createUnreadResourceScriptFile, Long::class.java)
    }

    @Value("classpath:scripts/create_unread_resource_batch.lua")
    private lateinit var batchCreateUnreadResourceScriptFile: Resource

    private val batchCreateUnreadResourceScript: RedisScript<Long> by lazy {
        RedisScript.of(batchCreateUnreadResourceScriptFile, Long::class.java)
    }


    @Value("classpath:scripts/delete_unread_resource.lua")
    private lateinit var deleteUnreadResourceScriptFile: Resource

    private val deleteUnreadResourceScript: RedisScript<Long> by lazy {
        RedisScript.of(deleteUnreadResourceScriptFile, Long::class.java)
    }

    @Value("classpath:scripts/delete_all_unread_resource.lua")
    private lateinit var deleteAllUnreadResourceScriptFile: Resource

    private val deleteAllUnreadResourceScript: RedisScript<Long> by lazy {
        RedisScript.of(deleteAllUnreadResourceScriptFile, Long::class.java)
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

        val result = stringRedisTemplate.execute(createUnreadResourceScript, keyList, *argList.toTypedArray())
        if (result < 0) {
            throw RuntimeException("Create unread resource error: $result")
        }
    }

    override fun batchCreateUnreadResource(store: String, path: String, vararg resources: String) {
        // 1. add resource to node set
        // 2. maintain unread count from bottom to top
        // do this by lua script

        // store key
        val storeKey = redisKeyManager.getStoreKey(store)

        val keyList = listOf(storeKey, redisKeyManager.getNodeKey(store, path))

        val argList = mutableListOf<String>()

        var reducePath = path
        do {
            argList.add(reducePath)
            reducePath = reducePath.substringBeforeLast("/")
        } while (reducePath.isNotEmpty())
        argList.add("/")

        // insert path args count to zero index item
        argList.add(0, argList.size.toString())

        // then append all resources after
        argList.addAll(resources)

        val result =
            stringRedisTemplate.execute(batchCreateUnreadResourceScript, keyList, *argList.toTypedArray())
        if (result < 0) {
            throw RuntimeException("Create unread resource error: $result")
        }
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

        val result = stringRedisTemplate.execute(deleteUnreadResourceScript, keyList, *argList.toTypedArray())
        if (result < 0) {
            throw RuntimeException("Delete unread resource error: $result")
        }
    }

    override fun deleteAllUnreadResource(store: String, path: String) {
        val storeKey = redisKeyManager.getStoreKey(store)

        val keyList = listOf(storeKey, redisKeyManager.getNodeKey(store, path))

        val argList = mutableListOf<String>()
        var reducePath = path
        do {
            argList.add(reducePath)
            reducePath = reducePath.substringBeforeLast("/")
        } while (reducePath.isNotEmpty())
        argList.add("/")

        val result = stringRedisTemplate.execute(deleteAllUnreadResourceScript, keyList, *argList.toTypedArray())
        if (result < 0) {
            throw RuntimeException("Delete all unread resource error: $result")
        }
    }

    override fun countUnreadResource(store: String, path: String): Int {
        val storeKey = redisKeyManager.getStoreKey(store)

        return hashOperations.get(storeKey, path)?.toIntOrNull() ?: 0
    }

    override fun batchCountUnreadResource(store: String, vararg paths: String): Map<String, Int> {
        val storeKey = redisKeyManager.getStoreKey(store)
        val hashKeyList = paths.toList()
        val hashValueList = hashOperations.multiGet(storeKey, hashKeyList)

        val result = mutableMapOf<String, Int>()
        hashKeyList.forEachIndexed { index, key ->
            result[key] = hashValueList[index].toIntOrNull() ?: 0
        }

        return result
    }
}