package pw.hais.http;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import pw.hais.http.base.BaseHttp;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;
import pw.hais.utils.L;

/**
 * 基于 OkHttp 的网络请求封装
 * Created by Hais1992 on 2015/8/25.
 */
public class Http extends BaseHttp {


    /*-----------------------------HTTP网络请求-------------------------------------*/
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

    /*-----------------------------HTTP文件上传-------------------------------------*/
    public static <T>T updateFile( String url, Map<String, String> params, File[] files, String[] fileKeys, Listener<T> listener){
        getInstance().addRequest(Method.FileUpdate,url,params,files,fileKeys,listener);
        return null;
    }

    public static <T>T updateFile( String url, Map<String, String> params, File file, String fileKey, Listener<T> listener){
        getInstance().addRequest(Method.FileUpdate,url,params,new File[]{file}, new String[]{fileKey},listener);
        return null;
    }

    /*-----------------------------图片下载显示-------------------------------------*/
    public static void displayImage(ImageView imageView,String url){
        getInstance().loadImage(imageView,url,null);
    }

    public static void displayImage(ImageView imageView,String url,Listener<Bitmap> listener){
        getInstance().loadImage(imageView,url,listener);
    }




}
