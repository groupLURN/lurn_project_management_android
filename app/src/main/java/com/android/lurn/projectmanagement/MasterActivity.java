package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;

public abstract class MasterActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private final static String TAG = "MasterActivity";

    protected Toolbar mToolbar;
    protected FloatingActionButton mFab;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected ViewGroup mContentContainer;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected CoordinatorLayout mCoordinatorLayout;

    @Override
    protected int getParentLayout()
    {
        return R.layout.master_activity;
    }

    @Override
    protected final void onParentWidgetReference()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mContentContainer = (ViewGroup) findViewById(R.id.content_container);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mContentContainer;
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
    }

    @Override
    protected final void onParentWidgetSetup()
    {
        // Use the toolbar instead of the system one.
        setSupportActionBar(mToolbar);

        // Connect an on-click listener.
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initialize the toggling of drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Bind a listener which calls different activities based on the chosen module.
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    protected void onPostSuccess(PostSuccessEvent event)
    {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void onPostFailure(PostFailureEvent event)
    {
        super.onPostFailure(event);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Does not close the application if the drawer is still opened when back button is pressed.
     */
    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    /**
     * Initialize settings.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.master_menu, menu);
        return true;
    }

    /**
     * Controller for the select menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Controller for the navigation bar.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected ViewGroup getContentContainer()
    {
        return mContentContainer;
    }

    @Override
    protected CoordinatorLayout getCoordinatorLayout()
    {
        return mCoordinatorLayout;
    }
}
