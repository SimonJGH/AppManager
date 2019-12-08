package com.simon.appmanager;


import android.app.Application;

import org.xutils.x;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@SuppressWarnings("all")
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = (MyApplication) this.getApplicationContext();

        //初始化xUtils
        x.Ext.init(this);
        x.Ext.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

    }

    public static MyApplication getInstance() {
        return mInstance;
    }
}
