package pw.hais.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SD卡工具类
 * @author Hello_海生
 * @date 2015年3月27日
 */
public class SDCardUtils {

    private SDCardUtils()
    {
        throw new UnsupportedOperationException("禁止实例化该类！");
    }

    /**
     * 判断SDCard是否可用
     * @return
     */
    public static boolean isEnable()
    {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD卡路径
     * @return
     */
    public static String getPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡的剩余容量
     * @return 容量大小 单位byte
     */
    public static long getSize()
    {
        if (isEnable())
        {
            StatFs stat = new StatFs(getPath());
            // 获取空闲的数据块的数量
            long availableBlocks = stat.getAvailableBlocksLong() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocksLong();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }

    /**
     * 获取系统存储路径
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
