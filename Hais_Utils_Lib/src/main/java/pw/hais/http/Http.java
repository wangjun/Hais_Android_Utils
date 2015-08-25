package pw.hais.http;

import org.json.JSONObject;

import java.util.Map;

import pw.hais.http.base.BaseHttp;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;

/**
 * 基于 OkHttp 的网络请求封装
 * Created by Hais1992 on 2015/8/25.
 */
public class Http extends BaseHttp {


    public static void getString(Method method, String url, Map<String, String> params,Listener<String> listener) {
        getInstance().addRequest(method, url, params, listener);
    }

    public static void getJSONObject(Method method, String url, Map<String, String> params,Listener<JSONObject> listener) {
        getInstance().addRequest(method, url, params, listener);
    }

    public static <T>T getObject(Method method, String url, Map<String, String> params,Listener<T> listener) {
        getInstance().addRequest(method, url, params, listener);
        return null;
    }




}
