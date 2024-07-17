package ru.practicum.android.diploma.filter.ui.country

import ru.practicum.android.diploma.filter.domain.models.Country

sealed class CountryState {
    data object Loading : CountryState()
    data class Content(val countriesList: List<Country>) : CountryState()
    data object Error : CountryState()
}
