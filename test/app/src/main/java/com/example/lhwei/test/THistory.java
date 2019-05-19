package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.squareup.okhttp.Response;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhwei on 2019/1/21.
 */
public class THistory extends Activity {
    String where;  //哪里？  大厅？   博览室？
    String username;

    LineChart lc;

    //float[] ys1;
    float[] history_data_day = new float[24];
    float[] history_data_week = new float[12];
    float[] history_data_month = new float[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        lc = (LineChart) findViewById(R.id.Tchart);

        Intent i = this.getIntent();//获取当前意图
        username = i.getStringExtra("username");
        where = i.getStringExtra("where");

        setTitle();//设置标题  xx的室内xx情况

        int count = 1;
        while (count>0){
            //showChart(history_data_day);
            showData_T();   //获取并显示数据
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count--;
        }
    }

    private void setXAxis() {
        // X轴
        XAxis xAxis = lc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部
        xAxis.setDrawGridLines(false); // 不绘制网格线
        xAxis.setLabelCount(20); // 设置标签数量
        xAxis.setTextColor(Color.WHITE); // 文本颜色为灰色
        xAxis.setTextSize(12f); // 文本大小为12dp
        xAxis.setGranularity(3f); // 设置间隔尺寸
        xAxis.setAxisMinimum(0f); // 设置X轴最小值
        xAxis.setAxisMaximum(24f); // 设置X轴最大值
        // 设置标签的显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? "℃" : value == 63 ? "(S)" : value < 10 ? "0" + (int) value : (int) value + "";
            }
        });
    }

    private void setYAxis() {
        // 左边Y轴
        final YAxis yAxisLeft = lc.getAxisLeft();
        yAxisLeft.setAxisMaximum(40); // 设置Y轴最大值
        yAxisLeft.setAxisMinimum(-10); // 设置Y轴最小值
        yAxisLeft.setGranularity(2f); // 设置间隔尺寸
        yAxisLeft.setTextSize(12f); // 文本大小为12dp
        yAxisLeft.setTextColor(Color.WHITE); // 文本颜色为灰色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == yAxisLeft.getAxisMinimum() ? (int) value + "" : (int) value + "";
            }
        });
        // 右侧Y轴
        lc.getAxisRight().setEnabled(false); // 不启用
    }

    public void setChartData(float[] history_data) {
        // 1. 获取一或多组Entry对象集合的数据
        // 模拟数据1
        List<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < history_data.length; i++) {
            yVals1.add(new Entry(i, history_data[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "最高有害气体浓度");
        //lineDataSet1.setColor(Color.GREEN); // 设置折线为红色
        lineDataSet1.setDrawCircles(true);//在点上画圆 默认true
        //lineDataSet1.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
        lineDataSet1.setHighLightColor(Color.GREEN); // 设置点击某个点时，横竖两条线的颜色
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置为贝塞尔曲线
        lineDataSet1.setCubicIntensity(0.15f); // 强度
        lineDataSet1.setCircleColor(Color.WHITE); // 设置圆点为颜色
        lineDataSet1.setCircleRadius(5f);
        lineDataSet1.setLineWidth(1f); // 设置线宽为2

        // 3.将每一组折线数据集添加到折线数据中
        LineData lineData = new LineData(lineDataSet1);
        lineData.setDrawValues(false);
        // 4.将折线数据设置给图表
        lc.setData(lineData);
    }

    //显示图表
    private void showChart(float[] history_data) {
        //向LineChart插入数据
        setXAxis();
        setYAxis();
        setChartData(history_data);
    }

    private void setTitle() {
        TextView title = (TextView)findViewById(R.id.THTitle);
        Intent  i = this.getIntent();
        title.setText(where + "的历史温度");
    }

    //从服务器获取数据并显示到lineChart上
    private void showData_T() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("msg===>" + msg.obj.toString());
                String data = msg.obj.toString().split("[=]")[1];
                System.out.println("data ===>" + data);

                String[] buff = data.split("[&]");

                //String[] buff = res_body.split("[&]");
                //走到这里， Android已经拿到了历史数据，并把每一条都保存到了buff中
                //接下来要保存到history_data_xxx中，使其绘制成图表，并显示
                for(int i = 0; i<buff.length; ++i) {
                    history_data_day[i] = Float.parseFloat(buff[i]);
                }

                for(int i = 0; i<history_data_day.length; ++i) {
                    System.out.println(history_data_day[i]);
                }
                showChart(history_data_day);  //显示LineChart
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //设置POST请求的body
                    String postBody = "username=" + username + "&where=" + where + "&data=T";
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("history_day", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        String res_body = response.body().string();
                        System.out.println("==== THistory start ====");
                        //此时服务器的数据已经存到了res_body中
                        System.out.println("username--->" + username);
                        System.out.println("where--->" + where);
                        System.out.println(res_body);

                        //向主线程发送消息
                        Message msg = new Message();
                        msg.obj = res_body;
                        handler.sendMessage(msg);

                        System.out.println("==== THistory end ====");
                    } else {
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    //1、提示服务器未运行 2、并返回到ChooseSpot
                    Looper.prepare();
                    Toast t = Toast.makeText(getApplicationContext(), "数据不可访问", Toast.LENGTH_SHORT);
                    t.show();
                    Looper.loop();
                    Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
                    IToInfo.putExtra("username", username);
                    IToInfo.setClass(THistory.this, ChooseSpot.class);
                    THistory.this.startActivity(IToInfo);//启动意图
                    THistory.this.finish(); //关闭当前Activity
                }
            }
        }).start();

    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(THistory.this,NowData.class);
            Iret.putExtra("username", username);
            Iret.putExtra("where", where);
            THistory.this.startActivity(Iret);
            THistory.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
