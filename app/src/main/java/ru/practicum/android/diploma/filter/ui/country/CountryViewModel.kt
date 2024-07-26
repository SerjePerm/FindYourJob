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
                    is ResponseData.Data -> sortAndPostData(data)
                    is ResponseData.Error -> _screenState.postValue(CountryState.Error(data.error))
                }
            }
        }
    }

    private fun sortAndPostData(data: ResponseData.Data<List<Country>>) {
        val firstList: MutableList<Country> = ArrayList()
        val secondList: MutableList<Country> = ArrayList()
        for (item in data.value) {
            if (item.id < COUNTRIES_FILTER_ID) {
                firstList.add(item)
            } else {
                secondList.add(item)
            }
        }
        _screenState.postValue(CountryState.Content(firstList + secondList))
    }

    private companion object {
        const val COUNTRIES_FILTER_ID = 1000
    }

}
