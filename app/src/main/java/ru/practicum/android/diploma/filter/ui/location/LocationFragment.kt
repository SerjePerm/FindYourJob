package ru.practicum.android.diploma.filter.ui.location

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        val country = filter.country?.name
        val region = filter.region?.name
        updateUI(country, region)
    }

    private fun initializeListeners() {
        binding.etCountryName.setOnClickListener {
            findNavController().navigate(
                resId = R.id.action_locationFragment_to_countryFragment,
                args = createArguments(viewModel.newFilter)
            )
        }

        binding.etRegionName.setOnClickListener {
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

        binding.ivCountryEndIcon.setOnClickListener {
            // Добавить логику очистки страны
            // заменить крестик на стрелочку
            // сделать элемент некликабельным
            // вернуть цвет подсказки на серый
        }

        binding.ivRegionEndIcon.setOnClickListener {
            // Добавить логику очистки региона
            // заменить крестик на стрелочку
            // сделать элемент некликабельным
            // вернуть цвет подсказки на серый
        }
    }

    private fun updateUI(country: String?, region: String?) {
        if (country != null) {
            binding.etCountryName.setText(country)
            binding.tilCountryLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.black)
            binding.ivCountryEndIcon.isClickable = true
            binding.ivCountryEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
        }

        if (region != null) {
            binding.etRegionName.setText(region)
            binding.tilRegionLabel.defaultHintTextColor =
                ContextCompat.getColorStateList(requireContext(), R.color.black)
            binding.ivRegionEndIcon.isClickable = true
            binding.ivRegionEndIcon.setImageDrawable(requireContext().getDrawable(R.drawable.ic_clear))
        }
    }
}
