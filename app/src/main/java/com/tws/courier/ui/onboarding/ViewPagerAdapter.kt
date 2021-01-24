package com.tws.courier.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.tws.courier.R

class ViewPagerAdapter(
    var mContext : Context,
    var mLayouts : ArrayList<Int>
) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater!!.inflate(mLayouts[position], container, false)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return mLayouts.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        val view = `object` as View
        container.removeView(view)
    }
}