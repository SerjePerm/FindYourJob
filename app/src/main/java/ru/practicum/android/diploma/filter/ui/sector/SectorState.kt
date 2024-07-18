package ru.practicum.android.diploma.filter.ui.sector

import ru.practicum.android.diploma.filter.domain.models.Sector

sealed class SectorState {
    data object Loading : SectorState()
    data class Content(val sectorsList: List<Sector>) : SectorState()
    data object Error : SectorState()
}
