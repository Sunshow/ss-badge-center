package net.sunshow.badge.domain.usecase.resource

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.service.resource.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank

@Component
class DeleteAllUnreadResourceUseCase :
    AbstractUseCase<DeleteAllUnreadResourceUseCase.InputData, DeleteAllUnreadResourceUseCase.OutputData>() {

    @Autowired
    private lateinit var resourceService: ResourceService

    override fun doAction(input: InputData): OutputData {
        val store = input.store
        val path = input.path

        resourceService.deleteAllUnreadResource(store, path)

        return OutputData()
    }

    class InputData(
        @field:NotBlank
        val store: String = "",

        @field:NotBlank
        val path: String = "",
    ) : AbstractUseCase.InputData()

    class OutputData : AbstractUseCase.OutputData()

}