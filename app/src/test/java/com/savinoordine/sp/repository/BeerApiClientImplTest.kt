package com.savinoordine.sp.repository

import com.savinoordine.sp.DataSourceMock
import com.savinoordine.sp.rule.BeerTestRule
import com.savinoordine.sp.util.network.map
import com.savinoordine.sp.util.network.success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BeerApiClientImplTest {

    @get:Rule
    var coroutinesTestRule = BeerTestRule()

    @RelaxedMockK
    lateinit var beerApiClient: BeerApiClient

    @OverrideMockKs
    lateinit var apiClient: BeerApiClientImpl

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        apiClient = BeerApiClientImpl(beerApiClient)
    }

    @Test
    fun `Beer detail API mapping`() = coroutinesTestRule.runBlockingTest {
        coEvery { beerApiClient.getBeerDetail(1) }.returns(DataSourceMock.networkBeersModel.success())
        val beerDetail = apiClient.getBeerDetail(1)
        beerDetail.map { assertEquals(DataSourceMock.domainBeer1, it) }
    }

    @Test
    fun `Beer list API mapping`() = coroutinesTestRule.runBlockingTest {
        coEvery { beerApiClient.getBeers(1) }.returns(DataSourceMock.networkBeersLightModel.success())
        apiClient.getBeers()
        assertEquals(DataSourceMock.domainBeers, apiClient.beers.value)
    }
}