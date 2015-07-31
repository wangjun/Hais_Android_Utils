package pw.hais.utils_demo.adapter;

import android.widget.TextView;

import java.util.List;

import pw.hais.app.AppAdapter;
import pw.hais.utils_demo.entity.Weather;

/**
 * Created by hais1992 on 2015/7/31.
 */
public class WeatherAdapter extends AppAdapter<Weather,WeatherAdapter.ViewHolder> {

    public WeatherAdapter(List<Weather> mList, int layout, Class<ViewHolder> mHolderClass) {
        super(mList, layout, mHolderClass);
    }

    @Override
    public void onBindView(int position, ViewHolder mViewHolder, Weather mItem) {
        mViewHolder.tv_name.setText(mItem.errNum+"");
        mViewHolder.tv_text.setText(mItem.errMsg);
    }

    //反射免FindViewById，名称必须和 XML的 控件ID一样
    public static class ViewHolder extends AppAdapter.ViewHolder{
        public TextView tv_name;
        public TextView tv_text;
    }
}
