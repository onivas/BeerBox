package com.savinoordine.sp.repository.model

import com.savinoordine.sp.domain.NO_VALUE
import com.savinoordine.sp.domain.BeerLight
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BeerLightModel(
    @Json(name = "id")
    val id: Double = NO_VALUE,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "tagline")
    val tagline: String? = null,
    @Json(name = "description")
    val description: String? = null,
    @Json(name = "image_url")
    val imageUrl: String? = null,
)

fun Array<BeerLightModel>.toEntity(): List<BeerLight> = map {
    BeerLight(
        id = it.id.toInt(),
        name = it.name.orEmpty(),
        tagline = it.tagline.orEmpty(),
        description = it.description.orEmpty(),
        imageUrl = it.imageUrl.orEmpty(),
    )
}
