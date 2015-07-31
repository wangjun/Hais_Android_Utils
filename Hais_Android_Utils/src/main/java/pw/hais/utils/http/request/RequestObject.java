package pw.hais.utils.http.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;
import pw.hais.utils.http.Http;
import pw.hais.utils.http.listener.Listener;

/**
 * 基于Volley的网络请求类，请求网络获取 JSON
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class RequestObject<T> extends BaseRequest<T> {
    private Class<T> clazz;    //对象类

    /**
     * 请求网络获取JSON 对象
     *
     * @param method   请求类型
     * @param url      请求url
     * @param params   请求参数
     * @param listener 回调
     */
    public RequestObject(int method, String url, Map<String, String> params, Class<T> clazz, Listener<T> listener) {
        super(method, url, params, listener);
        this.clazz = clazz;
    }


    @Override   //数据解析
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, Http.PROTOCOL_CHARSET));
            Response<T> responseValue=null;
            try {
                responseValue = Response.success(UtilConfig.GSON.fromJson(jsonString, clazz), HttpHeaderParser.parseCacheHeaders(response));
            }catch (Exception e){
                L.e(BaseHttp.TAG,"请求结果转换JSON出错",e);
            }
            return responseValue;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

}
