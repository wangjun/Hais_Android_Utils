package pw.hais.utils;

import java.util.List;
import java.util.Map;

/**
 * 检查是否为空
 */
public class EmptyUtil {
    public EmptyUtil() {
    }

    /**
     * 检测字符串是否为空
     * @param params
     * @return
     */
    public static boolean emptyOfString(String params) {
        return params == null || params.length() <= 0;
    }

    /**
     * 检测列表是否为空
     * @param params
     * @return
     */
    public static boolean emptyOfList(List<?> params) {
        return params == null || params.size() <= 0;
    }

    /**
     * 检查数组是否为空
     * @param params
     * @return
     */
    public static boolean emptyOfArray(Object[] params) {
        return params == null || params.length <= 0;
    }

    /**
     * 检查对象是否为空
     * @param params
     * @return
     */
    public static boolean emptyOfObject(Object params) {
        return params == null;
    }

    /**
     * 检查 Map 是否为空
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean emptyOfMap(Map<K, V> map) {
        return map == null || map.size() <= 0;
    }
}
