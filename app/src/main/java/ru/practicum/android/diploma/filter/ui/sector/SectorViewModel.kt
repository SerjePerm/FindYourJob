package ru.practicum.android.diploma.filter.ui.sector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class SectorViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<SectorState>(SectorState.Loading)
    val screenState: LiveData<SectorState> = _screenState

    private val originalList: MutableList<Sector> = ArrayList()
    private val filteredList: MutableList<Sector> = ArrayList()

    private var latestSearchText: String? = null

    var sector: Sector? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getSectors().collect { data ->
                when (data) {
                    is ResponseData.Data -> {
                        _screenState.postValue(SectorState.Content(data.value))
                        originalList.addAll(data.value)
                    }
                    is ResponseData.Error -> _screenState.postValue(SectorState.Error(data.error))
                }
            }
        }
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

        sector?.let { sector ->
            val indexChecked = filteredList.indexOfFirst { it.id == sector.id }
            if (indexChecked >= 0) {
                val item = filteredList[indexChecked]
                filteredList[indexChecked] = item.copy(isChecked = true)
            }
        }
        _screenState.postValue(SectorState.Content(filteredList))
    }

}
