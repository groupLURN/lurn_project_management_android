package com.android.lurn.projectmanagement.Models.Configurations;

import android.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Emmett on 16/03/2016.
 */
public final class RestURL {

    private static final String FORMAT = ".json";

    private static final String mHostname = "http://192.168.1.15";

    private URL mUrl;

    public static URL generate(String controller) throws MalformedURLException {
        return new URL(mHostname + "/" + controller + FORMAT);
    }

    public static URL generate(String controller, ArrayList<Pair<String, String> >queries) throws MalformedURLException {
        return new URL(mHostname + "/" + controller + FORMAT);
    }
}
