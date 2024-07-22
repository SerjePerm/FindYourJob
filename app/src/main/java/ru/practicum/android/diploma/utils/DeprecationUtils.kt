package ru.practicum.android.diploma.utils

import android.os.Build
import android.os.Bundle
import java.io.Serializable

object DeprecationUtils {
    inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(
            key,
            T::class.java
        )

        else ->
            @Suppress("Deprecation")
            getSerializable(key) as? T
    }
}
