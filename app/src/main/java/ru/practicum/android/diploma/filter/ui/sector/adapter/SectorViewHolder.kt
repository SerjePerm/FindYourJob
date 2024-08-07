package ru.practicum.android.diploma.filter.ui.sector.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemSectorBinding
import ru.practicum.android.diploma.filter.domain.models.Sector

class SectorViewHolder(
    parentView: ViewGroup,
    val binding: ItemSectorBinding = ItemSectorBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    ),
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sector: Sector) {
        binding.tvName.text = sector.name
        if (sector.isChecked) {
            binding.ivRadioButton.setImageResource(R.drawable.ic_radio_button_on)
        } else {
            binding.ivRadioButton.setImageResource(R.drawable.ic_radio_button_off)
        }
    }
}
