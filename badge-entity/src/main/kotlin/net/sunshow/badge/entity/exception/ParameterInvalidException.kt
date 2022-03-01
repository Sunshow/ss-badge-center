package net.sunshow.badge.entity.exception

class ParameterInvalidException(
    message: String = "Invalid Parameter",
    cause: Throwable? = null,
    val itemList: List<ParameterInvalidItem> = listOf()
) : RuntimeException(message, cause)

data class ParameterInvalidItem(val field: String, val message: String)