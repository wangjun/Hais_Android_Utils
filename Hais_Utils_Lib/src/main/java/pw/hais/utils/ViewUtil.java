package pw.hais.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.lang.reflect.Field;

import pw.hais.app.AppBaseActivity;

/**
 * View 工具类
 * Created by Hello_海生 on 2015/7/31.
 */
public class ViewUtil {
    private static Context context = UtilConfig.CONTEXT;

    /**
     * 自动findViewById
     * @param activity  当前 activity
     */
    public static void initViews(Activity activity) {
        try {
            Field[] mFields = activity.getClass().getDeclaredFields();
            for (Field mField : mFields) {
                if(View.class.isAssignableFrom(mField.getType())){  //判断父类是否 view
                    int resourceId = context.getResources().getIdentifier(mField.getName(), "id", context.getApplicationContext().getPackageName());
                    mField.setAccessible(true);
                    mField.set(activity, activity.findViewById(resourceId));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 从当前View 中 自动 findViewById 到 实体类。
     * @param itemView  父View
     * @param clazz   实体类
     * @param <T>
     * @return
     */
    public static <T>T loadingViewToObject(View itemView,Class<T> clazz)
    {
        Context mContext = itemView.getContext();
        Object obj = null;
        try
        {
            obj = clazz.newInstance();
            Field[] mFields = obj.getClass().getDeclaredFields();
            for (Field mField : mFields)
            {
                int resourceId = mContext.getResources().getIdentifier(mField.getName(),"id",mContext.getApplicationContext().getPackageName());
                mField.setAccessible(true);
                mField.set(obj,itemView.findViewById(resourceId));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return (T)obj;
    }


}
