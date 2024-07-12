package ru.practicum.android.diploma.filter.ui.sector.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Sector

class SectorAdapter(
    val onClick: (Sector) -> Unit
) : RecyclerView.Adapter<SectorViewHolder>() {
    private val sectors = mutableListOf<Sector>()

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

    fun addItems(newSectors: List<Sector>) {
        if (newSectors.isEmpty()) {
            return
        }
        val originalSize = sectors.size
        sectors += newSectors
        notifyItemRangeInserted(originalSize, newSectors.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        sectors.clear()
        notifyDataSetChanged()
    }
}
