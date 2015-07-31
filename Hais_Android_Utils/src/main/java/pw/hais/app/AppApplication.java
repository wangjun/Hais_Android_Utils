package pw.hais.app;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;

import pw.hais.ui.LoadProgressDialog;
import pw.hais.utils.AudioHandleUtils;
import pw.hais.utils.CrashUtil;
import pw.hais.utils.UtilConfig;

/**
 * APP 全局
 * Created by hais1992 on 15-5-13.
 */
public class AppApplication extends Application {
    public static LinkedList<Activity> activities = new LinkedList<Activity>();
    protected static AppApplication app;

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        //日记捕抓
        CrashUtil.getInstance().init(getApplicationContext());
        //初始化音频播放工具
        AudioHandleUtils.getInstance().initConfig(getBaseContext());
        //初始化工具类
        UtilConfig.init(getBaseContext());
    }

    public void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }


    /**
     * 移除Activity引用
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
        try {
            if (activities != null && !activities.isEmpty()) {
                for (Activity a : activities) {
                    a.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
