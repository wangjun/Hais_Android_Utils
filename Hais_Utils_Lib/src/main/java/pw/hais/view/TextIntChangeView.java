package pw.hais.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TextView 数字变化
 * Created by hais1992 on 15-5-29.
 */
public class TextIntChangeView extends TextView {
    private static final int REFRESH = 1;
    private int addValue=1000;   //每次增加的大小
    private int defaults;   //改变前的值
    private int toValue;     //目标值

    public TextIntChangeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextIntChangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextIntChangeView(Context context) {
        super(context);
    }


    /**
     * TextView 数字变化
     *
     * @param toValue 目标值
     * @return
     */
    public void setChange(int toValue) {
        defaults = Integer.valueOf(this.getText().toString());
        this.toValue = toValue;
        //addValue = ((toValue - defaults)/30);

        mHandler.sendEmptyMessage(REFRESH);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH:
                    if (defaults < toValue) {
                        defaults =defaults + addValue;
                        setText(defaults + "");
                        mHandler.sendEmptyMessageDelayed(REFRESH, 50);
                    }else{
                        setText(toValue + "");
                    }
                    break;
            }
        }
    };


}
