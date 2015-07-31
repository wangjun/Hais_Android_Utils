package pw.hais.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

/**
 * 网络
 * Created by hais1992 on 15-4-24.
 */
public class AppNetInfoUtil {
    private static final Context context = UtilConfig.CONTEXT;

    private AppNetInfoUtil() {
        throw new UnsupportedOperationException("不能实例化该类");
    }

    /**
     * 判断网络是否连接
     * @return
     */
    public static boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    public static void pingHost(final String str, final PingCallback callback){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    // TODO: Hardcoded for now, make it UI configurable
                    Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " +str);
                    int status = p.waitFor();
                    if (status == 0) {
                        if(callback!=null)callback.onSuccess();
                        return true;
                    }else{
                        if(callback!=null)callback.onError();
                    }
                } catch (Exception e){}
                return false;
            }
        }.execute();
    }

    //请求成功 回掉

    public static interface PingCallback{
        void onSuccess();
        void onError();
    }


}
