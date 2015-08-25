package pw.hais.utils_demo.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

import pw.hais.http.Http;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;
import pw.hais.utils_demo.R;
import pw.hais.utils_demo.app.BaseActivity;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextView text_hello;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Map<String,String> map = new HashMap<>();
        map.put("apikey","89feaaf0011c135911a4dd7679adb4cb");
        Http.getString(Method.POST, "http://apis.baidu.com/idl_baidu/faceverifyservice/face_deleteuser", map, new Listener<String>() {
            @Override
            public void success(Response response, String result) {

            }
        });

    }
}
