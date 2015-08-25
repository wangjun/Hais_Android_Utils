package pw.hais.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import pw.hais.utils.R;

/**
 * 加载框- 菊花+提示语
 * Created by Hais1992 on 2015/7/1.
 */
public class LoadProgressDialog {
    private static TextView textView;
    private static Dialog mDialog;


    public LoadProgressDialog(Context context){
        mDialog = new Dialog(context, R.style.dialog);

        LayoutInflater in = LayoutInflater.from(context);
        View viewDialog = in.inflate(R.layout.dialog_progress, null);
        viewDialog.setBackgroundColor(0x7f000000);
        textView = (TextView) viewDialog.findViewById(R.id.textView);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 这里可以设置dialog的大小，当然也可以设置dialog title等
        // LayoutParams layoutParams = new LayoutParams(width * 80 / 100, 50);
        // mDialog.setContentView(viewDialog, layoutParams);
        mDialog.setContentView(viewDialog);
        mDialog.setCanceledOnTouchOutside(true);
    }


    /**
     * 显示加载框
     */
    public void show(){
        show(null);
    }

    /**
     * 显示加载框
     * @param text
     */
    public void show(String text){
        if(text!=null && !"".equals(text)){
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }else{
            textView.setVisibility(View.GONE);
        }
        mDialog.show();
    }



    /**
     * 关闭Dialog
     */
    public void dismiss(){
        if(mDialog!=null)mDialog.dismiss();
    }

}