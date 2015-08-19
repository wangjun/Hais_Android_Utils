package pw.hais.utils_demo.utils;

import java.util.Random;

import pw.hais.utils.L;
import pw.hais.utils.SPUtils;
import pw.hais.utils.UtilConfig;

/**
 * 安慰奖
 * Created by Administrator on 2015/8/17.
 */
public class AnWeiJiangUtils {
    private static final String AnWeiJiangUtils_SP_key = "AnWeiJiangUtils.key";
    private static AnWeiJiang object;

    /**
     * 开始获取结果
     *
     * @param key   唯一表示
     * @param lower 多少次游戏后中奖
     * @return
     */
    public static AnWeiJiang getGameResults(String key, int lower) {
        object = SPUtils.getObject(key + "." + AnWeiJiangUtils_SP_key, AnWeiJiang.class, null);    //根据商品ID 获取当前游戏信息
        int playCount = 0;
        if (object == null) {   //开始新记录
            object = new AnWeiJiang();
            object.nowRan = getRandom(0, 8);   //获取1-9随机数
            object.win = new int[9];
            object.success = 0;
            object.status = "首次";
        } else {
            playCount = getPlayGameCount(object.win);   //获取当前已完游戏次数
            object.nowRan = getRandom(0, 8);   //获取1-9随机数
            if (playCount + 1 >= lower * (object.success + 1)) {
                object = winGame(object);   //要赢
            } else {
                object = failGame(object);  //要输
            }
        }

        object.win[object.nowRan] = object.win[object.nowRan] + 1;
        SPUtils.saveObject(key + "." + AnWeiJiangUtils_SP_key, object);
        L.i(object.status + "," + UtilConfig.GSON.toJson(object.win) + ",共:" + playCount + ",中奖：" + object.success + ",本次：" + object.nowRan);
        return object;
    }

    //获取已玩游戏次数
    private static int getPlayGameCount(int[] args) {
        int count = 0;
        for (int i = 0; i < args.length; i++) {
            count = count + args[i];
        }
        return count;
    }

    /**
     * 根据Key 删除 缓存信息
     *
     * @param key
     */
    public static void deleteGameInfoByKey(String key) {
        SPUtils.delObject(key + "." + AnWeiJiangUtils_SP_key);
    }


    //游戏胜利
    public static AnWeiJiang winGame(AnWeiJiang obj) {
        for (int i = 0; i <= 9; i++) {
            if (isReulstTrue(obj)) {
                obj.success = obj.success + 1;
                obj.status = "胜利";
                return obj; //返回赢的结果
            } else {   //如果不赢
                obj.nowRan = obj.nowRan >= 8 ? 0 : obj.nowRan + 1;
                obj.status = "无赢";
            }
        }
        return obj; //返回无赢的结果
    }


    //游戏失败
    public static AnWeiJiang failGame(AnWeiJiang obj) {
        for (int i = 0; i <= 9; i++) {
            if (!isReulstTrue(obj)) {
                obj.status = "失败";
                return obj; //返回输的结果
            } else {   //如果不赢
                obj.nowRan = obj.nowRan >= 8 ? 0 : obj.nowRan + 1;
                obj.status = "无输";
            }
        }
        return obj; //返回无输的结果
    }


    /**
     * 随机数的结果是否 可赢
     *
     * @param obj 对象
     * @return 是否可赢
     */
    public static boolean isReulstTrue(AnWeiJiang obj) {
        //判断横向
        int ran = obj.nowRan;
        int value = obj.win[ran];   //当前值
        if (ran == 0 || ran == 3 || ran == 6) {  //判断横向，147
            if (obj.win[ran + 1] > value && obj.win[ran + 2] > value) return true;
        } else if (ran == 1 || ran == 4 || ran == 7) {    //判断横向，258
            if (obj.win[ran - 1] > value && obj.win[ran + 1] > value) return true;
        } else if (ran == 2 || ran == 5 || ran == 8) {    //判断横向，369
            if (obj.win[ran - 2] > value && obj.win[ran - 1] > value) return true;
        }

        return false;
    }


    /**
     * 获取随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static int getRandom(int min, int max) {
        int ran = new Random().nextInt(max) % (max - min + 1) + min;  //获取随机数
        return ran;
    }


    public static class AnWeiJiang {
        public int[] win;    //已中的格子的记录
        public int success;  //中奖次数
        public String status;  //当前状态
        public int nowRan;      //当前随机数
    }

}
