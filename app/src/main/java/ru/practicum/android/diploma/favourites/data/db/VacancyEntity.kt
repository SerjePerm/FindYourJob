package ru.practicum.android.diploma.favourites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vacancies_table")
data class VacancyEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val company: String,
    val currency: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val area: String,
    val alternateUrl: String,
    val icon: String,
    val employment: String,
    val experience: String,
    val schedule: String,
    val description: String,
    val contact: String,
    val email: String,
    val phone: String,
    val comment: String,
    val keySkills: String,
    val timestamp: Long
)
