package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

public class ProjectDetailActivity extends DetailActivity {

    private final static String TAG = "ProjectDetailActivity";
    private TextView mTextView;

    @Override
    protected int getChildLayout()
    {
        return R.layout.project_detail_activity;
    }

    @Override
    protected void onChildWidgetReference()
    {
        mTextView = (TextView) findViewById(R.id.project_detail);
    }

    @Override
    protected void onChildWidgetSetup()
    {
        mTextView.setText("Hello World Indeed");

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
        Log.d(TAG, resultObject.toString());
        mAppBarLayout.setTitle(mId);
    }

    @Subscribe public void onPostFailure(PostFailureEvent event)
    {
        super.onPostFailure(event);
    }

}
