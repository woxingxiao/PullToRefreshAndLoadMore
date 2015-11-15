package com.example.xw.refresh.activitity;

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
        setContentView(R.layout.activity_main);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mStrings.add("normal item " + i);
        }
        mAdapter = new ListAdapter(this, 0, mStrings);
        mPullListView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.refreshFinish(true);
                mStrings.add("pull down item " + mStrings.size());
                mStrings.add("pull down item " + mStrings.size());
                mAdapter.updateData(mStrings);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.loadMoreFinish(true);
                mStrings.add("pull up item " + mStrings.size());
                mStrings.add("pull up item " + mStrings.size());
                mAdapter.updateData(mStrings);
            }
        }, 2000);
    }
}
