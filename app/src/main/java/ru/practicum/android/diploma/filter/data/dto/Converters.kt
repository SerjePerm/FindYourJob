package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.CountryDto
import ru.practicum.android.diploma.filter.domain.models.Country

fun countryDtoToCountry(source: List<CountryDto>): List<Country> {
    return source.map {
        Country(
            id = it.id,
            name = it.name
        )
    }

}
