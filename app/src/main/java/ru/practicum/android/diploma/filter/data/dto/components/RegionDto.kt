package ru.practicum.android.diploma.filter.data.dto.components

import com.google.gson.annotations.SerializedName

data class RegionDto(
    val id: Int,
    val name: String,
    @SerializedName("parent_id") val parentId: Int
)
