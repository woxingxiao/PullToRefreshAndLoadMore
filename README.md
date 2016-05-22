[![Download](https://api.bintray.com/packages/woxingxiao/maven/PullToRefreshAndLoadMore/images/download.svg)](https://bintray.com/woxingxiao/maven/PullToRefreshAndLoadMore/_latestVersion)

****
**一个轻量下拉刷新上拉加载更多控件，已封装ListView**
****
###Gradle
```groove
dependencies{
    compile 'com.xw.repo:PullToRefresh:1.1.0@aar'
}
```

###Demo
![demo2](https://github.com/woxingxiao/PullToRefreshAndLoadMore/blob/master/screenshots/demo2.gif)


###自定义资源(Customize Resources)
```java
// customize here
ResourceConfig resourceConfig = new ResourceConfig() {
    @Override
    public int[] configImageResIds() {
        return new int[]{R.mipmap.ic_arrow, R.mipmap.ic_ok,
            R.mipmap.ic_failed, R.mipmap.ic_ok, R.mipmap.ic_failed};
        }

    @Override
    public int[] configTextResIds() {
        return new int[]{
            R.string.pull_to_refresh, R.string.release_to_refresh,
            R.string.refreshing, R.string.refresh_succeeded,
            R.string.refresh_failed, R.string.pull_up_to_load,
            R.string.release_to_load, R.string.loading,
            R.string.load_succeeded, R.string.load_failed};
        }
    };

mRefreshLayout.setResourceConfig(resourceConfig);
```

###Thanks
####[PullToRefreshAndLoad](https://github.com/jingchenUSTC/PullToRefreshAndLoad)