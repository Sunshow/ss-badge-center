package net.sunshow.badge.domain.usecase.store

import net.sunshow.badge.domain.framework.usecase.AbstractUseCase
import net.sunshow.badge.domain.framework.usecase.AbstractUseCase.OutputData
import net.sunshow.badge.domain.service.store.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ListStoreUseCase : AbstractUseCase<ListStoreUseCase.InputData, ListStoreUseCase.OutputData>() {

    @Autowired
    private lateinit var storeService: StoreService

    override fun doAction(input: InputData): OutputData {
        val storeList = storeService.listStore()
        return OutputData(
            storeList = storeList,
        )
    }

    class InputData : AbstractUseCase.InputData()

    class OutputData(
        val storeList: List<String>
    ) : AbstractUseCase.OutputData()

}