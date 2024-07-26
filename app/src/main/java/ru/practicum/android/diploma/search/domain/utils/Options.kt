package ru.practicum.android.diploma.search.domain.utils

import ru.practicum.android.diploma.filter.domain.models.Filter

data class Options(
    val searchText: String,
    val itemsPerPage: Int,
    val page: Int,
    val filter: Filter
) {
    companion object {
        fun toMap(options: Options): Map<String, String> = with(options) {
            buildMap {
                put("text", searchText)
                put("per_page", itemsPerPage.toString())
                put("page", page.toString())
                filter.location.apply {
                    if (region != null) {
                        put("area", region.id.toString())
                    } else if (country != null) {
                        put("area", country.id.toString())
                    }
                }
                if (filter.sector != null) {
                    put("industry", filter.sector.id.toString())
                }
                if (filter.salary != null) {
                    put("salary", filter.salary.toString())
                }
                if (filter.onlyWithSalary) {
                    put("only_with_salary", "true")
                }
            }
        }
    }
}
