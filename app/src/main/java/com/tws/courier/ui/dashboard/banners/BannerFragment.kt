package com.app.monrotv.ui.auth.register

import android.os.Bundle
import android.view.View
import com.tws.courier.R
import com.tws.courier.databinding.FragmentBannerBinding
import com.tws.courier.domain.models.HomeSlider
import com.tws.courier.ui.home.base.HomeBaseFragment

class BannerFragment : HomeBaseFragment<BannerViewModel, FragmentBannerBinding>() {

    override fun getToolbarMenuHandler(): ToolbarMenuHandler? = null
    override fun getLayoutResource(): Int = R.layout.fragment_banner
    override fun getViewModelClass(): Class<BannerViewModel> = BannerViewModel::class.java

    private var homeSlider: HomeSlider? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        homeSlider?.let {
            viewBinding.item = it
        }

        viewBinding.constRoot.setOnClickListener {
            homeSlider?.let { item ->
                if (item.type.equals("M"))
                  //  fragmentListener?.navigateToMovieDetailsFragment(item.movieId)
                else if (item.type.equals("T"))
                    //fragmentListener?.navigateToTvShowDetailsFragment(item.tvSeriesId)
                else showMessageDialog("Invalid media type!!")
            }
        }
    }

    companion object {
        val TAG = "BannerFragment"
        val ARG_BANNER = "bannar"

        fun newInstance(homeSlider: HomeSlider) = BannerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_BANNER, homeSlider)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_BANNER)) homeSlider = it.getParcelable(ARG_BANNER)
        }
    }
}