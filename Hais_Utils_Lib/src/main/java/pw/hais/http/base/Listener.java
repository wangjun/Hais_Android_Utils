package pw.hais.http.base;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Http请求回调
 * Created by Hais1992 on 2015/8/25.
 */
public abstract class Listener<T> {

    public abstract void success(Response response, T result);


    public void error(Request request, Exception e) {
        e.printStackTrace();
    }


    public void onString(Response request, String string) {
    }

    //不管成功失败都回掉
    public void httpEnd(boolean isTrue) {

    }

}
