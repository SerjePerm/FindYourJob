package ru.practicum.android.diploma.filter.domain.models

import java.io.Serializable

data class Location(
    val country: Country? = null,
    val region: Region? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
