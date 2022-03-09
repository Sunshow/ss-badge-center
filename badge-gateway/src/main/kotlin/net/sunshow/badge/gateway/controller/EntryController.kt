package net.sunshow.badge.gateway.controller

import net.sunshow.badge.domain.usecase.resource.CountUnreadResourceUseCase
import net.sunshow.badge.domain.usecase.resource.CreateUnreadResourceUseCase
import net.sunshow.badge.domain.usecase.resource.DeleteUnreadResourceUseCase
import net.sunshow.badge.domain.usecase.store.CreateStoreUseCase
import net.sunshow.badge.gateway.request.resource.CreateUnreadResourceRequest
import net.sunshow.badge.gateway.request.resource.DeleteUnreadResourceRequest
import net.sunshow.badge.gateway.request.store.CreateStoreRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
class EntryController(
    private val createStoreUseCase: CreateStoreUseCase,
    private val createUnreadResourceUseCase: CreateUnreadResourceUseCase,
    private val countUnreadResourceUseCase: CountUnreadResourceUseCase,
    private val deleteUnreadResourceUseCase: DeleteUnreadResourceUseCase,
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
    fun createUnreadResource(
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

    @GetMapping("/{store}/**")
    fun getUnreadResource(@PathVariable store: String, request: HttpServletRequest): Any {
        val uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
        val path = uri.substringAfter("/$store")
        return countUnreadResourceUseCase.execute(
            CountUnreadResourceUseCase.InputData(
                store = store,
                path = path,
            )
        )
    }

    @DeleteMapping("/{store}/**")
    fun deleteUnreadResource(
        @PathVariable store: String,
        @RequestBody body: DeleteUnreadResourceRequest,
        request: HttpServletRequest
    ) {
        val uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
        val path = uri.substringAfter("/$store")
        deleteUnreadResourceUseCase.execute(
            DeleteUnreadResourceUseCase.InputData(
                store = store,
                path = path,
                resource = body.resource,
            )
        )
    }
}
