package com.example.yudongzhou.plugintest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static volatile PluginManager singleInstance;
    private Resources resources;
    private PackageInfo mActivityPackageInfo;
    private PackageInfo mReceiverPackageInfo;
    private DexClassLoader dexClassLoader;
    private Context mContext;
    private AtomicBoolean ifLoaded = new AtomicBoolean(false);

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

    public synchronized void loadPath(Context context) {
        if(!ifLoaded.get()) {
            ifLoaded.set(true);
            File filesDir = context.getDir("plugin", Context.MODE_PRIVATE);
            String name = "plugin.apk";
            String path = new File(filesDir, name).getAbsolutePath();

            //获取被代理apk的PackageInfo
            PackageManager packageManager = context.getPackageManager();
            mActivityPackageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
            mReceiverPackageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_RECEIVERS);

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
        }
//        parseReceivers(context, path);
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public PackageInfo getActivityPackageInfo() {
        return mActivityPackageInfo;
    }

    public void setActivityPackageInfo(PackageInfo packageInfo) {
        this.mActivityPackageInfo = packageInfo;
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

    public PackageInfo getReceiverPackageInfo() {
        return mReceiverPackageInfo;
    }

    public void setReceiverPackageInfo(PackageInfo receiverPackageInfo) {
        this.mReceiverPackageInfo = receiverPackageInfo;
    }
}
