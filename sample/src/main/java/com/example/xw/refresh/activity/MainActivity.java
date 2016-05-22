package com.example.xw.refresh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.xw.refresh.R;
import com.example.xw.refresh.adapter.ListAdapter;
import com.xw.repo.refresh.PullListView;
import com.xw.repo.refresh.PullToRefreshLayout;
import com.xw.repo.refresh.ResourceConfig;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        PullToRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private List<String> mStrings;
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pull_to_refresh_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mStrings = new ArrayList<>();
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(this);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_clear) {
            if (mStrings != null) {
                mStrings.clear();
                updateListData();

                return true;
            }
        } else if (item.getItemId() == R.id.action_customize) {
            // customize here
            ResourceConfig resourceConfig = new ResourceConfig() {
                @Override
                public int[] configImageResIds() {
                    return new int[]{R.mipmap.ic_arrow, R.mipmap.ic_ok,
                            R.mipmap.ic_failed, R.mipmap.ic_ok, R.mipmap.ic_failed};
                }

                @Override
                public int[] configTextResIds() {
                    return new int[]{R.string.pull_to_refresh, R.string.release_to_refresh, R.string.refreshing,
                            R.string.refresh_succeeded, R.string.refresh_failed, R.string.pull_up_to_load,
                            R.string.release_to_load, R.string.loading, R.string.load_succeeded,
                            R.string.load_failed};
                }
            };
            mRefreshLayout.setShowRefreshResultEnable(true);
            mRefreshLayout.setResourceConfig(resourceConfig);
            return true;
        }

        return false;
    }
}
