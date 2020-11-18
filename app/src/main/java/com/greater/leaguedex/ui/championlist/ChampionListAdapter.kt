package com.greater.leaguedex.ui.championlist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.greater.leaguedex.databinding.ItemviewChampionlistBinding
import com.greater.leaguedex.mvvm.recyclerview.SimpleAdapter
import tables.Champion

class ChampionListAdapter : SimpleAdapter<Champion, ItemviewChampionlistBinding>() {
    override fun getItemBinding(
        layoutInflator: LayoutInflater,
        parent: ViewGroup
    ): ItemviewChampionlistBinding =
        ItemviewChampionlistBinding.inflate(layoutInflator, parent, false)

    override fun onBindViewHolder(
        context: Context,
        viewHolder: RecyclerView.ViewHolder,
        item: Champion,
        binding: ItemviewChampionlistBinding
    ) {
        binding.label.text = item.name
        binding.image.load(item.imageIcon) {
            crossfade(true)
        }
    }
}
