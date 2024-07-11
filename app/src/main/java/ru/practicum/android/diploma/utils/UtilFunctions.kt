package ru.practicum.android.diploma.utils

@Suppress("detekt.MagicNumber")
fun getVacanciesText(count: Int): String {
    val lastDigit = count % 10
    val lastTwoDigits = count % 100

    val suffix = when {
        lastTwoDigits in 11..19 -> "вакансий"
        lastDigit == 1 -> "вакансия"
        lastDigit in 2..4 -> "вакансии"
        else -> "вакансий"
    }

    return "Найдено $count $suffix"
}
