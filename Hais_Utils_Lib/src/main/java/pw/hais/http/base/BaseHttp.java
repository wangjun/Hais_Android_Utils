package pw.hais.http.base;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * 基于 OkHttp 的网络请求 基本方法
 * Created by Hais1992 on 2015/8/25.
 */
public class BaseHttp {
    public static final String TAG = "Http请求";
    public static final int default_drawable_id = UtilConfig.DEFAULT_DRAWABLE_ID;   //默认图片
    public static final int error_drawable_id = UtilConfig.ERROR_DRAWABLE_ID;           //错误图片


    /**
     * 添加Http请求
     */
    public static void addHttpRequest(Method method, String url, Map<String, String> params, Listener<?> listener) {
        Request request = GetRequest.requestGetAndPost(method, url, params);  //根据请求 类型，获取 Request
        DoRequest.getInstance().doHttpRequest(request, listener);  //处理请求
    }

    /**
     * 添加一个 Post 文件上传 请求
     */
    public static void addUpdateRequest(String url, Map<String, String> params, File[] files, String[] fileKeys, Listener<?> listener) {
        Request request = GetRequest.requestFile(url, files, fileKeys, params);  //根据请求 类型，获取 Request
        DoRequest.getInstance().doHttpRequest(request, listener);  //处理请求
    }

    /**
     * 添加一个 图片加载 请求
     */
    public static void addImageRequest(final ImageView imageView, final String url, final Listener<Bitmap> listener) {
        if (default_drawable_id != 0) imageView.setImageResource(default_drawable_id);    //设置默认图
        Request request = GetRequest.requestImage(url);
        DoRequest.getInstance().mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                DoRequest.getInstance().mDelivery.post(new Runnable() {
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
                DoRequest.getInstance().doImageResponse(imageView, response, url, listener);
            }
        });
    }


}
