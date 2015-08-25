package pw.hais.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * APP信息活取
 * Created by hais1992 on 15-4-24.
 */
public class AppInfoUtil {
    private static final Context context = UtilConfig.CONTEXT;

    private AppInfoUtil() {
        throw new UnsupportedOperationException("不能实例化该类");
    }


    /**
     * 获取手机IMEI码
     *
     * @return 手机IMEI码
     */
    public static String getDeviceIMEI() {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getAndroidId() {
        String AndroidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return AndroidId;
    }


    /**
     * 获取用户 mac地址
     *
     * @return
     */
    public static String getMACAddress() {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String MacAddress = wm.getConnectionInfo().getMacAddress();
        return MacAddress;
    }

    /**
     * 获取CPU序列号
     *
     * @return CPU序列号(16位)
     * 读取失败为"0000000000000000"
     */

    public static String getCPUSerial() {
        String cpuAddress = "0000000000000000";
        try {// 读取CPU信息
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            // 查找CPU序列号
            for (int i = 1; i < 100; i++) {
                String str = input.readLine();
                if (str != null) {
                    // 查找到序列号所在行
                    if (str.indexOf("Serial") > -1) {
                        // 提取序列号
                        String strCPU = str.substring(str.indexOf(":") + 1, str.length());
                        // 去空格
                        cpuAddress = strCPU.trim();
                        break;
                    }
                } else {
                    break;  // 文件结尾
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cpuAddress;
    }

    /**
     * 获取用户唯一标识
     *
     * @return 唯一标识  DEVICE_ID+SimSerialNumber+CPU
     */
    public static String getUnique_code() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String SimSerialNumber = tm.getSimSerialNumber();
        return Md5Utils.encryptionFor32(getDeviceIMEI() + SimSerialNumber + getMACAddress()+getCPUSerial());
    }
}