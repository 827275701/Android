package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.okhttp.Response;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhwei on 2019/1/21.
 */
public class THistory extends Activity {
    Button ret;
    String where;  //哪里？  大厅？   博览室？
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        ret = (Button) findViewById(R.id.BTret);
        ret.setOnClickListener(new ButtonClickListener());

        Intent i = this.getIntent();//获取当前意图
        username = i.getStringExtra("username");
        where = i.getStringExtra("where");

        setTitle();//设置标题  xx的室内xx情况

        showChart();  //显示LineChart

        showData_T();
    }

    //返回键的监听函数
    class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(THistory.this, NowData.class);
            Iret.putExtra("username", username);
            Iret.putExtra("where", where);
            THistory.this.startActivity(Iret);
            THistory.this.finish();
        }
    }

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

    private void setTitle() {
        TextView title = (TextView)findViewById(R.id.THTitle);
        Intent  i = this.getIntent();
        title.setText(where + "的历史温度");
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


    //从服务器获取数据并显示到lineChart上
    private void showData_T() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //设置POST请求的body
                    String postBody = "username=" + username + "&where=" + where + "&data=T";
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("history", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        String res_body = response.body().string();
                        System.out.println("==== THistory start ====");
                        //此时服务器的数据已经存到了res_body中
                        System.out.println("username--->" + username);
                        System.out.println("where--->" + where);
                        System.out.println(res_body);
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


}
