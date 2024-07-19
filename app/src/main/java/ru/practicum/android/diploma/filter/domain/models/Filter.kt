package ru.practicum.android.diploma.filter.domain.models

data class Filter(
    val country: Country? = null,
    val region: Region? = null,
    val sector: Sector? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
)
