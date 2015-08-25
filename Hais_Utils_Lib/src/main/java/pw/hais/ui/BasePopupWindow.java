package pw.hais.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * Created by hais1992 on 15-5-28.
 */
public abstract class BasePopupWindow extends PopupWindow implements View.OnClickListener{
    protected static View popupWindowView; // 菜单弹窗
    protected View.OnKeyListener onKeyListener;
    protected View.OnClickListener onClickListener;
    protected Activity activity;


    public void setLayout(Activity activity,int layout){
        this.activity = activity;
        this.popupWindowView = LayoutInflater.from(activity).inflate(layout, null); // 获取视图
        setContentView(popupWindowView);
        setWindowLayoutMode(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true); //获得焦点
        setTouchable(true); //触摸
        setOutsideTouchable(true);  //空白处不退出
        setBackgroundDrawable(new BitmapDrawable(activity.getResources(), (Bitmap) null));

        drawEnd(popupWindowView);
    }

    /**
     * 显示在 view 的中间
     */
    public void showCenter(){
        showContent(null);
    }

    public void showContent(View v){
        if(v==null)showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER_VERTICAL, 0, 0);
        else showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);
        popupWindowView.setFocusable(true); //监听按键
        if(onKeyListener!=null)popupWindowView.setOnKeyListener(onKeyListener);    //监听按键
        if(onClickListener!=null)popupWindowView.setOnClickListener(onClickListener);
    }

    /**
     * 绘制完成 的回调
     * @param popupWindowView
     */
    protected abstract void drawEnd(View popupWindowView);


    public void setOnKeyListener(View.OnKeyListener onKeyListener){
        this.onKeyListener = onKeyListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {
        if(onClickListener!=null)onClickListener.onClick(view);
    }
}
