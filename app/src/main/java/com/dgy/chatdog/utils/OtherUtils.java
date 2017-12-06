package com.dgy.chatdog.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

/**
 * Created by DGY on 2016/11/22.
 */

public class OtherUtils {

    /**
     * 获得设备版本
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
             try {
                     PackageManager manager = context.getPackageManager();
                    PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                     return  info.versionName;

                } catch (Exception e) {
                     e.printStackTrace();
                    return "error version";
                }
         }

    /**
     * 获得设备id
     * @param context
     * @return
     */
    public static String getDeviceId(Context context){
       return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获得手机型号
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

}
