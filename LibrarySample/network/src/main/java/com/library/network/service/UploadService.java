package com.travis.library.network.service;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.travis.library.network.service.bind.BasicBinder;
import com.travis.library.util.AppUtils;
import com.travis.library.util.Logger;

/**
 * 上传文件使用的Service
 * Created by chenkai.gu on 2016/10/20.
 */
public class UploadService extends RxService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("UploadService已绑定");
        return new BasicBinder<UploadService>() {
            @Override
            public UploadService getService() {
                return UploadService.this;
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("UploadService绑定解除");
        AppUtils.stopRunningService(this, UploadService.class);
        return super.onUnbind(intent);
    }

    /**
     * 上传文件,带有进度处理
     *
     * @param url          上传地址
     * @param uploadParams 上传参数
     * @param listener     上传进度回调
     * @param callback     上传文件回调
     */
    /*public void upload(String url, Map<String, Serializable> uploadParams, UIProgressRequestListener listener, RequestCallback callback) {
        mUploadCall = OkHttpRequestHelper.newInstance(Integer.MAX_VALUE).addUploadParams(uploadParams)
                .upload(url, listener, callback);
    }*/
}
