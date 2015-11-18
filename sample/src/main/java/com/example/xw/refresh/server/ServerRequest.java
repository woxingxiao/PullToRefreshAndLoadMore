package com.example.xw.refresh.server;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.xw.refresh.bean.ListData;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * 通讯录HTTP接口处理
 *
 * @author Administrator
 */
public class ServerRequest {

    private static volatile ServerRequest mInstance;
    private static final String TAG = ServerRequest.class.getSimpleName();
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/json;charset=utf-8");
    private Handler mHandler;

    private ServerRequest(Handler handler) {
        mHandler = handler;
    }

    public static ServerRequest getInstance(Handler handler) {
        if (mInstance == null)
            synchronized (ServerRequest.class) {
                if (mInstance == null)
                    mInstance = new ServerRequest(handler);
            }

        return mInstance;
    }

    public List<ListData> loadDataList(String url) {
        try {
            ServerHttpResult json = getHttpValue(url, null);
            if (json == null)
                return null;

            if (!json.isError()) {
                return json.getResults();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * HTTP统一请求返回参数
     *
     * @param url      请求地址
     * @param postData 请求的数据
     */
    private ServerHttpResult getHttpValue(String url, Object postData) throws IOException {
        // 如果传入数据则转换为json格式,进入传输
        String jsonStr = "";
        if (postData != null) {
            if (!(postData instanceof String)) {
                jsonStr = JSON.toJSONString(postData);
            } else {
                jsonStr = postData.toString();
            }
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, jsonStr);
        Request request = new Request.Builder()
                .addHeader("Content-type", "application/json")
                .url(url)
                .post(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        String resultStr = response.body().string();
        Log.i(TAG, resultStr);

        return JSON.parseObject(resultStr, ServerHttpResult.class);
    }

}
