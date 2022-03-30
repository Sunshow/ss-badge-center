package net.sunshow.badge.domain.usecase.resource

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.service.resource.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Component
class BatchDeleteUnreadResourceUseCase :
    AbstractUseCase<BatchDeleteUnreadResourceUseCase.InputData, BatchDeleteUnreadResourceUseCase.OutputData>() {

    @Autowired
    private lateinit var resourceService: ResourceService

    override fun doAction(input: InputData): OutputData {
        val store = input.store
        val path = input.path
        val resources = input.resources

        resourceService.batchDeleteUnreadResource(store, path, *resources.toTypedArray())

        return OutputData()
    }

    data class InputData(
        @field:NotBlank
        val store: String,

        @field:NotBlank
        val path: String,

        @field:NotEmpty
        val resources: List<String>,
    ) : AbstractUseCase.InputData()

    class OutputData : AbstractUseCase.OutputData()

}