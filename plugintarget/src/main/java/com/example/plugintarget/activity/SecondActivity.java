package com.example.plugintarget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.pluginstandard.activity.AbsPluginActivity;
import com.example.plugintarget.R;

public class SecondActivity extends AbsPluginActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_second_activity_main);
    }
}
