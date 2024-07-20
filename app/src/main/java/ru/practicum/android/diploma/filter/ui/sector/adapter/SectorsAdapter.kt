package ru.practicum.android.diploma.filter.ui.sector.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.filter.domain.models.Sector
import ru.practicum.android.diploma.filter.domain.models.SelectedSector

class SectorsAdapter(
    private val onClick: (Sector) -> Unit
) : RecyclerView.Adapter<SectorViewHolder>() {
    private val sectors = mutableListOf<SelectedSector>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectorViewHolder {
        return SectorViewHolder(parent)
    }

    override fun getItemCount(): Int = sectors.size

    override fun onBindViewHolder(holder: SectorViewHolder, position: Int) {
        val selectedSector = sectors[position]
        holder.apply {
            bind(selectedSector)
            itemView.setOnClickListener {
                selectedSector.isSelected = !selectedSector.isSelected
                notifyItemChanged(position)
                onClick(selectedSector.sector)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(newSectors: List<Sector>) {
        val newSelectedSectors = newSectors.map { SelectedSector(it, false) }
        if (sectors.isNotEmpty()
            && newSectors.map { it.id } == sectors.map { it.sector.id }
        ) {
            for ((index, newSector) in newSelectedSectors.withIndex()) {
                val oldIndex = sectors.indexOfFirst { it.sector.id == newSector.sector.id }
                if (oldIndex != -1) {
                    sectors[oldIndex] = newSector
                    notifyItemChanged(oldIndex)
                }
            }
        } else {
            sectors.clear()
            sectors.addAll(newSelectedSectors)
            notifyDataSetChanged()
        }
    }

    fun clearItems() {
        setItems(emptyList())
    }

    fun getSelectedSectors(): List<SelectedSector> {
        return sectors.filter { it.isSelected }
    }
}
