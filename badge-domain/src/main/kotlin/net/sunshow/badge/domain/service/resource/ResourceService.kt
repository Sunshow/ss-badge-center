package net.sunshow.badge.domain.service.resource

interface ResourceService {

    fun createUnreadResource(store: String, path: String, resource: String)

    fun batchCreateUnreadResource(store: String, path: String, vararg resources: String)

    fun deleteUnreadResource(store: String, path: String, resource: String)

    fun batchDeleteUnreadResource(store: String, path: String, vararg resources: String)

    fun deleteAllUnreadResource(store: String, path: String)

    fun countUnreadResource(store: String, path: String): Int

    fun batchCountUnreadResource(store: String, vararg paths: String): Map<String, Int>

}