package com.eddy.healthaideandroid.config;


import com.alibaba.fastjson.JSONObject;
import com.eddy.healthaideandroid.util.L;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created with Android Studio
 * Author:Chen·ZD
 * Date:2019/5/10
 */

public interface HttpCallBack {

    void success(JSONObject json, Response response);

    default void error(String error, Response response, IOException e) {
        L.e(error);
    }

}
