package ru.practicum.android.diploma.filter.domain.models

import java.io.Serializable

data class Region(
    val id: Int,
    val name: String,
    val parentId: Int?
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }
}
