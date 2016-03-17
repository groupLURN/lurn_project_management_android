package com.android.lurn.projectmanagement.Models.Configurations;

import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Emmett on 16/03/2016.
 */
public final class HttpRequest
{
    private static final String TAG = "HttpRequest";
    private static final String FORMAT = ".json";

    private static String sHostname = "http://192.168.1.11";
    private static String sUsername = "admin";
    private static String sPassword = "admin";

    public static HttpURLConnection generate(String controller) throws IOException
    {
        // Generate URL Object.
        URL url = new URL(sHostname + "/" + controller + FORMAT);
        // Create an HTTP Connection.
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // Add Basic Authentication to HTTP headers.
        String userCredentials = sUsername + ":" + sPassword;
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
        urlConnection.setRequestProperty("Authorization", basicAuth);

        Log.d(TAG, url.toString());
        return urlConnection;
    }

    // TODO: Next time.
    public static HttpURLConnection generate(String controller, ArrayList<Pair<String, String>> queries) throws IOException
    {
        return generate(controller);
    }

    // Getters and Setters
    public static String getHostname()
    {
        return sHostname;
    }

    public static void setHostname(String hostname)
    {
        sHostname = hostname;
    }

    public static String getUsername()
    {
        return sUsername;
    }

    public static void setUsername(String username)
    {
        sUsername = username;
    }

    public static String getPassword()
    {
        return sPassword;
    }

    public static void setPassword(String password)
    {
        sPassword = password;
    }
}
