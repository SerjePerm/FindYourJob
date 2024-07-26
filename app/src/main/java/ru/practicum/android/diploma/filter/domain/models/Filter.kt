package ru.practicum.android.diploma.filter.domain.models

import java.io.Serializable

data class Filter(
    val location: Location = Location(),
    val sector: Sector? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
