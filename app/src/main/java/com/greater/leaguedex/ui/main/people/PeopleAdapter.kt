package com.greater.leaguedex.ui.main.people

import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ItemviewPeopleBinding
import timber.log.Timber

class PeopleAdapter(
    private val onFavouriteClicked: (itemId: Long, isFavourite: Boolean) -> Unit
) : PagingDataAdapter<PeopleItem, PeopleAdapter.ViewHolder>(DiffUtil) {

    object UPDATE_FAVOURITE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemviewPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)

        payloads.forEach { payLoad ->
            payLoad as UPDATE_FAVOURITE
            getItem(position)?.let { holder.bindFavourite(it) }
        }
    }

    private fun handleFavouriteClick(position: Int) {
        // we could let the dataSource handle it
        // but rebinding the whole list just for a tiny state
        // is not the most performing solution
        // currently this work because the DataSet doesn't drop pages!

        if (position == RecyclerView.NO_POSITION) return
        val item = getItem(position) ?: return

        // we update new state immediately
        // and dispatch a change to the viewModel
//        item.isFavourite = item.isFavourite.not()
//        notifyItemChanged(position, UPDATE_FAVOURITE)
        onFavouriteClicked(item.id, item.isFavourite)
    }

    inner class ViewHolder constructor(private val binding: ItemviewPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.favourite.setOnClickListener {
                handleFavouriteClick(bindingAdapterPosition)
            }
        }

        fun bind(peopleItem: PeopleItem) {
            Timber.i("Bind: $peopleItem")
            val context = binding.root.context

            binding.name.text = context.getString(R.string.name_label, peopleItem.name).html()
            binding.language.text =
                context.getString(R.string.language_label, peopleItem.language).html()
            binding.vehicles.text = if (peopleItem.vehicles.isEmpty()) {
                context.getString(R.string.vehicles_label_none).html()
            } else {
                context.getString(R.string.vehicles_label, peopleItem.vehicles)
                    .html()
            }

            binding.favourite.isSelected = peopleItem.isFavourite
            binding.icon.load(peopleItem.imageUrl) {
                crossfade(true)
                fallback(R.drawable.people_icon_default)
                placeholder(R.drawable.people_icon_default)
            }
        }

        fun bindFavourite(peopleItem: PeopleItem) {
            // map is the source of true
            // fallback to bind state
            binding.favourite.isSelected = peopleItem.isFavourite
        }

        private fun String.html(): Spanned {
            return Html.fromHtml(this)
        }
    }

    private object DiffUtil : ItemCallback<PeopleItem>() {
        override fun areItemsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: PeopleItem, newItem: PeopleItem): Any? {
            return if (oldItem.isFavouritesOnlyDifferent(newItem)) UPDATE_FAVOURITE else null
        }
    }
}
