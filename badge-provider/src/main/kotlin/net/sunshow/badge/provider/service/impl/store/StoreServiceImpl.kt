package net.sunshow.badge.provider.service.impl.store

import net.sunshow.badge.domain.service.store.StoreService
import net.sunshow.badge.provider.component.key.RedisKeyManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.SetOperations
import org.springframework.data.redis.core.StringRedisTemplate
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

    override fun createStoreByName(name: String) {
        // store as a hash
        val storeKey = redisKeyManager.getStoreKey(name)
        if (stringRedisTemplate.hasKey(storeKey)) {
            throw RuntimeException("Store already exists")
        }
        hashOperations.put(storeKey, "/", "0")
    }

}