package com.example.plugintarget.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.pluginstandard.LogConfig;
import com.example.pluginstandard.service.AbsPluginService;
import com.example.plugintarget.R;

public class TargetService extends AbsPluginService {
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(that,mResources.getString(R.string.service_started),Toast.LENGTH_LONG);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind,"+this);
        MyBinder binder = new MyBinder();
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MyBinder extends Binder{
        public TargetService getService()
        {
            return TargetService.this;
        }
    }
}
