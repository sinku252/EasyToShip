package com.tws.courier.domain.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;

public abstract class BaseDrawerActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;

    public abstract DrawerCreationInfo getDrawerCreationInfo();

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initDrawer();
    }

    public void initDrawer() {
        if (getDrawerCreationInfo() != null && !getDrawerCreationInfo().overrideInitialization()) {
            getDrawerCreationInfo().getDrawerLayout().addDrawerListener(getDrawerToggle());
            getDrawerToggle().setDrawerIndicatorEnabled(true);
            getDrawerCreationInfo().getDrawerRecyclerView()
                    .setLayoutManager(getDrawerCreationInfo().getDrawerRecyclerLayoutManager());
            getDrawerCreationInfo().getDrawerRecyclerView()
                    .setAdapter(getDrawerCreationInfo().getDrawerRecyclerAdapter());
        }
        if (getDrawerCreationInfo() != null) getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getDrawerCreationInfo() != null) getDrawerToggle().onConfigurationChanged(newConfig);
    }

    private void closeDrawer() {
        if (getDrawerCreationInfo() != null
                && getDrawerCreationInfo().getDrawerLayout().isDrawerOpen(getDrawerCreationInfo().getDrawerGravity()))
            getDrawerCreationInfo().getDrawerLayout().closeDrawer(getDrawerCreationInfo().getDrawerGravity());
    }

    private void openDrawer() {
        if (getDrawerCreationInfo() != null)
            getDrawerCreationInfo().getDrawerLayout().openDrawer(getDrawerCreationInfo().getDrawerGravity());
    }

    private void openCloseDrawer() {
        if (getDrawerCreationInfo() != null) {
            if (getDrawerCreationInfo().getDrawerLayout().isDrawerOpen(getDrawerCreationInfo().getDrawerGravity()))
                closeDrawer();
            else
                openDrawer();
        }
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        if (drawerToggle == null)
            drawerToggle = new ActionBarDrawerToggle(this, getDrawerCreationInfo().getDrawerLayout(),
                    getDrawerCreationInfo().getDrawerOpenContentDesc(), getDrawerCreationInfo().getDrawerCloseContentDesc()) {

                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    super.onDrawerSlide(drawerView, slideOffset);
//                    animateImageUser(slideOffset);
                }
            };
        return drawerToggle;
    }

    public interface DrawerCreationInfo {

        RecyclerView getDrawerRecyclerView();

        RecyclerView.LayoutManager getDrawerRecyclerLayoutManager();

        RecyclerView.Adapter getDrawerRecyclerAdapter();

        @Slide.GravityFlag
        int getDrawerGravity();

        @StringRes
        int getDrawerOpenContentDesc();

        @StringRes
        int getDrawerCloseContentDesc();

        DrawerLayout getDrawerLayout();

        // if you want to manually call initDrawer()
        boolean overrideInitialization();
    }
}
