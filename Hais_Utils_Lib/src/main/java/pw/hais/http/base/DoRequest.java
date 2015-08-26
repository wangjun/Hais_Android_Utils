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
import java.io.FileOutputStream;
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
public class DoRequest {
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
    public <T> T doHttpRequest(final Request request, final Listener<T> listener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                onHttpError(request, e, listener);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    T obj;
                    Class<T> clazz = GenericsUtils.getSuperClassGenricType(listener.getClass());
                    if (clazz == String.class) {        //字符串
                        obj = (T) response.body().string();
                    } else if (clazz == JSONObject.class) {     //JSONObject
                        obj = (T) new JSONObject(response.body().string());
                    } else {    //Object
                        obj = UtilConfig.GSON.fromJson(response.body().string(), clazz);
                    }
                    onHttpSuccess(response, listener, obj);
                } catch (Exception e) {
                    onHttpError(request, e, listener);
                }
            }
        });
        return null;
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
            } else {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        L.i(BaseHttp.TAG, "图片显示：" + url);
                        imageView.setImageBitmap(bm);
                    }
                });
                onHttpSuccess(response, listener, bm);
            }
        } catch (final Exception e) {
            onHttpError(response.request(), e, listener);
            mDelivery.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(BaseHttp.error_drawable_id);
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


    public void doDownloadResponse(final Request request, final String url, final String destFileDir, final Listener<String> listener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                onHttpError(request, e, listener);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();

                    File dir = new File(destFileDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    onHttpSuccess(response, listener, file.getAbsolutePath());
                } catch (IOException e) {
                    onHttpError(request, e, listener);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    /**
     * HTTP错误
     */
    public void onHttpError(final Request request, final Exception e, final Listener<?> listener) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                L.e(BaseHttp.TAG, "出错", e);
                if (listener != null) {
                    listener.httpEnd(false);
                    listener.error(request, e);
                }
            }
        });
    }

    /**
     * HTTP成功
     */
    public <T> T onHttpSuccess(final Response response, final Listener<T> listener, final T result) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                L.i(BaseHttp.TAG, "结果：" + UtilConfig.GSON.toJson(result));
                if (listener != null) {
                    listener.httpEnd(true);
                    listener.success(response, result);
                }
            }
        });
        return null;
    }


    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
}
