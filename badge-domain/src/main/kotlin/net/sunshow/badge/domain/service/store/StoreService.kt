package net.sunshow.badge.domain.service.store

interface StoreService {

    fun createStoreByName(name: String)

    fun deleteStoreByName(name: String)
    
}