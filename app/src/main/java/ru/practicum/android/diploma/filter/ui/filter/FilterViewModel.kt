package ru.practicum.android.diploma.filter.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {

    private var filter: Filter = filterInteractor.loadFilter()

    private var screenState = FilterScreenState(
        filter = filter,
        modified = false,
        isEmpty = isEmpty(),
    )
        set(value) {
            field = value
            _filterScreenStateLiveData.postValue(screenState)
        }

    private val _filterScreenStateLiveData = MutableLiveData(screenState)
    val filterScreenState: LiveData<FilterScreenState>
        get() = _filterScreenStateLiveData

    var location
        get() = filter.location
        set(value) {
            if (location != value) {
                filter = filter.copy(location = value)
                saveFilter()
            }
        }

    var sector
        get() = filter.sector
        set(value) {
            if (sector != value) {
                filter = filter.copy(sector = value)
                saveFilter()
            }
        }

    var salary: String
        get() = filter.salary.toString()
        set(value) {
            if (salary != value) {
                filter = filter.copy(salary = value.toIntOrNull())
                saveFilter()
            }
        }

    var onlyWithSalary: Boolean
        get() = filter.onlyWithSalary
        set(value) {
            if (onlyWithSalary != value) {
                filter = filter.copy(onlyWithSalary = value)
                saveFilter()
            }
        }

    private fun saveFilter() {
        filterInteractor.saveFilter(filter)
        screenState = screenState.copy(filter = filter, modified = true, isEmpty = isEmpty())
    }

    private fun isEmpty() = filter == Filter()

    fun resetFilter() {
        filter = Filter()
        saveFilter()
    }
}
