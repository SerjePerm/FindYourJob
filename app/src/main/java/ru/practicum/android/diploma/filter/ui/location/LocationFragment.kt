package ru.practicum.android.diploma.filter.ui.location

import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentLocationBinding
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.FILTER_EXTRA
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.createArguments

class LocationFragment : Fragment() {

    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LocationViewModel by viewModel()

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack(R.id.filterFragment, false)
        }
    }

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
        setFilterFromBundle()
        initializeListeners()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
        updateUI(
            country = filter.country?.name,
            region = filter.region?.name
        )
    }

    private fun initializeListeners() {
        binding.etCountryName.setOnClickListener {
            navigateToCountry()
        }

        binding.etRegionName.setOnClickListener {
            navigateToRegion()
        }
        binding.tbLocation.setNavigationOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_filterFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
        binding.btLocationSelect.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_filterFragment,
                args = createArguments(viewModel.newFilter)
            )
        }

        binding.ivCountryEndIcon.setOnClickListener {
            if (viewModel.newFilter.country != null) {
                viewModel.clearCountry()
                clearCountryUI()
                clearRegionUI()
            } else {
                navigateToCountry()
            }
        }

        binding.ivRegionEndIcon.setOnClickListener {
            if (viewModel.newFilter.region != null) {
                viewModel.clearRegion()
                clearRegionUI()
            } else {
                navigateToRegion()
            }
        }
    }

    private fun updateUI(country: String?, region: String?) {
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
    }

    private fun navigateToCountry() {
        findNavController().navigate(
            resId = R.id.action_locationFragment_to_countryFragment,
            args = createArguments(viewModel.newFilter)
        )
    }

    private fun navigateToRegion() {
        findNavController().navigate(
            resId = R.id.action_locationFragment_to_regionFragment,
            args = createArguments(viewModel.newFilter)
        )
    }

    private fun clearCountryUI() {
        with(binding) {
            ivCountryEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
            etCountryName.setText("")
            tilCountryLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
        }
    }

    private fun clearRegionUI() {
        with(binding) {
            ivRegionEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_forward))
            etRegionName.setText("")
            tilRegionLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
        }
    }

    private fun getOnPrimaryColor(): Int {
        val typedValue = TypedValue()
        requireContext().theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, typedValue, true)
        return typedValue.resourceId
    }
}
