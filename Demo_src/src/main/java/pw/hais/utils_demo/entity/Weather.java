package pw.hais.utils_demo.entity;

/**
 * Created by hais1992 on 2015/7/31.
 */
public class Weather {
    public int errNum;
    public String errMsg;


    public Weather() {
    }

    public Weather(int errNum, String errMsg) {
        this.errNum = errNum;
        this.errMsg = errMsg;
    }
}
