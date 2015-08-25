package pw.hais.utils_demo.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pw.hais.http.Http;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;
import pw.hais.utils.L;
import pw.hais.utils_demo.R;
import pw.hais.utils_demo.app.BaseActivity;
import pw.hais.utils_demo.entity.Weather;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextView text_hello;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Map<String,String> map = new HashMap<>();
//        map.put("key","7c0fca271915eee1061ab9410352fc26");
//        map.put("postcode","215001");
//        Http.getJSONObject(Method.POST, "http://v.juhe.cn/postcode/query", map, new Listener<JSONObject>() {
//            @Override
//            public void success(Response response, JSONObject result) {
//                text_hello.setText("---" + result.toString());
//            }
//        });

        String url = "http://apis.baidu.com/apistore/weatherservice/weather";
        Http.getObject(Method.GET, url, null, new Listener<Weather>() {
            @Override
            public void success(Response response, Weather result) {
                L.i("Http Hais解决了getObject的问题：" + gson.toJson(result));
            }
        });


    }
}
