package com.example.xw.refresh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.xw.refresh.R;
import com.example.xw.refresh.adapter.ListAdapter;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private List<String> mStrings;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pull_to_refresh_list_view);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mStrings = new ArrayList<>();
        mRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ListAdapter(this, mStrings);
        mPullListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStrings.add("refresh item " + mStrings.size());
                mStrings.add("refresh item " + (mStrings.size()));

                mRefreshLayout.refreshFinish(true);
                updateListData();
            }
        }, 2000); // 2秒后刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStrings.add("load item " + mStrings.size());
                mStrings.add("load item " + (mStrings.size()));

                mRefreshLayout.loadMoreFinish(true);
                updateListData();

            }
        }, 2000); // 2秒后刷新
    }

    private void updateListData() {
        if (mAdapter == null) {
            mAdapter = new ListAdapter(this, mStrings);
            mPullListView.setAdapter(mAdapter);
        } else {
            mAdapter.updateListView(mStrings);
        }
    }

}
