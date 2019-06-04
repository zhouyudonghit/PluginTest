package com.example.yudongzhou.plugintest.ChaZhuangPlugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.util.Log;
import com.example.pluginstandard.LogConfig;
import com.example.pluginstandard.broadcastreceiver.IPluginBroadcastReceiver;
import com.example.yudongzhou.plugintest.MainActivity;
import com.example.yudongzhou.plugintest.PluginManager;
import java.lang.reflect.Constructor;

public class ProxyBroadcastReceiver extends BroadcastReceiver {
    private String TAG = LogConfig.TAG_PREFIX+getClass().getSimpleName();
    private ActivityInfo[] mReceivers;

    public ProxyBroadcastReceiver()
    {
        Log.d(TAG,"ProxyBroadcastReceiver()");
        parseTargetReceivers();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"context = "+context+",intent.getAction() = "+intent.getAction());
        Log.d(TAG,"context.getApplicationContext() = "+context.getApplicationContext());
        //Toast.makeText(context,R.string.app_name,Toast.LENGTH_LONG).show();
        if(mReceivers != null && mReceivers.length > 0)
        {
            for(ActivityInfo activityInfo:mReceivers)
            {
                String className = activityInfo.name;
                Log.d(TAG,"className = "+className);
                try {
                    Class myClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
                    Constructor receiverConstructor = myClass.getConstructor(new Class[]{});
                    Object obj = receiverConstructor.newInstance(new Object[]{});
//                    Object obj = myClass.newInstance();
                    IPluginBroadcastReceiver rb = (IPluginBroadcastReceiver) obj;
//                    rb.setResources(PluginManager.getInstance().getResources());
                    rb.onReceive(MainActivity.mContext,intent);
                } catch (Exception e) {
                    Log.d(TAG,"",e);
                }
            }
        }
    }

    private void parseTargetReceivers()
    {
        Log.d(TAG,"parseTargetReceivers");
        PackageInfo packageInfo = PluginManager.getInstance().getReceiverPackageInfo();
        mReceivers = packageInfo.receivers;
    }
}
