package pw.hais.app;

import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import pw.hais.utils.EmptyUtil;
import pw.hais.utils.ViewHolderUtils;

/**
 * AppAdapter
 * 简介: 实现简单的列表形式,可以帮助你更快捷的开发,无须再重复实现内容
 * Created by Single on 15-7-18.
 * @version 1.0
 */
public abstract class AppAdapter<E,T extends AppAdapter.ViewHolder> extends android.widget.BaseAdapter {

    private int layout;
    private Class<T> mHolderClass;
    private List<E> mList;
    private E[] mArray;

    public AppAdapter(List<E> mList, int layout, Class<T> mHolderClass) {
        this.layout = layout;
        this.mList = mList;
        this.mHolderClass = mHolderClass;
    }

    public AppAdapter(E[] mArray, int layout, Class<T> mHolderClass) {
        this.layout = layout;
        this.mArray = mArray;
        this.mHolderClass = mHolderClass;
    }

    private AppAdapter(){
    }

    public void updateDataSet(List<E> mList){
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    public void updateDataSet(E[] mArray){
        this.mArray = mArray;
        this.notifyDataSetChanged();
    }

    public void appendData(List<E> mList){
        this.mList.addAll(mList);
        this.notifyDataSetChanged();
    }

    public void appendData(E[] mArray){
        int newLength = this.mArray.length + mArray.length;
        E[] mNewArray = Arrays.copyOf(this.mArray,newLength);
        for(int i=this.mArray.length-1;i<newLength;i++){
            mNewArray[i] = mArray[i-mArray.length-1];
        }
        this.mArray = mNewArray;
        this.notifyDataSetChanged();
    }

    public void appendData(E mItem){

        if(!EmptyUtil.emptyOfList(this.mList)) {
            this.mList.add(mItem);
        }

        if(!EmptyUtil.emptyOfArray(this.mArray)){
            this.mArray = Arrays.copyOf(this.mArray,this.mArray.length+1);
            this.mArray[this.mArray.length-1] = mItem;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        if(!EmptyUtil.emptyOfList(this.mList)){
            return this.mList.size();
        }

        if(!EmptyUtil.emptyOfArray(this.mArray)){
            return this.mArray.length;
        }

        return 0;
    }

    @Override
    public E getItem(int position) {

        if(!EmptyUtil.emptyOfList(this.mList)){
            return (E) this.mList.get(position);
        }

        if(!EmptyUtil.emptyOfArray(this.mArray)){
            return (E) this.mArray[position];
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = ViewHolderUtils.loadingConvertView(parent.getContext(), convertView, layout, mHolderClass);
        ViewHolder mHolder = (ViewHolder) convertView.getTag();
        mHolder.itemView = convertView;
        onBindView(position,(T)mHolder,(E)getItem(position));
        return convertView;
    }

    /**
     * 进行数据绑定
     * @param position
     * @param mViewHolder
     * @param mItem
     */
    public abstract void onBindView(int position,T mViewHolder,E mItem);

    /**
     * ViewHolder
     */
    public static class ViewHolder{
        public View itemView;
    }
}
