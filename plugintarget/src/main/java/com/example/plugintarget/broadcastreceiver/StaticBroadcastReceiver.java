package com.example.plugintarget.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.plugintarget.R;

public class StaticBroadcastReceiver extends BroadcastReceiver {
    private String TAG = "StaticBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        Toast.makeText(context, R.string.test_string,Toast.LENGTH_LONG);
    }
}
