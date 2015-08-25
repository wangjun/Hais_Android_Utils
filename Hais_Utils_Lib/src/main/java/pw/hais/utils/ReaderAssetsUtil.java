package pw.hais.utils;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

/**
 * 读取 assets 文件夹内容
 * Created by 韦海生 on 2015/1/16.
 */
public class ReaderAssetsUtil {
    private static final String ENCODING = "UTF-8";
    private static final Context context = UtilConfig.CONTEXT;
    private ReaderAssetsUtil() {
        throw new UnsupportedOperationException("不能实例化该类");
    }


    /**
     * 从assets 文件夹中获取文件并读取数据
     * @param fileName 文件名
     * @return 内容
     */
    public static String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);  //获取文件的字节数
            int lenght = in.available(); //创建byte数组
            byte[] buffer = new byte[lenght]; //将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
