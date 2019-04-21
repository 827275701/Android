package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhwei on 2019/1/22.
 */
public class GHistory extends Activity {
    Button ret;
    String where;  //哪里？  大厅？   博览室？
    String content;  //那一项内容？   温度？  湿度？ 有害气体浓度？

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_history);
        ret = (Button) findViewById(R.id.BTret);
        ret.setOnClickListener(new ButtonClickListener());

        setTitle();//设置标题  xx的室内xx情况

        showChart();  //显示LineChart

        showData_G();
    }

    //返回键的监听函数
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(GHistory.this, NowData.class);
            Iret.putExtra("where", where);
            GHistory.this.startActivity(Iret);
            GHistory.this.finish();
        }
    }

    //显示图表
    private void showChart() {
        //向LineChart插入数据
        LineChart lc = (LineChart) findViewById(R.id.Tchart);
        // 1. 获取一或多组Entry对象集合的数据
        // 模拟数据1
        List<Entry> yVals1 = new ArrayList<>();
        float[] ys1 = new float[]{22f, 24f, 25f, 25f, 25f, 22f};
        for (int i = 0; i < ys1.length; i++) {
            yVals1.add(new Entry(i, ys1[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "最高温度");
        // 3. 将每一组折线数据集添加到折线数据中
        LineData lineData = new LineData(lineDataSet1);
        // 4. 将折线数据设置给图表
        lc.setData(lineData);
    }

    //设置标题
    private void setTitle() {

        TextView title = (TextView) findViewById(R.id.THTitle);
        Intent i = this.getIntent();

        where = i.getStringExtra("where");
        content = i.getStringExtra("content");
        title.setText(where + "的历史" + content);
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(GHistory.this, NowData.class);
            Iret.putExtra("where", where);
            GHistory.this.startActivity(Iret);
            GHistory.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showData_G() {

        new Thread(new Runnable() {
            //创建有一个 Intent对象，并指定启动程序Login
            Intent Ilogin = new Intent();

            @Override
            public void run() {
                //连接服务器   HTTP协议
                String urlStr = "http://101.200.63.71:8080/historyData_G";  //服务器地址+端口号+访问程序

                try {
                    URL url = new URL(urlStr); //创建URL对象
                    System.out.println("111111111111111111111111");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //获取HttpURLConnection连接（尝试连接服务器）
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.connect();
                    System.out.println("222222222222222222222");
                    System.out.println(conn);
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {   //判断 获取到的状态码是不是HTTP_OK， HTTP服务器在连接成功是返回的状态码是200 OK
                        System.out.println("33333333333333333333");
                        //获取服务器发送的数据并显示到Activity上
                    } else {
//                            Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
//                            t.show();
                        System.out.println("4444444444444444444444444444");
                    }

                    conn.disconnect();//关闭HTTP连接
                } catch (Exception e) {
                    //1、提示服务器未运行 2、并返回到ChooseSpot

//                        Toast t = Toast.makeText(getApplicationContext(), "服务器维护中!", Toast.LENGTH_SHORT);
//                        t.show();
                    e.printStackTrace();
                    System.out.println("5555555555555555555555555");
                }
            }
        }).start();

    }
}
