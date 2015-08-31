package pw.hais.utils_demo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Response;

import java.io.File;

import pw.hais.http.Http;
import pw.hais.http.base.Listener;
import pw.hais.utils.L;
import pw.hais.utils_demo.R;
import pw.hais.utils_demo.app.BaseActivity;
import pw.hais.utils_demo.utils.TextViewStringToImageUtil;

public class MainActivity extends BaseActivity {
    //反射免FindViewById，名称必须和 XML的 控件ID一样
    private TextView text_hello;
    private ListView listview;
    private ImageView image_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String url = "http://image.beekka.com/blog/2014/bg2014052201.png";
//        String path = Environment.getExternalStorageDirectory().getPath() + "/";
//        Http.download(url, path, new Listener<String>() {
//            @Override
//            public void success(Response response, String result) {
//                L.i("Http","---------"+result);
//            }
//        });

        String url = "http://b.zol-img.com.cn/desk/bizhi/image/6/1920x1080/144064263261.jpg";
        Http.displayImage(image_test, url);


        String mssage = "1阿斯5顿123003风格5化2";

        TextViewStringToImageUtil.setTextViewImage(text_hello, this, mssage);
    }


}
