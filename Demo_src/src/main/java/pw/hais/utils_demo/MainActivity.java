package pw.hais.utils_demo;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

import pw.hais.utils.L;
import pw.hais.utils.http.Http;
import pw.hais.utils.http.listener.Listener;
import pw.hais.utils_demo.app.BaseActivity;
import pw.hais.utils_demo.entity.Weather;

public class MainActivity extends BaseActivity {
    private TextView text_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_hello.setText("Hello Hais~~~~~");
        String url = "http://apis.baidu.com/apistore/weatherservice/weather";
        Http.getObject(Request.Method.GET,url,null, new Listener<Weather>() {
            @Override
            public void success(Weather response) {
                L.i("Http-errMsg:"+response.errMsg);
            }
        });
    }
}
