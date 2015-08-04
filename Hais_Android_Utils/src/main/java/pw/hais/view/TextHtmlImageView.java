package pw.hais.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import pw.hais.utils.ImageUtil;
import pw.hais.utils.L;
import pw.hais.utils.R;
import pw.hais.utils.http.Http;
import pw.hais.utils.http.listener.ListenerImage;

/**
 * 用于显示HTML 并自动加载内部图片的 TextView
 * Created by Administrator on 2015/8/4.
 */
public class TextHtmlImageView extends TextView implements Html.ImageGetter {
    private String text;
    public TextHtmlImageView(Context context) {
        super(context);
    }

    public TextHtmlImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextHtmlImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setHtml(String html) {
        this.text = html;
        map = new HashMap<>();
        setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动
        setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        setText(Html.fromHtml(html, this, null));
    }


    private Map<String,Drawable> map;
    private int i=0;
    @Override
    public Drawable getDrawable(final String url) {
        Drawable drawable = map.get(url);
        if(drawable == null){
            i++;
            Http.getImageBitmap(url, new ListenerImage() {
                @Override
                public void success(ImageLoader.ImageContainer response) {
                    map.put(url, ImageUtil.bitmapToDrawble(response.getBitmap()));
                    i--;
                    if(i==0)setText(Html.fromHtml(text, TextHtmlImageView.this, null));
                }
            });
            drawable = map.get(url);
        }
        //设置图片宽、高
        if(drawable==null)drawable = getResources().getDrawable(R.drawable.image_default);   //获取图片
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

}
