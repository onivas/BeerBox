package com.savinoordine.sp.ui.list

import androidx.lifecycle.Observer
import com.savinoordine.sp.DataSourceMock
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.repository.BeerApiClient
import com.savinoordine.sp.repository.BeerApiClientImpl
import com.savinoordine.sp.repository.BeerRepository
import com.savinoordine.sp.repository.model.BeerLightModel
import com.savinoordine.sp.repository.model.BeerModel
import com.savinoordine.sp.rule.BeerTestRule
import com.savinoordine.sp.util.network.ResultOf
import com.savinoordine.sp.util.network.success
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {

    @get:Rule
    var coroutinesTestRule = BeerTestRule()

    @MockK
    lateinit var observer: Observer<List<BeerLight>>

    @OverrideMockKs
    lateinit var viewModel: ListViewModel

    private val capturingSlot = slot<List<BeerLight>>()
    private val beers = arrayListOf<BeerLight>()

    @Before
    fun before() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = ListViewModel(beerRepository)
        capturingSlot.clear()
        beers.clear()
    }

    @Test
    fun `Load beers`() {
        captureBeer()
        viewModel.loadBeers()

        assertTrue(beers.size == 1)
        assertEquals(beers[0], DataSourceMock.domainBeerLight1)
    }

    @Test
    fun `Reload beers`() = coroutinesTestRule.runBlockingTest {
        captureBeer()
        viewModel.reload()

        assertTrue(beers.size == 1)
        assertEquals(beers[0], DataSourceMock.domainBeerLight1)
    }

    @Test
    fun `Try loading more beers`() = coroutinesTestRule.runBlockingTest {
        captureBeer()
        viewModel.tryLoadingMoreBeers()

        assertTrue(beers.size == 1)
        assertEquals(beers[0], DataSourceMock.domainBeerLight1)
    }

    @Test
    fun `Filter word with auto-retry load beers`() {
        captureBeer()
        viewModel.filterBy("peroni")

        coroutinesTestRule.testDispatcher.advanceTimeBy(200)

        assertTrue(beers.size == 1)
        assertEquals(beers[0], DataSourceMock.domainBeerLight1)
    }

    private fun captureBeer() {
        beerRepository.beers.observeForever(observer)
        every { observer.onChanged(capture(capturingSlot)) }.answers {
            beers.add(capturingSlot.captured.first())
        }
    }

    private var beerRepository: BeerRepository = BeerApiClientImpl(object : BeerApiClient {
        override suspend fun getBeerDetail(id: Int): ResultOf<Array<BeerModel>> {
            return arrayOf(DataSourceMock.networkBeerModel1).success()
        }

        override suspend fun getBeers(page: Int, perPage: Int): ResultOf<Array<BeerLightModel>> {
            return arrayOf(DataSourceMock.networkBeerLightModel1).success()
        }
    })
}