package pw.hais.http.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import pw.hais.http.Http;
import pw.hais.utils.GenericsUtils;
import pw.hais.utils.ImageViewUtils;
import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * 基于 OkHttp 的网络请求 基本方法
 * Created by Hais1992 on 2015/8/25.
 */
public class BaseHttp<T> {
    public static final String TAG = "Http请求";
    protected static final int default_drawable_id = UtilConfig.DEFAULT_DRAWABLE_ID;   //默认图片
    protected static final int error_drawable_id = UtilConfig.ERROR_DRAWABLE_ID;           //错误图片

    protected static Http mInstance;
    protected OkHttpClient mOkHttpClient;
    protected Handler mDelivery;

    public BaseHttp() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    protected static Http getInstance() {
        if (mInstance == null) {
            synchronized (Http.class) {
                if (mInstance == null) {
                    mInstance = new Http();
                }
            }
        }
        return mInstance;
    }


    /**
     * 添加一个请求
     *
     * @param method   请求类型
     * @param url      请求地址
     * @param params   请求参数
     * @param files    文件
     * @param fileKeys
     * @param listener 监听
     */
    protected void addRequest(Method method, String url, Map<String, String> params, File[] files, String[] fileKeys, Listener<T> listener) {
        Request request = HttpRequest.getRequest(method, url, params, files, fileKeys);  //根据请求 类型，获取 Request
        doRequest(request, listener);    //添加请求
    }

    protected void addRequest(Method method, String url, Map<String, String> params, Listener<T> listener) {
        addRequest(method, url, params, null, null, listener);
    }

    /**
     * 处理Http 请求回调
     *
     * @param request
     * @param listener
     */
    private void doRequest(Request request, final Listener<T> listener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        L.e(TAG, "出错", e);
                        listener.error(request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    L.i(TAG, "结果：" + string);
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Class<T> clazz = GenericsUtils.getSuperClassGenricType(listener.getClass());
                                if (clazz == String.class) {        //字符串
                                    if (listener != null) listener.success(response, (T) string);
                                } else if (clazz == JSONObject.class) {     //JSONObject
                                    if (listener != null)
                                        listener.success(response, (T) new JSONObject(string));
                                } else {    //Object
                                    if (listener != null)
                                        listener.success(response, UtilConfig.GSON.fromJson(string, clazz));
                                }
                                if (listener != null) listener.httpEnd(true);
                            } catch (Exception e) {
                                L.e(TAG, "出错", e);
                                if (listener != null) {
                                    listener.httpEnd(false);
                                    listener.error(response.request(), e);
                                }
                            }
                        }
                    });
                } catch (final Exception e) {
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            L.e(TAG, "出错", e);
                            if (listener != null) {
                                listener.httpEnd(false);
                                listener.error(response.request(), e);
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     * 加载图片
     *
     * @param imageView
     * @param url
     * @param listener
     */
    protected void loadImage(final ImageView imageView, final String url, final Listener<Bitmap> listener) {
        if (default_drawable_id != 0 && imageView != null)
            imageView.setImageResource(default_drawable_id);    //设置默认图
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        L.i(TAG, "图片出错：" + url);
                        imageView.setImageResource(error_drawable_id);
                        if (listener != null) {
                            listener.httpEnd(false);
                            listener.error(request, e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                doImageResponse(imageView, response, url, listener);
            }
        });

    }

    /**
     * 处理 图片加载 数据
     *
     * @param imageView
     * @param response
     * @param url
     * @param listener
     */
    private void doImageResponse(final ImageView imageView, final Response response, final String url, final Listener<Bitmap> listener) {
        InputStream inputStream = null;
        try {
            inputStream = response.body().byteStream();
            ImageViewUtils.ImageSize actualImageSize = ImageViewUtils.getImageSize(inputStream);
            ImageViewUtils.ImageSize imageViewSize = ImageViewUtils.getImageViewSize(imageView);
            int inSampleSize = ImageViewUtils.calculateInSampleSize(actualImageSize, imageViewSize);

            Request request = new Request.Builder().url(url).build();
            Call call = mOkHttpClient.newCall(request);
            inputStream = call.execute().body().byteStream();

            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inJustDecodeBounds = false;
            ops.inSampleSize = inSampleSize;
            final Bitmap bm = BitmapFactory.decodeStream(inputStream, null, ops);
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    L.i(TAG, "图片显示：" + url);
                    imageView.setImageBitmap(bm);
                    if (listener != null) {
                        listener.httpEnd(true);
                        listener.success(response, bm);
                    }
                }
            });
        } catch (final Exception e) {
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(error_drawable_id);
                    if (listener != null) {
                        listener.httpEnd(false);
                        listener.error(response.request(), e);
                    }
                }
            });
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }

    }

}
