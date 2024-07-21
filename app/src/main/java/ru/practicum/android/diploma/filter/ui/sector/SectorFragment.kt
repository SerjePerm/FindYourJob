package ru.practicum.android.diploma.filter.ui.sector

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSectorBinding
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.FILTER_EXTRA
import ru.practicum.android.diploma.filter.ui.sector.adapter.SectorsAdapter
import ru.practicum.android.diploma.search.domain.utils.ResponseData

@SuppressLint("NotifyDataSetChanged")
class SectorFragment : Fragment() {

    private var _binding: FragmentSectorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectorViewModel by viewModel()

    private val sectorsAdapter: SectorsAdapter by lazy {
        SectorsAdapter { sector ->
            viewModel.changeSector(sector)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSectorBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFilterFromBundle()
        initializeOther()
        initializeAdapter()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFilterFromBundle() {
        val filter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(FILTER_EXTRA, Filter::class.java) as Filter
        } else {
            requireArguments().getSerializable(FILTER_EXTRA) as Filter
        }
        viewModel.setFilter(filter)
    }

    private fun initializeOther() {
        with(binding) {
            tbSector.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            etSearch.doOnTextChanged { text, _, _, _ ->
                if (viewModel.screenState.value is SectorState.Content) {
                    viewModel.search(text.toString())
                }
                ivClear.isVisible = !text.isNullOrEmpty()
                ivSearch.isVisible = text.isNullOrEmpty()
            }

            ivClear.setOnClickListener {
                etSearch.text.clear()
            }
        }
    }

    private fun initializeAdapter() {
        binding.rvSectors.adapter = sectorsAdapter
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SectorState.Content -> showContent(screenState)
                is SectorState.Error -> showError(screenState)
                SectorState.Loading -> showLoading()
            }
        }
    }

    private fun showContent(screenState: SectorState.Content) {
        with(binding) {
            if (screenState.sectorsList.isNotEmpty()) {
                sectorsAdapter.setItems(screenState.sectorsList)
                tvPlaceholder.isVisible = false
            } else {
                sectorsAdapter.clearItems()
                tvPlaceholder.setText(R.string.sector_no_sector)
                tvPlaceholder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.placeholder_no_results_cat, 0, 0)
                tvPlaceholder.isVisible = true
            }
            flSearch.isVisible = true
            rvSectors.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError(screenState: SectorState.Error) {
        with(binding) {
            if (screenState.error == ResponseData.ResponseError.NO_INTERNET) {
                tvPlaceholder.setText(R.string.search_no_internet)
                tvPlaceholder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.placeholder_no_internet, 0, 0)
            } else {
                tvPlaceholder.setText(R.string.sector_error)
                tvPlaceholder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.placeholder_no_results_carpet, 0, 0)
            }
            tvPlaceholder.isVisible = true
            flSearch.isVisible = true
            rvSectors.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.isVisible = true
            flSearch.isVisible = false
            rvSectors.isVisible = false
            tvPlaceholder.isVisible = false
        }
    }

}
