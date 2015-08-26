package pw.hais.utils;

import android.content.Context;

import com.google.gson.Gson;


/**
 * 工具配置类
 * Created by Administrator on 2015/3/26.
 */
public class UtilConfig {
    //全局
    public static boolean DEBUG = true;                 //是否开启调试
    public static final String CHARSET = "UTF-8";          //编码
    public static Context CONTEXT = null;                 //上下文
    public static String APP_ID = "pw.hais.util";      //包名
    public static final Gson GSON = new Gson();           //全局Gson

    //日记
    public static boolean LOG_PRINTF = UtilConfig.DEBUG;  //是否开启日记输出
    public static String LOG_DEFAULL_TAG = "Log";    //日记缺省标签
    public static String LOG_PREFIX = "-Hais";            //日记输出前缀

    //数据库
    public static String DB_NAME = UtilConfig.APP_ID+".db";      //数据库名
    public static int DB_VERSION = 1;                  //数据库版本

    //HTTP、图片
    public static final String HTTP_ERROR = "网络出现问题,请联系管理员!";           //请求网络出现问题提示
    public static final String HTTP_DATA_ERROR = "网络数据出错,请联系管理员!";      //请求网络数据错误提示

    public static final int DEFAULT_DRAWABLE_ID = R.drawable.image_default;   //默认图片
    public static final int ERROR_DRAWABLE_ID = R.drawable.image_error;              //错误图片
    public static final int PNG_COMPRESS = 100;      //图片缓存时的 PNG 压缩率,720P的手机默认压缩率为 PNG_COMPRESS -10
    public static final int JPG_COMPRESS = 100;      //图片缓存时的 JPG 压缩率,,720P的手机默认压缩率为 JPG_COMPRESS -10
    public static final String IMAGE_CACHE_DIR = "";    //缓存路径 如果为 "" 则缓存到 数据文件夹,建议为空


    /**
     * 初始化 Hais 工具类
     * @param context 上下文
     */
    public static void init(Context context){
        UtilConfig.CONTEXT = context;
        L.i("Hais-Util","Hais-Util初始化成功！");
    }

}
