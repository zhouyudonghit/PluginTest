package com.example.yudongzhou.plugintest;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dalvik.system.DexClassLoader;

public class PluginManager {
    private static volatile PluginManager singleInstance;
    private Resources resources;
    private PackageInfo mActivityPackageInfo;
    private PackageInfo mReceiverPackageInfo;
    private PackageInfo mServicePackageInfo;
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
            mServicePackageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_SERVICES);
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
            parseReceivers(context, path);
        }
    }

    private void parseReceivers(Context context, String path) {

//        Package对象
//        PackageParser pp = new PackageParser();

//        PackageParser.Package  pkg = pp.parsePackage(scanFile, parseFlags);
        try {
            Class   packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            Object packageParser = packageParserClass.newInstance();
            Object packageObj=  parsePackageMethod.invoke(packageParser, new File(path), PackageManager.GET_ACTIVITIES);

            Field receiverField=packageObj.getClass().getDeclaredField("receivers");
//拿到receivers  广播集合    app存在多个广播   集合  List<Activity>  name  ————》 ActivityInfo   className
            List receivers = (List) receiverField.get(packageObj);

            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");


            // 调用generateActivityInfo 方法, 把PackageParser.Activity 转换成
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");
//            generateActivityInfo方法
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object defaltUserState= packageUserStateClass.newInstance();
            Method generateReceiverInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);

            for (Object activity : receivers) {
                ActivityInfo info= (ActivityInfo) generateReceiverInfo.invoke(packageParser,  activity,0, defaltUserState, userId);
                BroadcastReceiver broadcastReceiver= (BroadcastReceiver) dexClassLoader.loadClass(info.name).newInstance();
                List<? extends IntentFilter> intents= (List<? extends IntentFilter>) intentsField.get(activity);
                for (IntentFilter intentFilter : intents) {
                    //动态注册广播
                    context.registerReceiver(broadcastReceiver, intentFilter);
                }
            }
            //generateActivityInfo
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void mergePluginResources(Application application, String apkName)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        // 创建一个新的 AssetManager 对象
        AssetManager newAssetManagerObj = AssetManager.class.newInstance();
        Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
        // 塞入原来宿主的资源
        addAssetPath.invoke(newAssetManagerObj, application.getBaseContext().getPackageResourcePath());
        // 塞入插件的资源
        File optDexFile = application.getBaseContext().getFileStreamPath(apkName);
        addAssetPath.invoke(newAssetManagerObj, optDexFile.getAbsolutePath());

        // ----------------------------------------------

        // 创建一个新的 Resources 对象
        Resources newResourcesObj = new Resources(newAssetManagerObj,
                application.getBaseContext().getResources().getDisplayMetrics(),
                application.getBaseContext().getResources().getConfiguration());

        // ----------------------------------------------

        // 获取 ContextImpl 中的 Resources 类型的 mResources 变量，并替换它的值为新的 Resources 对象
        Field resourcesField = application.getBaseContext().getClass().getDeclaredField("mResources");
        resourcesField.setAccessible(true);
        resourcesField.set(application.getBaseContext(), newResourcesObj);

        // ----------------------------------------------

        // 获取 ContextImpl 中的 LoadedApk 类型的 mPackageInfo 变量
        Field packageInfoField = application.getBaseContext().getClass().getDeclaredField("mPackageInfo");
        packageInfoField.setAccessible(true);
        Object packageInfoObj = packageInfoField.get(application.getBaseContext());

        // 获取 mPackageInfo 变量对象中类的 Resources 类型的 mResources 变量，，并替换它的值为新的 Resources 对象
        // 注意：这是最主要的需要替换的，如果不需要支持插件运行时更新，只留这一个就可以了
        Field resourcesField2 = packageInfoObj.getClass().getDeclaredField("mResources");
        resourcesField2.setAccessible(true);
        resourcesField2.set(packageInfoObj, newResourcesObj);

        // ----------------------------------------------

        // 获取 ContextImpl 中的 Resources.Theme 类型的 mTheme 变量，并至空它
        // 注意：清理mTheme对象，否则通过inflate方式加载资源会报错, 如果是activity动态加载插件，则需要把activity的mTheme对象也设置为null
        Field themeField = application.getBaseContext().getClass().getDeclaredField("mTheme");
        themeField.setAccessible(true);
        themeField.set(application.getBaseContext(), null);
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

    public PackageInfo getServicePackageInfo() {
        return mServicePackageInfo;
    }

    public void setServicePackageInfo(PackageInfo servicePackageInfo) {
        this.mServicePackageInfo = servicePackageInfo;
    }
}
