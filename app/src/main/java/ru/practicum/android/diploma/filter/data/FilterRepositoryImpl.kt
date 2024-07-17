package ru.practicum.android.diploma.filter.data

import ru.practicum.android.diploma.filter.data.dto.CountriesRequest
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.data.network.NetworkClient

class FilterRepositoryImpl(
    private val networkClient: NetworkClient
) : FilterRepository {

    override suspend fun getCountries(): List<Country> {
        networkClient.doRequest(CountriesRequest(emptyMap()))
        return emptyList<Country>()
    }

}
