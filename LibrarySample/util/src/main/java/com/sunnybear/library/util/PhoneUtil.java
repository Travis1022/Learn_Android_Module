package com.sunnybear.library.util;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * 手机组件调用工具类
 * Created by guchenkai on 2016/1/21.
 */
public final class PhoneUtil {

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getMobileModel() {
        try {
            return android.os.Build.MODEL; // 手机型号
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public static String getMobileBrand() {
        try {
            return android.os.Build.BRAND;// android系统版本号
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 获取手机系统版本号
     *
     * @return 手机系统版本号
     */
    public static int getVersion() {
        try {
            return android.os.Build.VERSION.SDK_INT;// android系统版本号
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 判断手机设备是否为双卡
     */
    public static boolean isDoubleSIM(Context context) {
        try {
            String imeiSIM2 = getOperatorSIM(context, "getDeviceIdGemini", 1);
            return !StringUtils.isEmpty(imeiSIM2);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取手机IMEI号
     */
    public static String getIMEI(Context context) {
        String IMEI = null;
        try {
            if (isDoubleSIM(context))
                IMEI = getOperatorSIM(context, "getDeviceIdGemini", 0);
            else
                IMEI = ((TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
        }
        return IMEI;
    }

    /**
     * 读取SIM卡槽信息
     *
     * @param predictedMethodName 反射方法名
     * @param simId               SIM卡槽id
     */
    private static String getOperatorSIM(Context context, String predictedMethodName, int simId) throws GeminiMethodNotFoundException {
        String result = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELECOM_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Method getSIMId = telephonyClass.getMethod(predictedMethodName, new Class[]{int.class});
            Object phone = getSIMId.invoke(telephony, new Object[]{simId});
            if (phone != null) result = phone.toString();
        } catch (Exception e) {
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
        return result;
    }

    /**
     *
     */
    private static class GeminiMethodNotFoundException extends Exception {

        public GeminiMethodNotFoundException(String message) {
            super(message);
        }
    }
}
