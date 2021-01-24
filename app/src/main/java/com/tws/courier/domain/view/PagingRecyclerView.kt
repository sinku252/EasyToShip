package com.tws.courier.domain.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagingRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor (context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor (context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    var pageNumber: Int = 0
    var isLoadingItems: Boolean = false
    var hasMoreItemsToLoad: Boolean = true

    var pagingListener: PagingListener? = null

    fun onPageLoadingSucceed() {
        isLoadingItems = false
        if (pageNumber == 0) {
            pagingListener?.showShimmer(false)
        }
        pageNumber++
    }

    fun onPageLoadingFailed() {
        isLoadingItems = false
        hasMoreItemsToLoad = false
        if (pageNumber == 0) pagingListener?.showShimmer(false)
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)

        if (layoutManager is LinearLayoutManager) {
            val layoutManager = layoutManager as LinearLayoutManager?
            val visibleItemCount = layoutManager!!.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (!isLoadingItems && hasMoreItemsToLoad) {
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    isLoadingItems = true
                    pagingListener!!.loadNextPage()
                }
            }
        }

        if (layoutManager is GridLayoutManager) {
            if (dy > 0) {
                val layoutManager = layoutManager as GridLayoutManager?
                val visibleItemCount = layoutManager!!.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                if (!isLoadingItems && hasMoreItemsToLoad) {
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        pagingListener!!.loadNextPage()
                    }
                }
            }
        }
    }

    interface PagingListener {
        fun loadNextPage()
        fun showShimmer(`is`: Boolean)
    }
}