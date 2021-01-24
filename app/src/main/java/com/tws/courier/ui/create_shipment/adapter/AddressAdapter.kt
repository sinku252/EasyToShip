package com.tws.courier.ui.create_shipment.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.databinding.RowAddressBinding
import com.tws.courier.domain.models.Address

class AddressAdapter(private val adapterCallbacks: AdapterCallbacks) :
    RecyclerView.Adapter<AddressAdapter.ItemHolder>() {
    private var lastSelectedPosition = -1
    private var list = ArrayList<Address>()

    var isEdit = true

    fun setList(sublist: ArrayList<Address>) {
        list.addAll(sublist)
        notifyDataSetChanged()
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
                R.layout.row_address, parent, false
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

    inner class ItemHolder(private val binding: RowAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var model: Address? = null

        init {
            binding.cvClick.setOnClickListener {
                model?.let { it1 -> adapterCallbacks.onSelectClicked(it1)
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
            binding.ivEditDelete.setOnClickListener {
                model?.let {
                        it1 -> adapterCallbacks.onEditDeleteClicked(it1)
                    if(!isEdit)
                        deleteItem(adapterPosition)
                }
            }
            if(isEdit)
                binding.ivEditDelete.setImageResource(R.drawable.edit_address)
            else
                binding.ivEditDelete.setImageResource(R.drawable.delete)
            //isEdit ? binding.ivEditDelete.setImageResource(R.drawable.): binding.ivEditDelete.setImageResource(R.drawable.)
        }

        fun bind(model: Address, position: Int) {
            this.model = model
            binding.setVariable(BR.item, model)
            binding.radio1.setChecked(lastSelectedPosition == position);
            binding.executePendingBindings()
        }

        private fun setText(textView: TextView, text: String) {
            if (!TextUtils.isEmpty(text)) textView.text = text else textView.text = ""
        }
    }

    interface AdapterCallbacks {
        fun onSelectClicked(address: Address)
        fun onEditDeleteClicked(address: Address)
    }
}