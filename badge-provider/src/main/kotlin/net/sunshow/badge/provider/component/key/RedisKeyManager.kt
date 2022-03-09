package net.sunshow.badge.provider.component.key

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class RedisKeyManager {

    @Value("\${deploy.redis-key-prefix:}")
    lateinit var redisKeyPrefix: String

    fun getAllStoreKey(): String {
        return "${redisKeyPrefix}store"
    }

    fun getStoreKey(name: String): String {
        return "${redisKeyPrefix}store_$name"
    }

    fun getNodeKey(store: String, path: String): String {
        return "${redisKeyPrefix}node_${store}_$path"
    }
}