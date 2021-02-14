package com.tws.courier.ui.orders.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tws.courier.BR
import com.tws.courier.R
import com.tws.courier.databinding.OrderItemBinding
import com.tws.courier.domain.models.Address
import com.tws.courier.domain.models.Order
import com.tws.courier.getDateWithMonthName
import com.tws.courier.getImageByVehicleType
import com.tws.courier.ui.create_shipment.adapter.AddressAdapter

class OrderAdapter(private val adapterCallbacks: AdapterCallbacks) :
    RecyclerView.Adapter<OrderAdapter.ItemHolder>() {
    private var list = ArrayList<Order>()


    fun setList(sublist: ArrayList<Order>) {
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
                R.layout.order_item, parent, false
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

    inner class ItemHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var model: Order? = null

        init {
            binding.llOrderClick.setOnClickListener {
                model?.let {
                     //  it1 -> adapterCallbacks.onSelectClicked(it1)
                    //lastSelectedPosition = getAdapterPosition();
                    //notifyDataSetChanged();
                    val popupMenu: PopupMenu = PopupMenu(binding.llOrderClick.context,binding.llOrderClick)
                    popupMenu.menuInflater.inflate(R.menu.order_menu,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_view_order ->
                                adapterCallbacks.onOrderViewClicked(it)
                            R.id.action_reschedule_order ->
                                adapterCallbacks.onOrderRescheduleClicked(it)
                            R.id.action_cancel_order ->
                                adapterCallbacks.onOrderCancelClicked(it)
                            //Toast.makeText(this@MainActivity, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        true
                    })
                    popupMenu.show()
                }

            }
           /* binding.cvClick.setOnClickListener {
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
                binding.ivEditDelete.setImageResource(R.drawable.delete)*/
            //isEdit ? binding.ivEditDelete.setImageResource(R.drawable.): binding.ivEditDelete.setImageResource(R.drawable.)
        }

        fun bind(model: Order, position: Int) {
            this.model = model
            binding.setVariable(BR.item, model)
            binding.tvOrderDate.text="order date: "+getDateWithMonthName(model.createdDate)
            binding.ivVehicle.setImageResource(getImageByVehicleType(model.vehicleType))
           // binding.radio1.setChecked(lastSelectedPosition == position);
            binding.executePendingBindings()
        }

        private fun setText(textView: TextView, text: String) {
            if (!TextUtils.isEmpty(text)) textView.text = text else textView.text = ""
        }
    }

    interface AdapterCallbacks {
        fun onOrderViewClicked(order: Order)
        fun onOrderRescheduleClicked(order: Order)
        fun onOrderCancelClicked(order: Order)
    }
}