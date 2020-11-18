package com.greater.leaguedex.mvvm.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class SimpleAdapter<Item : Any, VB : ViewBinding> :
    RecyclerView.Adapter<SimpleAdapter<Item, VB>.ViewHolder>() {
    private val itemList = mutableListOf<Item>()

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setAdapterItems(item: List<Item>) {
        this.itemList.clear()
        this.itemList.addAll(item)
        this.notifyDataSetChanged()
    }

    abstract fun getItemBinding(layoutInflator: LayoutInflater, parent: ViewGroup): VB
    open fun onInitViewHolder(binding: VB) {}
    abstract fun onBindViewHolder(
        context: Context,
        viewHolder: RecyclerView.ViewHolder,
        item: Item,
        binding: VB
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleAdapter<Item, VB>.ViewHolder {
        val binding = getItemBinding(LayoutInflater.from(parent.context), parent)
        return ViewHolder(binding).apply {
            onInitViewHolder(this.binding)
        }
    }

    override fun onBindViewHolder(holder: SimpleAdapter<Item, VB>.ViewHolder, position: Int) {
        val item: Item = itemList[position]
        holder.bindItem(item)
    }

    inner class ViewHolder(val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(item: Item) {
            onBindViewHolder(binding.root.context, this, item, binding)
        }
    }
}
