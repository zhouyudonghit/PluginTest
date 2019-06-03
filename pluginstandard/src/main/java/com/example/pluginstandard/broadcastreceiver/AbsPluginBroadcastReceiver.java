package com.example.pluginstandard.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.res.Resources;

public abstract class AbsPluginBroadcastReceiver extends BroadcastReceiver implements IPluginBroadcastReceiver {
    protected Resources mResource;

    @Override
    public void setResources(Resources mResource) {
        this.mResource = mResource;
    }
}
