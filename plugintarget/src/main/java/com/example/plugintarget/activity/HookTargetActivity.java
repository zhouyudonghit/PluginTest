package com.example.plugintarget.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.pluginstandard.LogConfig;

public class HookTargetActivity extends AppCompatActivity {
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
