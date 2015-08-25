package pw.hais.utils.http.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;

import pw.hais.utils.L;
import pw.hais.utils.R;
import pw.hais.utils.ScreenUtil;
import pw.hais.utils.UtilConfig;
import pw.hais.utils.http.Http;

/**
 * 基于Volley的下载图片类 集成本地缓存
 * 图片显示/压缩方式:
 * 1、720P以上手机根据传入的maxWidth,maxHeight进行显示
 * 2、720p以下手机：(1)根据传入的ImageView宽高计算;(2)若ImageView使用的是match_parent等属性，则获取不到宽高，采用屏幕宽/2为图片最大尺寸，本类140行为计算代码
 * 3、图片在{@link LocalImageManager}存储时会有压缩；720p以上手机存原图，以下手机则质量压缩到60%存储
 *
 * @author LiuZhi
 */
public class ImageLoaders {
    private static final String tag = "图片加载";
    protected static final int default_drawable_id = UtilConfig.DEFAULT_DRAWABLE_ID;   //默认图片
    protected static final int error_drawable_id = UtilConfig.ERROR_DRAWABLE_ID;           //错误图片
    private static final Context context = UtilConfig.CONTEXT;
    private static final RequestQueue requestQueue = Http.requestQueue;       //网络请求

    public static final Config BITMAP_CONFIG = Config.RGB_565;
    public static final boolean SD_IS_WIRTE = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);  //判断sd卡是否存在
    protected static final String IMAGE_CACHE_DIR = getFilePath();	        //缓存目录
    private static final BitmapCache bitmapCache = new BitmapCache();
    private static final ImageLoader imageLoader = new ImageLoader(requestQueue, bitmapCache);
    public static final int CONTAINER_TAG = R.id.image_container;//用于给ImageView设置一个container时使用的tag

    /**
     * 初始化文件缓存路径
     */
    private static String getFilePath() {
        String path="";
        try {
            //读取 配置文件，获取路径文件夹
            if(!"".equals(UtilConfig.IMAGE_CACHE_DIR) && !"/".equals(UtilConfig.IMAGE_CACHE_DIR)){
                if  (SD_IS_WIRTE) path = Environment.getExternalStorageDirectory().toString();//获取跟目录
            }
            //如果路径获取不到，配置为空，则存到 Android/date/包名 文件夹
            if("".equals(path))path = context.getExternalCacheDir().getAbsolutePath() + "/";
        }catch (Exception e){   //如果木有 读写权限，则把东西写入 缓存
            path = context.getCacheDir().getAbsolutePath() + "/";  //获取缓存目录
        }

        path =path + UtilConfig.IMAGE_CACHE_DIR;
        L.d(tag, "SD卡：" + path);
        return path;

    }

    /**
     * 根据tag 取消请求
     * @param tag
     */
    public static void cancelAll(Object tag) {
        if (tag != null) {
            requestQueue.cancelAll(tag);
        }
    }

    /** 加载图片 */
    public static ImageContainer loadImage(String url, ImageView imageView) {
        return loadImage(url, imageView, default_drawable_id, error_drawable_id, 0, 0, url,null);
    }

    public static ImageContainer loadImage(String url, ImageView imageView,ImageListener imageListener) {
        return loadImage(url, imageView, default_drawable_id, error_drawable_id, 0, 0, url,imageListener);
    }

    public static ImageContainer loadImage(String url, ImageView imageView, Object tag, ImageListener imageListener) {
        return loadImage(url, imageView, default_drawable_id, error_drawable_id, 0, 0, tag, imageListener);
    }

    public static ImageContainer loadImage(String url, ImageView imageView, int defaultImageResId, int errorImageResId, Object tag) {
        return loadImage(url, imageView, defaultImageResId, errorImageResId, 0, 0, tag,null);
    }

    public static ImageContainer loadImage(String url, ImageView imageView, int defaultImageResId, int errorImageResId, int maxWidth, int maxHeight, Object tag) {
        return loadImage(url, imageView, defaultImageResId <= 0 ? default_drawable_id : defaultImageResId, errorImageResId <= 0 ? error_drawable_id : errorImageResId, maxWidth, maxHeight, tag, null);
    }

    /**
     * @param url               图片路径，不可null
     * @param imageView         android.widget.ImageView,不可null
     * @param defaultImageResId 加载过程中显示的图片，可为0
     * @param errorImageResId   加载失败显示的图片，可为0
     * @param maxWidth          最大宽度，可为0
     * @param maxHeight         最大高度，可为0
     * @param imageListener     下载完成时的回调，可以获取Bitmap，可以是null
     * @return ImageContainer 图片容器，可以获取到图片的bitmap,null则没有图片
     */
    public static ImageContainer loadImage(String url, ImageView imageView, int defaultImageResId, int errorImageResId, int maxWidth, int maxHeight, Object tag, ImageListener imageListener) {
        if(imageView ==null)return null;

        if (url == null) {
            imageView.setScaleType(ScaleType.FIT_CENTER);
            imageView.setImageResource(errorImageResId);
            return null;
        }

        /* 计算图片显示的宽高 */
        if (maxWidth <= 0 || maxHeight <= 0) {
            /* 720p以下手机需要计算显示，以上的则不作特殊处理 */
            if (ScreenUtil.getScreenWidth() < 720) {
                if (imageView.getWidth() > 1) {
                    maxWidth = imageView.getWidth();
                    maxHeight = 0;
                } else {
                    maxWidth = ScreenUtil.getScreenHeight() / 2;
                    maxHeight = 0;
                }
            } else {
                maxWidth = 0;
                maxHeight = 0;
            }
        }

        /* 设置默认图 */
        if (defaultImageResId <= 0)defaultImageResId = default_drawable_id;
        if (errorImageResId <= 0)errorImageResId = error_drawable_id;
        imageView.setImageResource(defaultImageResId);

        /* 取消原ImageView的下载 */
        cancleLoading(imageView);
        /* 检查本地及SD卡缓存 */
        Bitmap bitmap = getBitmapCache(url);
        /* 下载/显示图片 */
        ImageContainer container = showImageAndCallBack(bitmap, url, imageView, defaultImageResId, errorImageResId, maxWidth, maxHeight, tag, imageListener);

        return container;
    }


    /**
     * 下载图片，用ImageListener回调
     *
     * @param url
     * @param maxWidth
     * @param maxHeight
     * @param tag
     * @param imageListener
     * @return ImageContainer
     */
    public static ImageContainer loadImageOnly(final String url, int maxWidth, int maxHeight, Object tag, final ImageListener imageListener) {
        ImageContainer container;
        Bitmap bitmap = getBitmapCache(url);    //获取缓存

        if (bitmap == null) {
            loadImageByImageRequest(url, maxWidth, maxHeight, imageListener);   //下载图片
        } else {
            container = createImageContainer(bitmap, url, null);
            if (imageListener != null) {
                imageListener.onResponse(container, false);
            }
        }
        return null;
    }

    public static ImageContainer loadImageOnly(String url,ImageListener imageListener){
        return loadImageOnly(url,0,0,url,imageListener);
    }


    /**
     * 加载本地图片,并显示到ImageView上
     *
     * @param imageView
     * @param path
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static ImageContainer loadLocalImage(ImageView imageView, String path, int maxWidth, int maxHeight) {
        Bitmap bitmap = LocalImageManager.getLocalImageBitmap(path, maxWidth, maxHeight);
        if (bitmap != null) {
            if (imageView != null) {

                ImageDisPlayer.displayBitmap(path, bitmap, imageView);
            }
        }
        return createImageContainer(bitmap, path, null);
    }

    /**
     * URL 获取缓存图片 Bitmap
     * @param url
     * @return
     */
    public static Bitmap getBitmapCache(String url) {
        Bitmap bitmap = bitmapCache.getBitmap(url); //从内存获取
        if (bitmap == null) { //从SD卡获取
            bitmap = LocalImageManager.getImageFromSDCache(url);
            if (bitmap != null) {   //如果内存卡有缓存，就存入 内存缓存
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

    /** 生成默认回调*/
    private static ImageListener getImageListener(final String url, final ImageView view, final int defaultImageResId, final int errorImageResId, final ScaleType scaleType) {
        ImageListener imageListener = new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) { //调用 图片淡出
                    ImageDisPlayer.displayBitmap(url, response.getBitmap(), view);
                } else if (defaultImageResId != 0) {    //下载失败
                    view.setImageResource(defaultImageResId);
                }
            }
        };
        return imageListener;
    }

    /**
     * 停止ImageView正在下载的图片
     * @param imageView
     */
    private static void cancleLoading(ImageView imageView) {
        try {
            ImageContainer container = (ImageContainer) imageView.getTag(CONTAINER_TAG);
            if (container != null)container.cancelRequest();
        } catch (Exception e) {
            L.e("ImageLoaders","imageview 停止下载异常");
        }
    }

    /** 通过bitmap,url生成ImageContainer实例 */
    private static ImageContainer createImageContainer(Bitmap bitmap, String url, ImageListener listener) {
        if (bitmap != null && null != url) {
            return imageLoader.new ImageContainer(bitmap, url, url, listener);
        } else {
            return null;
        }
    }

    /**下载图片并显示*/
    private static ImageContainer showImageAndCallBack(Bitmap bitmap, String url, ImageView imageView, int defaultImageResId, int errorImageResId, int maxWidth, int maxHeight, Object tag, ImageListener imageListener) {
        ImageContainer container = null;
        if (bitmap == null) {
			/* 本地bitmap为null，则下载图片 */
            if (imageListener == null) {    //如果未设定监听器，则 创建一个。
                imageListener = getImageListener(url, imageView, defaultImageResId, errorImageResId, imageView.getScaleType());
            }
            container = imageLoader.get(url, imageListener, maxWidth, maxHeight);
            imageView.setTag(CONTAINER_TAG,container);
        } else {
			/* 否则显示图片，并将bitmap加入缓存 */
            container = createImageContainer(bitmap, url, null);
            ImageDisPlayer.displayBitmap(url, bitmap, imageView);   //淡出显示
			/* 执行回调 */
            if (imageListener != null)imageListener.onResponse(container, false);
        }
        return container;
    }

    /** 下载图片 */
    private static void loadImageByImageRequest(final String url, int maxWidth, int maxHeight, final ImageListener imageListener) {
        ImageRequest imageRequest = new ImageRequest(url, new Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (response != null) {
                    LocalImageManager.saveBmpToSd(response, url, context);  //保存图片到内存卡
                    if (imageListener != null)
                        imageListener.onResponse(createImageContainer(response, url, null), false); //如果需要回调，则生成一个Container后回调
                } else {
                    L.d(tag, "图片下载出错:" + url);
                }
            }

        }, maxWidth, maxHeight, BITMAP_CONFIG, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (imageListener != null)
                    imageListener.onErrorResponse(error);
            }
        });
        imageRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(imageRequest);
    }
}
