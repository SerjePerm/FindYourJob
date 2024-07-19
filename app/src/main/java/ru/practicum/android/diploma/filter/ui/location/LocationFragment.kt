package ru.practicum.android.diploma.filter.ui.location

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        /*
        val selectedCountry = arguments?.getString("selectedCountry")
        val selectedRegion = arguments?.getString("selectedRegion")
        updateUI(selectedCountry, selectedRegion)
         */
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
        val country = filter.country?.name ?: "null"
        val region = filter.region?.name ?: "null"
        updateUI(country, region)
    }

    private fun initializeListeners() {
        binding.tvCountryLabel.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_countryFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
        binding.tvRegionLabel.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_regionFragment,
                args = createArguments(viewModel.newFilter)
            )
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
    }

    private fun updateUI(country: String?, region: String?) {
        if (country != null) {
            binding.tvCountryLabel.text = resources.getString(R.string.location_country)
            binding.tvRegionLabel.setTextColor(resources.getColor(R.color.black, null))
            binding.tvCountryLabel.textSize = SMALL_TEXT_SIZE
            binding.tvCountryValue.text = country
            binding.tvCountryValue.visibility = View.VISIBLE
            binding.btLocationSelect.visibility = View.VISIBLE
        } else {
            binding.tvCountryLabel.text = resources.getString(R.string.location_country)
            binding.tvCountryLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvCountryLabel.textSize = BIG_TEXT_SIZE
            binding.btLocationSelect.visibility = View.GONE
        }

        if (region != null) {
            binding.tvRegionLabel.text = resources.getString(R.string.location_region)
            binding.tvRegionLabel.setTextColor(resources.getColor(R.color.black, null))
            binding.tvRegionLabel.textSize = SMALL_TEXT_SIZE
            binding.tvRegionValue.text = region
            binding.tvRegionValue.visibility = View.VISIBLE
        } else {
            binding.tvRegionLabel.text = resources.getString(R.string.location_region)
            binding.tvRegionLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvRegionLabel.textSize = BIG_TEXT_SIZE
        }
    }

    companion object {
        private const val BIG_TEXT_SIZE = 16F
        private const val SMALL_TEXT_SIZE = 12F
    }
}
