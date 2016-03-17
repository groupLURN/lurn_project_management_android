package com.android.lurn.projectmanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.lurn.projectmanagement.Models.Adapters.JSONObjectWrapper;
import com.android.lurn.projectmanagement.Models.Adapters.ProjectsAdapter;
import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.squareup.otto.Subscribe;

import java.net.ConnectException;
import java.util.ArrayList;

/**
 * An activity representing a list of Projects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProjectDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProjectListActivity extends BaseActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener
{
    private static final String TAG = "ProjectListActivity";

    private ArrayList<JSONObjectWrapper> mList = new ArrayList<JSONObjectWrapper>();
    private ListView mListView;

    private void initializeList()
    {
        JSONObjectWrapper i = new JSONObjectWrapper();
        i.put("id", 1);
        i.put("content", "marvelous");
        mList.add(i);
        mList.add(i);
    }

    @Override
    protected int getResourceLayout()
    {
        return R.layout.project_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate() called");
        initializeList();

        mListView = (ListView) findViewById(R.id.project_list);
        mListView.setAdapter(new ProjectsAdapter((Context) this, mList));
        mListView.setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Context context = (Context) this;
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        intent.putExtra(ProjectDetailFragment.ARG_ITEM_ID, Long.toString(mList.get(position).getLong("id")));
        Log.d(TAG, Long.toString(mList.get(position).getLong("id")));
        context.startActivity(intent);
    }

    @Override
    public void onRefresh()
    {
        Log.d(TAG, "onRefresh() called");
        new RestClient().execute();
    }

    @Subscribe
    public void onPostSuccess(PostSuccessEvent event)
    {
        Log.i(TAG, event.getResult().toString());
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onPostFailure(PostFailureEvent event)
    {
        Exception exception = (Exception) event.getResult();
        if(exception instanceof ConnectException)
            Log.e(TAG, "Failed to connect. Please make sure the server IP address is " +
                    "correct or your network connectivity!");
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
