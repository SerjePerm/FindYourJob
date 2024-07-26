package ru.practicum.android.diploma.filter.domain.models

import java.io.Serializable

data class Country(
    val id: Int,
    val name: String
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
