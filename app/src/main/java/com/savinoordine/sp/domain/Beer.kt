package com.savinoordine.sp.domain

data class Beer(
    val name: String,
    val tagline: String,
    val description: String,
    val imageUrl: String,
    val abv: Double,
    val brewersTips: String,
    val ingredients: Ingredient,
)

data class Ingredient(
    val malt: List<String>,
    val hops: List<String>,
    val yeast: String,
)

const val NO_VALUE = -1.0

class BeerExtra {
    companion object{
        val tags =
            listOf("Blonde", "Lager", "Ale", "Dark", "Italian", "French", "Belgian", "Fruit", "Saison", "Bitter")
    }
}
