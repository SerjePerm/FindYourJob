package ru.practicum.android.diploma.filter.domain.api

import ru.practicum.android.diploma.filter.domain.models.Country

interface FilterRepository {
    suspend fun getCountries(): List<Country>
}
