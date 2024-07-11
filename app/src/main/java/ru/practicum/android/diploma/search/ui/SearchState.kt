package ru.practicum.android.diploma.search.ui

import ru.practicum.android.diploma.search.domain.models.Vacancy

sealed class SearchState {
    data object Loading : SearchState()
    data class Content(val results: List<Vacancy>, val foundVacancies: Int) : SearchState()
    data object Error : SearchState()
    data object Empty : SearchState()
}