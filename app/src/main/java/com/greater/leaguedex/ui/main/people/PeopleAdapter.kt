package com.greater.leaguedex.ui.main.people

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ItemviewPeopleBinding
import com.greater.leaguedex.util.parser.AvatarImageParser

class PeopleAdapter :
    PagingDataAdapter<PeopleModel, PeopleAdapter.ViewHolder>(DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemviewPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class ViewHolder constructor(private val binding: ItemviewPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(peopleModel: PeopleModel) {
            binding.name.text = peopleModel.name
            @SuppressLint("SetTextI18n")
            binding.language.text = "(lang: ${peopleModel.language})"
            binding.vehicles.text = peopleModel.vehicles.joinToString(", ")

            binding.icon.load(peopleModel.imageUrl) {
                crossfade(true)
                fallback(R.drawable.people_icon_default)
                placeholder(R.drawable.people_icon_default)
            }
        }
    }

    private object DiffUtil : ItemCallback<PeopleModel>() {
        override fun areItemsTheSame(oldItem: PeopleModel, newItem: PeopleModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PeopleModel, newItem: PeopleModel): Boolean {
            return oldItem == newItem
        }
    }
}