package ru.practicum.android.diploma.filter.ui.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor

class CountryViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    init {
        println("CountryViewModel created")
    }

    fun loadCountries() {
        println("load countries")
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getCountries()
        }
    }

}
