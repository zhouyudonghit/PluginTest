package com.example.pluginstandard.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class AbsPluginService extends Service implements IPluginService{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
