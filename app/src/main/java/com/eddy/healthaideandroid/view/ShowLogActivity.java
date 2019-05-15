package com.eddy.healthaideandroid.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.eddy.healthaideandroid.BaseActivity;
import com.eddy.healthaideandroid.R;
import com.eddy.healthaideandroid.config.HttpCallBack;
import com.eddy.healthaideandroid.entity.IndexDataVo;
import com.eddy.healthaideandroid.entity.TableEntity;
import com.eddy.healthaideandroid.util.CustomMap;
import com.eddy.healthaideandroid.util.HttpUtil;
import com.eddy.healthaideandroid.util.ToastUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

public class ShowLogActivity extends BaseActivity {


    private EditText mLogSelectStartTime;
    private BarChart mLogBarChart;
    private TimePickerView pvTime;
    private SmartTable mCreateTable;
    private TextView mLogTitle;
    private TextView mLogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initPvTime();
        initData();
    }

    private void initData() {
        String time = mLogSelectStartTime.getText().toString();
        HttpUtil.doPostForm("/index/getDataForDay", CustomMap.create("dayTime", time), new HttpCallBack() {
            @Override
            public void success(String json, Response response) {
                IndexDataVo vo = JSONObject.parseObject(json, IndexDataVo.class);
                List<TableEntity> entityList = new ArrayList<>();
                if (vo == null) {
                    entityList.add(new TableEntity("-", 0.0));
                    mCreateTable.setData(entityList);
                    mCreateTable.getTableData();
                    mCreateTable.invalidate();
                    entityList.clear();
                    initChart(entityList);
                    mLogTitle.setText("");
                    mLogInfo.setText("");
                    return;
                }
                mLogTitle.setText("预测结果:  " + vo.getIllnessName());
                mLogInfo.setText("专家建议: \n\n" + vo.getIllnessInfo());
                entityList.add(new TableEntity("体温", vo.getAnimalHeat()));
                entityList.add(new TableEntity("血氧", vo.getBlood()));
                entityList.add(new TableEntity("血糖", vo.getGlucose()));
                entityList.add(new TableEntity("胆固醇", vo.getCholesterol()));
                entityList.add(new TableEntity("血压高压", vo.getPressureH()));
                entityList.add(new TableEntity("血压低压", vo.getPressureL()));
                entityList.add(new TableEntity("血尿酸", vo.getUric()));
                entityList.add(new TableEntity("甘油三酯", vo.getTriglyceride()));
                entityList.add(new TableEntity("尿液酸碱度", vo.getUrinePh()));
                mCreateTable.setData(entityList);
                mCreateTable.getTableData();
                mCreateTable.invalidate();
                initChart(entityList);
            }

            @Override
            public void error(String error, Response response, IOException e) {
                ToastUtil.ShowShort(ShowLogActivity.this, error);
            }
        });
    }

    private void initChart(List<TableEntity> entities) {
        mLogBarChart.setDescription("");
        // 没有数据的时候，显示“暂无数据”
        mLogBarChart.setNoDataText("暂无数据");
        mLogBarChart.setBackgroundColor(Color.WHITE);
        mLogBarChart.setDrawGridBackground(false); // 不显示表格颜色
        mLogBarChart.setDrawBarShadow(false);
        //设置是否可以拖拽
        mLogBarChart.setDragEnabled(true);
        mLogBarChart.getLegend().setEnabled(false);
        XAxis xAxis = mLogBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        ArrayList<BarEntry> barEntryList = new ArrayList<>();
        List<String> barStringLab = new ArrayList<>();

        for (int i = 0; i < entities.size(); i++) {
            barEntryList.add(new BarEntry(Float.parseFloat(entities.get(i).getValue().toString()), i));
            barStringLab.add(entities.get(i).getName());
        }
        //设置数据
        BarDataSet dataSet = new BarDataSet(barEntryList, "");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        dataSet.setBarSpacePercent(25f);
        BarData data = new BarData(barStringLab, dataSet);
        data.setValueTextSize(20f);
        mLogBarChart.setData(data);
        mLogBarChart.animateXY(300, 300);
        mLogBarChart.notifyDataSetChanged();
        mLogBarChart.invalidate();
    }

    private void initView() {
        mLogSelectStartTime = findViewById(R.id.log_select_startTime);
        mLogBarChart = findViewById(R.id.log_barChart);
        mCreateTable = findViewById(R.id.create_table);
        mLogSelectStartTime.setText(DateTime.now().toString("yyyy-MM-dd"));
        mLogSelectStartTime.setOnClickListener(v -> pvTime.show(v));
        mLogTitle = findViewById(R.id.log_title);
        mLogInfo = findViewById(R.id.log_info);
    }

    /**
     * 初始化 时间选择器
     */
    private void initPvTime() {
        Calendar endDate = Calendar.getInstance();
        endDate.setTime(new Date());

        Calendar startDate = Calendar.getInstance();

        endDate.set(Calendar.DAY_OF_MONTH, -7);


        pvTime = new TimePickerView.Builder(this, (date, v) -> {
            DateTime dateTime = new DateTime(date.getTime());
            ((EditText) v).setText(dateTime.toString("yyyy-MM-dd"));
            v.setTag(dateTime);
            initData();
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("选择")//确认按钮文字
                .setRangDate(startDate, endDate)
                .build();
    }

    @Override
    protected void setTile() {
        super.title = "我的健康";
    }

    @Override
    protected void setLayoutId() {
        super.root = LayoutInflater.from(this).inflate(R.layout.activity_show_log, null);
    }
}
