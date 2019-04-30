package com.example.lhwei.test;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;


public class MainActivity extends AppCompatActivity {
    Button button_login;
    EditText Ename;
    EditText Epwd;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.login);
        button_login = (Button) findViewById(R.id.Blogin);
        Ename = (EditText) findViewById(R.id.name);
        Epwd = (EditText) findViewById(R.id.pwd);
        button_login.setOnClickListener(new ButtonClickListener());
    }

    class ButtonClickListener implements View.OnClickListener {    //登录按钮监听函数
        @Override
        public void onClick(View v) {
            username = Ename.getText().toString();//获得账号字符串
            password = Epwd.getText().toString();//获得密码字符串
            if (username.equals("") || password.equals("")) {
                Toast t = Toast.makeText(getApplicationContext(), "账号或密码不能为空", Toast.LENGTH_SHORT);
                t.show();
            } else {
                new Thread(new Runnable() {
                    //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                    MyHttp myHttp = new MyHttp();
                    String msg;

                    @Override
                    public void run() {
                        try {
                            //设置POST请求的body
                            String postBody = "username=" + username+"&password="+password;
                            MyHttp myHttp = new MyHttp();
                            Response response = myHttp.connect("login", postBody);
                            if (response.isSuccessful()) {  //如果返回200 OK
                                if(response.body().string().equals("yes")) {   //如果返回body的内容是“yes”
                                //if(true) {
                                    //登录成功
                                    Intent Ilogin = new Intent();   //创建有一个 Intent对象，并指定启动程序Login

                                    if(username.equals("admin")) {
                                        Ilogin.setClass(MainActivity.this, AdminMain.class);
                                        MainActivity.this.startActivity(Ilogin); //启动意图
                                        MainActivity.this.finish();  //关闭MainActivity
                                    } else {
                                        Ilogin.setClass(MainActivity.this, ChooseSpot.class);
                                        Ilogin.putExtra("username", username);
                                        MainActivity.this.startActivity(Ilogin); //启动意图
                                        MainActivity.this.finish();  //关闭MainActivity
                                    }
                                } else {
                                    //登录失败
                                    Looper.prepare();
                                    Toast t = Toast.makeText(getApplicationContext(), "账号或密码错误!", Toast.LENGTH_SHORT);
                                    t.show();
                                    Looper.loop();
                                }
                            } else{
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                        }catch (Exception e) {
                            //显示服务器未运行
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "服务器维护中!", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                            e.printStackTrace();
                        }

                    }
                }).start();
            }

        }
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
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}

