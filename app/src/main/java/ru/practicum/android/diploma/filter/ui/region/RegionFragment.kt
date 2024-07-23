package ru.practicum.android.diploma.filter.ui.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.domain.models.Location
import ru.practicum.android.diploma.filter.ui.region.adapter.RegionsAdapter
import ru.practicum.android.diploma.utils.DeprecationUtils.serializable

class RegionFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegionViewModel by viewModel()

    private val regionsAdapter: RegionsAdapter by lazy {
        RegionsAdapter { region ->
            viewModel.region = region
            setFragmentResult(REGION_REQUEST_KEY, bundleOf(LOCATION_EXTRA to viewModel.location))
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationFromBundle()
        initializeOther()
        initializeAdapter()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeOther() {
        with(binding) {
            tbRegion.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            etSearchRegion.doOnTextChanged { text, _, _, _ ->
                viewModel.search(text.toString())
                ivClear.isVisible = !text.isNullOrEmpty()
                ivSearch.isVisible = text.isNullOrEmpty()
            }

            ivClear.setOnClickListener {
                etSearchRegion.text.clear()
            }
        }
    }

    private fun initializeAdapter() {
        binding.rvRegions.adapter = regionsAdapter
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is RegionState.Content -> showContent(screenState)
                RegionState.Error -> showError()
                RegionState.Loading -> showLoading()
            }
        }
    }

    private fun setLocationFromBundle() {
        viewModel.location = requireArguments().serializable<Location>(LOCATION_EXTRA) ?: Location()
    }

    private fun showContent(screenState: RegionState.Content) {
        with(binding) {
            if (screenState.regionsList.isNotEmpty()) {
                regionsAdapter.setItems(screenState.regionsList)
                tvPlaceholder.isVisible = false
            } else {
                regionsAdapter.clearItems()
                tvPlaceholder.setText(R.string.region_no_region)
                tvPlaceholder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.placeholder_no_results_cat, 0, 0)
                tvPlaceholder.isVisible = true
            }
            flSearch.isVisible = true
            rvRegions.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        with(binding) {
            tvPlaceholder.setText(R.string.region_error)
            tvPlaceholder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.placeholder_no_results_carpet, 0, 0)
            tvPlaceholder.isVisible = true
            flSearch.isVisible = true
            rvRegions.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            flSearch.isVisible = false
            rvRegions.isVisible = false
            tvPlaceholder.isVisible = false
        }
    }

    companion object {
        const val LOCATION_EXTRA = "location"
        const val REGION_REQUEST_KEY = "region_request"
    }
}
