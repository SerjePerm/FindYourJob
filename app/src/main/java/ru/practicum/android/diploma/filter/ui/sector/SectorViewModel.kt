package ru.practicum.android.diploma.filter.ui.sector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class SectorViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<SectorState>(SectorState.Loading)
    val screenState: LiveData<SectorState> = _screenState

    private val originalList: MutableList<Sector> = ArrayList()
    private val filteredList: MutableList<Sector> = ArrayList()

    var newFilter = Filter()
    private var latestSearchText: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getSectors().collect { data ->
                when (data) {
                    is ResponseData.Data -> {
                        originalList.addAll(data.value)
                        postSectorsList()
                    }

                    is ResponseData.Error -> _screenState.postValue(SectorState.Error(data.error))
                }
            }
        }
    }

    fun setFilter(filterParam: Filter) {
        newFilter = filterParam
    }

    fun changeSector(sector: Sector) {
        newFilter = newFilter.copy(sector = sector)
        postSectorsList()
    }

    fun search(searchText: String?) {
        latestSearchText = searchText
        postSectorsList()
    }

    private fun postSectorsList() {
        filteredList.clear()
        if (latestSearchText.isNullOrEmpty()) {
            filteredList.addAll(originalList)
        }
        if (latestSearchText?.isNotBlank() == true) {
            for (item in originalList) {
                if (item.name.contains(latestSearchText!!, true)) {
                    filteredList.add(item)
                }
            }
        }
        newFilter.sector?.id.let { selectedId ->
            filteredList.forEachIndexed { index, sector ->
                if (sector.id == selectedId) {
                    filteredList[index] = filteredList[index].copy(isChecked = true)
                }
            }
        }
        _screenState.postValue(SectorState.Content(filteredList))
    }

}
