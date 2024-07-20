package ru.practicum.android.diploma.filter.ui.region

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class RegionViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<RegionState>(RegionState.Loading)
    val screenState: LiveData<RegionState> = _screenState

    var newFilter = Filter()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            if (newFilter.country != null) {
                filterInteractor.getRegions(newFilter.country!!.id).collect { data ->
                    when (data) {
                        is ResponseData.Data -> _screenState.postValue(RegionState.Content(data.value))
                        is ResponseData.Error -> _screenState.postValue(RegionState.Error)
                    }
                }
            } else {
                filterInteractor.getAllRegions().collect { data ->
                    when (data) {
                        is ResponseData.Data -> _screenState.postValue(RegionState.Content(data.value))
                        is ResponseData.Error -> _screenState.postValue(RegionState.Error)
                    }
                }
            }

        }
    }

    fun setFilter(filterParam: Filter) {
        newFilter = filterParam
    }

    fun changeRegion(region: Region) {
        newFilter = newFilter.copy(region = region)
    }

}
