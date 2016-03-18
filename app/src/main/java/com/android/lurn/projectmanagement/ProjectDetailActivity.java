package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.lurn.projectmanagement.Models.Adapters.ProjectDetailsAdapter;
import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectDetailActivity extends DetailActivity {

    private final static String TAG = "ProjectDetailActivity";
    private ListView mListView;

    @Override
    protected int getChildLayout()
    {
        return R.layout.list_view;
    }

    @Override
    protected void onChildWidgetReference()
    {
        mListView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    protected void onChildWidgetSetup()
    {
        mFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onIntentReceive(Intent intent)
    {
        super.onIntentReceive(intent);
        Log.d(TAG, "onIntentReceive(): View Details(" + ProjectListActivity.CONTROLLER + "," + mId + ")");
        new RestClient().viewDetails(ProjectListActivity.CONTROLLER, mId);
    }

    @Subscribe public void onPostSuccess(PostSuccessEvent event)
    {
        JSONObject resultObject = (JSONObject) event.getResult();

        try
        {
            resultObject = resultObject.getJSONObject("project");
            ArrayList<JSONObject> list = new ArrayList<JSONObject>();
            list.add(resultObject);
            Log.d(TAG, list.toString());
            mAppBarLayout.setTitle(resultObject.getString("title"));
            mListView.setAdapter(new ProjectDetailsAdapter(this, list));
        }
        catch(JSONException exception)
        {
            Log.e(TAG, "onPostSuccess: Failed on loading the data.");
        }
    }

    @Subscribe public void onPostFailure(PostFailureEvent event)
    {
        super.onPostFailure(event);
    }

}
