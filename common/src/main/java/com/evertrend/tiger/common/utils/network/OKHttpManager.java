package com.evertrend.tiger.common.utils.network;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHttpManager {
    private final String TAG = OKHttpManager.class.getSimpleName();

    private OkHttpClient client = null;
    private volatile static OKHttpManager manager = null;
    private Handler handler;

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    private static final MediaType MEDIA_TYE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    //提交png图片
    public static final MediaType MEDIA_TYE_PNG = MediaType.parse("image/png;charset=utf-8");

    private OKHttpManager(){
        getClient();
        handler = new Handler(Looper.getMainLooper());
    }

    public OkHttpClient getClient() {
        if (client == null) {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            builder.connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectionPool(new ConnectionPool());
            client = builder.build();
        }
        return client;
    }
    /**
     * 采用单例模式获取对象
     */
    public static OKHttpManager getInstance(){
        synchronized (OKHttpManager.class) {
            if (manager == null) {
                manager = new OKHttpManager();
            }
            return manager;
        }
    }

    /**
     * 模拟表单提交
     * @param url
     * @param params
     * @param callBack
     */
    public void sendComplexForm(String url, Map<String, String> params, final FuncJsonObj callBack, final FuncFailure callFailure){
        FormBody.Builder form_builder = new FormBody.Builder(); //表单对象，包含以input开始的对象，以html表单为主
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, String> entry : params.entrySet()){
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody request_body = form_builder.build();
        Request request = new Request.Builder().url(url).post(request_body).build();  //采用post方式提交
        client.newCall(request).enqueue(new Callback() {
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

    public void sendFileForm(MediaType type, String url, Map<String, String> params, File file, final FuncJsonObj callBack, final FuncFailure callFailure){
        // 设置文件以及文件上传类型封装
        RequestBody requestBody = RequestBody.create(type, file);
        // 文件上传的请求体封装
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        multipartBodyBuilder.addFormDataPart("file", file.getName(), requestBody);
        if(params != null && !params.isEmpty()){
            for(Map.Entry<String, String> entry : params.entrySet()){
                multipartBodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        Request request = new Request.Builder().url(url).post(multipartBodyBuilder.build()).build();
        client.newCall(request).enqueue(new Callback() {
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
//                        callBack.onResponse(new JSONObject(jsonValue));
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

    public interface FuncResponse{
        void onResponse(Response response);
    }

    public interface FuncFailure{
        void onFailure();
    }
}
