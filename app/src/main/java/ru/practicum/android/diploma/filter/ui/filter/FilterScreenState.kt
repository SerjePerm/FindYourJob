package ru.practicum.android.diploma.filter.ui.filter

import ru.practicum.android.diploma.filter.domain.models.Filter

data class FilterScreenState(
    val filter: Filter,
    val modified: Boolean,
    val isEmpty: Boolean,
)
