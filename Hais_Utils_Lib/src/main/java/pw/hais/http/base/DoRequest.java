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

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;

import pw.hais.utils.GenericsUtils;
import pw.hais.utils.ImageViewUtils;
import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * 拿来处理 各种请求
 * Created by Hais1992 on 2015/8/26.
 */
public class DoRequest<T> {
    public OkHttpClient mOkHttpClient;
    public Handler mDelivery;
    private static DoRequest mInstance;

    private DoRequest() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static DoRequest getInstance() {
        if (mInstance == null) {
            synchronized (DoRequest.class) {
                if (mInstance == null) {
                    mInstance = new DoRequest();
                }
            }
        }
        return mInstance;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @param listener
     */
    public void doHttpRequest(Request request, final Listener<T> listener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        L.e(BaseHttp.TAG, "出错", e);
                        listener.error(request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    L.i(BaseHttp.TAG, "结果：" + string);
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
                                L.e(BaseHttp.TAG, "出错", e);
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
                            L.e(BaseHttp.TAG, "出错", e);
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
     * 处理 图片加载 数据
     *
     * @param imageView
     * @param response
     * @param url
     * @param listener
     */
    public void doImageResponse(final ImageView imageView, final Response response, final String url, final Listener<Bitmap> listener) {
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
            if (bm == null) {
                L.i(BaseHttp.TAG, "图片不存在：" + url);
                throw new Exception("图片不存在：" + url);
            }
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    L.i(BaseHttp.TAG, "图片显示：" + url);
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
                    imageView.setImageResource(BaseHttp.error_drawable_id);
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
