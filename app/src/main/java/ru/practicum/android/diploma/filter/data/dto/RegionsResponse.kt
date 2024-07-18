package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.RegionDto
import ru.practicum.android.diploma.search.data.dto.Response

data class RegionsResponse(
    val regions: List<RegionDto>
) : Response()
