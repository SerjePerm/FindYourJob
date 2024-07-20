package ru.practicum.android.diploma.filter.ui.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {

    var newFilter = Filter()

    fun setFilter(filterParam: Filter) {
        newFilter = filterParam
    }

    fun changeSalary(value: String) {
        newFilter = newFilter.copy(salary = value.toIntOrNull())
    }

    fun changeOnlyWithSalary(value: Boolean) {
        newFilter = newFilter.copy(onlyWithSalary = value)
    }

}
