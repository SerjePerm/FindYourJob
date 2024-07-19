package ru.practicum.android.diploma.filter.ui.filter

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Sector

class FilterViewModel(
    private val filterInteractor: FilterInteractor,
) : ViewModel() {

    var filter = Filter()

    init {
        val country = Country(1, "Test country")
        val region = Region(2, "Test region")
        val sector = Sector(3, "Test sector")
        filter = Filter(country, region, sector, 777, true)
    }

}
