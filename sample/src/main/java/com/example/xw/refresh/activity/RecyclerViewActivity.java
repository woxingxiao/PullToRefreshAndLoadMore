package com.example.xw.refresh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.xw.refresh.R;
import com.example.xw.refresh.adapter.RecyclerAdapter;
import com.repo.xw.library.views.PullRecyclerView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity
        implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout mRefreshLayout;
    private PullRecyclerView mPullRecyclerView;

    private RecyclerAdapter mAdapter;
    private List<String> mStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pull_to_refresh_recycler_view);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout2);
        mPullRecyclerView = (PullRecyclerView) findViewById(R.id.pullRecyclerView);

        mStrings.add("normal item 0");
        mStrings.add("normal item 1");
        mStrings.add("normal item 2");
        mAdapter = new RecyclerAdapter(this, mStrings);
        mPullRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPullRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        mPullRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.refreshFinish(true);
                mStrings.add("pull down item " + mStrings.size());
                mStrings.add("pull down item " + mStrings.size());
                mAdapter.updateRecyclerView(mStrings);
            }
        }, 2000);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        mPullRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.refreshFinish(true);
                mStrings.add("pull up item " + mStrings.size());
                mStrings.add("pull up item " + mStrings.size());
                mAdapter.updateRecyclerView(mStrings);
            }
        }, 2000);
    }
}
