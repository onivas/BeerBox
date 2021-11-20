package com.savinoordine.sp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.repository.model.toEntity
import com.savinoordine.sp.util.network.ResultOf
import com.savinoordine.sp.util.network.map
import com.savinoordine.sp.util.network.onSuccess
import javax.inject.Inject


class BeerApiClientImpl
@Inject constructor(private val beerApiClient: BeerApiClient) : BeerRepository {

    private val _beers: MutableLiveData<List<BeerLight>> =
        MutableLiveData<List<BeerLight>>(emptyList())
    override val beers: LiveData<List<BeerLight>>
        get() = _beers

    override suspend fun getBeers(page: Int): ResultOf<List<BeerLight>> {
        return beerApiClient.getBeers(page)
            .map { it.toEntity() }
            .onSuccess { newBeers ->
                _beers.postValue(_beers.value?.plus(newBeers))
            }
    }

    override suspend fun getBeerDetail(id: Int): ResultOf<Beer> {
        return beerApiClient.getBeerDetail(id)
            .map { it.first().toEntity() }
    }
}