package com.android.lurn.projectmanagement.Models.Clients;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Events.PostSuccessEvent;
import com.android.lurn.projectmanagement.Models.Events.PreExecuteEvent;
import com.android.lurn.projectmanagement.Models.Helpers.SystemBus;
import com.android.lurn.projectmanagement.Models.Configurations.RestURL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Emmett on 16/03/2016.
 */
public class RestClient extends AsyncTask<Object, Object, Object> {

    private Exception exception;

    protected void onPreExecute() {
        SystemBus.instance().post(new PreExecuteEvent());
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            URL url = RestURL.generate("projects");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String userCredentials = "manager:admin";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            urlConnection.setRequestProperty ("Authorization", basicAuth);
            try {
                BufferedReader bufferedReader;
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return new JSONObject(stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }


    protected void onPostExecute(Object response) {
        if(response == null)
            SystemBus.instance().post(new PostFailureEvent(response));
        else
            SystemBus.instance().post(new PostSuccessEvent(response));
    }
}
