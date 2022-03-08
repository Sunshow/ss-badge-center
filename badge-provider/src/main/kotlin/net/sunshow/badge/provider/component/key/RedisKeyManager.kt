package net.sunshow.badge.provider.component.key

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RedisKeyManager {

    @Value("\${deploy.redis-key-prefix:}")
    lateinit var redisKeyPrefix: String

    fun getStoreKey(key: String): String {
        return "${redisKeyPrefix}store_$key"
    }

}