package com.example.bakingapp.rizwan.timberlog;

import android.app.Application;

import com.example.bakingapp.rizwan.BuildConfig;

import timber.log.Timber;

public class TimberApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        } else {
            Timber.plant(new ReleaseTree());
        }
    }
}

