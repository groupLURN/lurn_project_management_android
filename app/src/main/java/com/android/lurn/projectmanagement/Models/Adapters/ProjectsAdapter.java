package com.android.lurn.projectmanagement.Models.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.android.lurn.projectmanagement.R;

import java.util.ArrayList;

/**
 * Created by Emmett on 17/03/2016.
 */
public class ProjectsAdapter extends JSONAdapter
{

    public ProjectsAdapter(Context context, ArrayList<JSONObjectWrapper> list, String idKey)
    {
        super(context, list, idKey);
    }

    public ProjectsAdapter(Context context, ArrayList<JSONObjectWrapper> list)
    {
        super(context, list);
    }

    @Override
    protected int getResourceRowLayout()
    {
        return R.layout.project_list_content;
    }

    @Override
    protected View mapData(View row, JSONObjectWrapper data)
    {
        TextView idView = (TextView) row.findViewById(R.id.id);
        TextView contentView = (TextView) row.findViewById(R.id.content);

        idView.setText(Long.toString(data.getLong("id")));
        contentView.setText(data.getString("content"));

        return row;
    }
}
