package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.CountryDto
import ru.practicum.android.diploma.filter.data.dto.components.RegionDto
import ru.practicum.android.diploma.filter.data.dto.components.SectorDto
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Sector

fun countryDtoToCountry(source: List<CountryDto>): List<Country> {
    return source.map {
        Country(
            id = it.id,
            name = it.name
        )
    }
}

fun regionDtoToRegion(source: List<RegionDto>): List<Region> {
    return source.map {
        Region(
            id = it.id,
            name = it.name
        )
    }
}

fun sectorDtoToSector(source: List<SectorDto>): List<Sector> {
    return source.map {
        Sector(
            id = it.id,
            name = it.name
        )
    }
}
