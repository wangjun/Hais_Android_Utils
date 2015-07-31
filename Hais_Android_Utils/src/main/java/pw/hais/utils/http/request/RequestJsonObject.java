package pw.hais.utils.http.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import pw.hais.utils.http.Http;
import pw.hais.utils.http.listener.Listener;

/**
 * 基于Volley的网络请求类，请求网络获取 JSON
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class RequestJsonObject extends BaseRequest<JSONObject> {
    /**
     * 请求网络获取JSON 对象
     *
     * @param method   请求类型
     * @param url      请求url
     * @param params   请求参数
     * @param listener 回调
     */
    public RequestJsonObject(int method, String url, Map<String, String> params, Listener<JSONObject> listener) {
        super(method, url, params, listener);
    }


    @Override   //数据解析
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, Http.PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}
