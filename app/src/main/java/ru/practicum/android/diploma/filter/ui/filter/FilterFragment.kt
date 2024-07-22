package ru.practicum.android.diploma.filter.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.models.Location
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.filter.ui.location.LocationFragment
import ru.practicum.android.diploma.filter.ui.sector.SectorFragment
import ru.practicum.android.diploma.search.ui.SearchFragment
import ru.practicum.android.diploma.utils.DeprecationUtils.serializable

class FilterFragment : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
        initializeObservers()
        initializeFragmentResultListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeListeners() {
        binding.btLocation.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_filterFragment_to_locationFragment,
                args = LocationFragment.createArguments(viewModel.location)
            )
        }
        binding.btSector.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_filterFragment_to_sectorFragment,
                args = SectorFragment.createArguments(viewModel.sector)
            )
        }
        binding.tbSettingsFilter.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btFilterApply.setOnClickListener {
            setFragmentResult(SearchFragment.FILTERS_KEY, bundleOf(SearchFragment.FILTERS_EXTRA to true))
            findNavController().popBackStack()
        }

        binding.btFilterReset.setOnClickListener {
            viewModel.resetFilter()
            findNavController().popBackStack()
        }
        binding.etSalary.doOnTextChanged { text, _, _, _ ->
            viewModel.salary = text.toString()
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onlyWithSalary = isChecked
        }
    }

    private fun initializeObservers() {
        viewModel.filterScreenState.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(filterScreenState: FilterScreenState?) {
        filterScreenState?.let {
            with(binding) {
                if (!it.modified) {
                    etSalary.setText(it.filter.salary.toString())
                    checkBox.isChecked = it.filter.onlyWithSalary
                }
                btFilterApply.isVisible = it.modified
                btFilterReset.isVisible = !it.isEmpty

                it.filter.location.let { location ->
                    val country = location.country?.name ?: "null"
                    val region = location.region?.name ?: "null"
                    btLocation.text = requireContext().getString(R.string.filter_location, country, region)
                    btSector.text = it.filter.sector?.name ?: "null"
                }
            }
        }
    }

    private fun initializeFragmentResultListeners() {
        setFragmentResultListener(LocationFragment.LOCATION_REQUEST_KEY) { _, bundle ->
            bundle.serializable<Location>(LocationFragment.LOCATION_EXTRA)?.let {
                viewModel.location = it
            }
        }

        setFragmentResultListener(SectorFragment.SECTOR_REQUEST_KEY) { _, bundle ->
            bundle.serializable<Sector>(SectorFragment.SECTOR_EXTRA)?.let {
                viewModel.sector = it
            }
        }
    }
}
