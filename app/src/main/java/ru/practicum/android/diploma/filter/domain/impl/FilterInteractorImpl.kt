package ru.practicum.android.diploma.filter.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.api.FilterStoreRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Filter
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
    private val filterStoreRepository: FilterStoreRepository,
) : FilterInteractor {

    override fun loadFilter(): Filter =
        filterStoreRepository.load()

    override fun saveFilter(filter: Filter) =
        filterStoreRepository.save(filter)

    override fun getCountries(): Flow<ResponseData<List<Country>>> =
        filterRepository.getCountries()

    override fun getRegions(id: Int): Flow<ResponseData<List<Region>>> =
        filterRepository.getRegions(id)

    override fun getAllRegions(): Flow<ResponseData<List<Region>>> =
        filterRepository.getAllRegions()

    override fun getSectors(): Flow<ResponseData<List<Sector>>> =
        filterRepository.getSectors()
}
