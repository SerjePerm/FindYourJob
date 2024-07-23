package ru.practicum.android.diploma.filter.ui.country

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class CountryViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    var country: Country? = null

    private val _screenState = MutableLiveData<CountryState>(CountryState.Loading)
    val screenState: LiveData<CountryState> = _screenState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getCountries().collect { data ->
                when (data) {
                    is ResponseData.Data -> _screenState.postValue(CountryState.Content(data.value))
                    is ResponseData.Error -> _screenState.postValue(CountryState.Error(data.error))
                }
            }
        }
    }
}
