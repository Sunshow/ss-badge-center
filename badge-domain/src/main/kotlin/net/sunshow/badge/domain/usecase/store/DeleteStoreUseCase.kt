package net.sunshow.badge.domain.usecase.store

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.service.store.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank

@Component
class DeleteStoreUseCase : AbstractUseCase<DeleteStoreUseCase.InputData, DeleteStoreUseCase.OutputData>() {

    @Autowired
    private lateinit var storeService: StoreService

    override fun doAction(input: InputData): OutputData {
        storeService.deleteStoreByName(input.name)
        return OutputData()
    }

    class InputData(
        @field:NotBlank
        val name: String = ""
    ) : AbstractUseCase.InputData()

    class OutputData : AbstractUseCase.OutputData()

}