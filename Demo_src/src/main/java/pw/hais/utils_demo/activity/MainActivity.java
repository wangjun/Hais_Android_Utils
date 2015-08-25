package pw.hais.utils_demo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pw.hais.http.Http;
import pw.hais.http.base.Listener;
import pw.hais.http.base.Method;
import pw.hais.utils.DownTime;
import pw.hais.utils.L;
import pw.hais.utils_demo.R;
import pw.hais.utils_demo.app.BaseActivity;
import pw.hais.utils_demo.entity.Weather;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextView text_hello;
    private ListView listview;
    private ImageView image_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        show();
    }


    public void show(){
        loadDialog.show("下载图片中..");
        Http.displayImage(image_test, "http://img.ivsky.com/img/tupian/pic/201506/23/lvse_de_qingxi11an.jpg", new Listener<Bitmap>() {
            @Override
            public void success(Response response, Bitmap result) {
                loadDialog.dismiss();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                show();
                            }
                        }, 500);
                    }
                });
            }

            @Override
            public void error(Request request, Exception e) {
                loadDialog.dismiss();
            }
        });
    }

}
