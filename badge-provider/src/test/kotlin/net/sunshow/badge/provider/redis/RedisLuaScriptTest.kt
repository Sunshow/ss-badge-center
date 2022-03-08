package net.sunshow.badge.provider.redis

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.util.StopWatch

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [TestRedisConfig::class])
class RedisLuaScriptTest {

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    @Autowired
    @Qualifier("testScript")
    lateinit var testScript: RedisScript<String>

    @Autowired
    @Qualifier("testConcurrencyScript")
    lateinit var testConcurrencyScript: RedisScript<List<*>>

    private val testKey = "redis_test"

    private val testKey1 = "redis_test_1"

    private val testKey2 = "redis_test_2"

    private val testKey3 = "redis_test_3"

    private val testVal = "hello redis"

    private val stopWatch = StopWatch()

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
        val result = redisTemplate.execute(testScript, listOf(testKey), testVal)

        val checkResult = redisTemplate.opsForValue().get(testKey)

        Assertions.assertEquals(result, checkResult)
    }

    @Test
    fun testMultiGet() {
        stopWatch.start()
        val result = redisTemplate.execute(testConcurrencyScript, listOf(testKey1, testKey2), "15000000")
        stopWatch.stop()
        println(stopWatch.prettyPrint())

        val checkResult = redisTemplate.opsForValue().multiGet(listOf(testKey1, testKey2))

        Assertions.assertEquals(result, checkResult)
    }

    @Test
    fun testConcurrency() {
        val stopWatch1 = StopWatch()
        val stopWatch2 = StopWatch()
        stopWatch1.start()
        stopWatch2.start()
        val result = mutableMapOf<String, String>()
        Thread {
            Thread.sleep(1000)
            val first = redisTemplate.execute(testConcurrencyScript, listOf(testKey1, testKey2), "15000000")
            result["0"] = first[1] as String
            stopWatch1.stop()
            println("Stop1: ${stopWatch1.prettyPrint()}")
        }.start()

        Thread {
            val second = redisTemplate.execute(testConcurrencyScript, listOf(testKey3, testKey2), "15000000")
            result["1"] = second[1] as String
            stopWatch2.stop()
            println("Stop2: ${stopWatch2.prettyPrint()}")
        }.start()

        Thread.sleep(15000)
        println(result["0"])
        println(result["1"])
    }
}