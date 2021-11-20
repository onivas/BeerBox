package com.savinoordine.sp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.savinoordine.sp.R
import com.savinoordine.sp.databinding.DetailFragmentBinding
import com.savinoordine.sp.domain.Beer
import com.savinoordine.sp.domain.Ingredient
import com.savinoordine.sp.domain.NO_VALUE
import com.savinoordine.sp.util.ErrorHandler
import com.savinoordine.sp.util.extensions.gone
import com.savinoordine.sp.util.extensions.visible
import com.savinoordine.sp.util.fragment.viewBinding
import com.savinoordine.sp.util.network.ErrorType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : BottomSheetDialogFragment() {

    private val viewModel: DetailViewModel by viewModels()
    private val binding: DetailFragmentBinding by viewBinding(DetailFragmentBinding::bind)

    @Inject
    lateinit var errorHandler: ErrorHandler

    private var behavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById(R.id.root) as ViewGroup
        behavior = BottomSheetBehavior.from(bottomSheet).apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beerId = arguments?.getInt(BEER_ID_KEY) ?: NO_VALUE.toInt()
        if (beerId == NO_VALUE.toInt()) findNavController().popBackStack()

        with(binding) {
            initUi()
            observeViewModelState()
            viewModel.loadBeer(beerId)
        }
    }

    private fun DetailFragmentBinding.observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailState.Loading -> handleLoading()
                is DetailState.Success -> handleSuccess(state.beer)
                is DetailState.Error -> handleError(state.error)
            }
        }
    }

    private fun DetailFragmentBinding.initUi() {
        progress.visible()
    }

    private fun DetailFragmentBinding.handleError(error: ErrorType) {
        errorHandler.handleError(error)
        contentGroup.gone()
        progress.gone()
    }

    private fun DetailFragmentBinding.handleSuccess(beer: Beer) {
        progress.gone()
        contentGroup.visible()

        name.text = beer.name
        tagline.text = beer.tagline
        description.text = beer.description
        ingredient.text = beer.ingredients.toDetailedList()
        alcohol.text = getString(R.string.alcohol, beer.abv.toString())
        brewTips.text = beer.brewersTips
        Glide.with(requireContext())
            .load(beer.imageUrl)
            .centerInside()
            .placeholder(R.drawable.ic_baseline_photo_24)
            .into(detailImage)
    }

    private fun DetailFragmentBinding.handleLoading() {
        contentGroup.gone()
        progress.visible()
    }

    private fun Ingredient.toDetailedList(): String {
        return getString(R.string.ingredients, this.yeast, this.hops.toString(), this.malt.toString())
    }

    companion object {
        const val TAG = "DetailBottomSheet"
        const val BEER_ID_KEY = "ID_KEY"
    }
}


