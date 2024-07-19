package ru.practicum.android.diploma.filter.ui.region

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentRegionBinding
import ru.practicum.android.diploma.filter.ui.region.adapter.RegionsAdapter

class RegionFragment : Fragment() {

    private var _binding: FragmentRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegionViewModel by viewModel()

    private val regionsAdapter: RegionsAdapter by lazy {
        RegionsAdapter { region ->
            val bundle = Bundle().apply {
                putString("selectedRegion", region.name)
            }
            findNavController().navigate(R.id.action_regionFragment_to_locationFragment, bundle)
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
        initializeListeners()
        initializeAdapter()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeListeners() {
        binding.tbRegion.setNavigationOnClickListener {
            findNavController().navigateUp()
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

    private fun showContent(screenState: RegionState.Content) {
        if (screenState.regionsList.isNotEmpty()) {
            regionsAdapter.setItems(screenState.regionsList)
            // placeholder hide
        } else {
            regionsAdapter.clearItems()
            // placeholder empty results
        }
        // progressBar hide
    }

    private fun showError() {
        println("error")
    }

    private fun showLoading() {
        println("loading")
    }

}
