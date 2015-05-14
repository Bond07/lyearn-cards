package com.example.nirav.lyearn;

import android.app.Application;
import android.content.Context;

/**
 * Created by nirav on 5/4/15.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {

        super.onCreate();

        sInstance = this;

    }

    public static MainApplication getInstance() {

        return sInstance;

    }

    public static Context getAppContext() {

        return sInstance.getApplicationContext();

    }

}
