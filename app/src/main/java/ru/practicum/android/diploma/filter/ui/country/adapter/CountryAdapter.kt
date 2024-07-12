package ru.practicum.android.diploma.filter.ui.country.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Country
import ru.practicum.android.diploma.search.domain.models.Vacancy

class CountryAdapter(
    val onClick: (Country) -> Unit
) : RecyclerView.Adapter<CountryViewHolder>() {
    private val countries = mutableListOf<Country>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(parent)
    }

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country = countries[position]
        holder.apply {
            bind(country)
            itemView.setOnClickListener { onClick(country) }
        }
    }

    fun addItems(newCountries: List<Country>) {
        if (newCountries.isEmpty()) {
            return
        }
        val originalSize = countries.size
        countries += newCountries
        notifyItemRangeInserted(originalSize, newCountries.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        countries.clear()
        notifyDataSetChanged()
    }
}
