package pw.hais.http;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;
import java.util.Map;

import pw.hais.http.base.BaseHttp;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;

/**
 * 基于 OkHttp 的网络请求封装
 * Created by Hais1992 on 2015/8/25.
 */
public class Http {

    /*-----------------------------HTTP网络请求-------------------------------------*/
    public static void get(String url, Map<String, String> params, Listener<?> listener) {
        BaseHttp.addHttpRequest(Method.GET, url, params, listener);
    }

    public static void post(String url, Map<String, String> params, Listener<?> listener) {
        BaseHttp.addHttpRequest(Method.POST, url, params, listener);
    }

    public static void post(String url,String body,Listener<?> listener){
        BaseHttp.addPostBodyRequest(url,body,listener);
    }

    public static void post(String url,File bodyFile,Listener<?> listener){
        BaseHttp.addPostBodyRequest(url, bodyFile, listener);
    }

    public static void post(String url,byte[] bodyBytes,Listener<?> listener){
        BaseHttp.addPostBodyRequest(url, bodyBytes, listener);
    }


    /*-----------------------------HTTP文件上传-------------------------------------*/
    public static void updateFile(String url, Map<String, String> params, File[] files, String[] fileKeys, Listener<?> listener) {
        BaseHttp.addUpdateRequest(url, params, files, fileKeys, listener);
    }

    public static void updateFile(String url, Map<String, String> params, File file, String fileKey, Listener<?> listener) {
        BaseHttp.addUpdateRequest(url, params, new File[]{file}, new String[]{fileKey}, listener);
    }

    /*-----------------------------图片下载显示-------------------------------------*/
    public static void displayImage(ImageView imageView, String url) {
        BaseHttp.addImageRequest(imageView, url, null);
    }

    public static void displayImage(ImageView imageView, String url, Listener<Bitmap> listener) {
        BaseHttp.addImageRequest(imageView, url, listener);
    }

    /*-----------------------------文件　　下载-------------------------------------*/
    public static void download(String url, String fileDir, Listener<String> listener) {
        BaseHttp.addDownloadRequest(url,fileDir,listener);
    }



}
