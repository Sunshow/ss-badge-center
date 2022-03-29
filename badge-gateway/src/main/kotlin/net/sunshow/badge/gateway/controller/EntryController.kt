package net.sunshow.badge.gateway.controller

import net.sunshow.badge.domain.usecase.resource.*
import net.sunshow.badge.domain.usecase.store.CreateStoreUseCase
import net.sunshow.badge.domain.usecase.store.DeleteStoreUseCase
import net.sunshow.badge.domain.usecase.store.ListStoreUseCase
import net.sunshow.badge.gateway.request.resource.CreateUnreadResourceRequest
import net.sunshow.badge.gateway.request.resource.DeleteUnreadResourceRequest
import net.sunshow.badge.gateway.request.store.CreateStoreRequest
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest

@RestController
class EntryController(
    private val createStoreUseCase: CreateStoreUseCase,
    private val deleteStoreUseCase: DeleteStoreUseCase,
    private val listStoreUseCase: ListStoreUseCase,
    private val createUnreadResourceUseCase: CreateUnreadResourceUseCase,
    private val batchCreateUnreadResourceUseCase: BatchCreateUnreadResourceUseCase,
    private val countUnreadResourceUseCase: CountUnreadResourceUseCase,
    private val batchCountUnreadResourceUseCase: BatchCountUnreadResourceUseCase,
    private val deleteUnreadResourceUseCase: DeleteUnreadResourceUseCase,
    private val deleteAllUnreadResourceUseCase: DeleteAllUnreadResourceUseCase,
) {

    @GetMapping("/")
    fun listStore(): Any {
        return listStoreUseCase.execute(
            ListStoreUseCase.InputData()
        )
    }

    @PutMapping("/")
    fun createStore(@RequestBody body: CreateStoreRequest) {
        createStoreUseCase.execute(
            CreateStoreUseCase.InputData(
                name = body.name
            )
        )
    }

    @DeleteMapping("/{store}")
    fun deleteStore(@PathVariable store: String) {
        deleteStoreUseCase.execute(
            DeleteStoreUseCase.InputData(
                name = store
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
        if (body.resource == null && body.resources == null) {
            throw RuntimeException("No resource was supplied.")
        }
        if (body.resource != null && body.resources != null) {
            throw RuntimeException("Cannot both supply resource and resources.")
        }
        if (body.resource != null) {
            createUnreadResourceUseCase.execute(
                CreateUnreadResourceUseCase.InputData(
                    store = store,
                    path = path,
                    resource = body.resource!!,
                )
            )
        } else {
            batchCreateUnreadResourceUseCase.execute(
                BatchCreateUnreadResourceUseCase.InputData(
                    store = store,
                    path = path,
                    resources = body.resources!!,
                )
            )
        }
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

    @GetMapping("/{store}")
    fun batchGetUnreadResource(
        @PathVariable store: String,
        @RequestParam paths: Array<String>
    ): Any {
        return batchCountUnreadResourceUseCase.execute(
            BatchCountUnreadResourceUseCase.InputData(
                store = store,
                paths = paths,
            )
        )
    }

    @DeleteMapping("/{store}/**")
    fun deleteUnreadResource(
        @PathVariable store: String,
        @RequestBody(required = false) body: DeleteUnreadResourceRequest?,
        request: HttpServletRequest
    ) {
        val uri = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE) as String
        val path = uri.substringAfter("/$store")
        if (body != null) {
            deleteUnreadResourceUseCase.execute(
                DeleteUnreadResourceUseCase.InputData(
                    store = store,
                    path = path,
                    resource = body.resource,
                )
            )
        } else {
            deleteAllUnreadResourceUseCase.execute(
                DeleteAllUnreadResourceUseCase.InputData(
                    store = store,
                    path = path,
                )
            )
        }
    }
}
