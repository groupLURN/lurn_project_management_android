package com.android.lurn.projectmanagement.Models.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.lurn.projectmanagement.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Emmett on 17/03/2016.
 */
public class ProjectsAdapter extends JSONAdapter
{
    private static final String TAG = "JSONAdapter";

    public ProjectsAdapter(Context context, ArrayList<JSONObject> list, String idKey)
    {
        super(context, list, idKey);
    }

    public ProjectsAdapter(Context context, ArrayList<JSONObject> list)
    {
        super(context, list);
    }

    @Override
    protected int getResourceRowLayout()
    {
        return R.layout.project_list_content;
    }

    @Override
    protected View mapData(View row, JSONObject data)
    {
        TextView title = (TextView) row.findViewById(R.id.title);
        TextView location = (TextView) row.findViewById(R.id.location);
        TextView statusView = (TextView) row.findViewById(R.id.status);
        TextView startDate = (TextView) row.findViewById(R.id.start_date);
        TextView endDate = (TextView) row.findViewById(R.id.end_date);
        DonutProgress progress = (DonutProgress) row.findViewById(R.id.progress);

        try
        {
            title.setText(data.getString("title"));
            location.setText(data.getString("location"));
            statusView.setText(data.getJSONObject("project_status").getString("title"));
            startDate.setText(this.parse(data.getString("start_date")));
            endDate.setText(this.parse(data.getString("end_date")));
            progress.setProgress(data.getInt("progress"));
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

        return row;
    }
}
