package com.latticeInnovations.app.controller;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.latticeInnovations.app.data.ResourceProvider;

public class AppController extends Application {
    private static AppController instance;
    private RequestQueue requestQueue;
    private ResourceProvider mResourceProvider;
    public ResourceProvider getResourceProvider() {
        if (mResourceProvider == null)
            mResourceProvider = new ResourceProvider(this);

        return mResourceProvider;
    }

    public static synchronized AppController getInstance() {

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
