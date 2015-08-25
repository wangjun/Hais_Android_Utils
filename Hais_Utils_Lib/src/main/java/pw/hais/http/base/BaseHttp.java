package pw.hais.http.base;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import pw.hais.http.Http;
import pw.hais.utils.GenericsUtils;
import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * 基于 OkHttp 的网络请求 基本方法
 * Created by Hais1992 on 2015/8/25.
 */
public class BaseHttp<T> {
    public static final String TAG = "Http请求";
    protected static Http mInstance;
    protected OkHttpClient mOkHttpClient;
    protected Handler mDelivery;
    protected static Gson gson = UtilConfig.GSON;

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
                                    listener.success(response, (T) string);
                                } else if (clazz == JSONObject.class) {     //JSONObject
                                    listener.success(response, (T) new JSONObject(string));
                                } else {    //Object
                                    listener.success(response, UtilConfig.GSON.fromJson(string, clazz));
                                }
                            } catch (Exception e) {
                                L.e(TAG, "出错", e);
                                listener.error(response.request(), e);
                            }
                        }
                    });
                } catch (Exception e) {
                    L.e(TAG, "出错", e);
                    listener.error(response.request(), e);
                }
            }
        });
    }

}
