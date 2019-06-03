package com.example.pluginstandard.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import com.example.pluginstandard.base.IBase;

public interface IPluginBroadcastReceiver extends IBase {
    void onReceive(Context context, Intent intent);
}
