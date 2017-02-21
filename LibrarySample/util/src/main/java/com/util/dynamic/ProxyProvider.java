package com.travis.library.util.dynamic;

import com.travis.library.util.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * 动态代理提供器
 * Created by chenkai.gu on 2017/2/10.
 */
public final class ProxyProvider {

    /**
     * 代理
     *
     * @param runnable 代理运行
     */
    public static void proxy(ProxyRunnable runnable) {
        InvocationHandler handler = new ProxyHandler(runnable);
        ProxyRunnable pr = (ProxyRunnable) Proxy.newProxyInstance(runnable.getClass().getClassLoader(),
                runnable.getClass().getInterfaces(), handler);
        pr.run();
    }

    /**
     * 方法运行计时
     *
     * @param runnable 运行方法
     */
    public static void timekeeper(Runnable runnable) {
        proxy(new ProxyRunnable() {
            private long startTime = 0;

            @Override
            public void beforeProxy() {
                Logger.d("ProxyProvider.timekeeper", "方法运行计时开始");
                startTime = System.currentTimeMillis();
            }

            @Override
            public void afterProxy() {
                Logger.d("ProxyProvider.timekeeper", "方法运行结束,用时:" + (System.currentTimeMillis() - startTime) + "ms");
            }

            @Override
            public void run() {
                if (runnable != null) runnable.run();
            }
        });
    }
}
