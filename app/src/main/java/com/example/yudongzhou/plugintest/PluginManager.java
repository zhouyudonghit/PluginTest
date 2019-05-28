package com.example.yudongzhou.plugintest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static volatile PluginManager singleInstance;
    private Resources resources;
    private PackageInfo packageInfo;
    private DexClassLoader dexClassLoader;
    private Context mContext;

    public static PluginManager getInstance()
    {
        if(singleInstance == null)
        {
            synchronized (PluginManager.class)
            {
                if(singleInstance == null) {
                    singleInstance = new PluginManager();
                }
            }
        }
        return singleInstance;
    }

    public void loadPath(Context context) {

        File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
        String name = "plugin.apk";
        String path = new File(filesDir, name).getAbsolutePath();

        //获取被代理apk的PackageInfo
        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        //获取被代理apk的ClassLoader
        File dexOutFile = context.getDir("dex", Context.MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(path, dexOutFile.getAbsolutePath()
                , null, context.getClassLoader());

        //获取被代理apk的Resource
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        parseReceivers(context, path);
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    public void setDexClassLoader(DexClassLoader dexClassLoader) {
        this.dexClassLoader = dexClassLoader;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
