package pw.hais.utils.http.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import pw.hais.utils.http.listener.Listener;

/**
 * 基于Volley的网络请求类，请求网络获取 字符串
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class RequestString extends BaseRequest<String> {


    /**
     * 请求网络获取 字符串
     *
     * @param method   请求类型
     * @param url      请求url
     * @param params   请求参数
     * @param listener 回调
     */
    public RequestString(int method, String url, Map<String, String> params, Listener<String> listener) {
        super(method, url, params, listener);
    }

    @Override   //解析返回的数据
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }


}
