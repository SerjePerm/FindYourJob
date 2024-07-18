package ru.practicum.android.diploma.filter.ui.sector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSectorBinding
import ru.practicum.android.diploma.filter.ui.sector.adapter.SectorsAdapter

class SectorFragment : Fragment() {

    private var _binding: FragmentSectorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SectorViewModel by viewModel()

    private val sectorsAdapter: SectorsAdapter by lazy {
        SectorsAdapter { sector ->
            println("clicked: ${sector.name}")
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
        initializeListeners()
        initializeAdapter()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeListeners() {
        binding.tbSector.setNavigationOnClickListener {
            findNavController().navigateUp()
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
            // placeholder hide
        } else {
            sectorsAdapter.clearItems()
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
