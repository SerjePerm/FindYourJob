package ru.practicum.android.diploma.filter.ui.country.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.ItemCountryBinding
import ru.practicum.android.diploma.filter.domain.models.Country

class CountryViewHolder(
    parentView: ViewGroup,
    val binding: ItemCountryBinding = ItemCountryBinding.inflate(
        LayoutInflater.from(parentView.context),
        parentView,
        false
    ),
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(country: Country) {
        binding.tvName.text = country.name
    }
}
