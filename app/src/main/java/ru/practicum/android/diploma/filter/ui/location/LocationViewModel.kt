package ru.practicum.android.diploma.filter.ui.location

import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.api.FilterInteractor

class LocationViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {
    init {
        println("LocationViewModel created")
    }
}
