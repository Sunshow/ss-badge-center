package net.sunshow.badge.domain.framework.usecase

import net.sunshow.badge.entity.exception.ParameterInvalidException
import net.sunshow.badge.entity.exception.ParameterInvalidItem
import org.springframework.beans.factory.annotation.Autowired
import javax.validation.Validator

abstract class AbstractUseCase<in Input : AbstractUseCase.InputData, out Output : AbstractUseCase.OutputData> {

    @Autowired
    private lateinit var validator: Validator

    fun execute(input: Input): Output {
        val constraintViolations = validator.validate(input)

        if (constraintViolations != null && constraintViolations.isNotEmpty()) {
            val itemList =
                constraintViolations.map { ParameterInvalidItem(it.propertyPath.toString(), it.message) }.toList()

            throw ParameterInvalidException(
                message = "ParameterInvalid: Violation of the constraint",
                itemList = itemList
            )
        }
        return doAction(input)
    }

    protected abstract fun doAction(input: Input): Output

    abstract class InputData

    abstract class OutputData

}