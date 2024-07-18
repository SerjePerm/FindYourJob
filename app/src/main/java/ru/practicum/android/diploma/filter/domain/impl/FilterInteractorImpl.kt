package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.api.FilterStoreRepository
import ru.practicum.android.diploma.filter.domain.models.Filter

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
    private val filterStoreRepository: FilterStoreRepository,
) : FilterInteractor {
    override fun loadFilter(): Filter = filterStoreRepository.load()
    override fun saveFilter(filter: Filter) = filterStoreRepository.save(filter)
    override fun getCountries(): Flow<ResponseData<List<Country>>> = filterRepository.getCountries()
}
