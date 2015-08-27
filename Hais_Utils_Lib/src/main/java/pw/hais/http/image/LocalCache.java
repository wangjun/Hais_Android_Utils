package pw.hais.http.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pw.hais.http.base.BaseHttp;
import pw.hais.utils.L;
import pw.hais.utils.ScreenUtil;
import pw.hais.utils.UtilConfig;

/**
 * 图片 本地[SD卡 缓存]
 * Created by Hais1992 on 2015/8/27.
 */
public class LocalCache {

    /**
     * 获取 文件 缓存路径
     */
    public static String getFilePath() {
        String path = "";
        try {
            //读取 配置文件，获取路径文件夹
            if (!"".equals(CacheManager.IMAGE_CACHE_DIR) && !"/".equals(CacheManager.IMAGE_CACHE_DIR) && CacheManager.SD_IS_WIRTE) {
                path = Environment.getExternalStorageDirectory().toString()+ UtilConfig.IMAGE_CACHE_DIR;//获取跟目录
            }
            //如果路径获取不到，配置为空，则存到 Android/date/包名 文件夹
            if ("".equals(path) || "".equals(UtilConfig.IMAGE_CACHE_DIR))
                path = CacheManager.context.getExternalCacheDir().getAbsolutePath() + "/";
        } catch (Exception e) {   //如果木有 读写权限，则把东西写入 缓存
            path = CacheManager.context.getCacheDir().getAbsolutePath() + "/"+ UtilConfig.IMAGE_CACHE_DIR;  //获取缓存目录
        }

        L.d(BaseHttp.TAG, "SD卡：" + path);
        return path;
    }


    /**
     * 根据URL 从SD卡读取 缓存 图片
     *
     * @param url       地址
     * @param maxWidth  宽度，0为自适应
     * @param maxHeight 高  0
     * @return
     */
    public static Bitmap getImageFromSDCard(String url, int maxWidth, int maxHeight) {
        if (!CacheManager.SD_IS_WIRTE) return null;
        String filename = getFileNameByUrl(url);    //根据url生成文件名
        String path = CacheManager.IMAGE_CACHE_DIR + "/" + filename;  // 获取应用图片缓存路径
        try {
            if (new File(path).exists()) {
                return decodeBitmapFile(path, maxWidth, maxHeight);
            }
        } catch (Exception e) {
            L.i(BaseHttp.TAG, "读取" + path + "图片出错！");
        }
        return null;
    }


    /**
     * 根据url生成文件名
     */
    public static String getFileNameByUrl(String url) {
        int index = url.lastIndexOf("/");
        if (index != -1) {
            String name = url.substring(index);
            if (name.lastIndexOf(".jpg") == -1 && name.lastIndexOf(".png") == -1 && name.lastIndexOf(".jpeg") == -1) {
                name = name.replace("\\", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("\"", "").replace("<", "")
                        .replace(">", "").replace("|", "")
                        + ".jpg";
            }
            return name;
        } else return url;
    }

    /**
     * 读取文件 解码 成 Bitmap
     */
    public static Bitmap decodeBitmapFile(String path, int maxWidth, int maxHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opt);
        if (maxWidth > 0 && maxWidth > 0) {
            opt.inSampleSize = computeSampleSize(opt, -1, maxWidth * maxHeight);
        } else {
            opt.inSampleSize = computeSampleSize(opt, -1, (ScreenUtil.getScreenWidth() * 2 / 3) * (ScreenUtil.getScreenHeight() * 2 / 3));
        }

        opt.inPreferredConfig = CacheManager.BITMAP_CONFIG;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inTempStorage = new byte[8 * 1024];
        opt.inJustDecodeBounds = false;
        L.i(BaseHttp.TAG, "读取图片：" + path);
        return BitmapFactory.decodeFile(path, opt);
    }

    /**
     * 计算压缩率
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 将bitmap存储到SD卡
     */
    public static void saveBmpToSd(Bitmap bitmap, String url) {
        if (bitmap == null || !CacheManager.SD_IS_WIRTE) return;

        String filename = getFileNameByUrl(url);// 获取文件名
        String dir = CacheManager.IMAGE_CACHE_DIR;   // 获取存放目录
        File dirFile = new File(dir);
        File file = new File(dir, filename);
        try {
            if (!dirFile.exists()) dirFile.mkdirs();    //目录不存在，则创建
            if (file.exists()) return;    //文件存在则结束

            file.createNewFile();    //创建文件
            //保存 图片 到 文件
            BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file));

            /* 720P以下手机内存小，固压缩比例增大防止OOM */
            if (url.indexOf(".png") >= 0) {
                if (ScreenUtil.getScreenWidth() < 720) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, CacheManager.PNG_COMPRESS - 10, outStream);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                }
            } else {
                if (ScreenUtil.getScreenWidth() < 720) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, CacheManager.JPG_COMPRESS - 10, outStream);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                }
            }

            outStream.flush();
            outStream.close();
            L.i(BaseHttp.TAG, "已存SD:" + filename);
        } catch (FileNotFoundException e) {
            L.i(BaseHttp.TAG, "图片文件写入SD出错" + filename);
        } catch (IOException e) {
            L.i(BaseHttp.TAG, "图片写入处理除错" + filename);
            e.printStackTrace();
        }
    }

    /**
     * 清空本地缓存
     */
    public static boolean cleanLocalCache() {
        File dir = new File(CacheManager.IMAGE_CACHE_DIR);
        boolean b = deleteDir(dir);
        File file = new File(CacheManager.IMAGE_CACHE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return b;
    }

    /**
     * 递归删除文件
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            // 递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


}
