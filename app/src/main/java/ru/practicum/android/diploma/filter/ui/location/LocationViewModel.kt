package ru.practicum.android.diploma.filter.ui.location

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Filter

class LocationViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    var newFilter = Filter()

    fun setFilter(filterParam: Filter) {
        newFilter = filterParam
    }

    fun clearCountry() {
        newFilter = newFilter.copy(country = null, region = null)
    }

    fun clearRegion() {
        newFilter = newFilter.copy(region = null)
    }

}
