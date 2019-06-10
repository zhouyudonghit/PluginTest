package com.example.yudongzhou.plugintest;

import android.app.Application;
import android.content.Context;

import com.example.yudongzhou.plugintest.HookPlugin.HookMainActivity;
import com.example.yudongzhou.plugintest.HookPlugin.HookUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MyApplication extends Application {
    public static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
//        initPlugin();
        initHookPlugin();
    }

    public static Application getApplication()
    {
        return mApplication;
    }

    public void initPlugin()
    {
        PluginManager.getInstance().setContext(this);
        loadPlugin();
    }

    public void initHookPlugin()
    {
        HookUtil hookUtil = new HookUtil(HookMainActivity.class,this);
        hookUtil.hookAms();
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
}
