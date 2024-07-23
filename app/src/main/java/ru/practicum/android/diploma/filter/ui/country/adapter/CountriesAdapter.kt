package ru.practicum.android.diploma.filter.ui.country.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Country
import kotlin.math.min

class CountriesAdapter(
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

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newCountries: List<Country>) {
        if (newCountries.isNotEmpty()
            && newCountries.slice(0 until min(countries.size, newCountries.size)) == countries
        ) {
            val oldSize = countries.size
            val countNew = newCountries.size - oldSize
            countries += newCountries.slice(oldSize until newCountries.size)
            notifyItemRangeChanged(oldSize, countNew)
        } else {
            countries.clear()
            countries += newCountries
            notifyDataSetChanged()
        }
    }

    fun clearItems() {
        setItems(emptyList())
    }
}
