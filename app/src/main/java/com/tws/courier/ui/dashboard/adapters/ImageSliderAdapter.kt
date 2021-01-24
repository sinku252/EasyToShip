package com.tws.courier.ui.dashboard.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.tws.courier.R
import com.tws.courier.domain.models.HomeSlider
import kotlinx.android.synthetic.main.image_slider_item.view.*

class ImageSliderAdapter(private val context: Context,list: List<HomeSlider>) : PagerAdapter() {


    private var inflater: LayoutInflater? = null
    //private val images = arrayOf(R.drawable.slider1, R.drawable.slider1, R.drawable.slider1, R.drawable.slider1)
    val images = mutableListOf<HomeSlider>()


    fun addItems(items: List<HomeSlider>) {
        this.images.clear()
        this.images.addAll(items)
        notifyDataSetChanged()
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {

        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.image_slider_item, null)
       // view.imageView_slide.setImageResource(images[position])
        //viewBinding.image.imageUrlWithLoadingBroad = details.posterPath
        Log.e("afsfasf",""+images)

        Glide.with(context)
            .load(images.get(position).image)
            .placeholder(CircularProgressDrawable(context)?.apply {
                strokeWidth = 5f
                centerRadius = 30f
                start()
            })
            .error(R.drawable.placeholder_broad)
            .centerCrop().into(view.imageView_slide)

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }

}