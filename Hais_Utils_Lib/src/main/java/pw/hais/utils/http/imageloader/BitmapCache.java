package pw.hais.utils.http.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import pw.hais.utils.UtilConfig;

/**
 * volley中Imageloader使用的ImageCache的实现
 * @author LiuZhi
 */
public class BitmapCache implements ImageCache {
    private static final int MAX_CACHE = calCache();          //设置缓存数量
    private LruCache<String, Bitmap> lruCache;
    private static Context context = UtilConfig.CONTEXT;

    public BitmapCache() {
        lruCache = new LruCache<String, Bitmap>(MAX_CACHE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    /**计算 缓存数量*/
    private static int calCache() {
        int size = (int) Runtime.getRuntime().maxMemory();
        int x = size / 1024 / 1024;
        switch (x) {
            case 32:
                return size / 8;
            case 64:
                return size / 10;
            case 96:
                return size / 12;
            case 128:
                return size / 14;
            default:
                return size / 8;
        }
    }

    @Override   /**取出缓存*/
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }

    @Override   /**添加缓存*/
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
        LocalImageManager.saveBmpToSd(bitmap, url, context);    //缓存到本地
    }




}
