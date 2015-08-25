package pw.hais.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2015/7/2.
 */
public class ApkInfoUtil {
    private static final Context context = UtilConfig.CONTEXT;

    /**
     * 获取应用程序名称
     *
     * @return 应用程序名称
     */
    public static String getAppName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            L.e("AppInfoUtil", "获取应用程序APP名称出错", e);
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("AppInfoUtil", "获取应用程序版本名称信息出错", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序包名
     *
     * @return 应用程序包名
     */
    public static String getPackageName() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            L.e("AppInfoUtil", "获取应用程序包名出错", e);
        }
        return null;
    }

    /**
     * 获取当前软件的版本号
     *
     * @return 版本号
     */
    public static int getVersionCode() {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
