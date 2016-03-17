package com.android.lurn.projectmanagement.Models.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Emmett on 17/03/2016.
 */
public abstract class JSONAdapter extends BaseAdapter
{
    private static final String TAG = "JSONAdapter";

    private ArrayList<JSONObject> mList;
    private String mIdKey = "id";
    private Context mContext;

    public JSONAdapter(Context context, ArrayList<JSONObject> list)
    {
        mContext = context;
        mList = list;
    }

    public JSONAdapter(Context context, ArrayList<JSONObject> list, String idKey)
    {
        mContext = context;
        mList = list;
        mIdKey = idKey;
    }

    @Override
    public int getCount()
    {
        return mList.size();
    }

    @Override
    public JSONObject getItem(int position)
    {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        try
        {
            return mList.get(position).getLong(mIdKey);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(getResourceRowLayout(), parent, false);
        try
        {
            return mapData(row, getItem(position));
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    protected abstract int getResourceRowLayout();
    protected abstract View mapData(View row, JSONObject data) throws JSONException;
}
