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

import okio.ByteString;
import pw.hais.utils.L;
import pw.hais.utils.UtilConfig;

/**
 * Created by Hais1992 on 2015/8/25.
 */
public class GetRequest {
    private static MediaType type = MediaType.parse("application/octet-stream;charset="+UtilConfig.CHARSET);

    /**
     * 添加1个Post 或者Get 请求
     *
     * @param method
     * @param url
     * @param params
     * @return
     */
    public static Request requestGetAndPost(Method method, String url, Map<String, String> params) {
        if (params == null) params = new HashMap<>();
        if (method == Method.POST) {
            return requestPost(url, params);
        } else {
            return requestGet(url, params);
        }
    }

    /**
     * 直接 Post Body
     * @param url
     * @param body
     * @param <T>
     * @return
     */
    public static <T>Request requestPostBody(String url, T body) {
        L.i(BaseHttp.TAG, "地址：" + url);
        RequestBody requestBody = null;
        if(body.getClass() == String.class){
            requestBody = RequestBody.create(type, String.valueOf(body));
        }else if(body.getClass() == File.class){
            requestBody = RequestBody.create(type, (File) body);
        }else if(body.getClass() == byte[].class){
            requestBody = RequestBody.create(type, (byte[]) body);
        }
        L.i(BaseHttp.TAG, "参数：" + body);
        Request request = new Request.Builder().url(url).post(requestBody).tag(url).build();
        return request;
    }

    /**
     * 普通的 GET请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    private static Request requestGet(String url, Map<String, String> params) {
        try {
            StringBuffer sb = new StringBuffer();
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (value == null) {
                    L.e(BaseHttp.TAG, "注意：参数" + key + "为 null ,已自动更换为空字符串。");
                    value = "";
                }
                sb.append(key).append("=").append(URLEncoder.encode(value, UtilConfig.CHARSET)).append("&");
            }
            if (sb.length() != 0) url = url + "?" + sb;
        } catch (Exception e) {
            L.e(BaseHttp.TAG, "请求网络参数错误，不能为null。", e);
        }
        L.i(BaseHttp.TAG, "地址：" + url);
        Request request = new Request.Builder().url(url).tag(url).build();

        return request;
    }

    /**
     * 普通的Post请求
     *
     * @param url    请求地址
     * @param params 请求参数
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
        Request request = new Request.Builder().url(url).post(builder.build()).tag(url).build();
        return request;
    }

    /**
     * 上传文件
     *
     * @param url      请求地址
     * @param files    文件，可多文件
     * @param fileKeys
     * @param params   参数
     * @return
     */
    public static Request requestFile(String url, File[] files, String[] fileKeys, Map<String, String> params) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (String key : params.keySet()) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""), RequestBody.create(null, params.get(key)));
        }

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
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
        return new Request.Builder().url(url).post(requestBody).tag(url).build();
    }

    public static Request requestImage(String url) {
        Request request = new Request.Builder().url(url).tag(url).build();
        return request;
    }


    public static Request requestDownload(String url, String destFileDir) {
        Request request = new Request.Builder().url(url).tag(url).build();
        return request;
    }


}
