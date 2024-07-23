package ru.practicum.android.diploma.filter.ui.filter

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
import ru.practicum.android.diploma.filter.domain.models.Filter
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
        initializeLocationAndSectorListeners()
        initializeOtherListeners()
        initializeObservers()
        initializeFragmentResultListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeLocationAndSectorListeners() {
        binding.etLocationName.setOnClickListener {
            navigateToLocation()
        }
        binding.etSectorName.setOnClickListener {
            navigateToSector()
        }
        binding.ivLocationEndIcon.setOnClickListener {
            if (viewModel.location.country == null) {
                navigateToLocation()
            } else {
                viewModel.location = Location(country = null, region = null)
            }
        }
        binding.ivSectorEndIcon.setOnClickListener {
            if (viewModel.sector == null) {
                navigateToSector()
            } else {
                viewModel.sector = null
                renderSector(null)
            }
        }
    }

    private fun initializeOtherListeners() {
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
            viewModel.setSalary(text.toString())
            binding.ivClear.isVisible = !text.isNullOrEmpty()
        }
        binding.ivClear.setOnClickListener {
            binding.etSalary.text?.clear()
            viewModel.setSalary("")
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
                btFilterApply.isVisible = it.modified
                btFilterReset.isVisible = !it.isEmpty

                val country = it.filter.location.country?.name
                val region = it.filter.location.region?.name
                renderLocation(country, region)
                renderSector(it.filter.sector?.name)

                if (!it.modified) {
                    renderSalary(it.filter)
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

    private fun renderLocation(country: String?, region: String?) {
        if (country != null && region != null) {
            binding.etLocationName.setText(requireContext().getString(R.string.filter_location, country, region))
            binding.tilLocationLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
        } else if (country != null) {
            binding.etLocationName.setText(country)
            binding.tilLocationLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
        } else if (region != null) {
            binding.etLocationName.setText(region)
            binding.tilLocationLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
        }
        if (country == null) {
            binding.tilLocationLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
            binding.etLocationName.text?.clear()
            binding.ivLocationEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
        } else {
            binding.ivLocationEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear_for_button))
        }
    }

    private fun renderSector(sector: String?) {
        if (sector != null) {
            binding.tilSectorLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
            binding.tilSectorLabel.isEnabled = true
            binding.etSectorName.setText(sector)
            binding.ivSectorEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear_for_button))
        } else {
            binding.tilSectorLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
            binding.etSectorName.text?.clear()
            binding.ivSectorEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
        }
    }

    private fun renderSalary(filter: Filter) {
        if (filter.salary != null) {
            binding.etSalary.setText(filter.salary.toString())
        }
        binding.checkBox.isChecked = filter.onlyWithSalary
    }

    private fun getOnPrimaryColor(): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        return typedValue.resourceId
    }

    private fun navigateToLocation() {
        findNavController().navigate(
            resId = R.id.action_filterFragment_to_locationFragment,
            args = LocationFragment.createArguments(viewModel.location)
        )
    }

    private fun navigateToSector() {
        findNavController().navigate(
            resId = R.id.action_filterFragment_to_sectorFragment,
            args = SectorFragment.createArguments(viewModel.sector)
        )
    }

}
