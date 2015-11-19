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

        for (int i = 0; i < 5; i++) {
            mStrings.add("normal item " + i);
        }
        mAdapter = new ListAdapter(this, mStrings);
        mPullListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStrings.add("refresh item " + mStrings.size());
                mStrings.add("refresh item " + (mStrings.size() + 1));
                mAdapter.updateListView(mStrings);
                mRefreshLayout.refreshFinish(true);
            }
        }, 2000); // 2秒后刷新
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStrings.add("load item " + mStrings.size());
                mStrings.add("load item " + (mStrings.size() + 1));
                mAdapter.updateListView(mStrings);
                mRefreshLayout.loadMoreFinish(true);
            }
        }, 2000); // 2秒后刷新
    }

}
