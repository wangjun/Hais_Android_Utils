package pw.hais.utils.http.request;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * 基于Volley的 基础 网络请求类
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class BaseHttp {
    public static final String TAG = "Http";
    protected static final Context context = UtilConfig.CONTEXT;
    public static final String PROTOCOL_CHARSET = UtilConfig.CHARSET;       //编码
    public static final RequestQueue requestQueue = Volley.newRequestQueue(context);

    /**
     * 防止工具类被实例化
     */
    protected BaseHttp() {
        throw new AssertionError();
    }

    /**
     * 拼接每次请求需要的参数
     *
     * @param parameter
     * @return
     */
    protected static Map<String, String> addGlobalParameter(Map<String, String> parameter) {
        if (parameter == null) parameter = new HashMap<String, String>();
        //parameter.put("version", "1");   //添加版本
        //parameter.put("key", "1123");   //添加版本
        L.d(TAG, "参数：" + parameter);
        return parameter;
    }

    /**
     * 根据请求类型 修改 url
     * @param method
     * @param parameter
     * @return
     */
    protected static String basedMethodUpdateHttpUrl(String url,int method, Map<String, String> parameter) {
        if(parameter==null)return url;
        //根据请求拼接参数
        switch (method) {
            case Request.Method.GET:
                try {
                    StringBuffer sb = new StringBuffer();
                    for (String key : parameter.keySet()) {
                        String value = parameter.get(key);
                        if (value == null){
                            L.e(TAG, "注意：参数" + key + "为 null ,已自动更换为空字符串。");
                            value = "";
                        }
                        sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
                    }
                    url = url + "?" + sb;
                } catch (Exception e) {
                    L.e(TAG,"请求网络参数错误，不能为null。",e);
                }
                break;
        }
        L.i(TAG, "地址：" + url);
        return url;
    }



    /**
     * HTTP 请求回调
     * @param <T>
     */
    public interface Listener<T> {
        void success(T response);
        void error(VolleyError error);
    }

    /**
     * 图片 回调
     */
    public interface ListenerImage {
        void success(ImageLoader.ImageContainer response);
        void error(VolleyError error);
    }

}
