package net.sunshow.badge.gateway.request.resource

data class DeleteUnreadResourceRequest(
    var resource: String? = null,
    var resources: List<String>? = null,
)