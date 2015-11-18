package com.example.xw.refresh.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.xw.refresh.R;
import com.example.xw.refresh.adapter.ListAdapter;
import com.example.xw.refresh.bean.ListData;
import com.example.xw.refresh.server.ServerRequest;
import com.repo.xw.library.views.PullListView;
import com.repo.xw.library.views.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PullToRefreshLayout.OnRefreshListener {

    private PullToRefreshLayout mRefreshLayout;
    private PullListView mPullListView;

    private List<ListData> mDataList;
    private ListAdapter mAdapter;
    private MyHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pull_to_refresh_list_view);

        mRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullToRefreshLayout);
        mPullListView = (PullListView) findViewById(R.id.pullListView);
        mDataList = new ArrayList<>();
        mRefreshLayout.setOnRefreshListener(this);
        mHandler = new MyHandler();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        new LoadDataTask().execute();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mDataList = ServerRequest.getInstance(mHandler)
                    .loadDataList("http://gank.avosapps.com/api/data/福利/10/1");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRefreshLayout.refreshFinish(true);
            if (mDataList == null || mDataList.isEmpty())
                return;

            if (mAdapter == null) {
                mAdapter = new ListAdapter(MainActivity.this, mDataList);
                mPullListView.setAdapter(mAdapter);
            } else {
                mAdapter.updateListView(mDataList);
            }
        }
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    }
}
