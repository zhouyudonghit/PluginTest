package com.example.pluginstandard.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;

import com.example.pluginstandard.base.IBase;

public interface IPluginService extends IBase {
    void onCreate();

    void onStart(Intent intent, int startId);

    int onStartCommand(Intent intent, int flags, int startId);

    void onDestroy();

    void onConfigurationChanged(Configuration newConfig);

    void onLowMemory();

    void onTrimMemory(int level);

    IBinder onBind(Intent intent);

    boolean onUnbind(Intent intent);

    void onRebind(Intent intent);

    void onTaskRemoved(Intent rootIntent);
    //传递Context
    void attach(Service proxyService);
}
