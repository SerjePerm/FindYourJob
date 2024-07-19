package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.CountryDto
import ru.practicum.android.diploma.filter.data.dto.components.RegionDto
import ru.practicum.android.diploma.filter.data.dto.components.SectorDto
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.filter.domain.models.Sector

fun countryDtoToCountry(source: List<CountryDto>): List<Country> =
    source.map {
        Country(
            id = it.id,
            name = it.name
        )
    }

fun regionDtoToRegion(source: List<RegionDto>): List<Region> =
    source.map {
        Region(
            id = it.id,
            name = it.name
        )
    }

fun countryDtoToAllRegions(source: List<CountryDto>): List<Region> =
    source.flatMap { it.areas }
        .map { regionDto ->
            Region(
                id = regionDto.id,
                name = regionDto.name
            )
        }

fun sectorDtoToSector(source: List<SectorDto>): List<Sector> =
    source.map {
        Sector(
            id = it.id,
            name = it.name
        )
    }
