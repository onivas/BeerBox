package com.savinoordine.sp.repository

import androidx.lifecycle.LiveData
import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.util.network.ResultOf

interface BeerRepository {
    val beers: LiveData<List<BeerLight>>
    suspend fun getBeers(): ResultOf<List<BeerLight>>
    suspend fun getBeerDetail(id: Int): ResultOf<Beer>
}