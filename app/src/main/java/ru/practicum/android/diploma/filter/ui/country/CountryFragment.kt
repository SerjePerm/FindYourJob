package ru.practicum.android.diploma.filter.ui.country

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.ui.country.adapter.CountriesAdapter
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.FILTER_EXTRA
import ru.practicum.android.diploma.filter.ui.filter.FilterFragment.Companion.createArguments

class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountryViewModel by viewModel()

    private val countriesAdapter: CountriesAdapter by lazy {
        CountriesAdapter { country ->
            viewModel.changeCountry(country)
            findNavController().navigate(
                resId = R.id.action_countryFragment_to_locationFragment,
                args = createArguments(viewModel.newFilter)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFilterFromBundle()
        initializeListeners()
        initializeAdapter()
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
    }

    private fun initializeListeners() {
        binding.tbCountry.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initializeAdapter() {
        binding.rvCountries.adapter = countriesAdapter
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is CountryState.Content -> showContent(screenState)
                CountryState.Error -> showError()
                CountryState.Loading -> showLoading()
            }
        }
    }

    private fun showContent(screenState: CountryState.Content) {
        if (screenState.countriesList.isNotEmpty()) {
            countriesAdapter.setItems(screenState.countriesList)
            // placeholder hide
        } else {
            countriesAdapter.clearItems()
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
