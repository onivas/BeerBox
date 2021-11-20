package com.savinoordine.sp.repository.model

import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.domain.Ingredient
import com.savinoordine.sp.domain.NO_VALUE
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BeerModel(
    @Json(name = "name")
    val name:  String? = null,
    @Json(name = "tagline")
    val tagline: String? = null,
    @Json(name = "description")
    val description:  String? = null,
    @Json(name = "image_url")
    val imageUrl:  String? = null,
    @Json(name = "abv")
    val abv:  Double? = null,
    @Json(name = "ingredients")
    val ingredients: IngredientModel,
    @Json(name="brewers_tips")
    val brewersTips : String?,
)

@JsonClass(generateAdapter = true)
data class IngredientModel(
    @Json(name = "malt")
    val malt: List<Name>,
    @Json(name = "hops")
    val hops: List<Name>,
    @Json(name = "yeast")
    val yeast: String? = null,
)

@JsonClass(generateAdapter = true)
data class Name(
    @Json(name = "name")
    val name: String? = null
)

fun BeerModel.toEntity() = Beer(
    name = this.name.orEmpty(),
    tagline = this.tagline.orEmpty(),
    description = this.description.orEmpty(),
    imageUrl = this.imageUrl.orEmpty(),
    abv = this.abv ?: NO_VALUE,
    brewersTips = this.brewersTips.orEmpty(),
    ingredients = this.ingredients.toEntity(),
)

fun IngredientModel.toEntity() = Ingredient(
    malt = this.malt.toEntity(),
    hops = this.hops.toEntity(),
    yeast = this.yeast.orEmpty(),
)

private fun List<Name>.toEntity(): List<String> = map {
    it.name.orEmpty()
}
