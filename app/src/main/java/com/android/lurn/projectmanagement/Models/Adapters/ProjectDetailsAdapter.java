package com.android.lurn.projectmanagement.Models.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.lurn.projectmanagement.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmett on 18/03/2016.
 */
public class ProjectDetailsAdapter extends JSONAdapter
{
    private static final String TAG = "ProjectDetailsAdapter";

    public ProjectDetailsAdapter(Context context, ArrayList<JSONObject> list)
    {
        super(context, list);
    }

    public ProjectDetailsAdapter(Context context, ArrayList<JSONObject> list, String idKey)
    {
        super(context, list, idKey);
    }

    @Override
    protected int getResourceRowLayout()
    {
        return R.layout.project_detail_content;
    }

    @Override
    protected View mapData(View row, JSONObject data)
    {
        TextView header = (TextView) row.findViewById(R.id.header_text_view);
        TextView content = (TextView) row.findViewById(R.id.content_text_view);

        try
        {
            header.setText(data.getString("header"));
            content.setText(data.getString("content"));
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }

        return row;
    }

    @Override
    protected ArrayList<JSONObject> onPreProcess(ArrayList<JSONObject> list)
    {
        ArrayList<JSONObject> processedList = new ArrayList<JSONObject>();
        Map<String, String> map = new HashMap<String, String>();
        JSONObject sourceObject = list.get(0);

        try
        {
            map.put("Title", sourceObject.getString("title"));
            map.put("Description", sourceObject.getString("description"));
            map.put("Location", sourceObject.getString("location"));
            map.put("Client", sourceObject.getJSONObject("client").getString("company_name"));
            map.put("Project Status", sourceObject.getJSONObject("project_status").getString("title"));
            map.put("Project Manager", sourceObject.getJSONObject("employee").getString("name"));
            map.put("Start Date", this.parse(sourceObject.getString("start_date")));
            map.put("End Date", this.parse(sourceObject.getString("end_date")));

            int i = 0;
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                JSONObject objectContainer = new JSONObject();
                objectContainer.put("id", i++);
                objectContainer.put("header", entry.getKey());
                objectContainer.put("content", entry.getValue());
                processedList.add(objectContainer);
            }
        }
        catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }

        Log.d(TAG, processedList.toString());

        return processedList;
    }
}
