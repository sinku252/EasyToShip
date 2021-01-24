package com.tws.courier.domain.base

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.ViewDataBinding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide

abstract class BaseDrawerFragment<T : BaseViewModel, B : ViewDataBinding> : BaseFragment<T, B>() {

    private var drawerToggle: ActionBarDrawerToggle? = null
    lateinit var drawerCreationInfo: DrawerCreationInfo

    fun initDrawer(drawerCreationInfo: DrawerCreationInfo) {
        this.drawerCreationInfo = drawerCreationInfo
        drawerCreationInfo.let { info ->
            getDrawerToggle()?.let {
                info.drawerLayout.addDrawerListener(it)
                it.isDrawerIndicatorEnabled = true
            }
            info.drawerRecyclerView.layoutManager = info.drawerRecyclerLayoutManager
            info.drawerRecyclerView.adapter = info.drawerRecyclerAdapter
        }
        getDrawerToggle()?.syncState()
    }

    private fun getDrawerToggle(): ActionBarDrawerToggle? {
        if (drawerToggle == null)
            drawerToggle = object : ActionBarDrawerToggle(
                activity,
                drawerCreationInfo.drawerLayout,
                drawerCreationInfo.drawerOpenContentDesc,
                drawerCreationInfo.drawerCloseContentDesc
            ) {
//                override fun onDrawerClosed(view: View?) {
//                    super.onDrawerClosed(view)
//                }
//
//                override fun onDrawerOpened(drawerView: View?) {
//                    super.onDrawerOpened(drawerView)
//                }
//
//                override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
//                    super.onDrawerSlide(drawerView, slideOffset)
//                    //                    animateImageUser(slideOffset);
//                }
            }
        return drawerToggle
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerCreationInfo.let {
            getDrawerToggle()?.onConfigurationChanged(newConfig)
        }
    }

    fun closeDrawer() {
        if (drawerCreationInfo.drawerLayout.isDrawerOpen(
                drawerCreationInfo.drawerGravity
            )
        )
            drawerCreationInfo.drawerLayout.closeDrawer(drawerCreationInfo.drawerGravity)
    }

    fun openDrawer() {
        drawerCreationInfo.drawerLayout.openDrawer(drawerCreationInfo.drawerGravity)
    }

    fun openCloseDrawer() {
        if (drawerCreationInfo.drawerLayout.isDrawerOpen(drawerCreationInfo.drawerGravity))
            closeDrawer()
        else
            openDrawer()
    }

    interface DrawerCreationInfo {

        val drawerRecyclerView: RecyclerView

        var drawerRecyclerLayoutManager: RecyclerView.LayoutManager

        val drawerRecyclerAdapter: RecyclerView.Adapter<*>

        @get:Slide.GravityFlag
        val drawerGravity: Int

        @get:StringRes
        val drawerOpenContentDesc: Int

        @get:StringRes
        val drawerCloseContentDesc: Int

        val drawerLayout: DrawerLayout
    }
}