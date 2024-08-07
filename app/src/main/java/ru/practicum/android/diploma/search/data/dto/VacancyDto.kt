package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.data.dto.components.Area
import ru.practicum.android.diploma.search.data.dto.components.Employer
import ru.practicum.android.diploma.search.data.dto.components.Salary

data class VacancyDto(
    val id: Int,
    val name: String,
    val employer: Employer,
    val salary: Salary?,
    val area: Area
)
