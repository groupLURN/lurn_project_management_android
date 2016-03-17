package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Configurations.HttpRequest;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.android.lurn.projectmanagement.Models.Helpers.SystemBus;

public abstract class MasterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "MasterActivity";

    protected final static String CONNECTION_ERROR = "Failed to connect. Please check your\n" +
            "server IP address or network connectivity!";

    protected final static String USER_CREDENTIAL_ERROR = "Failed to connect. Please check your\n" +
            "username or password!";

    protected final static String SOCKET_TIMEOUT_ERROR = "Connection timeout! Please try again.";

    protected final static String UNKNOWN_ERROR = "Failed to connect! Unknown Error!";


    protected Toolbar mToolbar;
    protected FloatingActionButton mFab;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected ViewGroup mInclusionViewGroup;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected CoordinatorLayout mCoordinatorLayout;

    private void onWidgetReference()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mInclusionViewGroup = (ViewGroup) findViewById(R.id.main_inclusion_layout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mInclusionViewGroup;
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.base_coordinator_layout);
    }

    private void onWidgetSetup()
    {
        // Use the toolbar instead of the system one.
        setSupportActionBar(mToolbar);

        // Connect an on-click listener.
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void onSettingsReload()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        HttpRequest.setHostname(
                settings.getString(getString(R.string.pref_key_ip_address), getString(R.string.pref_default_ip_address))
        );
        HttpRequest.setUsername(
                settings.getString(getString(R.string.pref_key_username), getString(R.string.pref_default_username))
        );
        HttpRequest.setPassword(
                settings.getString(getString(R.string.pref_key_password), getString(R.string.pref_default_password))
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // Inflate views.
        setContentView(R.layout.activity_master);

        // Initialize widgets.
        onWidgetReference();
        onWidgetSetup();

        // Pre-load the saved settings.
        onSettingsReload();

        // Inflate the child view.
        View content = LayoutInflater.from(this).inflate(
                getResourceLayout(), null);
        mInclusionViewGroup.addView(content);

        // Register this activity to the Bus.
        SystemBus.instance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister this activity to the Bus.
        SystemBus.instance().unregister(this);
    }

    protected void onPostSuccess(PostSuccessEvent event)
    {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void onPostFailure(PostFailureEvent event)
    {
        int exception = (int) event.getResult();
        String errorMessage = UNKNOWN_ERROR;

        switch(exception)
        {
            case RestClient.CONNECTION_EXCEPTION:
                errorMessage = CONNECTION_ERROR;
                break;
            case RestClient.USER_CREDENTIAL_EXCEPTION:
                errorMessage = USER_CREDENTIAL_ERROR;
                break;
            case RestClient.SOCKET_TIMEOUT_EXCEPTION:
                errorMessage = SOCKET_TIMEOUT_ERROR;
                break;
        }

        Snackbar.make(mCoordinatorLayout, errorMessage, Snackbar.LENGTH_LONG).show();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Does not close the application if the drawer is still opened when back button is pressed.
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Initialize settings.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    /**
     * Controller for the select menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
    public boolean onNavigationItemSelected(MenuItem item) {
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

    protected abstract int getResourceLayout();
}
