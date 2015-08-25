package pw.hais.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片处理工具类
 * Created by hais1992 on 15-4-24.
 */
public class ImageUtil {
    private static final Context context = UtilConfig.CONTEXT;

    private ImageUtil() {
        throw new UnsupportedOperationException("不能实例化该类");
    }

    /**
     * Drawable转化为Bitmap
     * @param drawable
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * Bitmap转化为Drawable
     * @param bitmap
     * @return Drawable
     */
    public static Drawable bitmapToDrawble(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    /**
     * Bitmap转换到Byte[]
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap){
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bas);
        return bas.toByteArray();
    }




    /**
     * 保存图片到SD卡
     * @param imagePath 路径
     * @param buffer byte
     */
    public static void saveImageToSD(String imagePath, byte[] buffer){
        try {
            File f = new File(imagePath);
            if (!f.exists()){
                File parentFile = f.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                f.createNewFile();
                FileOutputStream fos = new FileOutputStream(imagePath);
                fos.write(buffer);
                fos.flush();
                fos.close();
            }
        }catch (Exception e){
            L.e("ImageUtil","保存图片到SD卡出错",e);
        }
    }

    /**
     * 从SD卡加载图片
     * @param imagePath 路径
     * @return
     */
    public static Bitmap getImageFromLocal(String imagePath){
        File file = new File(imagePath);
        if(file.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            file.setLastModified(System.currentTimeMillis());
            return bitmap;
        }
        return null;
    }

}
