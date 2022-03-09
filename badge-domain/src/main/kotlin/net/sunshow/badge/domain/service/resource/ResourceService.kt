package net.sunshow.badge.domain.service.resource

interface ResourceService {

    fun createUnreadResource(store: String, path: String, resource: String)

    fun deleteUnreadResource(store: String, path: String, resource: String)

    fun countUnreadResource(store: String, path: String): Int

}