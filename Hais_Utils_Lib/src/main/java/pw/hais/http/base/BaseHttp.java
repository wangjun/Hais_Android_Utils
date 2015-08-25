package pw.hais.http.base;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import pw.hais.http.Http;
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


    protected void addRequest(Method method, String url, Map<String, String> params, final Listener<T> listener) {
        Request request = getRequest(method, url, params);  //根据请求 类型，获取 Request

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                L.e(TAG, "出错", e);
                listener.httpEnd(false);
                listener.error(request, e);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    String string = response.body().string();
                    L.i(TAG, "结果：" + string);
                    listener.httpEnd(true);
                    listener.success(response, (T) string);
                } catch (Exception e) {
                    L.e(TAG, "出错", e);
                    listener.httpEnd(false);
                    listener.error(response.request(), e);
                }

            }
        });
    }

    protected static Request getRequest(Method method, String url, Map<String, String> params) {
        //根据请求拼接参数
        Request request = null;
        switch (method) {
            case GET:
                try {
                    if (params == null) params = new HashMap<>();
                    StringBuffer sb = new StringBuffer();
                    for (String key : params.keySet()) {
                        String value = params.get(key);
                        if (value == null) {
                            L.e(TAG, "注意：参数" + key + "为 null ,已自动更换为空字符串。");
                            value = "";
                        }
                        sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
                    }
                    if (sb.length() != 0) url = url + "?" + sb;
                } catch (Exception e) {
                    L.e(TAG, "请求网络参数错误，不能为null。", e);
                }
                L.i(TAG, "地址：" + url);
                request = new Request.Builder().url(url).build();
                break;
            case POST:
                if (params == null) params = new HashMap<>();
                FormEncodingBuilder builder = new FormEncodingBuilder();
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (value == null) {
                        L.e(TAG, "注意：参数" + key + "为 null ,已自动更换为空字符串。");
                        value = "";
                    }
                    builder.add(key, value);
                }
                L.i(TAG, "地址：" + url);
                L.i(TAG, "参数：" + gson.toJson(params));
                request = new Request.Builder().url(url).post(builder.build()).build();
                break;
        }

        return request;
    }


}
