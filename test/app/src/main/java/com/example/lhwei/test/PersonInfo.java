package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
 * Created by lhwei on 2019/2/1.
 */
public class PersonInfo extends Activity {
    Button ret;
    TextView Tname;
    TextView Tjob_number;
    TextView Tsex;
    TextView Tage;
    TextView Thiredate;
    TextView Tbirthday;
    TextView Tmotto;

    String username = null;

    String name = null;    //姓名
    String job_number = null;   //工号
    String sex = null;   //性别
    String age = null;   //年龄
    String hiredate = null;  //入职时间
    String birthday = null; //生日
    String motto = null; //座右铭

    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);


        //从mainActivity的意图中获取登录的账号
        Intent  i = this.getIntent();
        username = i.getStringExtra("username");
        System.out.println("--------------" + username);

        ret = (Button)findViewById(R.id.Bperson_info_ret);
        Tname = (TextView)findViewById(R.id.Tinfo_name);
        Tjob_number = (TextView)findViewById(R.id.Tinfo_job_number);
        Tsex = (TextView)findViewById(R.id.Tinfo_sex);
        Tage = (TextView)findViewById(R.id.Tinfo_age);
        Thiredate = (TextView)findViewById(R.id.Tinfo_hiredate);
        Tbirthday = (TextView)findViewById(R.id.Tinfo_birthday);
        Tmotto = (TextView)findViewById(R.id.Tinfo_motto);

        ret.setOnClickListener(new ButtonClickListener_PersonInfoRet());


        // 实例化主线程,用于更新接收过来的消息
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Tname.setText(name);
                        Tjob_number.setText(job_number);
                        Tsex.setText(sex);
                        Tage.setText(age);
                        Thiredate.setText(hiredate);
                        Tbirthday.setText(birthday);
                        Tmotto.setText(motto);
                        break;
                }
            }
        };


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

                    name = br.readLine();
                    job_number = br.readLine();
                    sex = br.readLine();
                    age = br.readLine();
                    hiredate = br.readLine();
                    birthday = br.readLine();
                    motto = br.readLine();

                    // 步骤4:通知主线程,将接收的消息显示到界面
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
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(PersonInfo.this, ChooseSpot.class);
            PersonInfo.this.startActivity(Iret);//启动意图
            //PersonInfo.this.finish(); //关闭当前Activity
        } else {
            finish();
            System.exit(0);
        }
    }


    class ButtonClickListener_PersonInfoRet implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(PersonInfo.this, ChooseSpot.class);
            PersonInfo.this.startActivity(Iret);//启动意图
            //PersonInfo.this.finish(); //关闭当前Activity
        }
    }
}
