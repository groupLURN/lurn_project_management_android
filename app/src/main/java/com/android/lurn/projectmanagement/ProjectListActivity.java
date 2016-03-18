package com.android.lurn.projectmanagement;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.lurn.projectmanagement.Models.Adapters.ProjectsAdapter;
import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * An activity representing a list of Projects. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProjectDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProjectListActivity extends MasterActivity implements AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener
{
    private static final String TAG = "ProjectListActivity";
    private static final String CONTROLLER = "Projects";

    private ArrayList<JSONObject> mList = new ArrayList<JSONObject>();
    private ListView mListView;

    @Override
    protected int getChildLayout()
    {
        return R.layout.project_list;
    }

    @Override
    protected void onChildWidgetReference()
    {
        mListView = (ListView) findViewById(R.id.project_list);
    }

    @Override
    protected void onChildWidgetSetup()
    {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Context context = (Context) this;
        Intent intent = new Intent(context, ProjectDetailActivity.class);
        Log.d(TAG, "Sending id:" + Long.toString(id) + " to detail activity.");
        intent.putExtra(ProjectDetailFragment.ARG_ITEM_ID, Long.toString(id));
        context.startActivity(intent);
    }

    @Override
    public void onRefresh()
    {
        Log.d(TAG, "onRefresh() called");
        new RestClient().viewMaster(CONTROLLER);
    }

    @Subscribe
    public void onPostSuccess(PostSuccessEvent event)
    {
        JSONObject resultObject = (JSONObject) event.getResult();
        try
        {
            JSONArray resultSet = resultObject.getJSONArray("projects");
            mList.clear();
            for(int i = 0; i < resultSet.length(); i++)
                mList.add((JSONObject)resultSet.get(i));
            mListView.setAdapter(new ProjectsAdapter(this, mList));
            Log.d(TAG, mList.toString());
        }
        catch(JSONException exception)
        {
            Log.e(TAG, "onPostSuccess: Failed on loading the data.");
        }
        finally
        {
            super.onPostSuccess(event);
        }
    }

    @Subscribe
    public void onPostFailure(PostFailureEvent event)
    {
        super.onPostFailure(event);
    }
}
