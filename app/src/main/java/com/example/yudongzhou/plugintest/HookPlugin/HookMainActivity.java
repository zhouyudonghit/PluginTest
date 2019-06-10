package com.example.yudongzhou.plugintest.HookPlugin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.pluginstandard.LogConfig;

public class HookMainActivity extends AppCompatActivity {
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG,"oncreate");
        super.onCreate(savedInstanceState);
    }


}
