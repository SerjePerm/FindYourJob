package ru.practicum.android.diploma.filter.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {

    private var oldFilter = Filter()
    var newFilter = Filter()
    private val _isChanges = MutableLiveData(false)
    val isChanges: LiveData<Boolean> = _isChanges

    init {
        loadAndSetOldFilter()
    }

    private fun loadAndSetOldFilter() {
        oldFilter = filterInteractor.loadFilter()
        newFilter = oldFilter
    }

    private fun compareFilters() {
        if (newFilter != oldFilter) {
            _isChanges.value = true
        } else {
            _isChanges.value = false
        }
    }

    fun saveFilter(reset: Boolean) {
        if (!reset) {
            filterInteractor.saveFilter(newFilter)
        } else {
            filterInteractor.saveFilter(Filter())
        }
    }

    fun setFilter(filterParam: Filter) {
        newFilter = filterParam
        compareFilters()
    }

    fun changeSalary(value: String) {
        newFilter = newFilter.copy(salary = value.toIntOrNull())
        compareFilters()
    }

    fun changeOnlyWithSalary(value: Boolean) {
        newFilter = newFilter.copy(onlyWithSalary = value)
        compareFilters()
    }

}
