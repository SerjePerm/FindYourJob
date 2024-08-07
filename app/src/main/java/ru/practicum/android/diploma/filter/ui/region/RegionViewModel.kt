package ru.practicum.android.diploma.filter.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Location
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class RegionViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<RegionState>(RegionState.Loading)
    val screenState: LiveData<RegionState> = _screenState

    private val countriesList: MutableList<Country> = ArrayList()
    private val originalList: MutableList<Region> = ArrayList()
    private val filteredList: MutableList<Region> = ArrayList()

    var location: Location = Location()

    var region: Region?
        get() = location.region
        set(value) {
            location = location.copy(
                country = findCountry(value?.parentId),
                region = value
            )
        }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getCountries().collect { data ->
                when (data) {
                    is ResponseData.Data -> {
                        countriesList.addAll(data.value)
                    }

                    is ResponseData.Error -> {
                        println("Error on getting countries list")
                    }
                }
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val country = location.country
            if (country != null) {
                filterInteractor.getRegions(country.id).collect { data ->
                    when (data) {
                        is ResponseData.Data -> {
                            _screenState.postValue(RegionState.Content(data.value.sortedBy { it.name }))
                            originalList.addAll(data.value)
                        }

                        is ResponseData.Error -> _screenState.postValue(RegionState.Error(data.error))
                    }
                }
            } else {
                filterInteractor.getAllRegions().collect { data ->
                    when (data) {
                        is ResponseData.Data -> {
                            val result = data.value
                                .filter { it.parentId < COUNTRIES_FILTER_ID }.sortedBy { it.name }
                            _screenState.postValue(RegionState.Content(result))
                            originalList.addAll(result)
                        }

                        is ResponseData.Error -> _screenState.postValue(RegionState.Error(data.error))
                    }
                }
            }
        }
    }

    fun search(searchText: String?) {
        filteredList.clear()
        if (searchText.isNullOrEmpty()) {
            _screenState.postValue(RegionState.Content(originalList))
        } else {
            for (item in originalList) {
                if (item.name.contains(searchText, true)) {
                    filteredList.add(item)
                }
            }
            _screenState.postValue(RegionState.Content(filteredList))
        }
    }

    private fun findCountry(id: Int?): Country {
        id.let {
            val result = countriesList.filter { it.id == id }.first()
            return result
        }
    }

    private companion object {
        const val COUNTRIES_FILTER_ID = 1000
    }

}
