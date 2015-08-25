package pw.hais.http.base;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * Created by Hais1992 on 2015/8/25.
 */
public class HttpRequest {

    /**
     * 根据请求类型 获取 Request
     * @param method
     * @param url
     * @param params
     * @param files
     * @param fileKeys
     * @return
     */
    public static Request getRequest(Method method, String url, Map<String, String> params, File[] files, String[] fileKeys) {
        if (params == null) params = new HashMap<>();
        //根据请求拼接参数
        Request request = null;
        switch (method) {
            case GET:
                request = requestTypeGet(url, params);
                break;
            case POST:
                request = requestPost(url, params);
                break;
            case FileUpdate:
                request = requestFile(url,files,fileKeys,params);
                break;
        }
        return request;
    }

    /**
     * 普通的 GET请求
     * @param url   请求地址
     * @param params 请求参数
     * @return
     */
    private static Request requestTypeGet(String url, Map<String, String> params) {
        try {
            StringBuffer sb = new StringBuffer();
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (value == null) {
                    L.e(BaseHttp.TAG, "注意：参数" + key + "为 null ,已自动更换为空字符串。");
                    value = "";
                }
                sb.append(key).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
            }
            if (sb.length() != 0) url = url + "?" + sb;
        } catch (Exception e) {
            L.e(BaseHttp.TAG, "请求网络参数错误，不能为null。", e);
        }
        L.i(BaseHttp.TAG, "地址：" + url);
        Request request = new Request.Builder().url(url).build();

        return request;
    }

    /**
     * 普通的Post请求
     * @param url   请求地址
     * @param params    请求参数
     * @return
     */
    private static Request requestPost(String url, Map<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (value == null) L.e(BaseHttp.TAG, "注意：参数" + key + "为 null。");
            builder.add(key, value);
        }
        L.i(BaseHttp.TAG, "地址：" + url);
        L.i(BaseHttp.TAG, "参数：" + UtilConfig.GSON.toJson(params));
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        return request;
    }

    /**
     * 上传文件
     * @param url 请求地址
     * @param files 文件，可多文件
     * @param fileKeys
     * @param params    参数
     * @return
     */
    private static Request requestFile(String url,File[] files,String[] fileKeys,Map<String,String> params){
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (String key : params.keySet())
        {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), RequestBody.create(null, params.get(key)));
        }

        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                String fileName = file.getName();

                String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if (contentTypeFor == null) contentTypeFor = "application/octet-stream";

                RequestBody fileBody = RequestBody.create(MediaType.parse(contentTypeFor), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""), fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }


}
