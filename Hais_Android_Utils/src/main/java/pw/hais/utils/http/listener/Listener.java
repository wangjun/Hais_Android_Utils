package pw.hais.utils.http.listener;

import com.android.volley.VolleyError;

import pw.hais.utils.L;
import pw.hais.utils.http.request.BaseHttp;

/**
 * Http请求回掉接口
 * Created by hais1992 on 2015/7/31.
 */
public abstract class Listener<T> {

    public abstract void success(T response);

    public void error(VolleyError error){
        L.e(BaseHttp.TAG,"请求出错",error);
    }
}
