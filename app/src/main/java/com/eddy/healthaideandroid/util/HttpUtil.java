package com.eddy.healthaideandroid.util;


import com.alibaba.fastjson.JSONObject;
import com.eddy.healthaideandroid.config.HttpCallBack;
import com.eddy.healthaideandroid.config.MyApplication;
import com.eddy.healthaideandroid.constant.Constant;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static final String AGENT_KEY = "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3253.3 Mobile Safari/537.36";

    private static class Get {
        public static final OkHttpClient OK_HTTP_CLIENT =
                new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)
                        .writeTimeout(50, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();
                                Request.Builder requestBuilder = request.newBuilder();
                                Headers.Builder builder = request.headers().newBuilder();
                                builder.add(Constant.TOKEN_KEY, DataUtil.readSpDataByString(MyApplication.getContext(), Constant.TOKEN_DIR, Constant.TOKEN_DIR));
                                requestBuilder.headers(builder.build());
                                request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        })
                        .build();
    }

    public static final OkHttpClient getInstance() {
        return Get.OK_HTTP_CLIENT;
    }


    public static Call doPost(String url, RequestBody requestBody) {
        OkHttpClient okHttpClient = getInstance();
        Request request = new Request.Builder()
                .url(HttpUtil.getUrl(url))
                .post(requestBody)
                .addHeader("User-Agent", AGENT_KEY)
                .build();
        return okHttpClient.newCall(request);
    }

    /**
     * Post Form请求
     *
     * @param url
     * @param params
     * @return
     */
    public static void doPostForm(String url, Map<String, Object> params, final HttpCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        doPost(url, builder.build()).enqueue(new MyCall(callBack));
    }


    public static void doGet(String url, Map<String, Object> params, final HttpCallBack callBack) {
        OkHttpClient okHttpClient = getInstance();
        Request.Builder request = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(HttpUtil.getUrl(url))
                .newBuilder();
        if (MapUtils.isNotEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        request.url(urlBuilder.build());
        Call call = okHttpClient.newCall(request.build());
        call.enqueue(new MyCall(callBack));
    }


    public static String getUrl(String suffix) {
        return Constant.PREFIX_URL + suffix;
    }

    private static class MyCall implements Callback {

        private HttpCallBack callBack;

        public MyCall(HttpCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            callBack.error(e.getMessage(), null, e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                String string = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(string);
                L.HLog(jsonObject);
                if (jsonObject.getString("errorCode").equals("0")) {
                    callBack.success(jsonObject.getJSONObject("result"), response);
                } else {
                    callBack.error(jsonObject.getString("errorMessage"), response, null);
                }
            } else {
                callBack.error("请求失败", response, null);
            }
            response.close();
        }
    }

}
