package com.app.monrotv.ui.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tws.courier.AppManager
import com.tws.courier.R
import com.tws.courier.databinding.RowListDrawerFooterBinding
import com.tws.courier.databinding.RowListDrawerHeaderBinding
import com.tws.courier.databinding.RowListDrawerItemBinding
import com.tws.courier.setTextOrEmpty

class DrawerAdapter(val items: List<DrawerItem>, val callbacks: DrawerAdapterCallbacks) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 1
    private val TYPE_ITEM = 2
    private val TYPE_FOOTER = 3
    private var headerHolder: HeaderHolder? = null
    private var footerHolder: FooterHolder? = null

    override fun getItemViewType(position: Int): Int = if (position == 0) TYPE_HEADER else if(position==13) TYPE_FOOTER else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) return HeaderHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_list_drawer_header, parent, false
            )
        )
        else if (viewType == TYPE_FOOTER) return FooterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_list_drawer_footer, parent, false
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
       /* if (holder is FooterHolder) {
            footerHolder = holder
            holder.bind()
        }*/

        if (holder is ItemHolder) holder.bind(items[position - 1],position)

    }

    override fun getItemCount(): Int = items.size + 2

    inner class FooterHolder(private val binding: RowListDrawerFooterBinding) :
        RecyclerView.ViewHolder(binding.root)
    {
        init {
            binding.constRoot.setOnClickListener {
                callbacks.onHeaderClicked()
            }
        }
        }


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
                binding.textEmail.setTextOrEmpty(user.email)
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

        fun bind(drawerItem: DrawerItem,position: Int) {
            this.drawerItem = drawerItem
            binding.textTitle.setTextOrEmpty(drawerItem.title)
            binding.imageIcon.setImageResource(drawerItem.drawable)
            if(position==3 || position==7)
                binding.vu0.visibility= View.VISIBLE
            else
                binding.vu0.visibility= View.GONE

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
                DrawerItem(33, "Home", R.drawable.menu_home),
                DrawerItem(34, "Create New Shipment", R.drawable.icon_create_shipment),
                DrawerItem(35, "Shipment History", R.drawable.icon_shipment_history),
                DrawerItem(36, "Account Setting", R.drawable.icon_account),
                DrawerItem(37, "Wallet", R.drawable.icon_wallet),
                DrawerItem(38, "Profile", R.drawable.icon_profile),
                DrawerItem(39, "Chat", R.drawable.icon_chat),
                DrawerItem(40, "Ticket", R.drawable.icon_create_docket),
                DrawerItem(41, "Share", R.drawable.icon_share),
                DrawerItem(42, "Notification", R.drawable.icon_notification),
                DrawerItem(43, "Contact us", R.drawable.icon_contact_us),
                DrawerItem(44, "Logout", R.drawable.icon_logout)
             /*   DrawerItem(36, "Downloads", R.drawable.lock),
                DrawerItem(37, "Settings", R.drawable.lock)*/
            )
        }
    }
}