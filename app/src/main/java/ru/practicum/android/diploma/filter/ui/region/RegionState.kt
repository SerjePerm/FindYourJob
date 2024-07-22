package ru.practicum.android.diploma.filter.ui.region

import ru.practicum.android.diploma.filter.domain.models.Region
import ru.practicum.android.diploma.search.domain.utils.ResponseData

sealed class RegionState {
    data object Loading : RegionState()
    data class Content(val regionsList: List<Region>) : RegionState()
    data class Error(val error: ResponseData.ResponseError) : RegionState()
}
