package com.android.lurn.projectmanagement.Models.Adapters;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Emmett on 17/03/2016.
 */
public class JSONObjectWrapper extends JSONObject
{
    private static final String TAG = "JSONObjectWrapper";

    @Override
    public Object get(String name)
    {
        try
        {
            return super.get(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public boolean getBoolean(String name)
    {
        try
        {
            return super.getBoolean(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }

    @Override
    public double getDouble(String name)
    {
        try
        {
            return super.getDouble(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    @Override
    public int getInt(String name)
    {
        try
        {
            return super.getInt(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    @Override
    public JSONArray getJSONArray(String name)
    {
        try
        {
            return super.getJSONArray(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject getJSONObject(String name)
    {
        try
        {
            return super.getJSONObject(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public long getLong(String name)
    {
        try
        {
            return super.getLong(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return -1;
    }

    @Override
    public String getString(String name)
    {
        try
        {
            return super.getString(name);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject put(String name, boolean value)
    {
        try
        {
            return super.put(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject put(String name, double value)
    {
        try
        {
            return super.put(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject put(String name, int value)
    {
        try
        {
            return super.put(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject put(String name, long value)
    {
        try
        {
            return super.put(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject put(String name, Object value)
    {
        try
        {
            return super.put(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public JSONObject putOpt(String name, Object value)
    {
        try
        {
            return super.putOpt(name, value);
        } catch (JSONException e)
        {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
