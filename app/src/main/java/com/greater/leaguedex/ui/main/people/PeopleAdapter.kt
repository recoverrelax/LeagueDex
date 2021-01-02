package com.greater.leaguedex.ui.main.people

import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.greater.leaguedex.R
import com.greater.leaguedex.databinding.ItemviewPeopleBinding
import timber.log.Timber

class PeopleAdapter(
    private val onFavouriteClicked: (itemId: Long, isFavourite: Boolean) -> Unit
) : PagedListAdapter<PeopleItem, PeopleAdapter.ViewHolder>(DiffUtil) {

    object UPDATE_FAVOURITE

    private val IMAGE_SIZE = 210

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemviewPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        payloads.forEach { payLoad ->
            payLoad as UPDATE_FAVOURITE
            holder.bindFavourite(getItem(position)!!)
        }
    }

    private fun handleFavouriteClick(position: Int) {
        // we could let the dataSource handle it
        // but rebinding the whole list just for a tiny state
        // is not the most performing solution
        // currently this work because the DataSet doesn't drop pages!

        if (position == RecyclerView.NO_POSITION) return
        val item = getItem(position) ?: return

        onFavouriteClicked(item.id, item.isFavourite)
    }

    inner class ViewHolder constructor(private val binding: ItemviewPeopleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.favourite.setOnClickListener {
                handleFavouriteClick(adapterPosition)
            }
        }

        fun bind(peopleItem: PeopleItem) {
            val context = binding.root.context

            binding.name.text = context.getString(R.string.name_label, peopleItem.name).html()
            binding.language.text =
                context.getString(R.string.language_label, peopleItem.spokenLanguage).html()
            binding.vehicles.text = if (peopleItem.vehicles.isEmpty()) {
                context.getString(R.string.vehicles_label_none).html()
            } else {
                context.getString(R.string.vehicles_label, peopleItem.vehicles)
                    .html()
            }
            bindFavourite(peopleItem)

            binding.icon.load(peopleItem.imageUrl(IMAGE_SIZE)) {
                crossfade(true)
                fallback(R.drawable.people_icon_default)
                placeholder(R.drawable.people_icon_default)
            }
        }

        fun bindFavourite(peopleItem: PeopleItem) {
            binding.favourite.isSelected = peopleItem.isFavourite
        }

        private fun String.html(): Spanned {
            return Html.fromHtml(this)
        }
    }

    private object DiffUtil : ItemCallback<PeopleItem>() {
        override fun areItemsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
            Timber.i(
                """areItemsTheSame
                |oldItem: $oldItem
                |newItem: $newItem
                |result: ${oldItem.id == newItem.id}
            """.trimMargin()
            )
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PeopleItem, newItem: PeopleItem): Boolean {
            Timber.i(
                """areContentsTheSame
                |oldItem: $oldItem
                |newItem: $newItem
                |result: ${oldItem == newItem}
            """.trimMargin()
            )
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: PeopleItem, newItem: PeopleItem): Any? {
            return if (oldItem.isFavouritesOnlyDifferent(newItem)) UPDATE_FAVOURITE else null
        }
    }
}
