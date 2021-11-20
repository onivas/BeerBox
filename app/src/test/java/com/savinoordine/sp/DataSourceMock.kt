package com.savinoordine.sp

import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.domain.Ingredient
import com.savinoordine.sp.repository.model.BeerLightModel
import com.savinoordine.sp.repository.model.BeerModel
import com.savinoordine.sp.repository.model.IngredientModel


object DataSourceMock {
    /** **  Domain model ** */

    val domainBeer1 = Beer(
        name = "Peroni",
        tagline = "Bevi in compagnia",
        description = "Gusto raffinato, si abbina con pizza,kebab e parmigiana",
        imageUrl = "url",
        abv = 5.0,
        brewersTips = "Berla fredda e alla calata",
        ingredients = Ingredient(
            malt = emptyList(),
            hops = emptyList(),
            yeast = "Lievito"
        )
    )

    val domainBeerLight1 = BeerLight(
        id = 1,
        name = "Peroni",
        tagline = "Bevi in compagnia",
        description = "Gusto raffinato, si abbina con pizza,kebab e parmigiana",
        imageUrl = "url"
    )

    private val domainBeerLight2 = BeerLight(
        id = 2,
        name = "Moretti",
        tagline = "Bevila in solitaria",
        description = "Gustosa, si abbina con la carbonara",
        imageUrl = "url"
    )

    val domainBeers = listOf(domainBeerLight1, domainBeerLight2)


    /** **  Network model ** */

    val networkBeerModel1 = BeerModel(
        name = "Peroni",
        tagline = "Bevi in compagnia",
        description = "Gusto raffinato, si abbina con pizza,kebab e parmigiana",
        imageUrl = "url",
        abv = 5.0,
        brewersTips = "Berla fredda e alla calata",
        ingredients = IngredientModel(
            malt = emptyList(),
            hops = emptyList(),
            yeast = "Lievito"
        )
    )

    val networkBeersModel = arrayOf(networkBeerModel1)

    val networkBeerLightModel1 = BeerLightModel(
        id = 1.0,
        name = "Peroni",
        tagline = "Bevi in compagnia",
        description = "Gusto raffinato, si abbina con pizza,kebab e parmigiana",
        imageUrl = "url",
    )

    private val networkBeerLightModel2 = BeerLightModel(
        id = 2.0,
        name = "Moretti",
        tagline = "Bevila in solitaria",
        description = "Gustosa, si abbina con la carbonara",
        imageUrl = "url"
    )

    val networkBeersLightModel = arrayOf(networkBeerLightModel1, networkBeerLightModel2)
}