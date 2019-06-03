package com.example.plugintarget.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.pluginstandard.LogConfig;
import com.example.pluginstandard.broadcastreceiver.AbsPluginBroadcastReceiver;
import com.example.plugintarget.R;

public class StaticBroadcastReceiver extends AbsPluginBroadcastReceiver {
    private String TAG = LogConfig.TAG_PREFIX+"StaticBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive,context = "+context+",intent.getAction() = "+intent.getAction());
//        Toast.makeText(context, mResource.getText(R.string.test_string),Toast.LENGTH_LONG).show();
        Toast.makeText(context, R.string.test_string,Toast.LENGTH_LONG).show();
    }
}
