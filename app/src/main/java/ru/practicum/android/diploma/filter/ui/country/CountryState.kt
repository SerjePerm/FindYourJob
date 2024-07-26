package ru.practicum.android.diploma.filter.ui.country

import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.domain.utils.ResponseData

sealed class CountryState {
    data object Loading : CountryState()
    data class Content(val countriesList: List<Country>) : CountryState()
    data class Error(val error: ResponseData.ResponseError) : CountryState()
}
