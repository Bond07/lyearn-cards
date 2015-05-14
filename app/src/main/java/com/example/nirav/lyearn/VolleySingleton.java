package com.example.nirav.lyearn;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by nirav on 5/4/15.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;

    private RequestQueue mRequestQueue;

    private VolleySingleton() {

        mRequestQueue = Volley.newRequestQueue(MainApplication.getAppContext());

    }

    public static VolleySingleton getsInstance() {

        if(sInstance == null) {

            sInstance = new VolleySingleton();

        }

        return sInstance;
    }

    public RequestQueue getRequestQueue() {

        return  mRequestQueue;

    }
}
