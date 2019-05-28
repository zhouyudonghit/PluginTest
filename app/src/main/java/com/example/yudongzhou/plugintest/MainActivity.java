package com.example.yudongzhou.plugintest;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = getClass().getSimpleName();
    private TextView jumpToPlugin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PluginManager.getInstance().setContext(this);
        loadPlugin();
        jumpToPlugin = findViewById(R.id.jump_to_plugin);
        jumpToPlugin.setOnClickListener(this);
    }

    private void loadPlugin()
    {
        File filesDir = getDir("plugin",Context.MODE_PRIVATE);
        String name = "plugin.apk";
        File file = new File(filesDir,name);
        String filePath = file.getAbsolutePath();
//        if(file.exists())
//        {
//            file.delete();
//        }
        InputStream is = null;
        FileOutputStream os = null;
        try{
//            Log.i(TAG,"加载插件 "+new File(Environment.getExternalStorageDirectory(),name).getAbsolutePath());
//            String externalPath = "/storage/emulated/0";
//            is = new FileInputStream(new File(externalPath,name));
//            os = new FileOutputStream(filePath);
//            int len = 0;
//            byte[] buffer = new byte[1024];
//            while((len = is.read(buffer)) != -1)
//            {
//                os.write(buffer,0,len);
//            }
//            File f = new File(filePath);
//            if(f.exists())
//            {
//                Toast.makeText(this,"dex overwrite",Toast.LENGTH_LONG).show();
//            }
            PluginManager.getInstance().loadPath(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(os != null)
                {
                    os.close();
                    os = null;
                }
                if(is != null)
                {
                    is.close();
                    is = null;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void click(View view) {
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("className", PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra("className", PluginManager.getInstance().getPackageInfo().activities[0].name);
        startActivity(intent);
    }
}
