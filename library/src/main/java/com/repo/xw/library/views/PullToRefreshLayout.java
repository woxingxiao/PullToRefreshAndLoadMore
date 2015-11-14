package com.repo.xw.library.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.repo.xw.library.R;

import java.util.Timer;
import java.util.TimerTask;

public class PullToRefreshLayout extends RelativeLayout {

    //    public static final String TAG = "PullToRefreshLayout";
    public static final int INIT = 0; // 初始状态
    public static final int RELEASE_TO_REFRESH = 1; // 释放刷新
    public static final int REFRESHING = 2; // 正在刷新
    public static final int RELEASE_TO_LOAD = 3; // 释放加载
    public static final int LOADING = 4; // 正在加载
    public static final int DONE = 5; // 操作完毕

    private int state = INIT;// 当前状态
    private float lastY; // 按下Y坐标，上一个事件点Y坐标
    public float pullDownY = 0; // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    private float pullUpY = 0; // 上拉的距离
    private float refreshDist = 200; // 释放刷新的距离
    private float loadMoreDist = 200; // 释放加载的距离

    private MyTimer timer; // 计时器
    public float MOVE_SPEED = 8; // 回滚速度
    private boolean isLayout = false; // 第一次执行布局
    private boolean isTouch = false;  // 在刷新过程中滑动操作
    private float radio = 2; // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化

    //    private RotateAnimation rotateAnimation; // 下拉箭头的转180°动画
    private View refreshView; // 下拉头
    private ImageView refreshArrowImg;    // 下拉的箭头
    private ProgressBar refreshingBar;     // 正在刷新的图标
    private TextView refreshStateTextView;    // 刷新结果：成功或失败
    private View loadMoreView;    // 上拉头
    private ImageView loadArrowImg;    // 上拉的箭头
    private ProgressBar loadingBar;    // 正在加载的图标
    private TextView loadStateTextView;    // 加载结果：成功或失败

    private View pullableView;    // 实现了Pullable接口的View
    private int mEvents;    // 过滤多点触碰
    private OnRefreshListener mOnRefreshListener; // 刷新回调接口

    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    public PullToRefreshLayout(Context context) {
        super(context);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        timer = new MyTimer(new UpdateHandler());
        refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_head, this, false);
        loadMoreView = LayoutInflater.from(context).inflate(R.layout.layout_load_foot, this, false);
        addView(refreshView);
        addView(loadMoreView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // 这里是第一次进来的时候做一些初始化
        if (!isLayout) {
            isLayout = true;
            pullableView = getChildAt(2);
            initView();
        }
        refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
        loadMoreDist = ((ViewGroup) loadMoreView).getChildAt(0).getMeasuredHeight();
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        refreshView.layout(0,
                (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        pullableView.layout(0, (int) (pullDownY + pullUpY),
                pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + pullableView.getMeasuredHeight());
        loadMoreView.layout(0,
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                loadMoreView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
                        + loadMoreView.getMeasuredHeight());
    }

    private void initView() {
        // 初始化下拉布局
        refreshArrowImg = (ImageView) refreshView.findViewById(R.id.head_arrow_img);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.head_hint_text);
        refreshingBar = (ProgressBar) refreshView.findViewById(R.id.head_progress_bar);
        // 初始化上拉布局
        loadArrowImg = (ImageView) loadMoreView.findViewById(R.id.foot_arrow_img);
        rotateArrow(loadArrowImg);
        loadStateTextView = (TextView) loadMoreView.findViewById(R.id.foot_hint_text);
        loadingBar = (ProgressBar) loadMoreView.findViewById(R.id.foot_progress_bar);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     *
     * @param isSuccess true成功，false失败
     */
    public void refreshFinish(boolean isSuccess) {
        if (refreshingBar == null || refreshStateTextView == null)
            return;
        refreshingBar.clearAnimation();
        refreshingBar.setVisibility(View.GONE);

        if (isSuccess) {
            // 刷新成功
            refreshStateTextView.setText(R.string.refresh_succeed);
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_refresh_succeed);
            refreshStateTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            // 刷新失败
            refreshStateTextView.setText(R.string.refresh_fail);
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_refresh_failed);
            refreshStateTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        if (pullDownY > 0) {
            // 刷新结果停留1秒
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeState(DONE);
                    hide();
                }
            }, 1000);
        } else {
            changeState(DONE);
            hide();
        }
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param isSuccess true成功，false代表失败
     */
    public void loadMoreFinish(boolean isSuccess) {
        if (loadingBar == null  || loadStateTextView == null)
            return;
        loadingBar.clearAnimation();
        loadingBar.setVisibility(View.GONE);

        if (isSuccess) {
            // 加载成功
            loadStateTextView.setText(R.string.load_succeed);
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_load_succeed);
            loadStateTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else {
            // 加载失败
            loadStateTextView.setText(R.string.load_fail);
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_load_failed);
            loadStateTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        if (pullUpY < 0) {
            // 刷新结果停留1秒
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeState(DONE);
                    hide();
                }
            }, 1000);
        } else {
            changeState(DONE);
            hide();
        }
    }

    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT: // 下拉布局初始状态
                refreshStateTextView.setText(R.string.pull_to_refresh);
                refreshStateTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                refreshArrowImg.setVisibility(View.VISIBLE);
                rotateArrow(refreshArrowImg);

                // 上拉布局初始状态
                loadStateTextView.setText(R.string.pull_up_to_load);
                loadStateTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                loadArrowImg.setVisibility(View.VISIBLE);
                rotateArrow(loadArrowImg);
                break;
            case RELEASE_TO_REFRESH: // 释放刷新状态
                refreshStateTextView.setText(R.string.release_to_refresh);
                rotateArrow(refreshArrowImg);
                break;
            case REFRESHING: // 正在刷新状态
                refreshArrowImg.clearAnimation();
                refreshingBar.setVisibility(View.VISIBLE);
                refreshArrowImg.setVisibility(View.INVISIBLE);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD: // 释放加载状态
                loadStateTextView.setText(R.string.release_to_load);
                rotateArrow(loadArrowImg);
                break;
            case LOADING: // 正在加载状态
                loadArrowImg.clearAnimation();
                loadingBar.setVisibility(View.VISIBLE);
                loadArrowImg.setVisibility(View.INVISIBLE);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE: // 刷新或加载完毕，do nothing
                break;
        }
    }

    /**
     * 利用属性动画旋转箭头
     */
    private void rotateArrow(ImageView imageView) {
        if (imageView == refreshArrowImg && pullDownY == 0) { // 下拉箭头恢复最初状态
            ObjectAnimator.ofFloat(imageView, "rotation", 0).setDuration(150).start();
        } else if (imageView == loadArrowImg && pullUpY == 0) { // 上拉箭头恢复最初状态
            ObjectAnimator.ofFloat(imageView, "rotation", 180).setDuration(150).start();
        } else {
            ObjectAnimator.ofFloat(imageView, "rotation",
                    imageView.getRotation() + 180).setDuration(150).start();
        }
    }

    /**
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getY();
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvents == 0) {
                    if (pullDownY > 0
                            || (((Pullable) pullableView).canPullDown()
                            && canPullDown && state != LOADING)) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight()) {
                            pullDownY = getMeasuredHeight();
                        }
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (pullUpY < 0
                            || (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight()) {
                            pullUpY = -getMeasuredHeight();
                        }
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else {
                        releasePull();
                    }
                } else {
                    mEvents = 0;
                }

                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0) {
                    requestLayout();
                }
                if (pullDownY > 0) {
                    if (pullDownY <= refreshDist
                            && (state == RELEASE_TO_REFRESH || state == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }
                    if (pullDownY >= refreshDist && state == INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }
                } else if (pullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadMoreDist
                            && (state == RELEASE_TO_LOAD || state == DONE)) {
                        changeState(INIT);
                    }
                    // 上拉操作
                    if (-pullUpY >= loadMoreDist && state == INIT) {
                        changeState(RELEASE_TO_LOAD);
                    }

                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadMoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mOnRefreshListener != null)
                        mOnRefreshListener.onRefresh(this);
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mOnRefreshListener != null)
                        mOnRefreshListener.onLoadMore(this);
                }
                hide();
            default:
                break;
        }

        //TODO 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /**
     * 执行自动回滚的handler
     */
    private class UpdateHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadMoreDist) {
                    pullUpY = -loadMoreDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                refreshArrowImg.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                loadArrowImg.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT);
                timer.cancel();
                requestLayout();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY + Math.abs(pullUpY) == 0)
                timer.cancel();
        }
    }

    /**
     * 自动模拟手指滑动的task
     */
    private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {

        @Override
        protected String doInBackground(Integer... params) {
            while (pullDownY < 4 / 3 * refreshDist) {
                pullDownY += MOVE_SPEED;
                publishProgress(pullDownY);
                try {
                    Thread.sleep(params[0]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            changeState(REFRESHING);
            // 刷新操作
            if (mOnRefreshListener != null)
                mOnRefreshListener.onRefresh(PullToRefreshLayout.this);
            hide();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (pullDownY > refreshDist)
                changeState(RELEASE_TO_REFRESH);
            requestLayout();
        }

    }

    /**
     * 自动刷新
     */
    public void autoRefresh() {
        AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
        task.execute(20);
    }

    /**
     * 自动加载
     */
    public void autoLoad() {
        pullUpY = -loadMoreDist;
        requestLayout();
        changeState(LOADING);
        // 加载操作
        if (mOnRefreshListener != null)
            mOnRefreshListener.onLoadMore(this);
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    /**
     * 刷新回调接口
     */
    public interface OnRefreshListener {
        /**
         * 下拉刷新
         */
        void onRefresh(PullToRefreshLayout pullToRefreshLayout);

        /**
         * 上拉加载
         */
        void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
    }

}
