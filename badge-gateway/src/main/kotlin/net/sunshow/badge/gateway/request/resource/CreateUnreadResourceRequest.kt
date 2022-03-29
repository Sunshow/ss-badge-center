package net.sunshow.badge.gateway.request.resource

data class CreateUnreadResourceRequest(
    var resource: String? = null,
    var resources: List<String>? = null,
)