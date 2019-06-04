package com.example.pluginstandard.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import com.example.pluginstandard.LogConfig;

public  class AbsPluginService extends Service implements IPluginService{
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();
    protected Resources mResources;
    protected Service that;
    @Override
    public void setResources(Resources resources) {
        mResources = resources;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void attach(Service proxyService) {
        that = proxyService;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onCreate, "+this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onStartCommand,"+this);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onDestroy,"+this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onConfigurationChanged,"+this);
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onLowMemory,"+this);
    }

    @Override
    public void onTrimMemory(int level) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onTrimMemory,"+this);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onUnbind,"+this);
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onRebind,"+this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Log.d(TAG, TAG + " onTaskRemoved,"+this);
    }
}
