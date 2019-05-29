package com.example.yudongzhou.plugintest.ChaZhuangPlugin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pluginstandard.activity.IPluginActivity;
import com.example.pluginstandard.LogConfig;
import com.example.yudongzhou.plugintest.PluginManager;

import java.lang.reflect.Constructor;

public class ProxyActivity extends AppCompatActivity {
    private String TAG = LogConfig.TAG_PREFIX+getClass().getSimpleName();
    private String className;
    private IPluginActivity mTargetActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"getWindow() = "+getWindow());
        className = getIntent().getStringExtra("className");
        Log.d(TAG,"onCreate,className = "+className);
        try{
            Class activityClass = getClassLoader().loadClass(className);
            Log.d(TAG,getClassLoader().toString());
            Constructor activityConstructor = activityClass.getConstructor(new Class[]{});
            Object activityObject = activityConstructor.newInstance(new Object[]{});
            mTargetActivity = (IPluginActivity) activityObject;

            mTargetActivity.attach(this);
            Bundle bundle = new Bundle();
            mTargetActivity.onCreate(bundle);
        }catch (Exception e)
        {
            Log.d(TAG,"",e);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        String className1 = intent.getStringExtra("className");
        Intent intent1 = new Intent(this, ProxyActivity.class);
        intent1.putExtra("className", className1);
        super.startActivity(intent1);
    }

    @Override
    public ComponentName startService(Intent service) {
        String serviceName = service.getStringExtra("serviceName");
        Intent intent1 = new Intent(this, ProxyService.class);
        intent1.putExtra("serviceName", serviceName);
        return super.startService(intent1);
    }

    @Override
    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        String serviceName = service.getStringExtra("serviceName");
        Intent intent1 = new Intent(this, ProxyService.class);
        intent1.putExtra("serviceName", serviceName);
        return super.bindService(intent1, conn, flags);
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

//    @Override
//    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
//        IntentFilter newInterFilter = new IntentFilter();
//        for (int i=0;i<filter.countActions();i++) {
//            newInterFilter.addAction(filter.getAction(i));
//        }
//        return super.registerReceiver(new ProxyBroadcastReceiver(receiver.getClass().getName(),this),newInterFilter);
//    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getResources();
    }


    @Override
    protected void onStart() {
        Log.d(TAG,"onStart()");
        super.onStart();
        mTargetActivity.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
        mTargetActivity.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");
        super.onPause();
        mTargetActivity.onPause();
    }
}
