package com.example.xw.refresh.adapter;

import android.content.Context;
import android.os.Handler;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * 下拉刷新列表父类适配器
 * Created by woxingxiao on 2015/9/27.
 */
public class BaseListAdapter<T> extends ArrayAdapter<T> {

    protected Context mContext;
    protected Handler mHandler;
    protected List<T> mDataList;
    protected boolean hasNoData;

    public BaseListAdapter(Context context, List<T> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mDataList = objects;
        if (this.mDataList == null || this.mDataList.isEmpty())
            hasNoData = true;
    }

    public BaseListAdapter(Context context, List<T> objects, Handler handler) {
        super(context, 0, objects);
        this.mContext = context;
        this.mDataList = objects;
        this.mHandler = handler;
        if (this.mDataList == null || this.mDataList.isEmpty())
            hasNoData = true;
    }

    public void updateListView(List<T> dataList) {
        this.mDataList = dataList;
        hasNoData = (this.mDataList == null || this.mDataList.isEmpty());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (hasNoData)
            return 1;
        return (mDataList == null || mDataList.isEmpty()) ? 0 : mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (hasNoData)
            return null;
        return mDataList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (hasNoData)
            return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
