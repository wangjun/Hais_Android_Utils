package pw.hais.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘管理
 * Created by hais1992 on 15-4-24.
 */
public class KeyBoardUtil {
    private static final Context context = UtilConfig.CONTEXT;

    private KeyBoardUtil() {
        throw new UnsupportedOperationException("不能实例化该类");
    }
    /**
     * 打开软键盘
     *
     * @param mEditText
     */
    public static void openKeybord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText
     */
    public static void closeKeybord(EditText mEditText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
