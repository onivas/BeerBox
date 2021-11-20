package com.savinoordine.sp.ui.list

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.savinoordine.sp.R
import com.savinoordine.sp.databinding.ListFragmentBinding
import com.savinoordine.sp.domain.BeerExtra
import com.savinoordine.sp.domain.BeerLight
import com.savinoordine.sp.ui.detail.DetailFragment
import com.savinoordine.sp.ui.list.adapter.BeerLightAdapter
import com.savinoordine.sp.ui.list.adapter.BeerTagAdapter
import com.savinoordine.sp.util.ErrorHandler
import com.savinoordine.sp.util.extensions.castAdapterTo
import com.savinoordine.sp.util.extensions.gone
import com.savinoordine.sp.util.extensions.visible
import com.savinoordine.sp.util.fragment.viewBinding
import com.savinoordine.sp.util.network.ErrorType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.list_fragment) {

    private val viewModel: ListViewModel by viewModels()
    private val binding: ListFragmentBinding by viewBinding(ListFragmentBinding::bind)

    @Inject
    lateinit var errorHandler: ErrorHandler

    private var delayJob: Job? = null  // delay job to avoid multiple viewmodel calls while user is searching

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            observingViewModelState()
            initUI()
        }
    }

    private fun ListFragmentBinding.observingViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ListState.Success -> handleSuccess(state.beers, state.filteredBeer)
                is ListState.Error -> handleError(state.error)
                is ListState.Loading -> handleLoading()
            }
        }
    }

    private fun ListFragmentBinding.handleSuccess(
        beers: List<BeerLight>?,
        filteredBeer: List<BeerLight>?
    ) {
        beerRecyclerView.castAdapterTo<BeerLightAdapter>().submitList(filteredBeer ?: beers)
        progress.gone()
        reloadListBtn.gone()
        contentGroup.visible()
    }

    private fun ListFragmentBinding.handleError(error: ErrorType) {
        progress.gone()
        if (reloadListBtn.visibility != View.VISIBLE) {
            reloadListBtn.visible()
            errorHandler.handleError(error)
        }
    }

    private fun onBeerClick(beer: BeerLight) {
        val bundle = Bundle()
        bundle.putInt(DetailFragment.BEER_ID_KEY, beer.id)
        val modalBottomSheet = DetailFragment()
        modalBottomSheet.arguments = bundle
        modalBottomSheet.show(childFragmentManager, DetailFragment.TAG)
        binding.searchBeer.clearFocus()
    }

    private fun onTagClick(tag: String) {
        binding.searchBeer.setQuery(tag, true)
    }

    private fun ListFragmentBinding.initUI() {
        reloadListBtn.setOnClickListener { viewModel.reload() }
        initBeerRecyclerView()
        initTagRecyclerView()
        initSearchView()
    }

    private fun ListFragmentBinding.initTagRecyclerView() {
        tagRecyclerView.apply {
            setHasFixedSize(true)
            adapter = BeerTagAdapter(::onTagClick)
            castAdapterTo<BeerTagAdapter>().submitList(BeerExtra.tags)
        }
    }

    private fun ListFragmentBinding.initBeerRecyclerView() {
        beerRecyclerView.apply {
            setHasFixedSize(true)
            adapter = BeerLightAdapter(::onBeerClick)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val totItem = recyclerView.layoutManager?.itemCount ?: 0
                    val lastItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    if (totItem >= 0 && lastItemPosition + MORE_ITEM_OFFSET >= totItem) {
                        viewModel.tryLoadingMoreBeers()
                    }
                }
            })
        }
    }

    private fun ListFragmentBinding.initSearchView() {
        searchBeer.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                delayJob?.cancel()
                delayJob = lifecycleScope.launch(Dispatchers.Main) {
                    delay(200)
                    viewModel.filterBy(newText.orEmpty())
                }
                return true
            }
        })
    }

    private fun ListFragmentBinding.handleLoading() {
        progress.visible()
    }

    companion object {
        const val MORE_ITEM_OFFSET = 2
    }
}