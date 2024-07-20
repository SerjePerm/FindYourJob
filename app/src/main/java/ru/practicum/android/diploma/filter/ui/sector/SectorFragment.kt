package ru.practicum.android.diploma.filter.ui.sector

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
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.createArguments
import ru.practicum.android.diploma.filter.ui.sector.adapter.SectorsAdapter

class SectorFragment : Fragment() {

    private var _binding: FragmentSectorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectorViewModel by viewModel()

    private val sectorsAdapter: SectorsAdapter by lazy {
        SectorsAdapter { sector ->
            viewModel.changeSector(sector)
            updateSelectButtonVisibility()
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
                viewModel.search(text.toString())
                ivClear.isVisible = !text.isNullOrEmpty()
                ivSearch.isVisible = text.isNullOrEmpty()
            }

            ivClear.setOnClickListener {
                etSearch.text.clear()
            }

            btSelect.setOnClickListener {
                findNavController().navigate(
                    resId = R.id.action_sectorFragment_to_filterFragment,
                    args = createArguments(viewModel.newFilter)
                )
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
                SectorState.Error -> showError()
                SectorState.Loading -> showLoading()
            }
        }
    }

    private fun showContent(screenState: SectorState.Content) {
        if (screenState.sectorsList.isNotEmpty()) {
            sectorsAdapter.setItems(screenState.sectorsList)
            updateSelectButtonVisibility()
        } else {
            sectorsAdapter.clearItems()
            updateSelectButtonVisibility()
        }
    }

    private fun showError() {
        println("error")
    }

    private fun showLoading() {
        println("loading")
    }

    private fun updateSelectButtonVisibility() {
        val hasSelectedItems = sectorsAdapter.getSelectedSectors().isNotEmpty()
        binding.btSelect.isVisible = hasSelectedItems
    }
}
