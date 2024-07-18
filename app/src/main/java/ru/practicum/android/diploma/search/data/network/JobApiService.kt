package ru.practicum.android.diploma.search.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.filter.data.dto.components.CountryDto
import ru.practicum.android.diploma.filter.data.dto.components.RegionListDto
import ru.practicum.android.diploma.filter.data.dto.components.SectorDto
import ru.practicum.android.diploma.search.data.dto.SearchResponse
import ru.practicum.android.diploma.vacancy.data.dto.VacancyResponse

interface JobApiService {

    @GET("vacancies/{vacancy_id}")
    suspend fun getVacancy(@Path("vacancy_id") id: Int): VacancyResponse

    @GET("vacancies")
    suspend fun searchVacancies(@QueryMap options: Map<String, String>): SearchResponse

    @GET("areas")
    suspend fun getCountries(): Response<List<CountryDto>>

    @GET("areas/{area_id}")
    suspend fun getRegions(@Path("area_id") areaId: Int): Response<RegionListDto>

    @GET("industries")
    suspend fun getSectors(): Response<List<SectorDto>>

}
