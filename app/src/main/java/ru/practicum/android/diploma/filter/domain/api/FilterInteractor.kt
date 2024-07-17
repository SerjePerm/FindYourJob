package ru.practicum.android.diploma.filter.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.domain.utils.ResponseData

interface FilterInteractor {
    fun getCountries(): Flow<ResponseData<List<Country>>>
}
