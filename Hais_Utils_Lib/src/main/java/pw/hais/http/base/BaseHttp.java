package pw.hais.http.base;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import pw.hais.http.image.CacheManager;
import pw.hais.http.image.DisplayAnim;

/**
 * 基于 OkHttp 的网络请求 基本方法
 * Created by Hais1992 on 2015/8/25.
 */
public class BaseHttp {
    public static final String TAG = "Http请求";


    /**
     * 添加Http请求
     */
    public static void addHttpRequest(Method method, String url, Map<String, String> params, Listener<?> listener) {
        Request request = GetRequest.requestGetAndPost(method, url, params);  //根据请求 类型，获取 Request
        DoRequest.getInstance().doHttpRequest(request, listener);  //处理请求
    }

    /**
     * 添加Post Body请求
     */
    public static <T> T addPostBodyRequest(String url, T body, Listener<?> listener) {
        Request request = GetRequest.requestPostBody(url, body);  //根据请求 类型，获取 Request
        DoRequest.getInstance().doHttpRequest(request, listener);  //处理请求
        return null;
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
        if(imageView != null){
            if (url == null) imageView.setImageResource(CacheManager.error_drawable_id);
            else imageView.setImageResource(CacheManager.default_drawable_id);
        }

        //开始获取缓存  或 下载
        Bitmap bitmap = CacheManager.getBitmapCache(url);   //根据URL获取缓存
        if (bitmap == null){    //如果缓存为空，则开始下载
            Request request = GetRequest.requestImage(url);
            DoRequest.getInstance().mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    DoRequest.getInstance().onHttpError(request, e, listener);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    DoRequest.getInstance().doImageResponse(imageView, response, url, listener);
                }
            });
        }else if(imageView!=null){
            DisplayAnim.displayBitmap(url,bitmap,imageView);
            DoRequest.getInstance().onHttpSuccess(null,listener,bitmap);
        }
    }

    /**
     * 添加一个 文件下载 请求
     */
    public static void addDownloadRequest(String url, String fileDir, Listener<String> listener) {
        Request request = GetRequest.requestDownload(url, fileDir);
        DoRequest.getInstance().doDownloadResponse(request, url, fileDir, listener);
    }

    /**  取消一个请求 */
    public static void cancel(String url){
        if(DoRequest.getInstance().mOkHttpClient!=null){
            DoRequest.getInstance().mOkHttpClient.cancel(url);
        }
    }
}
