package ru.practicum.android.diploma.filter.ui.sector

import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.search.domain.utils.ResponseData

sealed class SectorState {
    data object Loading : SectorState()
    data class Content(val sectorsList: List<Sector>) : SectorState()
    data class Error(val error: ResponseData.ResponseError) : SectorState()
}
