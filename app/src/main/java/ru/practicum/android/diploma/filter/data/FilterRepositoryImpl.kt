package ru.practicum.android.diploma.filter.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.filter.data.dto.CountriesRequest
import ru.practicum.android.diploma.filter.data.dto.CountriesResponse
import ru.practicum.android.diploma.filter.data.dto.RegionsRequest
import ru.practicum.android.diploma.filter.data.dto.RegionsResponse
import ru.practicum.android.diploma.filter.data.dto.SectorsRequest
import ru.practicum.android.diploma.filter.data.dto.SectorsResponse
import ru.practicum.android.diploma.filter.data.dto.countryDtoToCountry
import ru.practicum.android.diploma.filter.data.dto.regionDtoToRegion
import ru.practicum.android.diploma.filter.data.dto.sectorDtoToSector
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.utils.ResponseData

class FilterRepositoryImpl(
    private val networkClient: NetworkClient
) : FilterRepository {

    override fun getCountries(): Flow<ResponseData<List<Country>>> = flow {
        when (val response = networkClient.doRequest(CountriesRequest(emptyMap()))) {
            is CountriesResponse -> {
                val countriesList = countryDtoToCountry(response.countries)
                emit(ResponseData.Data(countriesList))
            }
            else -> {
                responseToError(response)
            }
        }
    }

    override fun getRegions(id: Int): Flow<ResponseData<List<Region>>> = flow {
        when (val response = networkClient.doRequest(RegionsRequest(id))) {
            is RegionsResponse -> {
                val regionsList = regionDtoToRegion(response.regions)
                emit(ResponseData.Data(regionsList))
            }
            else -> {
                responseToError(response)
            }
        }
    }

    override fun getSectors(): Flow<ResponseData<List<Sector>>> = flow {
        when (val response = networkClient.doRequest(SectorsRequest(emptyMap()))) {
            is SectorsResponse -> {
                val sectorsList = sectorDtoToSector(response.sectors)
                emit(ResponseData.Data(sectorsList))
            }
            else -> {
                responseToError(response)
            }
        }
    }

    private fun responseToError(response: Response): ResponseData<CountriesResponse> =
        ResponseData.Error(
            when (response.resultCode) {
                RESULT_CODE_NO_INTERNET -> {
                    ResponseData.ResponseError.NO_INTERNET
                }

                RESULT_CODE_BAD_REQUEST -> {
                    ResponseData.ResponseError.CLIENT_ERROR
                }

                else -> {
                    ResponseData.ResponseError.SERVER_ERROR
                }
            }
        )

}
