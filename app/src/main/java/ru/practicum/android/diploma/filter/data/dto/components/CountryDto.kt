package ru.practicum.android.diploma.filter.data.dto.components

data class CountryDto(
    val id: Int,
    val name: String,
    val areas: List<RegionDto>
)
