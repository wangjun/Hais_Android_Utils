package pw.hais.http.image;

import android.graphics.Bitmap;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;


/**
 * 图片加载完成后的显示 动画
 *
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class DisplayAnim {

    private static Map<String, String> mapString = new HashMap<>();

    /**
     * 淡出显示 图片
     *
     * @param bitmap
     * @param imageView
     */
    public static void displayBitmap(String url, Bitmap bitmap, ImageView imageView) {
        imageView.setImageBitmap(bitmap);
        if (mapString.get(url) != null) return;
        animate(imageView, 500);
        mapString.put(url, url);
        imageView.setTag(url);
    }

    /**
     * 淡出显示图片 效果
     *
     * @param imageView
     * @param durationMillis
     */
    private static void animate(ImageView imageView, int durationMillis) {
        AlphaAnimation fadeImage = new AlphaAnimation(0, 1);    //开始透明度0，变化到1
        fadeImage.setDuration(durationMillis);    // 变化时间
        imageView.startAnimation(fadeImage);    //启动动画

        //AlphaAnimation fadeImage = (AlphaAnimation) AnimationUtils.loadAnimation(imageView.getContext(), R.anim.alpha_anim);
    }
}
