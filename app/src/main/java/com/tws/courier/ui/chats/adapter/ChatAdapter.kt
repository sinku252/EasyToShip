package com.tws.courier.ui.chats.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.databinding.ItemChatBinding
import com.tws.courier.databinding.RowAddressBinding
import com.tws.courier.databinding.RowTicketBinding
import com.tws.courier.domain.models.Address
import com.tws.courier.domain.models.Chat
import com.tws.courier.domain.models.Help
import com.tws.courier.getDateWithMonthNameFromCalendar

class ChatAdapter(private val adapterCallbacks: AdapterCallbacks) :
    RecyclerView.Adapter<ChatAdapter.ItemHolder>() {
    private var list = ArrayList<Chat>()

    fun setList(sublist: ArrayList<Chat>) {
        list.addAll(sublist)
        notifyDataSetChanged()
    }

    fun addMsg(sublist: Chat) {
        list.add(sublist)
        notifyItemInserted(list.size - 1);
       // notifyDataSetChanged()
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int)
    {
        list.drop(position)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_chat, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        //holder..setChecked(lastSelectedPosition == position);
        //holder.selectionState.setChecked(lastSelectedPosition == position);
        holder.bind(list[position],position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemHolder(private val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var model: Chat? = null

        init {
          /*  binding.llClick.setOnClickListener {
                model?.let { it1 ->
                    adapterCallbacks.onSelectClicked(it1)
                }
            }*/
/*            binding.ivEditDelete.setOnClickListener {
                model?.let {
                        it1 -> adapterCallbacks.onEditDeleteClicked(it1)
                }
            }*/
        }

        fun bind(model: Chat, position: Int) {
            this.model = model
           // binding.tvTicketDate.text= getDateWithMonthNameFromCalendar(model.createdDate)
            binding.setVariable(BR.item, model)
            binding.executePendingBindings()
        }

        private fun setText(textView: TextView, text: String) {
            if (!TextUtils.isEmpty(text)) textView.text = text else textView.text = ""
        }
    }

    fun getStatus(help: Chat)
    {
        //important =1 inbox=0 draft=2

    }


    interface AdapterCallbacks {
        fun onSelectClicked(chat: Chat)

    }
}