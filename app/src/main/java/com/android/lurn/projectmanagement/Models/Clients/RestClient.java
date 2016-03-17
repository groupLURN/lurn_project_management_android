package com.android.lurn.projectmanagement.Models.Clients;

import android.os.AsyncTask;
import android.util.Log;

import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.android.lurn.projectmanagement.Models.Events.PreExecuteEvent;
import com.android.lurn.projectmanagement.Models.Helpers.SystemBus;
import com.android.lurn.projectmanagement.Models.Configurations.HttpRequest;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Emmett on 16/03/2016.
 */
public class RestClient extends AsyncTask<Object, Object, Object>
{
    private final static String TAG = "RestClient";

    private HttpURLConnection mHttpConnection;
    private Exception mException;

    public void viewMaster(String controller)
    {
        try
        {
            mHttpConnection = HttpRequest.generate(new String[] {controller});
            this.execute();
        } catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public void viewDetails(String controller, int id)
    {
        try
        {
            mHttpConnection = HttpRequest.generate(new String[] {controller, Integer.toString(id)});
            this.execute();
        } catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    protected void onPreExecute()
    {
        SystemBus.instance().post(new PreExecuteEvent());
    }

    @Override
    protected Object doInBackground(Object... params)
    {
        try
        {
            try
            {
                BufferedReader bufferedReader;
                bufferedReader = new BufferedReader(new InputStreamReader(mHttpConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return new JSONObject(stringBuilder.toString());
            } finally
            {
                mHttpConnection.disconnect();
            }
        } catch (Exception exception)
        {
            mException = exception;
            Log.e(TAG, exception.getMessage(), exception);
            return null;
        }
    }

    protected void onPostExecute(Object response)
    {
        if (response == null)
            SystemBus.instance().post(new PostFailureEvent(mException));
        else
            SystemBus.instance().post(new PostSuccessEvent(response));
    }
}
