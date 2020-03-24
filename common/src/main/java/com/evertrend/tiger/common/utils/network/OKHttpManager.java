package com.evertrend.tiger.common.utils.network;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpManager {
    private static final String TAG = OKHttpManager.class.getSimpleName();

    private OkHttpClient okHttpClient = null;
    private volatile static OKHttpManager okHttpManager = null;
    private Handler handler;

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static final MediaType MEDIA_TYE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private String url;
    private Map<String, String> params;
    private Object object;
    private List<Object> list;

    private OKHttpManager() {
        getClient();
        handler = new Handler(Looper.getMainLooper());
    }

    private void getClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool());
            okHttpClient = builder.build();
        }
    }

    /**
     * 采用单例模式获取对象
     */
    public static OKHttpManager getInstance(){
        synchronized (OKHttpManager.class) {
            if (okHttpManager == null) {
                okHttpManager = new OKHttpManager();
            }
            return okHttpManager;
        }
    }

    public OKHttpManager url(String url) {
        this.url = url;
        return okHttpManager;
    }

    public OKHttpManager addParams(String key, String val) {
        if (this.params == null)
        {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return okHttpManager;
    }

    /**
     * 模拟表单提交
     * @param callBack
     */
    public void sendComplexForm(final FuncJsonObj callBack, final FuncFailure callFailure){
        FormBody.Builder form_builder = new FormBody.Builder(); //表单对象，包含以input开始的对象，以html表单为主
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, String> entry : params.entrySet()){
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody request_body = form_builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();  //采用post方式提交
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //LogUtil.i(TAG, "onFailure");
                onFailureMethod(callFailure);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response != null && response.isSuccessful()){
                    onSuccessJsonObjectMethod(response.body().string(), callBack);
                } else {
                    onFailureMethod(callFailure);
                }
            }
        });
    }

    /**
     * 返回响应的结果是json对象
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, final FuncJsonObj callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    try{
                        callBack.onResponse(JSONObject.parseObject(jsonValue));
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onFailureMethod(final FuncFailure callBack){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(callBack != null){
                    callBack.onFailure();
                }
            }
        });
    }

    public interface FuncJsonObj{
        void onResponse(JSONObject jsonObject) throws JSONException;
    }

    public interface FuncFailure{
        void onFailure();
    }
}
