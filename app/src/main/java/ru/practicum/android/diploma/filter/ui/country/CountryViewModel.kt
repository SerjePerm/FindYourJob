package ru.practicum.android.diploma.filter.ui.country

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favourites.ui.FavouritesState
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import java.sql.SQLException

class CountryViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<CountryState>(CountryState.Loading)
    val screenState: LiveData<CountryState> = _screenState

    init {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun loadCountries() {
        println("load countries")
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getCountries()
        }
    }

}
