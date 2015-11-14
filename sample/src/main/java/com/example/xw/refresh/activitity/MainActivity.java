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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pull_to_refresh_layout);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        List<String> strings = new ArrayList<>();
        strings.add("item01");
        strings.add("item02");
        strings.add("item03");
        mPullListView.setAdapter(new ListAdapter(this, 0, strings));
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.refreshFinish(true);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.loadMoreFinish(true);
            }
        }, 2000);
    }
}
