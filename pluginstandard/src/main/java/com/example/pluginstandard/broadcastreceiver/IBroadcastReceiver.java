package com.example.pluginstandard.broadcastreceiver;

import android.content.Context;
import android.content.Intent;

public interface IBroadcastReceiver {
    void onReceive(Context context, Intent intent);
}
