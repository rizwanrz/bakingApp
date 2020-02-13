package com.example.bakingapp.rizwan.timberlog;

import android.util.Log;

import timber.log.Timber;

public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO || priority == Log.ERROR || priority == Log.WARN) {
            return;
        }
    }
}
