package com.travis.library.util.dynamic;

/**
 * 代理运行
 * Created by chenkai.gu on 2017/2/10.
 */
public interface ProxyRunnable extends Runnable {
    /**
     * 代理方法运行之前回调
     */
    void beforeProxy();

    /**
     * 代理方法运行之后回调
     */
    void afterProxy();
}
