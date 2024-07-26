package ru.practicum.android.diploma.filter.domain.models

import java.io.Serializable

data class Sector(
    val id: Int,
    val name: String,
    val isChecked: Boolean
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
