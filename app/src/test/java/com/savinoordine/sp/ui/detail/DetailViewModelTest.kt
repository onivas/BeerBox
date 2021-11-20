package com.savinoordine.sp.ui.detail

import com.savinoordine.sp.DataSourceMock
import com.savinoordine.sp.repository.BeerRepository
import com.savinoordine.sp.rule.BeerTestRule
import com.savinoordine.sp.util.network.NetworkUnavailable
import com.savinoordine.sp.util.network.error
import com.savinoordine.sp.util.network.success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.OverrideMockKs
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var coroutinesTestRule = BeerTestRule()

    @RelaxedMockK
    lateinit var beerRepository: BeerRepository

    @OverrideMockKs
    lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        viewModel = DetailViewModel(beerRepository)
    }

    @Test
    fun `Show beer`() {
        coEvery { beerRepository.getBeerDetail(1) }.returns(DataSourceMock.domainBeer1.success())
        viewModel.loadBeer(1)
        assertEquals(DetailState.Success(DataSourceMock.domainBeer1), viewModel.state.value)
    }

    @Test
    fun `Error in showing beer`() {
        coEvery { beerRepository.getBeerDetail(1) }.returns(NetworkUnavailable.error())
        viewModel.loadBeer(1)
        assertEquals(DetailState.Error(NetworkUnavailable), viewModel.state.value)
    }

}