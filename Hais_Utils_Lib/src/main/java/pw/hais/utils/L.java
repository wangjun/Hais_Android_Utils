package pw.hais.utils;

import android.widget.Toast;

/**
 * 日记，吐丝类
 * Created by hais1992 on 15-4-24.
 */
public class L {
    private static final boolean debug = UtilConfig.LOG_PRINTF;    //是否开启调试
    private static final String tag = UtilConfig.LOG_DEFAULL_TAG;  //缺省的 tag
    private static final String prefix = UtilConfig.LOG_PREFIX;    //日记输出前缀

    private L()
    {
        throw new UnsupportedOperationException("不能实例化该类");
    }

    /*--------------------------------------Log输出-----------------------------------------*/
    public static void i(String msg) {
        if (debug) {
            try {
                android.util.Log.i(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void i(String tag, String msg) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.i(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void d(String tag, String msg) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.d(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void v(String tag, String msg) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.v(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }

    public static void w(String tag, String msg) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.w(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }


    public static void e(String tag, String msg) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.e(tag+prefix, msg);
            } catch (Exception e) {

            }
        }
    }


    public static void e(String tag, String msg, Throwable tr) {
        if (debug) {
            try {
                tag = tag == null ? L.tag : tag;
                android.util.Log.e(tag+prefix, msg, tr);
            } catch (Exception e) {

            }
        }
    }

    /*--------------------------------------Toast输出-----------------------------------------*/
    /**
     * 短时间显示 Toast
     * @param message
     */
    public static void showShort(CharSequence message){
       try {
           Toast.makeText(UtilConfig.CONTEXT, message, Toast.LENGTH_SHORT).show();
       }catch (Exception e){e.printStackTrace();}
    }

    /**
     * 短时间显示 Toast
     * @param message
     */
    public static void showShort(int message){
        Toast.makeText(UtilConfig.CONTEXT,message,Toast.LENGTH_SHORT).show();
    }


    /**
     * 长时间显示 Toast
     * @param message
     */
    public static void showLong(CharSequence message){
        Toast.makeText(UtilConfig.CONTEXT,message,Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示 Toast
     * @param message
     */
    public static void showLong(int message){
        Toast.makeText(UtilConfig.CONTEXT,message,Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义时间显示 Toast
     * @param message
     * @param time 时间
     */
    public static void show(CharSequence message,int time){
        Toast.makeText(UtilConfig.CONTEXT,message,time).show();
    }

    /**
     * 自定义时间显示 Toast
     * @param message
     * @param time 时间
     */
    public static void show(int message,int time){
            Toast.makeText(UtilConfig.CONTEXT,message,time).show();
    }
}
