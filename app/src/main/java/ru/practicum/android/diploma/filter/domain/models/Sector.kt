package ru.practicum.android.diploma.filter.domain.models

data class Sector(
    val id: Int,
    val name: String,
    var isChecked: Boolean = false
)