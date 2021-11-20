package com.savinoordine.sp.repository

import com.savinoordine.sp.repository.model.BeerLightModel
import com.savinoordine.sp.repository.model.BeerModel
import com.savinoordine.sp.util.network.ResultOf
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BeerApiClient {

    //https://api.punkapi.com/v2/beers?page=1&per_page=25
    @GET("beers")
    suspend fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 25,
    ): ResultOf<Array<BeerLightModel>>


    //https://api.punkapi.com/v2/beers/1
    @GET("beers/{id}")
    suspend fun getBeerDetail(@Path("id") id: Int): ResultOf<Array<BeerModel>>

}