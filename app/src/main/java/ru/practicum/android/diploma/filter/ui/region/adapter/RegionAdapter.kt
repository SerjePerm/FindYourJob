package ru.practicum.android.diploma.filter.ui.region.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Region

class RegionAdapter(
    val onClick: (Region) -> Unit
) : RecyclerView.Adapter<RegionViewHolder>() {
    private val countries = mutableListOf<Region>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(parent)
    }

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = countries[position]
        holder.apply {
            bind(region)
            itemView.setOnClickListener { onClick(region) }
        }
    }

    fun addItems(newCountries: List<Region>) {
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
