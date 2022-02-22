package net.sunshow.badge.gateway.controller

import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
class EntryController {
    @RequestMapping("/{store}")
    fun testStore(@PathVariable store: String, request: HttpServletRequest) {
        println("Without slash: $store")
    }

    @RequestMapping("/{store}/")
    fun testBadge(@PathVariable store: String, request: HttpServletRequest) {
        println("With slash: $store")
    }

    @RequestMapping("/{store}/**")
    fun test(@PathVariable store: String, request: HttpServletRequest) {
        println(store)
        val uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
        println(uri)
    }

    @DeleteMapping("/{store}/test")
    fun testDelete(@PathVariable store: String, @RequestBody body: String, request: HttpServletRequest) {
        println("Delete body: $body")
    }
}
