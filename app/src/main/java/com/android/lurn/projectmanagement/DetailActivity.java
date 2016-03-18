package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * An activity representing a single Project detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ProjectListActivity}.
 */
public abstract class DetailActivity extends BaseActivity
{
    public static final String ARG_ITEM_ID = "item_id";

    protected String mId;

    protected CoordinatorLayout mCoordinatorLayout;
    protected ViewGroup mContentContainer;
    protected Toolbar mToolbar;
    protected FloatingActionButton mFab;
    protected CollapsingToolbarLayout mAppBarLayout;

    @Override
    protected int getParentLayout()
    {
        return R.layout.detail_activity;
    }

    @Override
    protected void onParentWidgetReference()
    {
        mToolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mContentContainer = (ViewGroup) findViewById(R.id.content_container);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mAppBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    }

    @Override
    protected void onParentWidgetSetup()
    {
        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected ViewGroup getContentContainer()
    {
        return mContentContainer;
    }

    @Override
    protected void onIntentReceive(Intent intent)
    {
        mId = intent.getStringExtra(ProjectDetailActivity.ARG_ITEM_ID);
    }

    @Override
    protected CoordinatorLayout getCoordinatorLayout()
    {
        return mCoordinatorLayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
