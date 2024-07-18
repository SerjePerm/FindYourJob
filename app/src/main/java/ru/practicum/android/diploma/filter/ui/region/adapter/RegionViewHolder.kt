package ru.practicum.android.diploma.filter.ui.region.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemRegionBinding
import ru.practicum.android.diploma.filter.domain.models.Region

class RegionViewHolder(
    parentView: ViewGroup,
    val binding: ItemRegionBinding = ItemRegionBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    ),
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(region: Region) {
        binding.tvName.text = region.name
    }
}
