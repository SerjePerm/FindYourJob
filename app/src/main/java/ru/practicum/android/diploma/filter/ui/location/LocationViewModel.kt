package ru.practicum.android.diploma.filter.ui.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.filter.domain.models.Location
import ru.practicum.android.diploma.filter.domain.models.Region

class LocationViewModel : ViewModel() {

    var country: Country?
        set(value) {
            location = location.copy(country = value)
        }
        get() = location.country

    var region: Region?
        set(value) {
            location = location.copy(region = value)
        }
        get() = location.region

    var location: Location = Location()
        set(value) {
            field = value
            _locationScreenState.postValue(location)
        }

    private val _locationScreenState = MutableLiveData(location)
    val locationScreenState: LiveData<Location>
        get() = _locationScreenState
}
