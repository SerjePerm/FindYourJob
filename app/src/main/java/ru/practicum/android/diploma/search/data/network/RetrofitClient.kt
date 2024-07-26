package ru.practicum.android.diploma.search.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.filter.data.dto.CountriesRequest
import ru.practicum.android.diploma.filter.data.dto.CountriesResponse
import ru.practicum.android.diploma.filter.data.dto.RegionsRequest
import ru.practicum.android.diploma.filter.data.dto.RegionsResponse
import ru.practicum.android.diploma.filter.data.dto.SectorsRequest
import ru.practicum.android.diploma.filter.data.dto.SectorsResponse
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_BAD_REQUEST
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_NO_INTERNET
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_SERVER_ERROR
import ru.practicum.android.diploma.search.data.dto.RESULT_CODE_SUCCESS
import ru.practicum.android.diploma.search.data.dto.Response
import ru.practicum.android.diploma.search.data.dto.SearchRequest
import ru.practicum.android.diploma.utils.isInternetAvailable
import ru.practicum.android.diploma.vacancy.data.dto.VacancyRequest

class RetrofitClient(
    private val jobApiService: JobApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isInternetAvailable(context)) {
            return Response().apply { resultCode = RESULT_CODE_NO_INTERNET }

        }
        return getNetworkResponse(dto = dto)
    }

    private suspend fun getNetworkResponse(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                when (dto) {
                    is VacancyRequest -> getVacancyResponse(dto)
                    is SearchRequest -> getSearchResponse(dto)
                    is CountriesRequest -> getCountriesResponse()
                    is RegionsRequest -> getRegionsResponse(dto)
                    is SectorsRequest -> getSectorsResponse()
                    else -> {
                        Response().apply { resultCode = RESULT_CODE_BAD_REQUEST }
                    }
                }

            } catch (e: HttpException) {
                println("RetrofitClient error code: ${e.code()} message: ${e.message}")
                Response().apply { resultCode = RESULT_CODE_SERVER_ERROR }
            }
        }
    }

    private suspend fun getVacancyResponse(dto: VacancyRequest): Response {
        val response = jobApiService.getVacancy(dto.id)
        return response.apply { resultCode = RESULT_CODE_SUCCESS }
    }

    private suspend fun getSearchResponse(dto: SearchRequest): Response {
        val response = jobApiService.searchVacancies(options = dto.options)
        return response.apply { resultCode = RESULT_CODE_SUCCESS }
    }

    private suspend fun getCountriesResponse(): Response {
        val networkResponse = jobApiService.getCountries()
        if (networkResponse.isSuccessful) {
            val countriesResponse = CountriesResponse(networkResponse.body() ?: emptyList())
            countriesResponse.resultCode = networkResponse.code()
            return countriesResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }

    private suspend fun getRegionsResponse(dto: RegionsRequest): Response {
        val networkResponse = jobApiService.getRegions(dto.id)
        if (networkResponse.isSuccessful) {
            val regionsResponse = RegionsResponse(networkResponse.body()?.areas ?: emptyList())
            regionsResponse.resultCode = networkResponse.code()
            return regionsResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }

    private suspend fun getSectorsResponse(): Response {
        val networkResponse = jobApiService.getSectors()
        if (networkResponse.isSuccessful) {
            val sectorsResponse = SectorsResponse(networkResponse.body() ?: emptyList())
            sectorsResponse.resultCode = networkResponse.code()
            return sectorsResponse
        } else {
            return Response().apply { resultCode = networkResponse.code() }
        }
    }
}
