package com.example.plugintarget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.example.pluginstandard.activity.PluginActivity;
import com.example.plugintarget.R;

public class SecondActivity extends PluginActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_second_activity_main);
    }
}
