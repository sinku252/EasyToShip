package com.app.monrotv.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tws.courier.BR
import com.app.monrotv.ui.dashboard.generic.AddressListItem

class GenericAdapter<T : AddressListItem>(
    @LayoutRes val rowLayoutId: Int,
    val adapterCallbacks: AdapterCallbacks<T>
) :
    RecyclerView.Adapter<GenericAdapter.GenericViewHolder<T>>() {

    val items = mutableListOf<T>()

    fun addItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        return GenericViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                rowLayoutId, parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        val itemViewModel = items[position]
        itemViewModel.adapterPosition = position
        adapterCallbacks?.let { itemViewModel.adapterCallbacks = it }
        holder.bind(itemViewModel)
    }


    class GenericViewHolder<T : AddressListItem>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(addressItem: T) {
            binding.setVariable(BR.item, addressItem)
            binding.executePendingBindings()
        }
    }

    interface AdapterCallbacks<T> {
        fun onItemClicked(item: T)
        fun onClick2(item: T) {}
    }
}