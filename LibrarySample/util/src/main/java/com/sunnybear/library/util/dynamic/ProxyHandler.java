package com.sunnybear.library.util.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 代理处理者
 * Created by chenkai.gu on 2017/2/10.
 */
class ProxyHandler implements InvocationHandler {
    private ProxyRunnable mProxyRunnable;

    public ProxyHandler(ProxyRunnable proxyRunnable) {
        mProxyRunnable = proxyRunnable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        mProxyRunnable.beforeProxy();
        Object result = method.invoke(mProxyRunnable, args);
        mProxyRunnable.afterProxy();
        return result;
    }
}
