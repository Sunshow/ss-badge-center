package net.sunshow.badge.provider.redis

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.scripting.support.ResourceScriptSource
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestRedisConfig::class])
class RedisLuaScriptTest {

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    private val testKey = "redis_test"

    private val testVal = "hello redis"

    @BeforeEach
    fun setup() {
        redisTemplate.delete(testKey)
    }

    @AfterEach
    fun teardown() {
        redisTemplate.delete(testKey)
    }

    @Test
    fun testRedisConnection() {
        redisTemplate.opsForValue().set(testKey, testKey)
        Assertions.assertEquals(testKey, redisTemplate.opsForValue().get(testKey))
    }

    @Test
    fun testSimpleScript() {
        val script = DefaultRedisScript<String>()
        script.resultType = String::class.java

        script.setScriptSource(ResourceScriptSource(ClassPathResource("scripts/test.lua")))

        val result = redisTemplate.execute(script, listOf(testKey), testVal)

        val checkResult = redisTemplate.opsForValue().get(testKey)

        Assertions.assertEquals(result, checkResult)
    }
}