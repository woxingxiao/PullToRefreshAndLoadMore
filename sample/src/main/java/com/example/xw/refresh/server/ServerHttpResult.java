package com.example.xw.refresh.server;

import com.alibaba.fastjson.JSON;
import com.example.xw.refresh.bean.ListData;

import java.util.List;

/**
 * 请求服务器结果
 */
public final class ServerHttpResult {

    private boolean error;// 判断接口数据是否成功
    private List<ListData> results;// 请求成功对应的数据

    public ServerHttpResult() {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ListData> getResults() {
        return results;
    }

    public void setResults(List<ListData> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        if (results != null) {
            return JSON.toJSONString(this.results);
        }
        return null;
    }
}