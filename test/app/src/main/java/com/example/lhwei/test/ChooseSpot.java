package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

/**
 * Created by lhwei on 2019/1/17.
 */
public class ChooseSpot extends Activity {
    Button room1;
    Button room2;

    //左边拓展框内容
    Button exitLogin;   //退出登录
    Button personInfo;  //个人信息

    String username = null; //登录账号
    String where = null;  //

    TextView tv_name;  //姓名
    TextView tv_motto;  //座右铭

    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_spot);

        room1 = (Button)findViewById(R.id.Broom1);
        room2 = (Button)findViewById(R.id.Broom2);
        exitLogin = (Button)findViewById(R.id.Bexit_login);
        personInfo = (Button)findViewById(R.id.Bperson_info);

        tv_name = (TextView)findViewById(R.id.Tname);
        tv_motto = (TextView)findViewById(R.id.Tmotto);

        room1.setOnClickListener(new ButtonClickListener1());   //博览室1按键监听
        room2.setOnClickListener(new ButtonClickListener2());   //博览室2按键监听
        exitLogin.setOnClickListener(new ButtonClickListener_ExitLogin());   //退出按键监听
        personInfo.setOnClickListener(new ButtonClickListener_PersonInfo());   //个人信息按键监听

        //从mainActivity的意图中获取登录的账号
        Intent  i = this.getIntent();
        username = i.getStringExtra("username");

        getPersonInfo();
    }

    class ButtonClickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            where = room1.getText().toString(); //获取Button上的信息传递给下一个界面标题处的TestView


            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(ChooseSpot.this, NowData.class);
            Iret.putExtra("where", where);
            Iret.putExtra("username", username);
            ChooseSpot.this.startActivity(Iret);//启动意图
            ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            where = room2.getText().toString(); //获取Button上的信息传递给下一个界面标题处的TestView

            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(ChooseSpot.this, NowData.class);
            Iret.putExtra("where", where);
            Iret.putExtra("username", username);
            ChooseSpot.this.startActivity(Iret);//启动意图
            ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    //个人信息按钮监听函数
    class ButtonClickListener_PersonInfo implements View.OnClickListener {
        String msg;
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", username);
            IToInfo.setClass(ChooseSpot.this, PersonInfo.class);
            ChooseSpot.this.startActivity(IToInfo);//启动意图
            ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    private void getPersonInfo() {
        //发送HTTP请求去服务其数据库获取用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=" + username;
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("choose_spot", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        String res_body = response.body().string();
                        System.out.println("==== chooseSpot start ====");
                        System.out.println("username--->" + username);
                        System.out.println(res_body);
                        System.out.println("==== chooseSpot end ====");
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

    //对返回键进行监听，两次返回键之间小于2秒，程序退出
    private long mExitTime;     //退出时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(ChooseSpot.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //退出登录安按钮监听
    class ButtonClickListener_ExitLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(ChooseSpot.this, MainActivity.class);
            ChooseSpot.this.startActivity(Iret);//启动意图
            ChooseSpot.this.finish(); //关闭当前Activity
        }
    }
}



