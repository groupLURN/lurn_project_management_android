package com.android.lurn.projectmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.lurn.projectmanagement.Models.Clients.RestClient;
import com.android.lurn.projectmanagement.Models.Configurations.HttpRequest;
import com.android.lurn.projectmanagement.Models.Events.PostFailureEvent;
import com.android.lurn.projectmanagement.Models.Helpers.SystemBus;

/**
 * Created by Emmett on 17/03/2016.
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private final static String TAG = "BaseActivity";

    protected final static String CONNECTION_ERROR = "Failed to connect. Please check your\nserver IP address or network connectivity!";
    protected final static String USER_CREDENTIAL_ERROR = "Failed to connect. Please check your\nusername or password!";
    protected final static String SOCKET_TIMEOUT_ERROR = "Connection timeout! Please try again.";
    protected final static String UNKNOWN_ERROR = "Failed to connect! Unknown Error!";

    // Re-loads the settings on every activity creation.
    private void onSettingsReload()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        HttpRequest.setHostname(
                settings.getString(getString(R.string.pref_key_ip_address), getString(R.string.pref_default_ip_address))
        );
        HttpRequest.setUsername(
                settings.getString(getString(R.string.pref_key_username), getString(R.string.pref_default_username))
        );
        HttpRequest.setPassword(
                settings.getString(getString(R.string.pref_key_password), getString(R.string.pref_default_password))
        );
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Inflate views.
        setContentView(getParentLayout());
        // Initialize widgets.
        onParentWidgetReference();
        onParentWidgetSetup();

        // Inflate the child view.
        View content = LayoutInflater.from(this).inflate(
                getChildLayout(), null);
        getContentContainer().addView(content);
        // Initialize widgets.
        onChildWidgetReference();
        onChildWidgetSetup();

        // Pre-load the saved settings.
        onSettingsReload();

        // Register this activity to the Bus.
        SystemBus.instance().register(this);

        Intent intent = getIntent();
        if(intent != null)
            onIntentReceive(intent);
    }


    @Override
    protected final void onDestroy() {
        super.onDestroy();
        // Unregister this activity to the Bus.
        SystemBus.instance().unregister(this);
    }

    protected void onPostFailure(PostFailureEvent event)
    {
        int exception = (int) event.getResult();
        String errorMessage = UNKNOWN_ERROR;

        switch (exception)
        {
            case RestClient.CONNECTION_EXCEPTION:
                errorMessage = CONNECTION_ERROR;
                break;
            case RestClient.USER_CREDENTIAL_EXCEPTION:
                errorMessage = USER_CREDENTIAL_ERROR;
                break;
            case RestClient.SOCKET_TIMEOUT_EXCEPTION:
                errorMessage = SOCKET_TIMEOUT_ERROR;
                break;
        }

        Snackbar.make(getCoordinatorLayout(), errorMessage, Snackbar.LENGTH_LONG).show();
    }

    protected abstract int getParentLayout();
    protected abstract void onParentWidgetReference();
    protected abstract void onParentWidgetSetup();
    protected abstract int getChildLayout();
    protected abstract void onChildWidgetSetup();
    protected abstract void onChildWidgetReference();
    protected abstract ViewGroup getContentContainer();
    protected abstract CoordinatorLayout getCoordinatorLayout();
    protected void onIntentReceive(Intent intent) {}
}
