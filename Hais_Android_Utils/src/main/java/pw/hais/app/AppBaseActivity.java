package pw.hais.app;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import pw.hais.ui.LoadProgressDialog;
import pw.hais.utils.UtilConfig;
import pw.hais.utils.ViewUtil;


/**
 * 基础Activity
 * Created by hais1992 on 15-5-6.
 */
public class AppBaseActivity extends Activity implements View.OnClickListener{
    protected String tag = "";              //当前TAG
    protected Context context;              //方便设置监听器等。。
    protected Gson gson = UtilConfig.GSON;
    protected AppApplication app = AppApplication.app;
    protected LoadProgressDialog loadDialog;    //菊花

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tag = getClass().getSimpleName();   //获取 当前类名，方便 打log

        context = this;
        app.addActivity(this);
        loadDialog = new LoadProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ViewUtil.initViews(this);   //反射获取 类中的 View
    }

    /**
     * 根据ID 获取 View
     */
    public <T extends View> T findView(int id) {
        T v = (T) findViewById(id);
        return v;
    }

    /**
     * 根据ID 获取 View,并设置监听器为this
     */
    public <T extends View> T findViewAndSetOnClick(int id) {
        T v = (T) findViewById(id);
        if(v!=null)v.setOnClickListener(this);
        return v;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void finish() {
        app.removeActivity(this);
        super.finish();
    }


    @Override
    public void onClick(View view) {

    }
}
