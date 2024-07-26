package ru.practicum.android.diploma.search.data.dto

import ru.practicum.android.diploma.search.domain.models.Vacancy

fun VacancyDto.toVacancy(): Vacancy =
    Vacancy(
        id = id,
        name = name,
        company = employer.name,
        currency = salary?.currency.orEmpty(),
        salaryFrom = salary?.from,
        salaryTo = salary?.to,
        area = area.name,
        icon = employer.logoUrls?.logo240.orEmpty()
    )
