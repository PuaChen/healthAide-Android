package com.eddy.healthaideandroid.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.TimePickerView;
import com.eddy.healthaideandroid.BaseActivity;
import com.eddy.healthaideandroid.R;
import com.eddy.healthaideandroid.config.HttpCallBack;
import com.eddy.healthaideandroid.entity.IndexDataVo;
import com.eddy.healthaideandroid.util.CustomMap;
import com.eddy.healthaideandroid.util.HttpUtil;
import com.eddy.healthaideandroid.util.ToastUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import okhttp3.Response;

/**
 * Created with Android Studio
 * Author:Chen·ZD
 * Date:2019/5/11
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private TextView mMainWelcome;

    private TextView mTime;

    private LineChart lineChart;

    private BarChart barChart;

    private TimePickerView pvTime;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String time = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
            mTime.setText(time);
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };

    private QMUIRoundButton mMainCreate;
    private QMUIRoundButton mMainLog;
    private EditText mMainSelectStartTime;
    private EditText mMainSelectEndTime;
    private Spinner mMainSelectType;
    private Spinner mMainSelectChart;

    //默认的选择类型下标值
    private int typeIndex = 0;
    private List<CustomMap> types = Arrays.asList(CustomMap.create("name", "体温")
                    .put("field", "animalHeat"),
            CustomMap.create("name", "血氧").put("field", "blood"),
            CustomMap.create("name", "血糖").put("field", "glucose"),
            CustomMap.create("name", "胆固醇").put("field", "cholesterol"),
            CustomMap.create("name", "血压 高压").put("field", "pressureH"),
            CustomMap.create("name", "血压 低压").put("field", "pressureL"),
            CustomMap.create("name", "血尿酸").put("field", "uric"),
            CustomMap.create("name", "甘油三酯").put("field", "triglyceride"),
            CustomMap.create("name", "尿液酸碱度").put("field", "urinePh")

    );

    private List<String> chartType = Arrays.asList("折线图", "柱状图");
    private int chartIndex = 0; //图表类型下表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainWelcome = findViewById(R.id.main_welcome);
        mTime = findViewById(R.id.main_time);
        lineChart = findViewById(R.id.main_lineChart);
        barChart = findViewById(R.id.main_barChart);
        mMainCreate = findViewById(R.id.main_create);
        mMainLog = findViewById(R.id.main_log);
        mMainSelectStartTime = findViewById(R.id.main_select_startTime);
        mMainSelectEndTime = findViewById(R.id.main_select_endTime);
        mMainSelectType = findViewById(R.id.main_select_type);
        mMainSelectChart = findViewById(R.id.main_select_chart);

        initPvTime();

        GregorianCalendar ca = new GregorianCalendar();
        if (ca.get(GregorianCalendar.AM_PM) == 0) {
            mMainWelcome.setText("欢迎您,上午好 !");
        } else {
            mMainWelcome.setText("欢迎您,下午好 !");
        }
        handler.sendEmptyMessageDelayed(1, 1000);

        //获取当前时间和前7天时间
        DateTime now = DateTime.now();
        DateTime lastTime = now.minusDays(7);
        mMainSelectStartTime.setText(lastTime.toString("yyyy-MM-dd"));
        mMainSelectStartTime.setTag(lastTime);
        mMainSelectEndTime.setText(now.toString("yyyy-MM-dd"));
        mMainSelectEndTime.setTag(now);

        //设置下拉框数据
        List<String> names = new ArrayList<>();
        for (CustomMap type : types) {
            names.add(MapUtils.getString(type, "name"));
        }
        mMainSelectType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names));
        mMainSelectChart.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, chartType));

        mMainSelectChart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //选择图表的
                chartIndex = position;
                replaceChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMainSelectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //选择类型的
                typeIndex = position;
                if (chartIndex == 0) {
                    initChartForLine();
                } else {
                    initChartForBar();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMainSelectStartTime.setOnClickListener(this);
        mMainSelectEndTime.setOnClickListener(this);

        mMainCreate.setOnClickListener(this);
        mMainLog.setOnClickListener(this);
    }

    /**
     * 切换表格
     */
    private void replaceChart() {
        if (lineChart.getVisibility() == View.GONE) {
            lineChart.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);
            chartIndex = 0;
        } else {
            barChart.setVisibility(View.VISIBLE);
            lineChart.setVisibility(View.GONE);
            chartIndex = 1;
        }
        if (chartIndex == 0) {
            initChartForLine();
        } else {
            initChartForBar();
        }
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
            if (chartIndex == 0) {
                initChartForLine();
            } else {
                initChartForBar();
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("选择")//确认按钮文字
                .setRangDate(startDate, endDate)
                .build();
    }

    /**
     * 获取数据 类型 柱状图
     */
    private void initChartForBar() {
        CustomMap dataMap = types.get(typeIndex);
        barChart.setDescription(dataMap.getToString("name"));
        // 没有数据的时候，显示“暂无数据”
        barChart.setNoDataText("暂无数据");
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDrawGridBackground(false); // 不显示表格颜色
        barChart.setDrawBarShadow(false);
        //设置是否可以拖拽
        barChart.setDragEnabled(true);
        Legend l = barChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        ArrayList<BarEntry> barEntryList = new ArrayList<>();
        List<String> barStringLab = new ArrayList<>();
        String startTime = mMainSelectStartTime.getText().toString();
        String endTime = mMainSelectEndTime.getText().toString();

        requestData(startTime, endTime, list -> {
            if (CollectionUtils.isEmpty(list)) {
                BarData data = new BarData(new ArrayList<>(), new ArrayList<>());
                barChart.setData(data);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }
            int k = 0;
            for (IndexDataVo vo : list) {
                try {
                    Field field = vo.getClass().getDeclaredField(dataMap.getToString("field"));
                    field.setAccessible(true);
                    Double val = (Double) field.get(vo);
                    BarEntry entry = new BarEntry(Float.parseFloat(val.toString()), k++);
                    barEntryList.add(entry);
                    barStringLab.add(new DateTime(vo.getCreateTime().getTime()).toString("yyyy/MM/dd"));


                    //设置数据
                    BarDataSet dataSet = new BarDataSet(barEntryList, "");
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    dataSet.setBarSpacePercent(25f);
                    BarData data = new BarData(barStringLab, dataSet);
                    data.setValueTextSize(20f);
                    barChart.setData(data);
                    barChart.animateXY(300, 300);
                    barChart.notifyDataSetChanged();
                    barChart.invalidate();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置数据折线图的
     */
    private void initChartForLine() {
        CustomMap dataMap = types.get(typeIndex);
        lineChart.setDescription(dataMap.getToString("name"));
        // 没有数据的时候，显示“暂无数据”
        lineChart.setNoDataText("暂无数据");
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.setDrawGridBackground(false); // 不显示表格颜色

        YAxis axisLeft = lineChart.getAxisLeft();
//        axisLeft.setDrawGridLines(false);
        axisLeft.setAxisMaxValue(4000);

        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisLeft.setAxisMaxValue(1000f);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //将横轴的显示位置为下方显示
//        xAxis.setGridColor(Color.TRANSPARENT); //去掉网格的竖线显示

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);   //非常重要的  去掉下方的颜色方块13083278337


        List<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();
        String startTime = mMainSelectStartTime.getText().toString();
        String endTime = mMainSelectEndTime.getText().toString();
        requestData(startTime, endTime, list -> {
            if (CollectionUtils.isEmpty(list)) {
                LineData data = new LineData(new ArrayList<>(), new ArrayList<>());
                lineChart.setData(data);
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
            }
            int k = 0;
            for (IndexDataVo vo : list) {
                try {
                    Field field = vo.getClass().getDeclaredField(dataMap.getToString("field"));
                    field.setAccessible(true);
                    Double val = (Double) field.get(vo);
                    Entry entry = new Entry(Float.parseFloat(val.toString()), k++);
                    yValues.add(entry);
                    xValues.add(new DateTime(vo.getCreateTime().getTime()).toString("yyyy/MM/dd"));


                    //设置数据
                    LineDataSet dataSet = new LineDataSet(yValues, "");
                    dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                    LineData data = new LineData(xValues, dataSet);
                    data.setValueTextSize(20f);
                    lineChart.setData(data);
                    lineChart.animateXY(300, 300);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void setTile() {
        super.title = "首页";
    }

    @Override
    protected void setLayoutId() {
        super.root = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
    }


    /**
     * 请求数据
     *
     * @param startTime
     * @param endTime
     * @param callBackData
     */
    private void requestData(String startTime, String endTime, CallBackData callBackData) {
        CustomMap customMap = CustomMap.create();
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            customMap.put("startTime", startTime).put("endTime", endTime);
        }
        //请求获取数据
        HttpUtil.doGet("/index/getDataList", customMap, new HttpCallBack() {
            @Override
            public void success(String json, Response response) {
                List<IndexDataVo> dataList = JSONObject.parseArray(json, IndexDataVo.class);
                callBackData.onCall(dataList);
            }

            @Override
            public void error(String error, Response response, IOException e) {
                ToastUtil.ShowShort(MainActivity.this, error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_select_endTime:
                pvTime.show(v);
                break;
            case R.id.main_select_startTime:
                pvTime.show(v);
                break;
            case R.id.main_create:
                startActivity(new Intent(this, CreateActivity.class));
                break;
            case R.id.main_log:
                startActivity(new Intent(this, ShowLogActivity.class));
                break;
        }
    }

    /**
     * 回调数据接口
     */
    private interface CallBackData {
        void onCall(List<IndexDataVo> list);
    }

}
