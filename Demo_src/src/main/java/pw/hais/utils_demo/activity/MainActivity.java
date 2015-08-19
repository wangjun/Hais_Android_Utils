package pw.hais.utils_demo.activity;

import android.graphics.Typeface;
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
import pw.hais.utils_demo.utils.AnWeiJiangUtils;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextView text_hello;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text_hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnWeiJiangUtils.getGameResults("hais", 5);
            }
        });
    }
}
