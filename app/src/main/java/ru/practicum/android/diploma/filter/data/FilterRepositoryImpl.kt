package ru.practicum.android.diploma.filter.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import ru.practicum.android.diploma.filter.data.dto.CountriesRequest
import ru.practicum.android.diploma.filter.data.dto.CountriesResponse
import ru.practicum.android.diploma.filter.data.dto.countryDtoToCountry
import ru.practicum.android.diploma.filter.domain.api.FilterRepository
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.network.NetworkClient
import ru.practicum.android.diploma.search.domain.models.VacanciesResponse
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
