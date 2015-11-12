package com.repo.xw.library.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 支持上下拉刷新的RecyclerView
 * Created by woxingxiao on 2015/11/12.
 */
public class PullRecyclerView extends RecyclerView implements Pullable {

    private boolean pullDownEnable = true; //下拉刷新开关
    private boolean pullUpEnable = true; //上拉刷新开关

    private LinearLayoutManager mLinLayManager;

    public PullRecyclerView(Context context) {
        super(context);
    }

    public PullRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getAdapter() == null || getAdapter().getItemCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        }
        // 滑到ListView的顶部了
        if (mLinLayManager == null)
            mLinLayManager = (LinearLayoutManager) getLayoutManager();
        return mLinLayManager.findFirstCompletelyVisibleItemPosition() == 0 && getChildAt(0).getTop() >= 0;
    }

    @Override
    public boolean canPullUp() {
        if (getAdapter() == null || getAdapter().getItemCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }
        if (mLinLayManager == null)
            mLinLayManager = (LinearLayoutManager) getLayoutManager();
        int lastCompPosition = mLinLayManager.findLastCompletelyVisibleItemPosition();
        int lastVPosition = mLinLayManager.findLastVisibleItemPosition();
        int firstVPosition = mLinLayManager.findFirstVisibleItemPosition();
        if (lastCompPosition == (getAdapter().getItemCount() - 1)) {
            // 滑到底部了
            if (getChildAt(lastVPosition - firstVPosition) != null &&
                    getChildAt(lastVPosition - firstVPosition).getBottom() <= getMeasuredHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean isPullDownEnable() {
        return pullDownEnable;
    }

    public void setPullDownEnable(boolean pullDownEnable) {
        this.pullDownEnable = pullDownEnable;
    }

    public boolean isPullUpEnable() {
        return pullUpEnable;
    }

    public void setPullUpEnable(boolean pullUpEnable) {
        this.pullUpEnable = pullUpEnable;
    }

}
