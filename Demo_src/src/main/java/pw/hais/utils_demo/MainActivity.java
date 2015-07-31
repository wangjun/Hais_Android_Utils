package pw.hais.utils_demo;

import android.os.Bundle;
import android.widget.TextView;

import pw.hais.utils_demo.app.BaseActivity;

public class MainActivity extends BaseActivity {
    private TextView text_hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_hello.setText("Hello Hais~~~~~");
    }
}
