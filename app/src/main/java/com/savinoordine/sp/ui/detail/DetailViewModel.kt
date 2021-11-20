package com.savinoordine.sp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.repository.BeerRepository
import com.savinoordine.sp.util.network.ErrorType
import com.savinoordine.sp.util.network.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    private val _beerRepository: BeerRepository,
) : ViewModel() {

    private val _state: MutableLiveData<DetailState> = MutableLiveData(DetailState.Idle)
    val state: LiveData<DetailState> = _state

    fun loadBeer(id: Int) = viewModelScope.launch {
        _state.value = DetailState.Loading
        _beerRepository.getBeerDetail(id)
            .fold(
                success = {
                    _state.value = DetailState.Success(it)
                },
                error = {
                    _state.value = DetailState.Error(it)
                }
            )
    }
}


sealed class DetailState {
    object Loading : DetailState()
    data class Success(val beer: Beer) : DetailState()
    object Idle : DetailState()
    data class Error(val error: ErrorType) : DetailState()
}