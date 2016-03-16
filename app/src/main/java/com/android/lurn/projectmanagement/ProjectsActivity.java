package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.android.lurn.projectmanagement.Models.Events.PreExecuteEvent;
import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Helpers.SystemBus;
import com.squareup.otto.Subscribe;

public class ProjectsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TAG = "ProjectsActivity";
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ArrayAdapter mAdapter;
    private Button mQueryButton;
    private ProgressBar mProgressBar;
    private TextView mResponseView;

    private void onWidgetReference()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mQueryButton = (Button) findViewById(R.id.queryButton);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mResponseView = (TextView) findViewById(R.id.responseView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        onWidgetReference();
        // Use the toolbar instead of the system one.
        setSupportActionBar(mToolbar);

        // I don't know what this code does.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Bind a listener.
        mNavigationView.setNavigationItemSelectedListener(this);

        // Connect a listener.
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Connect a listener.
        mQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RestClient().execute();
            }
        });

        SystemBus.instance().register(this);

//        String [] array = {
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today",
//                "Today"
//        };
//        List<String> list = new ArrayList<String>(Arrays.asList(array));
//        mAdapter = new ArrayAdapter(this, R.layout.list_item_projects, R.id.list_item_projects_text_view, list);

//        ListView recyclerView = (ListView) findViewById(R.id.recycler_view_projects);
//        recyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onPreExecute(PreExecuteEvent event) {
        mProgressBar.setVisibility(View.VISIBLE);
        mResponseView.setText("");
    }

    @Subscribe
    public void onPostSuccess(PostSuccessEvent event) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mResponseView.setText(event.getResult().toString());
    }

    @Subscribe
    public void onPostFailure(PostFailureEvent event) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mResponseView.setText("Failure");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SystemBus.instance().unregister(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.projects, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
