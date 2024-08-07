package ru.practicum.android.diploma.filter.ui.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentLocationBinding
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Location
import ru.practicum.android.diploma.filter.ui.country.CountryFragment
import ru.practicum.android.diploma.filter.ui.region.RegionFragment
import ru.practicum.android.diploma.utils.DeprecationUtils.serializable

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLocationFromBundle()
        initializeListeners()
        initializeObservers()
        initializeFragmentResultListeners()
    }

    private fun initializeObservers() {
        viewModel.locationScreenState.observe(viewLifecycleOwner, ::updateUI)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setLocationFromBundle() {
        val location = requireArguments().serializable<Location>(LOCATION_EXTRA)
        if (location != null) {
            viewModel.location = location
        }
    }

    private fun initializeListeners() {
        binding.etCountryName.setOnClickListener {
            navigateToCountry()
        }

        binding.etRegionName.setOnClickListener {
            navigateToRegion()
        }
        binding.tbLocation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.btLocationSelect.setOnClickListener {
            closeWithLocation()
        }

        binding.ivCountryEndIcon.setOnClickListener {
            if (viewModel.country != null) {
                viewModel.country = null
                clearCountryUI()
                clearRegionUI()
            } else {
                navigateToCountry()
            }
        }

        binding.ivRegionEndIcon.setOnClickListener {
            if (viewModel.region != null) {
                viewModel.region = null
                clearRegionUI()
            } else {
                navigateToRegion()
            }
        }
    }

    private fun initializeFragmentResultListeners() {
        setFragmentResultListener(CountryFragment.COUNTRY_REQUEST_KEY) { _, bundle ->
            bundle.serializable<Country>(CountryFragment.COUNTRY_EXTRA)?.let {
                viewModel.country = it
                compareCountryAndRegion()
            }
        }
        setFragmentResultListener(RegionFragment.REGION_REQUEST_KEY) { _, bundle ->
            bundle.serializable<Location>(RegionFragment.LOCATION_EXTRA)?.let {
                viewModel.location = it
            }
        }
    }

    private fun closeWithLocation() {
        setFragmentResult(LOCATION_REQUEST_KEY, bundleOf(LOCATION_EXTRA to viewModel.location))
        findNavController().popBackStack()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateUI(location: Location?) {
        val country = location?.country?.name
        val region = location?.region?.name
        if (country != null) {
            binding.etCountryName.setText(country)
            binding.tilCountryLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
            binding.ivCountryEndIcon.isClickable = true
            binding.ivCountryEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear_for_button))
        } else {
            clearCountryUI()
        }

        if (region != null) {
            binding.etRegionName.setText(region)
            binding.tilRegionLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), getOnPrimaryColor())
            binding.ivRegionEndIcon.isClickable = true
            binding.ivRegionEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear_for_button))
        } else {
            clearRegionUI()
        }

        binding.btLocationSelect.isVisible = location != Location()
    }

    private fun navigateToCountry() {
        findNavController().navigate(
            resId = R.id.action_locationFragment_to_countryFragment,
            args = bundleOf(CountryFragment.COUNTRY_EXTRA to viewModel.country)
        )
    }

    private fun navigateToRegion() {
        findNavController().navigate(
            resId = R.id.action_locationFragment_to_regionFragment,
            args = bundleOf(RegionFragment.LOCATION_EXTRA to viewModel.location)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clearCountryUI() {
        with(binding) {
            ivCountryEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
            etCountryName.text?.clear()
            tilCountryLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clearRegionUI() {
        with(binding) {
            ivRegionEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
            etRegionName.text?.clear()
            tilRegionLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
        }
    }

    private fun getOnPrimaryColor(): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        return typedValue.resourceId
    }

    private fun compareCountryAndRegion() {
        if (viewModel.region?.parentId != viewModel.country?.id) {
            viewModel.region = null
            clearRegionUI()
        }
    }

    companion object {
        const val LOCATION_EXTRA = "location"
        const val LOCATION_REQUEST_KEY = "location_request"
        fun createArguments(location: Location): Bundle = bundleOf(LOCATION_EXTRA to location)
    }
}
