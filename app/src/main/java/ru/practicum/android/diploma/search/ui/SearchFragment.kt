package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.ui.adapter.VacanciesAdapter
import ru.practicum.android.diploma.utils.Placeholder
import ru.practicum.android.diploma.utils.StringConstants.SEARCH_LIST_STATE_KEY
import ru.practicum.android.diploma.utils.showPlaceholder
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModel()

    private val vacanciesAdapter: VacanciesAdapter by lazy {
        VacanciesAdapter { vacancy ->
            findNavController().navigate(
                R.id.action_searchFragment_to_vacancyFragment,
                VacancyFragment.createArguments(vacancy.id)
            )
        }
    }

    private var listState: Parcelable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        initializeAdapter()

        binding.ivFilter.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_filterFragment)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // реализация не требуется
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (viewModel.latestSearchText != s?.toString()) {
                    viewModel.search(s?.toString() ?: "")
                }

                binding.ivClear.isVisible = !s.isNullOrEmpty()
                binding.ivSearch.isVisible = s.isNullOrEmpty()
                showPlaceholder(requireContext(), Placeholder.HIDE)

            }

            override fun afterTextChanged(s: Editable?) {
                // реализация не требуется
            }
        }

        textWatcher.let { binding.etSearch.addTextChangedListener(it) }

        binding.ivClear.setOnClickListener {
            binding.etSearch.setText("")
            viewModel.latestSearchText = ""
            showEmpty()
        }

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(SEARCH_LIST_STATE_KEY)
        }

        viewModel.previousSearchResults?.let { showContent(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        listState = binding.rvVacancies.layoutManager?.onSaveInstanceState()
        outState.putParcelable(SEARCH_LIST_STATE_KEY, listState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeObservers() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchState.Content -> showContent(screenState)
                SearchState.Error -> {
                    showError()
                }
                SearchState.Loading -> {
                    showLoading()
                }
                SearchState.Empty -> {
                    showEmpty()
                }
            }
        }
    }

    private fun initializeAdapter() {
        binding.rvVacancies.adapter = vacanciesAdapter
        binding.rvVacancies.layoutManager = LinearLayoutManager(context)

        listState?.let {
            binding.rvVacancies.layoutManager?.onRestoreInstanceState(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(screenState: SearchState.Content) {
        vacanciesAdapter.clearItems()
        vacanciesAdapter.addItems(screenState.results)
        binding.rvVacancies.isVisible = true
        binding.numberVacancies.isVisible = true
        if (screenState.foundVacancies == 0) {
            binding.numberVacancies.text = resources.getString(R.string.search_no_vacancies)
            showPlaceholder(requireContext(), Placeholder.NO_RESULTS_CAT)
        } else {
            binding.numberVacancies.text = getVacanciesText(requireContext(), screenState.foundVacancies)
            showPlaceholder(requireContext(), Placeholder.HIDE)
        }
        binding.progressBar.isVisible = false

        listState?.let {
            binding.rvVacancies.layoutManager?.onRestoreInstanceState(it)
            listState = null
        }
    }

    private fun showError() {
        binding.progressBar.isVisible = false
        showPlaceholder(requireContext(), Placeholder.NO_INTERNET)
        hideKeyboard()
    }

    private fun showEmpty() {
        vacanciesAdapter.clearItems()
        binding.numberVacancies.isVisible = false
        binding.progressBar.isVisible = false
        showPlaceholder(requireContext(), Placeholder.SEARCH)
        hideKeyboard()
    }

    private fun showLoading() {
        vacanciesAdapter.clearItems()
        binding.progressBar.isVisible = true
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun getVacanciesText(context: Context, count: Int): String {
        return context.resources.getQuantityString(R.plurals.vacancies, count, count)
    }
}
