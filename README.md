[![](https://jitpack.io/v/woxingxiao/PullToRefreshAndLoadMore.svg)](https://jitpack.io/#woxingxiao/PullToRefreshAndLoadMore)

****
**一个轻量下拉刷新上拉加载更多控件，已封装ListView**
****

由于`bintray`上不去无法更新版本（梯子也不行），因此从`v2.0`起以后的更新都将发布到`JitPack`上。

###Gradle
```groove
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}

	...

dependencies{
    compile 'com.github.woxingxiao:PullToRefreshAndLoadMore:v2.0'
}
```

###Demo
![demo3](https://github.com/woxingxiao/PullToRefreshAndLoadMore/blob/master/screenshots/demo3.gif)


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