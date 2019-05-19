package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhwei on 2019/1/17.
 */
public class TestChooseSpot extends Activity {
    //左边拓展框内容
    Button exitLogin;   //退出登录
    Button personInfo;  //个人信息
    Button chang_epassword; //修改密码
    TextView tv_name;  //姓名
    TextView tv_no;  //工号
    Spinner Srooms;

    String username = null; //登录账号
    String where = null;  //
    String[] rooms = null;

    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;


    // 主线程Handler
    // 用于将从服务器获取的消息显示出来
    private Handler mMainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_choose_spot);

        exitLogin = (Button)findViewById(R.id.Bexit_login);
        personInfo = (Button)findViewById(R.id.Bperson_info);
        chang_epassword = (Button)findViewById(R.id.Bchange_password);
        tv_name = (TextView)findViewById(R.id.Tname);
        tv_no = (TextView)findViewById(R.id.Tno);
        Srooms = (Spinner) findViewById(R.id.Schooser_spot);

        exitLogin.setOnClickListener(new ButtonClickListener_ExitLogin());   //退出按键监听
        personInfo.setOnClickListener(new ButtonClickListener_PersonInfo());   //个人信息按键监听
        chang_epassword.setOnClickListener(new ButtonClickListener_ChangePassword());

        //从mainActivity的意图中获取登录的账号
        Intent  i = this.getIntent();
        username = i.getStringExtra("username");

        getPersonInfo();

        get_room();
        System.out.println("博览室 rooms---》" + rooms[0]);


        set_room();

    }




    //个人信息按钮监听函数
    class ButtonClickListener_PersonInfo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", username);
            IToInfo.setClass(TestChooseSpot.this, PersonInfo.class);
            TestChooseSpot.this.startActivity(IToInfo);//启动意图
            TestChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_ChangePassword implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", username);
            IToInfo.setClass(TestChooseSpot.this, ChangePassword.class);
            TestChooseSpot.this.startActivity(IToInfo);//启动意图
            TestChooseSpot.this.finish(); //关闭当前Activity
        }
    }

    private void getPersonInfo() {
        //发送HTTP请求去服务其数据库获取用户的姓名和工号，并显示到对应的TextView
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=" + username;
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("choose_spot", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        final String res_body = response.body().string();

                        System.out.println("==== ChooseSpot start ====");
                        System.out.println("username--->" + username);
                        System.out.println(res_body);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                String[] buff = res_body.split("[&]");
                                tv_name.setText(buff[0].split("[=]")[1]);
                                tv_no.setText(buff[1].split("[=]")[1]);
                            }
                        });
                        System.out.println("==== ChooseSpot end ====");

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

    private void get_room() {
//发送HTTP请求去服务其数据库获取用户的姓名和工号，并显示到对应的TextView
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=" + username;
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("get_room", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        final String res_body = response.body().string();

                        System.out.println("==== Get room start ====");
                        System.out.println("username--->" + username);
                        System.out.println(res_body);

                        String[] buff = res_body.split("[&]");

                        //使用

                        System.out.println("==== Get room end ====");

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

    public void set_room() {
        data_list = new ArrayList<String>();
        data_list.add("北京");
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        Srooms.setAdapter(arr_adapter);
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
            Toast.makeText(TestChooseSpot.this, "再按一次退出", Toast.LENGTH_SHORT).show();
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
            Iret.setClass(TestChooseSpot.this, MainActivity.class);
            TestChooseSpot.this.startActivity(Iret);//启动意图
            TestChooseSpot.this.finish(); //关闭当前Activity
        }
    }
}