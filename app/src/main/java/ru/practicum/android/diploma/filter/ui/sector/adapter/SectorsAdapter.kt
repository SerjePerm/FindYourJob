package ru.practicum.android.diploma.filter.ui.sector.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Sector
import kotlin.math.min

class SectorsAdapter(
    val onClick: (Sector) -> Unit
) : RecyclerView.Adapter<SectorViewHolder>() {
    val sectors = mutableListOf<Sector>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        return SectorViewHolder(parent)
    }

    override fun getItemCount(): Int = sectors.size

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        val sector = sectors[position]
        holder.apply {
            bind(sector)
            itemView.setOnClickListener { onClick(sector) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newSectors: List<Sector>) {
        if (newSectors.isNotEmpty()
            && newSectors.slice(0 until min(sectors.size, newSectors.size)) == sectors
        ) {
            val oldSize = sectors.size
            val countNew = newSectors.size - oldSize
            sectors += newSectors.slice(oldSize until newSectors.size)
            notifyItemRangeChanged(oldSize, countNew)
        } else {
            sectors.clear()
            sectors += newSectors
            notifyDataSetChanged()
        }
    }

    fun clearItems() {
        setItems(emptyList())
    }
}
