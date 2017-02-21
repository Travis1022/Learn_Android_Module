package com.travis.library.util.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.travis.library.util.Logger;
import com.travis.library.util.SDCardUtils;
import com.travis.library.util.StringUtils;
import com.travis.library.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 本地日志捕捉器
 * Created by chenkai.gu on 2017/2/17.
 */
public class LogNativeHandler implements CrashHandler.ExceptionHandler {
    private static final String TAG = "LogNativeHandler";
    private Context mContext;
    private static final String SDCARD_ROOT = SDCardUtils.getSDCardPath();
    private String mSavePath;

    public LogNativeHandler(Context context, String savePath) {
        mContext = context;
        mSavePath = savePath;
    }

    @Override
    public void handlerException(Thread thread, Throwable throwable) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                saveInfoToSD(mContext, throwable);
                ToastUtils.showToastLong(mContext, "程序出现异常,请查看异常日志");
            }
        });
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDCard中
     *
     * @param context
     * @param ex
     * @return
     */
    private String saveInfoToSD(Context context, Throwable ex) {
        String fileName = null;
        StringBuffer sb = new StringBuffer();

        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append("=====异常信息=====").append('\n');
        sb.append(obtainExceptionInfo(ex));
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(!StringUtils.isEmpty(mSavePath) ? mSavePath : SDCARD_ROOT + File.separator + "crash" + File.separator);
            if (!dir.exists()) dir.mkdir();
            try {
                fileName = dir.toString() + File.separator + parseTime(System.currentTimeMillis()) + ".log";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在HashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        HashMap<String, String> map = new HashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);

        map.put("MODEL", "" + Build.MODEL);
        map.put("SDK_INT", "" + Build.VERSION.SDK_INT);
        map.put("PRODUCT", "" + Build.PRODUCT);
        return map;
    }

    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        Logger.e(TAG, mStringWriter.toString());
        return mStringWriter.toString();
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式
     *
     * @param milliseconds
     * @return
     */
    private String parseTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String times = format.format(new Date(milliseconds));
        return times;
    }
}
