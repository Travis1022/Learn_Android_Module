package com.sunnybear.library.util.dynamic;

import com.sunnybear.library.util.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 点击事件防抖
 * Created by chenkai.gu on 2017/2/16.
 */
public class AntiShakeClick {
    private long lastClickTime = 0;

    public void click(Runnable runnable) {
        InvocationHandler invocationHandler = new AntiShakeClickHandler(runnable);
        Runnable r = (Runnable) Proxy.newProxyInstance(runnable.getClass().getClassLoader(),
                runnable.getClass().getInterfaces(), invocationHandler);
        r.run();
    }

    /**
     * 防抖Handler
     */
    class AntiShakeClickHandler implements InvocationHandler {
        private Runnable mRunnable;

        public AntiShakeClickHandler(Runnable runnable) {
            mRunnable = runnable;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime >= 2000) {
                lastClickTime = currentTime;
                result = method.invoke(mRunnable, args);
            } else {
                Logger.e("重复点击");
            }
            return result;
        }
    }
}
