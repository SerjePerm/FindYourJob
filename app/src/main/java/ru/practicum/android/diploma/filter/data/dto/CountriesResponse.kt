package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.CountryDto
import ru.practicum.android.diploma.search.data.dto.Response

data class CountriesResponse(
    val countries: List<CountryDto>
) : Response()
