package net.sunshow.badge.domain.service.resource

interface ResourceService {

    fun createUnreadResource(store: String, path: String, resource: String)

}