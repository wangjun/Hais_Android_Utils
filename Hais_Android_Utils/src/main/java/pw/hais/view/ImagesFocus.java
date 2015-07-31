package pw.hais.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 带选中，和点击效果 的  ImageView
 * Created by hais1992 on 15-5-6.
 */
public class ImagesFocus extends ImageView{
    private Paint paint;
    private boolean select_state =false;    //选中状态
    private String select_color = "#400D17FF";     //选中颜色

    public ImagesFocus(Context context) {
        super(context);
        init();
    }

    public ImagesFocus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImagesFocus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){

        paint = new Paint();
        paint.setAlpha(0x40); //设置透明程度

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(select_color));
        paint.setAntiAlias(true);
        this.setDrawingCacheEnabled(true);
        this.setClickable(true);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (paint!=null && select_state) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                select_state = true;        //设置当前需求为 选中 状态
                invalidate();                   //重新绘图
                break;
            case MotionEvent.ACTION_UP:
                select_state = false;       //设置当前需求为 未选中 状态
                invalidate();               //重新绘图
                break;
        }
        return super.onTouchEvent(event);
    }
}
