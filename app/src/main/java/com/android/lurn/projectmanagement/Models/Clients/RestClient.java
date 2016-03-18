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
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

/**
 * Created by Emmett on 16/03/2016.
 */
public class RestClient extends AsyncTask<Object, Object, Object>
{
    public final static int CONNECTION_EXCEPTION = 0x01;
    public final static int USER_CREDENTIAL_EXCEPTION = 0x02;
    public final static int UNKNOWN_EXCEPTION = 0x04;
    public final static int SOCKET_TIMEOUT_EXCEPTION = 0x08;

    private final static int TIMEOUT_IN_MILLISECONDS = 5000;

    private final static String TAG = "RestClient";

    private HttpURLConnection mHttpConnection;
    private int mException;

    public void viewMaster(String controller)
    {
        try
        {
            mHttpConnection = HttpRequest.generate(new String[]{controller});
            this.execute();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    public void viewDetails(String controller, String id)
    {
        try
        {
            mHttpConnection = HttpRequest.generate(new String[]{controller, id});
            this.execute();
        }
        catch (IOException e)
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
            mHttpConnection.setConnectTimeout(TIMEOUT_IN_MILLISECONDS);
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
        }
        catch (ConnectException exception)
        {
            mException = CONNECTION_EXCEPTION;
            Log.e(TAG, exception.getMessage(), exception);
        }
        catch (SocketTimeoutException exception)
        {
            mException = SOCKET_TIMEOUT_EXCEPTION;
            Log.e(TAG, exception.getMessage(), exception);
        }
        catch (IOException exception)
        {
            mException = USER_CREDENTIAL_EXCEPTION;
            Log.e(TAG, exception.getMessage(), exception);
        }
        catch (Exception exception)
        {
            mException = UNKNOWN_EXCEPTION;
            Log.e(TAG, exception.getMessage(), exception);
        }
        finally
        {
            mHttpConnection.disconnect();
        }
        return null;
    }

    protected void onPostExecute(Object response)
    {
        if (response == null)
            SystemBus.instance().post(new PostFailureEvent(mException));
        else
            SystemBus.instance().post(new PostSuccessEvent(response));
    }
}
