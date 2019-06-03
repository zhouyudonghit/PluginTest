package com.example.plugintarget.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.pluginstandard.LogConfig;
import com.example.pluginstandard.activity.AbsPluginActivity;
import com.example.plugintarget.R;

public class MainActivity extends AbsPluginActivity {
    private String TAG = LogConfig.TAG_PREFIX+getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        Log.d(TAG,"getWindow() = "+getWindow());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(that, "Target toast show...", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"that.getResources() = "+that.getResources());
                Toast.makeText(that, R.string.test_string, Toast.LENGTH_SHORT).show();
                Log.d(TAG,"first activity btn clicked");
                startActivity(new Intent(that, SecondActivity.class));
                //startService(new Intent(that, TestService.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
