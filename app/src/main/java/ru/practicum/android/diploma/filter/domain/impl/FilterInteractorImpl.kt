package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class FilterInteractorImpl(
    private val filterRepository: FilterRepository
) : FilterInteractor {

    override fun getCountries(): Flow<ResponseData<List<Country>>> {
        return filterRepository.getCountries()
    }

}
