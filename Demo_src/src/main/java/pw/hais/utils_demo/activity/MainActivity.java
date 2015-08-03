package pw.hais.utils_demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.List;

import pw.hais.utils.L;
import pw.hais.utils.http.Http;
import pw.hais.utils.http.listener.Listener;
import pw.hais.utils_demo.R;
import pw.hais.utils_demo.adapter.WeatherAdapter;
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

        /*--------  免FindViewById  -----------*/
        text_hello.setText("Hello Hais~~~~~");

        /*---------      请求网络       --------*/
        loadDialog.show("请求网络中...");
        String url = "http://apis.baidu.com/apistore/weatherservice/weather";
        Http.getObject(Request.Method.GET, url, null, new Listener<Weather>() {
            @Override
            public void success(Weather response) {
                L.i("Http Hais解决了getObject的问题："+gson.toJson(response));
            }

            @Override
            public void httpEnd(boolean isTrue) {
                loadDialog.dismiss();
            }
        });

        /*-------- 简易Adapter  -------*/
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather(1,"111111"));
        weatherList.add(new Weather(2, "222222"));
        weatherList.add(new Weather(3, "333333"));
        weatherList.add(new Weather(4, "444444"));
        final WeatherAdapter weatherAdapter = new WeatherAdapter(weatherList,R.layout.activity_main_list_item, WeatherAdapter.ViewHolder.class);
        listview.setAdapter(weatherAdapter);

        listview.setOnItemClickListener((adapterView, view, i, l) -> L.showShort("点击：" + weatherAdapter.getItem(i).errMsg));

        
    }

}
