package ru.practicum.android.diploma.filter.ui.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentCountryBinding
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.ui.country.adapter.CountriesAdapter
import ru.practicum.android.diploma.utils.DeprecationUtils.serializable

class CountryFragment : Fragment() {

    private var _binding: FragmentCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CountryViewModel by viewModel()

    private val countriesAdapter: CountriesAdapter by lazy {
        CountriesAdapter { country ->
            setFragmentResult(COUNTRY_REQUEST_KEY, bundleOf(COUNTRY_EXTRA to country))
            findNavController().popBackStack()
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

        setCountryFromBundle()

        initializeListeners()
        initializeAdapter()
        initializeObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCountryFromBundle() {
        viewModel.country = requireArguments().serializable<Country>(COUNTRY_EXTRA)
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

    companion object {
        const val COUNTRY_REQUEST_KEY = "country_request"
        const val COUNTRY_EXTRA = "country"
    }
}
