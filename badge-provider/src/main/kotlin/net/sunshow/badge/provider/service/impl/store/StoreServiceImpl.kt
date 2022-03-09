package net.sunshow.badge.provider.service.impl.store

import net.sunshow.badge.domain.service.store.StoreService
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
class StoreServiceImpl : StoreService {

    @Autowired
    private lateinit var redisKeyManager: RedisKeyManager

    @Autowired
    private lateinit var stringRedisTemplate: StringRedisTemplate

    private val hashOperations: HashOperations<String, String, String> by lazy {
        stringRedisTemplate.opsForHash()
    }

    private val setOperations: SetOperations<String, String> by lazy {
        stringRedisTemplate.opsForSet()
    }

    @Value("classpath:scripts/create_store.lua")
    private lateinit var createStoreScriptFile: Resource

    private val createStoreScript: RedisScript<Long> by lazy {
        RedisScript.of(createStoreScriptFile, Long::class.java)
    }

    @Value("classpath:scripts/delete_store.lua")
    private lateinit var deleteStoreScriptFile: Resource

    private val deleteStoreScript: RedisScript<Long> by lazy {
        RedisScript.of(deleteStoreScriptFile, Long::class.java)
    }

    override fun createStoreByName(name: String) {
        val storeKey = redisKeyManager.getStoreKey(name)
        val manageKey = redisKeyManager.getAllStoreKey()

        val keyList = listOf(storeKey, manageKey)

        stringRedisTemplate.execute(createStoreScript, keyList, name)
    }

    override fun deleteStoreByName(name: String) {
        val storeKey = redisKeyManager.getStoreKey(name)
        val allStoreKey = redisKeyManager.getAllStoreKey()

        val keyList = mutableListOf(storeKey, allStoreKey)

        // get all path
        val entries = hashOperations.entries(storeKey)
        entries.keys.forEach {
            keyList.add(redisKeyManager.getNodeKey(name, it))
        }

        val result = stringRedisTemplate.execute(deleteStoreScript, keyList, name)
        if (result < 0) {
            throw RuntimeException("Delete store error: $result")
        }
    }
}