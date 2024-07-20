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

    private fun squeezeFieldOutOfFilter(emptyCountry: Boolean = false, emptyRegion: Boolean = false) {
        val originalFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(FILTER_EXTRA, Filter::class.java) as Filter
        } else {
            requireArguments().getSerializable(FILTER_EXTRA) as Filter
        }
        val modifiedFilter = originalFilter.copy(
            country = if (emptyCountry) null else originalFilter.country,
            region = if (emptyRegion) null else originalFilter.region
        )
        viewModel.setFilter(modifiedFilter)
        val country = modifiedFilter.country?.name ?: "null"
        val region = modifiedFilter.region?.name ?: "null"
        updateUI(country, region)
    }

    private fun initializeListeners() {
        binding.filterInputCountryLayout.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_countryFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
        binding.filterInputRegionLayout.setOnClickListener {
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

        binding.tvCountryClear.setOnClickListener {
            squeezeFieldOutOfFilter(emptyCountry = true, emptyRegion = true)
        }

        binding.tvRegionClear.setOnClickListener {
            squeezeFieldOutOfFilter(emptyCountry = false, emptyRegion = true)
        }
    }

    private fun updateUI(country: String?, region: String?) {
        if (country != "null") {
            binding.tvCountryLabel.setTextColor(resources.getColor(R.color.black_white, null))
            binding.tvCountryLabel.textSize = SMALL_TEXT_SIZE
            binding.tvCountryValue.text = country
            binding.tvCountryValue.visibility = View.VISIBLE
            binding.btLocationSelect.visibility = View.VISIBLE
            binding.tvCountryClear.visibility = View.VISIBLE
            binding.tvCountryLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            binding.tvCountryLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvCountryLabel.textSize = BIG_TEXT_SIZE
            binding.tvCountryValue.visibility = View.GONE
            binding.btLocationSelect.visibility = View.GONE
            binding.tvCountryClear.visibility = View.GONE
            binding.tvCountryLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward, 0)
        }

        if (region != "null") {
            binding.tvRegionLabel.setTextColor(resources.getColor(R.color.black_white, null))
            binding.tvRegionLabel.textSize = SMALL_TEXT_SIZE
            binding.tvRegionValue.text = region
            binding.tvRegionValue.visibility = View.VISIBLE
            binding.tvRegionClear.visibility = View.VISIBLE
            binding.tvRegionLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            binding.tvRegionLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvRegionLabel.textSize = BIG_TEXT_SIZE
            binding.tvRegionValue.visibility = View.GONE
            binding.tvRegionClear.visibility = View.GONE
            binding.tvRegionLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward, 0)
        }
    }

    companion object {
        private const val BIG_TEXT_SIZE = 16F
        private const val SMALL_TEXT_SIZE = 12F
    }
}
