package com.tws.courier.ui.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tws.courier.R
import com.tws.courier.databinding.FragmentOrderBinding
import com.tws.courier.domain.models.Order
import com.tws.courier.ui.home.base.HomeBaseFragment
import com.tws.courier.ui.orders.adapter.OrderAdapter
import kotlinx.android.synthetic.main.fragment_order.*


class OrdersFragment : HomeBaseFragment<OrdersViewModel, FragmentOrderBinding>()
{
    companion object {
        val TAG = "OrdersFragment"
        val ARG_STATUS = "status"
       // fun newInstance(status: Int) = OrdersFragment()

        fun newInstance(email: Int) = OrdersFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_STATUS, email)
            }
        }
    }

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = object : ToolbarMenuHandler {
        override val toolbarId: Int
            get() = R.id.toolbar
        override val toolbarTitleId: Int
            get() = R.id.toolbarTitle
        override val menuResource: Int
            get() = 0
        override val toolbarTitle: String
            get() = getString(R.string.neworder)

        override fun hasMenu(): Boolean = false
        override fun hasBackButton(): Boolean = true
    }
    override fun getLayoutResource(): Int = R.layout.fragment_order
    override fun getViewModelClass(): Class<OrdersViewModel> = OrdersViewModel::class.java

    private var status: Int = 0

    val adapter = OrderAdapter(
        object : OrderAdapter.AdapterCallbacks
        {
            override fun onOrderViewClicked(order: Order)
            {

            }

            override fun onOrderRescheduleClicked(order: Order) {

            }

            override fun onOrderCancelClicked(order: Order) {

            }

        }
        /*object : OrderAdapter.AdapterCallbacks {
            override fun onSelectClicked(order: Order) {

            }

            override fun onEditDeleteClicked(order: Order) {

            }
        }*/)


   /* val adapter = OrderAdapter(
        R.layout.order_item,
        object : OrderAdapter.AdapterCallbacks<Order> {
            override fun onItemClicked(item: Order) {
               // fragmentListener?.navigateToMediaDetailsFragment(item)
            }
        })*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        initViews()

        status?.let {
            viewModel.orders(it)
            changeToolBarTitle(it)
        }

    }

    fun changeToolBarTitle(it:Int)
    {

        if(it==0)
        {
            toolbarTitle.text=getString(R.string.neworder)
        }
        if(it==1)
        {
            toolbarTitle.text=getString(R.string.pickup)
        }
        if(it==2)
        {
            toolbarTitle.text=getString(R.string.undelivered)
        }
        if(it==3)
        {
            toolbarTitle.text=getString(R.string.outfordelivery)
        }
        if(it==4)
        {
            toolbarTitle.text=getString(R.string.deliverd)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_STATUS)) status = it.getInt(ARG_STATUS)
        }
    }

    private fun initViews() {
        viewBinding.rvOrderList.layoutManager = LinearLayoutManager(context)
        viewBinding.rvOrderList.adapter = adapter
    }

    private fun observeLiveData()
    {
        viewModel.onOrdersReceived.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                adapter.setList(ArrayList(it))
               /* val listAlt: ArrayList<Order> = ArrayList()
                val list = ArrayList(it)
                list.forEach { mo ->
                    listAlt.add(mo)
                }
                list.clear()
                list.addAll(listAlt)
                setOrdersToviews(list)*/
            }
        })


    }

   /* private fun setOrdersToviews(list: List<Order>) {
        if (list.isNotEmpty()) {
            adapter.addItems(list)
            viewBinding.textEmpty.visibility = View.GONE
        } else viewBinding.textEmpty.visibility = View.VISIBLE
    }*/



}