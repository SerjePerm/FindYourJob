package ru.practicum.android.diploma.filter.ui.sector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemSectorBinding
import ru.practicum.android.diploma.filter.domain.models.SelectedSector

class SectorViewHolder(
    parentView: ViewGroup,
    val binding: ItemSectorBinding = ItemSectorBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    ),
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(selectedSector: SelectedSector) {
        binding.tvName.text = selectedSector.sector.name
        val iconRes = if (selectedSector.isSelected) {
            R.drawable.ic_radio_button_on
        } else {
            R.drawable.ic_radio_button_off
        }
        binding.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconRes, 0)
    }
}
