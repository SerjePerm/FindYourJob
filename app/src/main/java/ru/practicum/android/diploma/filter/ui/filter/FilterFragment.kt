package ru.practicum.android.diploma.filter.ui.filter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filter.domain.models.Filter

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
        setFilterFromBundle()
        initializeListeners()
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
        with(binding) {
            val country = filter.country?.name ?: "null"
            val region = filter.region?.name ?: "null"
            val location = "$country, $region"
            val sector = filter.sector?.name ?: "null"
            etSalary.setText(filter.salary.toString())
            checkBox.isChecked = filter.onlyWithSalary
            updateLocationUI(location)
            updateSectorUI(sector)
        }
    }

    private fun squeezeFieldOutOfFilter(emptyLocation: Boolean = false, emptySector: Boolean = false) {
        val originalFilter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getSerializable(FILTER_EXTRA, Filter::class.java) as Filter
        } else {
            requireArguments().getSerializable(FILTER_EXTRA) as Filter
        }
        val modifiedFilter = originalFilter.copy(
            country = if (emptyLocation) null else originalFilter.country,
            region = if (emptyLocation) null else originalFilter.region,
            sector = if (emptySector) null else originalFilter.sector
        )
        viewModel.setFilter(modifiedFilter)
        val country = modifiedFilter.country?.name ?: "null"
        val region = modifiedFilter.region?.name ?: "null"
        val location = "$country, $region"
        val sector = modifiedFilter.sector?.name ?: "null"
        if (emptyLocation) updateLocationUI(location)
        if (emptySector) updateSectorUI(sector)
    }

    private fun initializeListeners() {
        binding.filterInputLocationLayout.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_filterFragment_to_locationFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
        binding.filterInputSectorLayout.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_filterFragment_to_sectorFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
        binding.tbSettingsFilter.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_filterFragment_to_searchFragment)
        }
        binding.btFilterApply.setOnClickListener {
            viewModel.saveFilter(reset = false)
            findNavController().navigate(R.id.action_filterFragment_to_searchFragment)
        }
        binding.btFilterReset.setOnClickListener {
            viewModel.saveFilter(reset = true)
            findNavController().navigate(R.id.action_filterFragment_to_searchFragment)
        }
        binding.etSalary.doOnTextChanged { text, _, _, _ ->
            viewModel.changeSalary(text.toString())
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeOnlyWithSalary(isChecked)
        }

        binding.tvLocationClear.setOnClickListener {
            squeezeFieldOutOfFilter(emptyLocation = true, emptySector = false)
        }

        binding.tvSectorClear.setOnClickListener {
            squeezeFieldOutOfFilter(emptyLocation = false, emptySector = true)
        }
    }

    private fun initializeObservers() {
        viewModel.isChanges.observe(viewLifecycleOwner) { isChanges ->
            binding.btFilterApply.isVisible = isChanges
            binding.btFilterReset.isVisible = viewModel.newFilter != Filter()
        }
    }

    private fun updateLocationUI(location: String?) {
        val invalidLocations = setOf(", ", "null, null", ", null", "null, ")

        if (location != null && location !in invalidLocations) {
            binding.tvLocationLabel.setTextColor(resources.getColor(R.color.black, null))
            binding.tvLocationLabel.textSize = SMALL_TEXT_SIZE
            binding.tvLocationValue.text = location
            binding.tvLocationValue.visibility = View.VISIBLE
            binding.tvLocationClear.visibility = View.VISIBLE
            binding.tvLocationLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            binding.btFilterApply.visibility = View.VISIBLE
            binding.btFilterReset.visibility = View.VISIBLE
        } else {
            binding.tvLocationLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvLocationLabel.textSize = BIG_TEXT_SIZE
            binding.tvLocationValue.visibility = View.GONE
            binding.tvLocationClear.visibility = View.GONE
            binding.tvLocationLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward, 0)
            binding.btFilterApply.visibility = View.GONE
            binding.btFilterReset.visibility = View.GONE
        }
    }

    private fun updateSectorUI(sector: String?) {
        if (sector != "null") {
            binding.tvSectorLabel.setTextColor(resources.getColor(R.color.black, null))
            binding.tvSectorLabel.textSize = SMALL_TEXT_SIZE
            binding.tvSectorValue.text = sector
            binding.tvSectorValue.visibility = View.VISIBLE
            binding.tvSectorClear.visibility = View.VISIBLE
            binding.tvSectorLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            binding.tvSectorLabel.setTextColor(resources.getColor(R.color.gray, null))
            binding.tvSectorLabel.textSize = BIG_TEXT_SIZE
            binding.tvSectorValue.visibility = View.GONE
            binding.tvSectorClear.visibility = View.GONE
            binding.tvSectorLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_forward, 0)
        }
    }

    companion object {
        const val FILTER_EXTRA = "FILTER_EXTRA"
        fun createArguments(filter: Filter): Bundle = bundleOf(FILTER_EXTRA to filter)
        private const val BIG_TEXT_SIZE = 16F
        private const val SMALL_TEXT_SIZE = 12F
    }

}
