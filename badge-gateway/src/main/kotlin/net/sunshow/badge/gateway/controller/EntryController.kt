package net.sunshow.badge.gateway.controller

import net.sunshow.badge.domain.usecase.resource.CreateUnreadResourceUseCase
import net.sunshow.badge.domain.usecase.store.CreateStoreUseCase
import net.sunshow.badge.gateway.request.resource.CreateUnreadResourceRequest
import net.sunshow.badge.gateway.request.store.CreateStoreRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
class EntryController(
    private val createStoreUseCase: CreateStoreUseCase,
    private val createUnreadResourceUseCase: CreateUnreadResourceUseCase,
) {

    @PutMapping("/")
    fun createStore(@RequestBody body: CreateStoreRequest) {
        createStoreUseCase.execute(
            CreateStoreUseCase.InputData(
                name = body.name
            )
        )
    }

    @PutMapping("/{store}/**")
    fun createResource(
        @PathVariable store: String,
        @RequestBody body: CreateUnreadResourceRequest,
        request: HttpServletRequest
    ) {
        val uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
        val path = uri.substringAfter("/$store")
        createUnreadResourceUseCase.execute(
            CreateUnreadResourceUseCase.InputData(
                store = store,
                path = path,
                resource = body.resource,
            )
        )
    }

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
