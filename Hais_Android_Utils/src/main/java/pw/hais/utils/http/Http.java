package pw.hais.utils.http;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.http.imageloader.ImageLoaders;
import pw.hais.utils.http.imageloader.LocalImageManager;
import pw.hais.utils.http.request.BaseHttp;
import pw.hais.utils.http.request.RequestJsonObject;
import pw.hais.utils.http.request.RequestObject;
import pw.hais.utils.http.request.RequestString;


/**
 * 基于Volley的网络请求类
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class Http extends BaseHttp {

    /**
     * 防止工具类被实例化
     */
    private Http() {
        super();
    }


    /*------------------图片------------------*/
    /**
     * 根据url 加载网络图片
     * @param v imageView
     * @param url url
     */
    public static void loadImage(ImageView v,String url){
        ImageLoaders.loadImage(url, v);
    }

    /**
     * 根据url 缓存图片
     * @param url url
     */
    public static void cacheImageBitmap(String url) {
        if(!LocalImageManager.imagesIsCache(url)){
            getImageBitmap(url, null);
        }
    }

    /**
     * 根据url 下载网论图片,缓存图片
     * @param url url
     * @param listenerImage 监听器
     */
    public static void getImageBitmap(String url, final ListenerImage listenerImage) {
        ImageLoaders.loadImageOnly(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (listenerImage != null) listenerImage.success(response);
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (listenerImage != null) listenerImage.error(error);
            }
        });
    }


    /**
     * 根据url 加载本地图片
     * @param url 地址
     * @param v
     */
    public static void loadLocalImage(String url, ImageView v) {
        ImageLoaders.loadLocalImage(v,url,0,0);
    }




    /*------------------网络HTTP------------------*/

    /**
     * 请求网络 获取 String
     *
     * @param method        Method.POST、Method.GET
     * @param parameter     请求参数
     * @param listener      回调
     */
    public static void getString(int method, String url, Map<String, String> parameter,Listener<String> listener) {
        parameter = addGlobalParameter(parameter);  //添加参数
        url = basedMethodUpdateHttpUrl(url, method, parameter);   //根据 请求类型 拼接GET的请求URL
        //请求网络
        RequestString requestString = new RequestString(method,url,parameter,listener);
        requestQueue.add(requestString);
    }

    /**
     * 请求网络 获取 JSON 对象
     * @param method        Method.POST、Method.GET
     * @param parameter     请求参数
     * @param listener      回调
     */
    public static void getJsonObject(int method, String httpUrl, Map<String, String> parameter,Listener<JSONObject> listener) {
        parameter = addGlobalParameter(parameter);  //添加参数
        httpUrl = basedMethodUpdateHttpUrl(httpUrl,method,parameter);   //根据 请求类型 拼接GET的请求URL
        //请求网络
        RequestJsonObject requestString = new RequestJsonObject(method,httpUrl,parameter,listener);
        requestQueue.add(requestString);
    }


    /**
     * 请求网络 获取 Object 对象
     *
     * @param method        Method.POST、Method.GET
     * @param parameter     请求参数
     * @param listener      回调
     */
    public static void getObject(int method, String httpUrl, Map<String, String> parameter,Class clazz,Listener<?> listener) {
        parameter = addGlobalParameter(parameter);  //添加参数
        httpUrl = basedMethodUpdateHttpUrl(httpUrl,method,parameter);   //根据 请求类型 拼接GET的请求URL
        //请求网络
        RequestObject requestObject = new RequestObject<>(method,httpUrl,parameter,clazz,listener);
        requestQueue.add(requestObject);
    }

}
