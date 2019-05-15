package com.example.lhwei.test;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
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
    Button t_history;
    Button h_history;
    Button g_history;
    TextView title;

    String where = null;  //哪里？  大厅？   博览室？
    String username = null;

    TextView now_t;
    TextView now_h;
    TextView now_g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.now_data);

        t_history = (Button)findViewById(R.id.Bt_history);
        h_history = (Button)findViewById(R.id.Bh_history);
        g_history = (Button)findViewById(R.id.Bg_history);
        now_t = (TextView)findViewById(R.id.Tnow_t);
        now_h = (TextView)findViewById(R.id.Tnow_h);
        now_g = (TextView)findViewById(R.id.Tnow_g);

        t_history.setOnClickListener(new ButtonClickListener_T());  //温度按钮监听
        h_history.setOnClickListener(new ButtonClickListener_H());  //湿度按钮监听
        g_history.setOnClickListener(new ButtonClickListener_G());  //有害气体浓度按钮监听

        title = (TextView) findViewById(R.id.Ttitle);

        Intent i = this.getIntent();//获取当前意图
        username = i.getStringExtra("username");
        where = i.getStringExtra("where");
        title.setText(where + "当前状态"); //获取意图内容并设置标题

        showData();

        // =============================== 通知栏显示代码==============================
        //创建通知栏管理工具
        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        //实例化通知栏构造器
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        //设置Builder
        //设置标题
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText("我是内容")
                        //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                        //设置通知时间
                .setWhen(System.currentTimeMillis())
                        //首次进入时显示效果
                .setTicker("我是测试内容")
                        //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
        // =============================== 通知栏显示代码 end ==============================
    }

    //查看当前室内历史温度
    class ButtonClickListener_T implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(NowData.this,THistory.class);
            Iret.putExtra("username", username);
            Iret.putExtra("where", where);
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
            Iret.putExtra("username", username);
            Iret.putExtra("where", where);
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
            Iret.putExtra("username", username);
            Iret.putExtra("where", where);
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
            Iret.putExtra("username", username);
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
                        final String res_body = response.body().string();

                        System.out.println("==== nowData start ====");
                        System.out.println(username);
                        System.out.println(res_body);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                String[] buff = res_body.split("[&]");
                                now_t.setText(buff[0].split("[=]")[1]);
                                now_h.setText(buff[1].split("[=]")[1]);
                                now_g.setText(buff[2].split("[=]")[1]);
                            }

                        });

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
