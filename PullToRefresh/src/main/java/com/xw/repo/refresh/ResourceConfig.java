package com.xw.repo.refresh;

import com.xw.repo.pulltorefresh.R;

/**
 * customize image resources and text resources
 * <p/>
 * Created by woxingxiao on 2016/5/22.
 */
public abstract class ResourceConfig {

    public int[] getImageResIds() {
        return configImageResIds() == null || configImageResIds().length != 5 ?
                configImageResIdsByDefault() : configImageResIds();
    }

    public int[] getTextResIds() {
        return configTextResIds() == null || configTextResIds().length != 10 ?
                configTextResIdsByDefault() : configTextResIds();
    }

    /**
     * customize image resources
     *
     * @return must 6 elements
     */
    public abstract int[] configImageResIds();

    /**
     * customize text resources
     *
     * @return must 10 elements
     */
    public abstract int[] configTextResIds();

    private int[] configImageResIdsByDefault() {
        return new int[]{R.mipmap.icon_xw_ptr_arrow, R.mipmap.icon_xw_ptr_refresh_succeeded,
                R.mipmap.icon_xw_ptr_refresh_failed, R.mipmap.icon_xw_ptr_load_succeeded,
                R.mipmap.icon_xw_ptr_load_failed};
    }

    private int[] configTextResIdsByDefault() {
        return new int[]{R.string.xw_ptr_pull_to_refresh, R.string.xw_ptr_release_to_refresh, R.string.xw_ptr_refreshing,
                R.string.xw_ptr_refresh_succeeded, R.string.xw_ptr_refresh_failed, R.string.xw_ptr_pull_up_to_load,
                R.string.xw_ptr_release_to_load, R.string.xw_ptr_loading, R.string.xw_ptr_load_succeeded,
                R.string.xw_ptr_load_failed};
    }
}