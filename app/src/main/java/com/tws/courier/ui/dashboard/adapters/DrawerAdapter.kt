package com.app.monrotv.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.RowListDrawerHeaderBinding
import com.tws.courier.databinding.RowListDrawerItemBinding
import com.tws.courier.setTextOrEmpty

class DrawerAdapter(val items: List<DrawerItem>, val callbacks: DrawerAdapterCallbacks) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 1
    private val TYPE_ITEM = 2
    private var headerHolder: HeaderHolder? = null

    override fun getItemViewType(position: Int): Int = if (position == 0) TYPE_HEADER else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) return HeaderHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_list_drawer_header, parent, false
            )
        )
        return ItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_list_drawer_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderHolder) {
            headerHolder = holder
            holder.bind()
        }
        if (holder is ItemHolder) holder.bind(items[position - 1])
    }

    override fun getItemCount(): Int = items.size + 1

    inner class HeaderHolder(private val binding: RowListDrawerHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.constRoot.setOnClickListener {
                callbacks.onHeaderClicked()
            }
        }

        fun bind() {
            invalidateUserInfo()
            binding.executePendingBindings()
        }

        fun setUserImage(profileImageUrl: String?) {
            if (URLUtil.isValidUrl(profileImageUrl))
                Glide.with(itemView.context).load(profileImageUrl)
                    .placeholder(R.drawable.placeholder_user_photo)
                    .error(R.drawable.placeholder_user_photo).centerCrop().into(binding.imageUser)
            else Glide.with(itemView.context).load(R.drawable.placeholder_user_photo).centerCrop()
                .into(binding.imageUser)
        }

        fun invalidateUserInfo() {
            AppManager.getUser()?.let { user ->
                setUserImage(user.imagePath)
                binding.textName.setTextOrEmpty(user.username)
            }
        }
    }

    fun invalidateUserInfo() {
        headerHolder?.let { it.invalidateUserInfo() }
    }

    inner class ItemHolder(private val binding: RowListDrawerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var drawerItem: DrawerItem? = null

        init {
            binding.constRoot.setOnClickListener {
                drawerItem?.let { it1 -> callbacks.onDrawerItemClicked(it1) }
            }
        }

        fun bind(drawerItem: DrawerItem) {
            this.drawerItem = drawerItem
            binding.textTitle.setTextOrEmpty(drawerItem.title)
            binding.imageIcon.setImageResource(drawerItem.drawable)
            binding.executePendingBindings()
        }
    }

    interface DrawerAdapterCallbacks {
        fun onDrawerItemClicked(drawerItem: DrawerItem)
        fun onHeaderClicked()
    }

    data class DrawerItem(val id: Int, val title: String, @DrawableRes val drawable: Int) {
        companion object {
            val getItemsList = listOf<DrawerItem>(
                DrawerItem(33, "Home", R.drawable.lock),
                DrawerItem(34, "Create Shipment", R.drawable.lock),
                DrawerItem(35, "Create Ticket", R.drawable.lock),
                DrawerItem(36, "Chats", R.drawable.lock),
                DrawerItem(37, "Orders", R.drawable.lock),
                DrawerItem(38, "Manage Profile", R.drawable.lock),
                DrawerItem(39, "Notification", R.drawable.lock)
             /*   DrawerItem(36, "Downloads", R.drawable.lock),
                DrawerItem(37, "Settings", R.drawable.lock)*/
            )
        }
    }
}