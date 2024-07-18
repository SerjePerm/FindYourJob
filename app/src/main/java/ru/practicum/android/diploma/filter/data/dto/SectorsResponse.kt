package ru.practicum.android.diploma.filter.data.dto

import ru.practicum.android.diploma.filter.data.dto.components.SectorDto
import ru.practicum.android.diploma.search.data.dto.Response

data class SectorsResponse(
    val sectors: List<SectorDto>
) : Response()
