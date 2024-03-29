package com.example.yudongzhou.plugintest.HookPlugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.pluginstandard.LogConfig;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HookUtil {
    private String TAG = LogConfig.TAG_PREFIX + getClass().getSimpleName();

    private Class<?> proxyActivity;

    private Context context;

    public HookUtil(Class<?> proxyActivity, Context context) {
        this.proxyActivity = proxyActivity;
        this.context = context;
    }

    public void hookAms() {

        //一路反射，直到拿到IActivityManager的对象
        try {
            Class<?> ActivityManagerNativeClss = Class.forName("android.app.ActivityManagerNative");

//            Method getDefault = ActivityManagerNativeClss.getDeclaredMethod("getDefault");
//            Object iActivityManagerObject = getDefault.invoke(null);

            Field defaultFiled = ActivityManagerNativeClss.getDeclaredField("gDefault");
            defaultFiled.setAccessible(true);
            Object defaultValue = defaultFiled.get(null);
            //反射SingleTon
//            Class<?> SingletonClass = Class.forName("android.util.Singleton");
//            defaultFiled.getClass()
            Field mInstance = defaultFiled.getType().getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            //到这里已经拿到ActivityManager对象
            Object iActivityManagerObject = mInstance.get(defaultValue);


            //开始动态代理，用代理对象替换掉真实的ActivityManager，瞒天过海
            Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");

            AmsInvocationHandler handler = new AmsInvocationHandler(iActivityManagerObject);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{IActivityManagerIntercept}, handler);

            //现在替换掉这个对象
            mInstance.set(defaultValue, proxy);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class AmsInvocationHandler implements InvocationHandler {

        private Object iActivityManagerObject;

        public AmsInvocationHandler(Object iActivityManagerObject) {
            this.iActivityManagerObject = iActivityManagerObject;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.i(TAG, method.getName());
            //我要在这里搞点事情
            if ("startActivity".contains(method.getName())) {
                Log.e(TAG,"Activity已经开始启动");
                Log.e(TAG,"小弟到此checkPermission一游！！！");
                replaceIntent(args);
            }
            return method.invoke(iActivityManagerObject, args);
        }
    }

    public void replaceIntent(Object[] args)
    {
        int intentIndex = 0;
        Intent oldIntent = null;
        if(args != null && args.length > 0)
        {
            for(int i = 0;i < args.length;i++)
            {
                if(args[i] instanceof Intent)
                {
                    intentIndex = i;
                    oldIntent = (Intent) args[i];
                    break;
                }
            }

            if(oldIntent != null && oldIntent.getComponent() != null)
            {
                ComponentName componentName = oldIntent.getComponent();
                if(TestHookTargetActivity.class.getName().equals(componentName.getClassName()))
                {
                    Intent newIntent = new Intent(context,TestHookTargetActivity.class);
                    args[intentIndex] = newIntent;
                }
            }
        }
    }
}
