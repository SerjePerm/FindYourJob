package ru.practicum.android.diploma.filter.ui.region.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Region
import kotlin.math.min

class RegionsAdapter(
    val onClick: (Region) -> Unit
) : RecyclerView.Adapter<RegionViewHolder>() {
    private val regions = mutableListOf<Region>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionViewHolder {
        return RegionViewHolder(parent)
    }

    override fun getItemCount(): Int = regions.size

    override fun onBindViewHolder(holder: RegionViewHolder, position: Int) {
        val region = regions[position]
        holder.apply {
            bind(region)
            itemView.setOnClickListener { onClick(region) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newRegions: List<Region>) {
        if (newRegions.isNotEmpty()
            && newRegions.slice(0 until min(regions.size, newRegions.size)) == regions
        ) {
            val oldSize = regions.size
            val countNew = newRegions.size - oldSize
            regions += newRegions.slice(oldSize until newRegions.size)
            notifyItemRangeChanged(oldSize, countNew)
        } else {
            regions.clear()
            regions += newRegions
            notifyDataSetChanged()
        }
    }

    fun clearItems() {
        setItems(emptyList())
    }
}
