package pw.hais.http.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import pw.hais.utils.UtilConfig;

/**
 * Created by Hais1992 on 2015/8/27.
 */
public class CacheManager {
    public static final int default_drawable_id = UtilConfig.DEFAULT_DRAWABLE_ID;   //默认图片
    public static final int error_drawable_id = UtilConfig.ERROR_DRAWABLE_ID;           //错误图片
    public static final int PNG_COMPRESS = UtilConfig.PNG_COMPRESS;// 存入SD卡时，PNG的压缩率
    public static final int JPG_COMPRESS = UtilConfig.JPG_COMPRESS;// 存入SD卡时，JPG的压缩率
    public static Context context = UtilConfig.CONTEXT;                 //上下文

    public static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.RGB_565;    //压缩编码
    public static final boolean SD_IS_WIRTE = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
    public static String IMAGE_CACHE_DIR = LocalCache.getFilePath();  //文件缓存目录
    private static SwapCache bitmapCache = new SwapCache();             //内存缓存

    /**
     * URL 获取缓存图片 Bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapCache(String url) {
        Bitmap bitmap = bitmapCache.getBitmap(url);     //从内存获取
        if (bitmap == null) { //从SD卡获取
            bitmap = LocalCache.getImageFromSDCard(url, 0, 0);
            if (bitmap != null) {   //如果内存卡有缓存，就存入 内存
                bitmapCache.putBitmap(url, bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 存入缓存
     * @param url
     * @param bitmap
     */
    public static void putBitmapCache(String url, Bitmap bitmap) {
        bitmapCache.putBitmap(url, bitmap);
    }



    /** 清空本地缓存 */
    public static void cleanLocalCache(){
        LocalCache.cleanLocalCache();
    }


}
