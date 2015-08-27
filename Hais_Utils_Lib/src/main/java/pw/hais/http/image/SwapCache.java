package pw.hais.http.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 图片 内存 缓存
 * Created by Hais1992 on 2015/8/27.
 */
public class SwapCache {
    private static final int MAX_CACHE = calCache();          //设置缓存数量
    private LruCache<String, Bitmap> lruCache;

    public SwapCache() {
        lruCache = new LruCache<String, Bitmap>(MAX_CACHE) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    /**
     * 计算 缓存数量
     */
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

    /**
     * 取出缓存
     */
    public Bitmap getBitmap(String url) {
        return lruCache.get(url);
    }

    /**
     * 添加缓存
     */
    public void putBitmap(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
        LocalCache.saveBmpToSd(bitmap, url);    //缓存到本地
    }


}
