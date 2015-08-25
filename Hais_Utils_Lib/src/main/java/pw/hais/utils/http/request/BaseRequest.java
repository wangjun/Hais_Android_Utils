package pw.hais.utils.http.request;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;
import pw.hais.utils.http.listener.Listener;

/**
 * 基于Volley的网络请求类
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public abstract class BaseRequest<T> extends Request<T> {
    private Map<String, String> params;  //参数
    private Listener<T> listener; //回调
    private static final int Retry_Time = 25 * 1000;// 请求超时时间
    private static final int Retry_Num = 0;// 重试请求次数

    public BaseRequest(int method, String url, Map<String, String> params, Listener<T> listener) {
        super(method, url, null);
        this.params = params;
        this.listener = listener;
        //设置重试
        setRetryPolicy(new DefaultRetryPolicy(Retry_Time, Retry_Num, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    @Override   //获取请求参数
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


    @Override   //数据解析
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);


    /**
     * 请求成功回调
     *
     * @param response
     */
    @Override
    protected void deliverResponse(T response) {
        if (listener != null){
            listener.httpEnd(true);
            listener.success(response);
        }
        L.i(BaseHttp.TAG, "结果：" + UtilConfig.GSON.toJson(response));
    }

    /**
     * 请求出错 回调
     *
     * @param error
     */
    @Override
    public void deliverError(VolleyError error) {
        if (listener != null){
            listener.httpEnd(false);
            listener.error(error);
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        cancel();
        this.deliverError(volleyError);
        return super.parseNetworkError(volleyError);
    }
}
