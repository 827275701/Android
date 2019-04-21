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

    TextView tv_name;  //姓名
    TextView tv_motto;  //座右铭

    String t_name = null;    //姓名
    String t_job_number = null;   //工号
    String t_sex = null;   //性别
    String t_age = null;   //年龄
    String t_hiredate = null;  //入职时间
    String t_birthday = null; //生日
    String t_motto = null; //座右铭

    String m_name = null;    //姓名
    String m_motto = null; //座右铭

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

        room1.setOnClickListener(new ButtonClickListener1());
        room2.setOnClickListener(new ButtonClickListener2());
        exitLogin.setOnClickListener(new ButtonClickListener_ExitLogin());
        personInfo.setOnClickListener(new ButtonClickListener_PersonInfo());

        //从mainActivity的意图中获取登录的账号
        getUesrname();
        System.out.println(username);

        // 实例化主线程,用于更新接收过来的消息
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        m_name = t_name;
                        m_motto = t_motto;
                        tv_name.setText(m_name);
                        tv_motto.setText(m_motto);
                        break;
                }
            }
        };
        getPersonInfo();
    }

    class ButtonClickListener1 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String where = room1.getText().toString(); //获取Button上的信息传递给下一个界面标题处的TestView

            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(ChooseSpot.this, NowData.class);
            Iret.putExtra("where", where);

            ChooseSpot.this.startActivity(Iret);//启动意图
            //ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener2 implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String where = room2.getText().toString(); //获取Button上的信息传递给下一个界面标题处的TestView

            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(ChooseSpot.this, NowData.class);
            Iret.putExtra("where", where);
            ChooseSpot.this.startActivity(Iret);//启动意图
            //ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    //个人信息
    class ButtonClickListener_PersonInfo implements View.OnClickListener {
        String msg;
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", username);
            IToInfo.setClass(ChooseSpot.this, PersonInfo.class);
            ChooseSpot.this.startActivity(IToInfo);//启动意图
            //ChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    private void getUesrname() {
        Intent  i = this.getIntent();
        username = i.getStringExtra("username");
    }

    private void getPersonInfo() {
        //发送HTTP请求去服务其数据库获取用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg;
                    //1、连接服务器，并请求对应的资源
                    MyHttp myHttp = new MyHttp();
                    HttpURLConnection conn = myHttp.connect("person_info", username);  //连接服务器
                    System.out.println(conn);

                    //2、拿到服务器的回复保存到字符串里
                    msg = myHttp.get_response();
                    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(msg.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));;
                    t_name = br.readLine();
                    t_job_number = br.readLine();
                    t_sex = br.readLine();
                    t_age = br.readLine();
                    t_hiredate = br.readLine();
                    t_birthday = br.readLine();
                    t_motto = br.readLine();

                    // 步骤3:通知主线程,将接收的消息显示到界面
                    Message msg_ret_to_main = Message.obtain();
                    msg_ret_to_main.what = 0;
                    mMainHandler.sendMessage(msg_ret_to_main);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
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

    //退出登录
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



