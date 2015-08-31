package pw.hais.utils_demo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import pw.hais.utils_demo.R;

/**
 * 把  TextView 中的 数字转为图片。
 * Created by Hais1992 on 2015/8/31.
 */
public class TextViewStringToImageUtil {


    public static void setTextViewImage(TextView textView,Context context, String str) {
        setTextViewImage(textView,str,getDefaultDrawableMap(context));
    }

    public static void setTextViewImage(TextView textView, String str, Map<String, Drawable> drawableMap) {
        //设置为画笔
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);

        //给 Drawable 设置宽度,并转为Span
        for (String key : drawableMap.keySet()) {
            Drawable drawable = drawableMap.get(key);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

            //开始循环替换
            int keyCount = count(key, str);
            int xy = -1;    //出现的位置
            for (int i = 0; i < keyCount; i++) {
                xy = str.indexOf(key, xy + 1);  //获取出现的位置
                ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                spannableStringBuilder.setSpan(span, xy, xy + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        textView.setText(spannableStringBuilder);
    }


    /**
     * 获取默认的 0-9 图片
     *
     * @param context
     * @return
     */
    private static Map<String, Drawable> getDefaultDrawableMap(Context context) {
        Map<String, Drawable> drawableMap = new HashMap<>();
        drawableMap.put("0", context.getResources().getDrawable(R.drawable.num_0));
        drawableMap.put("1", context.getResources().getDrawable(R.drawable.num_1));
        drawableMap.put("2", context.getResources().getDrawable(R.drawable.num_2));
        drawableMap.put("3", context.getResources().getDrawable(R.drawable.num_3));
        drawableMap.put("4", context.getResources().getDrawable(R.drawable.num_4));
        drawableMap.put("5", context.getResources().getDrawable(R.drawable.num_5));
        drawableMap.put("6", context.getResources().getDrawable(R.drawable.num_6));
        drawableMap.put("7", context.getResources().getDrawable(R.drawable.num_7));
        drawableMap.put("8", context.getResources().getDrawable(R.drawable.num_8));
        drawableMap.put("9", context.getResources().getDrawable(R.drawable.num_9));
        return drawableMap;
    }


    /**
     * 获取字符串 出现次数
     *
     * @param one 短的字符串
     * @param two 长的字符串
     * @return
     */
    private static int count(String one, String two) {
        int oneLen = one.length();
        int twoLen = two.length();
        twoLen = twoLen - two.replace(one, "").length();
        return oneLen == 0 ? 0 : (twoLen / oneLen);
    }

}
