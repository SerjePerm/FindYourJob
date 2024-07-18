package ru.practicum.android.diploma.filter.ui.sector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class SectorViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<SectorState>(SectorState.Loading)
    val screenState: LiveData<SectorState> = _screenState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            filterInteractor.getSectors().collect { data ->
                when (data) {
                    is ResponseData.Data -> _screenState.postValue(SectorState.Content(data.value))
                    is ResponseData.Error -> _screenState.postValue(SectorState.Error)
                }
            }
        }
    }

}
