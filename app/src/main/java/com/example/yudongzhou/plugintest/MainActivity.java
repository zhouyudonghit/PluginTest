package com.example.yudongzhou.plugintest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pluginstandard.LogConfig;
import com.example.yudongzhou.plugintest.ChaZhuangPlugin.ProxyActivity;
import com.example.yudongzhou.plugintest.ChaZhuangPlugin.ProxyService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = LogConfig.TAG_PREFIX+getClass().getSimpleName();
    private TextView jumpToPlugin,jumpToPluginService,stopPluginService;
    private TextView bindPluginService,unbindPluginService;
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
//        PluginManager.getInstance().setContext(this);
//        loadPlugin();
        jumpToPlugin = findViewById(R.id.jump_to_plugin);
        jumpToPlugin.setOnClickListener(this);

        jumpToPluginService = findViewById(R.id.jump_to_plugin_service);
        jumpToPluginService.setOnClickListener(this);

        stopPluginService = findViewById(R.id.stop_plugin_service);
        stopPluginService.setOnClickListener(this);

        bindPluginService = findViewById(R.id.bind_plugin_service);
        bindPluginService.setOnClickListener(this);

        unbindPluginService = findViewById(R.id.unbind_plugin_service);
        unbindPluginService.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.jump_to_plugin:
                jumpToPluginActivity();
                break;
            case R.id.jump_to_plugin_service:
                jumpToPluginService();
                break;
            case R.id.stop_plugin_service:
                stopPluginService();
                break;
            case R.id.bind_plugin_service:
                bindPluginService();
                break;
            case R.id.unbind_plugin_service:
                unbindPluginService();
                break;
            default:
        }
    }

    public void jumpToPluginActivity()
    {
        Intent intent = new Intent(this, ProxyActivity.class);
        intent.putExtra(Constants.CLASS_NAME, PluginManager.getInstance().getActivityPackageInfo().activities[0].name);
        startActivity(intent);
    }

    public void jumpToPluginService()
    {
        Log.d(TAG,"jumpToPluginService()");
        Intent intent = new Intent(this, ProxyService.class);
        intent.putExtra(Constants.CLASS_NAME, PluginManager.getInstance().getServicePackageInfo().services[0].name);
        startService(intent);
    }

    public void stopPluginService()
    {
        Log.d(TAG,"stopPluginService()");
        Intent intent = new Intent(this,ProxyService.class);
        stopService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected,"+service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected");
        }
    };

    public void bindPluginService()
    {
        Log.d(TAG,"bindPluginService()");
        Intent intent = new Intent(this,ProxyService.class);
        intent.putExtra(Constants.CLASS_NAME,PluginManager.getInstance().getServicePackageInfo().services[0].name);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    public void unbindPluginService()
    {
        Log.d(TAG,"unbindPluginService()");
        unbindService(serviceConnection);
    }
}
