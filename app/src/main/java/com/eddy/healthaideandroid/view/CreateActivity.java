package com.eddy.healthaideandroid.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.eddy.healthaideandroid.BaseActivity;
import com.eddy.healthaideandroid.R;
import com.eddy.healthaideandroid.config.HttpCallBack;
import com.eddy.healthaideandroid.util.CustomMap;
import com.eddy.healthaideandroid.util.HttpUtil;
import com.eddy.healthaideandroid.util.ToastUtil;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class CreateActivity extends BaseActivity implements View.OnClickListener {

    private EditText mCreateAnimalHeat;
    private EditText mCreateBlood;
    private EditText mCreateGlucose;
    private EditText mCreateCholesterol;
    private EditText mCreatePressureH;
    private EditText mCreatePressureL;
    private EditText mCreateUric;
    private EditText mCreateTriglyceride;
    private EditText mCreateUrinePh;
    private QMUIRoundButton mCreateSubmit;
    private AlertDialog dialog;

    private QMUIDialog dialogWait;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (dialogWait != null) {
                dialogWait.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initShowWaitDialog();
        dialogWait.show();
        handler.sendEmptyMessageDelayed(0, 5000);
    }

    private void initView() {
        mCreateAnimalHeat = findViewById(R.id.create_animalHeat);
        mCreateBlood = findViewById(R.id.create_blood);
        mCreateGlucose = findViewById(R.id.create_glucose);
        mCreateCholesterol = findViewById(R.id.create_cholesterol);
        mCreatePressureH = findViewById(R.id.create_pressureH);
        mCreatePressureL = findViewById(R.id.create_pressureL);
        mCreateUric = findViewById(R.id.create_uric);
        mCreateTriglyceride = findViewById(R.id.create_triglyceride);
        mCreateUrinePh = findViewById(R.id.create_urinePh);
        mCreateSubmit = findViewById(R.id.create_submit);
        mCreateSubmit.setOnClickListener(this);
        dialog = new ProgressDialog.Builder(this)
                .setCancelable(false)
                .setTitle("提示")
                .setMessage("正在努力计算中请稍等...")
                .create();
    }

    /**
     * 判断是否输入为空
     *
     * @return
     */
    private boolean checkFeild() {
        List<EditText> list = new ArrayList<>();
        list.add(mCreateAnimalHeat);
        list.add(mCreateBlood);
        list.add(mCreateGlucose);
        list.add(mCreateCholesterol);
        list.add(mCreatePressureH);
        list.add(mCreatePressureL);
        list.add(mCreateUric);
        list.add(mCreateTriglyceride);
        list.add(mCreateUrinePh);
        for (EditText editText : list) {
            if (StringUtils.isBlank(editText.getText())) {
                String string = editText.getHint().toString();
                ToastUtil.ShowShort(this, string);
                return false;
            }
        }
        return true;
    }

    private void initShowWaitDialog() {
        dialogWait = new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("正在连接相关设备请等待....")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog);
        dialogWait.setCanceledOnTouchOutside(false);

    }

    @Override
    protected void setTile() {
        super.title = "记录我的健康情况";
    }

    @Override
    protected void setLayoutId() {
        super.root = LayoutInflater.from(this).inflate(R.layout.activity_create, null);
    }

    @Override
    public void onClick(View v) {
        if (!checkFeild()) {
            return;
        }
        //取值
        Double animalHeat = Double.valueOf(mCreateAnimalHeat.getText().toString());
        Double blood = Double.valueOf(mCreateBlood.getText().toString());
        Double glucose = Double.valueOf(mCreateGlucose.getText().toString());
        Double cholesterol = Double.valueOf(mCreateCholesterol.getText().toString());
        Double pressureH = Double.valueOf(mCreatePressureH.getText().toString());
        Double pressureL = Double.valueOf(mCreatePressureL.getText().toString());
        Double uric = Double.valueOf(mCreateUric.getText().toString());
        Double triglyceride = Double.valueOf(mCreateTriglyceride.getText().toString());
        Double urinePh = Double.valueOf(mCreateUrinePh.getText().toString());

        CustomMap data = CustomMap.create()
                .put("animalHeat", animalHeat)
                .put("blood", blood)
                .put("glucose", glucose)
                .put("cholesterol", cholesterol)
                .put("pressureH", pressureH)
                .put("pressureL", pressureL)
                .put("uric", uric)
                .put("triglyceride", triglyceride)
                .put("urinePh", urinePh);

        dialog.show();

        HttpUtil.doPostForBody("/index/record", data, new HttpCallBack() {
            @Override
            public void success(String json, Response response) {
                dialog.dismiss();
                JSONObject object = JSONObject.parseObject(json);
                mCreateSubmit.setVisibility(View.GONE);
                StringBuffer sb = new StringBuffer();
                sb.append("预测可能的疾病：" + object.getString("illnessName"));
                sb.append("\n\n");
                sb.append("专家建议 ：\n\n");
                sb.append(object.getString("illnessInfo"));
                QMUIDialog dialog = new QMUIDialog.MessageDialogBuilder(CreateActivity.this)
                        .setTitle("预测结果")
                        .setMessage(sb.toString())
                        .addAction("关闭", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                finish();
                            }
                        })
                        .create(com.qmuiteam.qmui.R.style.QMUI_Dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }

            @Override
            public void error(String error, Response response, IOException e) {
                dialog.dismiss();
                ToastUtil.ShowShort(CreateActivity.this, error);
            }
        });

    }
}
