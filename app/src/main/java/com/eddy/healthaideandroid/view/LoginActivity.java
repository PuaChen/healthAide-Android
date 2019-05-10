package com.eddy.healthaideandroid.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.eddy.healthaideandroid.MainActivity;
import com.eddy.healthaideandroid.R;
import com.eddy.healthaideandroid.config.HttpCallBack;
import com.eddy.healthaideandroid.constant.Constant;
import com.eddy.healthaideandroid.util.CustomMap;
import com.eddy.healthaideandroid.util.DataUtil;
import com.eddy.healthaideandroid.util.HttpUtil;
import com.eddy.healthaideandroid.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtLoginPhone;
    private EditText mEtLoginPwd;
    private Button mBtLoginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEtLoginPhone = findViewById(R.id.et_login_phone);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mBtLoginSubmit = findViewById(R.id.bt_login_submit);
        mBtLoginSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phone = mEtLoginPhone.getText().toString();
        String password = mEtLoginPwd.getText().toString();
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(password)) {
            ToastUtil.ShowShort(this, "请输入完整");
            return;
        }
        //请求接口
        HttpUtil.doPostForm("/main/login", CustomMap.create("phone", phone)
                .put("password", password), new HttpCallBack() {
            @Override
            public void success(JSONObject json, Response response) {
                {
                    String token = json.getString("token");
                    DataUtil.saveDataByString(LoginActivity.this, Constant.TOKEN_DIR, Constant.TOKEN_DIR, token);
                    //登录成功
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void error(String error, Response response, IOException e) {
                ToastUtil.ShowShort(LoginActivity.this, error);
            }
        });
    }
}
