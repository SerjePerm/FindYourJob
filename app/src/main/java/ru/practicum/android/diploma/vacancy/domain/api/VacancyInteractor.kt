package ru.practicum.android.diploma.vacancy.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.search.domain.utils.ResponseData
import ru.practicum.android.diploma.vacancy.domain.models.VacancyFull

interface VacancyInteractor {
    fun getVacancy(id: Int): Flow<ResponseData<VacancyFull>>
}
