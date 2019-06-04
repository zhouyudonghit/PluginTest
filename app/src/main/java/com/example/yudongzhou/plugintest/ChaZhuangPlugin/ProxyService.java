package com.example.yudongzhou.plugintest.ChaZhuangPlugin;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.util.Log;

import com.example.pluginstandard.LogConfig;
import com.example.pluginstandard.service.IPluginService;
import com.example.yudongzhou.plugintest.Constants;
import com.example.yudongzhou.plugintest.PluginManager;

public class ProxyService extends Service {
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();
    private IPluginService mTargetService;
    private String serviceClassName;
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        if(mTargetService == null) {
            serviceClassName = intent.getStringExtra(Constants.CLASS_NAME);
            try {
                Class myClass = getClassLoader().loadClass(serviceClassName);
                mTargetService = (IPluginService) myClass.newInstance();
                mTargetService.attach(this);
                mTargetService.setResources(PluginManager.getInstance().getResources());
                mTargetService.onCreate();
                return mTargetService.onBind(intent);
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }
        }else{
            return mTargetService.onBind(intent);
        }
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand,this = "+this);
        if(mTargetService == null) {
            serviceClassName = intent.getStringExtra(Constants.CLASS_NAME);
            Log.d(TAG,"serviceClassName = "+serviceClassName);
            try {
                Class myClass = getClassLoader().loadClass(serviceClassName);
                mTargetService = (IPluginService) myClass.newInstance();
                mTargetService.attach(this);
                mTargetService.setResources(PluginManager.getInstance().getResources());
                mTargetService.onCreate();
                return mTargetService.onStartCommand(intent, flags, startId);
            } catch (Exception e) {
                Log.d(TAG, "", e);
            }
        }else{
            return mTargetService.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind");
        if(mTargetService != null)
        {
            return mTargetService.onUnbind(intent);
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy()");
        if(mTargetService != null)
        {
            mTargetService.onDestroy();
            mTargetService = null;
        }else {
            super.onDestroy();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }
}
