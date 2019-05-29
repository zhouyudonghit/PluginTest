package com.example.pluginstandard.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pluginstandard.LogConfig;

public class PluginActivity extends AppCompatActivity implements IPluginActivity {
    private String TAG = LogConfig.TAG_PREFIX+"PluginActivity";
    protected Activity that;

    @Override
    public void attach(Activity proxyActivity) {
        this.that = proxyActivity;
    }

    @Override
    public void setContentView(View view) {
        if(that != null)
        {
            that.setContentView(view);
        }else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if(that != null) {
            that.setContentView(layoutResID);
        }else{
            super.setContentView(layoutResID);
        }
    }

    @Override
    public ComponentName startService(Intent service) {
        if(that != null)
        {
            Intent m = new Intent();
            m.putExtra("serviceName",service.getComponent().getClassName());
            return that.startService(m);
        }
        return super.startService(service);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if(that != null)
        {
            return that.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        if(that!=null){
            return that.getIntent();
        }
        return super.getIntent();
    }
    @Override
    public ClassLoader getClassLoader() {
        if(that != null) {
            return that.getClassLoader();
        }
        return super.getClassLoader();
    }

    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        if(that != null) {
            return that.registerReceiver(receiver, filter);
        }
        return super.registerReceiver(receiver, filter);
    }

    @Override
    public void sendBroadcast(Intent intent) {
        if(that != null) {
            that.sendBroadcast(intent);
        }else {
            super.sendBroadcast(intent);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        Log.d(TAG,"startActivity");
        if(that != null) {
            //ProxyActivity --->className
            Intent m = new Intent();
            m.putExtra("className", intent.getComponent().getClassName());
            that.startActivity(m);
        }else {
            super.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if(that != null) {
            return that.getLayoutInflater();
        }
        return super.getLayoutInflater();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if(that != null) {
            return that.getApplicationInfo();
        }
        return super.getApplicationInfo();
    }


    @Override
    public Window getWindow() {
        if(that != null) {
            return that.getWindow();
        }
        return super.getWindow();
    }


    @Override
    public WindowManager getWindowManager() {
        if(that != null) {
            return that.getWindowManager();
        }
        return super.getWindowManager();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStart() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onResume() {

    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onPause() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onStop() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onDestroy() {

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}