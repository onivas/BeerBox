package com.savinoordine.sp.util.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorModel(
    @Json(name = "error")
    val error: String? = null,
    @Json(name = "exception")
    val exception: String? = null,
    @Json(name = "exceptionCode")
    val exceptionCode: String? = null,
    @Json(name = "message")
    val message: String? = null,
    @Json(name = "path")
    val path: String? = null,
    @Json(name = "status")
    val status: Int? = null,
    @Json(name = "timeStamp")
    val timeStamp: Long? = null
)
