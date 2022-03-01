package net.sunshow.badge.domain.usecase.store

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import org.springframework.stereotype.Component
import javax.validation.constraints.NotBlank

@Component
class CreateStoreUseCase : AbstractUseCase<CreateStoreUseCase.InputData, CreateStoreUseCase.OutputData>() {

    override fun doAction(input: InputData): OutputData {
        return OutputData()
    }

    class InputData(
        @field:NotBlank
        val name: String = ""
    ) : AbstractUseCase.InputData()

    class OutputData : AbstractUseCase.OutputData()

}