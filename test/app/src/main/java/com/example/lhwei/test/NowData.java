package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lhwei on 2019/1/21.
 */
public class NowData extends Activity {
    Button ret;
    Button t_history;
    Button h_history;
    Button g_history;
    TextView title;

    String where = null;  //哪里？  大厅？   博览室？
    String username = null;
    String content;  //那一项内容？   温度？  湿度？ 有害气体浓度？

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_data);

        ret = (Button)findViewById(R.id.Bret1);
        t_history = (Button)findViewById(R.id.Bt_history);
        h_history = (Button)findViewById(R.id.Bh_history);
        g_history = (Button)findViewById(R.id.Bg_history);

        ret.setOnClickListener(new ButtonClickListener_R());
        t_history.setOnClickListener(new ButtonClickListener_T());
        h_history.setOnClickListener(new ButtonClickListener_H());
        g_history.setOnClickListener(new ButtonClickListener_G());

        title = (TextView) findViewById(R.id.Ttitle);
        Intent i = this.getIntent();//获取当前意图
        username = i.getStringExtra("username");
        where = i.getStringExtra("where");
        title.setText(where + "当前状态"); //获取意图内容并设置标题

        showData();
    }

    //返回选择场景界面
    class ButtonClickListener_R implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(NowData.this,ChooseSpot.class);
            NowData.this.startActivity(Iret);
            NowData.this.finish();
        }
    }

    //查看当前室内历史温度
    class ButtonClickListener_T implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(NowData.this,THistory.class);
            Iret.putExtra("where", where);
            Iret.putExtra("content","温度");
            NowData.this.startActivity(Iret);
            NowData.this.finish();
        }
    }

    //查看当前室内历史湿度
    class ButtonClickListener_H implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(NowData.this, HHistory.class);
            Iret.putExtra("where", where);
            Iret.putExtra("content", "湿度");
            NowData.this.startActivity(Iret);
            NowData.this.finish();
        }
    }

    //查看当前室内历史有害气体浓度
    class ButtonClickListener_G implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(NowData.this, GHistory.class);
            Iret.putExtra("where", where);
            Iret.putExtra("content","有害气体浓度");
            NowData.this.startActivity(Iret);
            NowData.this.finish();
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(NowData.this,ChooseSpot.class);
            Iret.putExtra("where", where);
            NowData.this.startActivity(Iret);
            NowData.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=" + username + "&where=" + where;
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("now_data", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        String res_body = response.body().string();
                        System.out.println("==== nowData start ====");
                        System.out.println(res_body);
                        System.out.println("==== nowData end ====");
                    } else {
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
