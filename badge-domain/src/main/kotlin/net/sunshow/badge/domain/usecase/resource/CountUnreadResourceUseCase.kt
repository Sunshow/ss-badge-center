package net.sunshow.badge.domain.usecase.resource

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.service.resource.ResourceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank

@Component
class CountUnreadResourceUseCase :
    AbstractUseCase<CountUnreadResourceUseCase.InputData, CountUnreadResourceUseCase.OutputData>() {

    @Autowired
    private lateinit var resourceService: ResourceService

    override fun doAction(input: InputData): OutputData {
        val store = input.store
        val path = input.path

        val count = resourceService.countUnreadResource(store, path)

        return OutputData(
            count = count
        )
    }

    class InputData(
        @field:NotBlank
        val store: String = "",

        @field:NotBlank
        val path: String = "",
    ) : AbstractUseCase.InputData()

    class OutputData(
        val count: Int = 0
    ) : AbstractUseCase.OutputData()

}