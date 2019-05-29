package com.example.yudongzhou.plugintest.ChaZhuangPlugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.example.pluginstandard.LogConfig;
import com.example.yudongzhou.plugintest.PluginManager;

public class ProxyBroadcastReceiver extends BroadcastReceiver {
    private String TAG = LogConfig.TAG_PREFIX+getClass().getSimpleName();

    public ProxyBroadcastReceiver()
    {
        parseTargetReceivers();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"intent.getAction()"+intent.getAction());
    }

    private void parseTargetReceivers()
    {
        Log.d(TAG,"parseTargetReceivers");
        PackageInfo packageInfo = PluginManager.getInstance().getReceiverPackageInfo();
        ActivityInfo[] receivers = packageInfo.receivers;
        Log.d(TAG,"receivers = "+receivers);
    }
}
