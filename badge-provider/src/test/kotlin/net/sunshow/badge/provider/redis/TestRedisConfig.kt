package net.sunshow.badge.provider.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript

@Configuration
class TestRedisConfig {

    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration("127.0.0.1", 6379)

        config.database = 0
        config.password = RedisPassword.of("")

        return LettuceConnectionFactory(config)
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        return StringRedisTemplate(redisConnectionFactory())
    }

    @Bean
    fun testScript(): RedisScript<String> {
        return RedisScript.of(ClassPathResource("scripts/test.lua"), String::class.java)
    }

    @Bean
    fun testConcurrencyScript(): RedisScript<List<*>> {
        return RedisScript.of(ClassPathResource("scripts/test_concurrency.lua"), List::class.java)
    }

}