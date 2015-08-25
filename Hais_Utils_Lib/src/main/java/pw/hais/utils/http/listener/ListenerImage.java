package pw.hais.utils.http.listener;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import pw.hais.utils.L;
import pw.hais.utils.http.request.BaseHttp;

/**
 * 图片请求回掉接口
 * Created by hais1992 on 2015/7/31.
 */
public abstract class ListenerImage {
    public abstract void success(ImageLoader.ImageContainer response);

    public void error(VolleyError error){
        L.e(BaseHttp.TAG, "请求出错", error);
    }

}
