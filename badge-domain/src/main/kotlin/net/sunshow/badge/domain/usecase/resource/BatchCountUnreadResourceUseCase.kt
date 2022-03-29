package net.sunshow.badge.domain.usecase.resource

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.service.resource.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Component
class BatchCountUnreadResourceUseCase :
    AbstractUseCase<BatchCountUnreadResourceUseCase.InputData, BatchCountUnreadResourceUseCase.OutputData>() {

    @Autowired
    private lateinit var resourceService: ResourceService

    override fun doAction(input: InputData): OutputData {
        val store = input.store
        val paths = input.paths

        val count = resourceService.batchCountUnreadResource(store, *paths)

        return OutputData(
            count = count
        )
    }

    class InputData(
        @field:NotBlank
        val store: String,

        @field:NotEmpty
        val paths: Array<String>,
    ) : AbstractUseCase.InputData()

    class OutputData(
        val count: Map<String, Int>
    ) : AbstractUseCase.OutputData()

}