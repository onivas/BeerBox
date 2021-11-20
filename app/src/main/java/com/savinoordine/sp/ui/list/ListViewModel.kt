package com.savinoordine.sp.ui.list

import androidx.lifecycle.*
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.repository.BeerRepository
import com.savinoordine.sp.util.network.ErrorType
import com.savinoordine.sp.util.network.onError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel
@Inject
constructor(
    private val _beerRepository: BeerRepository,
) : ViewModel() {

    private var _state: MutableLiveData<ListState> = MutableLiveData(ListState.Idle)
    val state: LiveData<ListState> = _state.distinctUntilChanged()

    private var _lastBeers = emptyList<BeerLight>()
    private var _filterBeers: List<BeerLight>? = null
    private var _searchingPage = 1
    private var _filterWord: String? = null
    private var autoSearchEnable = true

    init {
        loadBeers()
        observeBeers()
    }

    private fun observeBeers() {
        _beerRepository.beers.observeForever { newBeers ->
            if (_lastBeers != newBeers) {
                autoSearchEnable = true
                _lastBeers = newBeers
                _searchingPage =
                    _searchingPage.inc().takeIf { newBeers.isNotEmpty() } ?: _searchingPage
            } else {
                autoSearchEnable = false  // disable auto-search because no more beer are available
            }

            _filterWord?.let { filterBeers() }
            success()
        }
    }

    // this method is called if during a filtered search, user has no results
    private fun autoSearchBeers() = viewModelScope.launch {
        delay(200)  // small delay for a smooth autosearch
        autoSearchEnable.takeIf { it }?.apply { tryLoadingMoreBeers() }
    }

    fun loadBeers() = viewModelScope.launch {
        if (autoSearchEnable) { // avoid call because last success api call return no more beer available
            _state.value = ListState.Loading
            _beerRepository.getBeers(_searchingPage)
                .onError {
                    autoSearchEnable = true
                    _state.value = ListState.Error(it)
                }
        }
    }

    fun tryLoadingMoreBeers() = viewModelScope.launch {
        if (_state.value !is ListState.Loading) {
            loadBeers()
        }
    }

    fun reload() {
        tryLoadingMoreBeers()
    }

    fun filterBy(word: String) {
        _filterWord = word
        if (word.isEmpty()) {
            _filterBeers = null
        } else {
            filterBeers()
        }
        success()
    }

    // this method begin filtering the beers from the "already searched beers", if no match, auto search new beers
    private fun filterBeers() = viewModelScope.launch {
        val currentFilteredBeer = _filterBeers
        _filterWord?.let { word ->
            _filterBeers = _beerRepository.beers.value?.filter {
                it.description.contains(word, true) ||
                        it.name.contains(word, true)
            }
            if (currentFilteredBeer.isNullOrEmpty() || currentFilteredBeer == _filterBeers || _filterBeers.isNullOrEmpty()) {
                autoSearchBeers()
            }
        }
    }

    private fun success() = viewModelScope.launch {
        _state.value = ListState.Success(
            _beerRepository.beers.value,
            _filterBeers,
            _filterWord,
            _searchingPage
        )
    }
}

sealed class ListState {
    object Loading : ListState()
    data class Success(
        val beers: List<BeerLight>?,
        val filteredBeer: List<BeerLight>?,
        val filteredWord: String?,
        val nextPage: Int
    ) : ListState()

    object Idle : ListState()
    data class Error(val error: ErrorType) : ListState()
}

