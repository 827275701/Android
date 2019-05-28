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
    EditText Ename;  //用来关联username
    EditText Epwd;   //用来关联password

    String username;  //用来保存从Ename中获取的username
    String password;  //用来保存从Epwd中获取的password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.login);  //启动layout

        //将对象与UI关联
        button_login = (Button) findViewById(R.id.Blogin);
        Ename = (EditText) findViewById(R.id.name);
        Epwd = (EditText) findViewById(R.id.pwd);

        //设置登录按钮监听
        button_login.setOnClickListener(new ButtonClickListener());
    }

    class ButtonClickListener implements View.OnClickListener {
        /*
            功能：登录按钮监听函数
            基本逻辑：
                1、获取UI中输入的username和password
                2、校验username和password
                3、校验通过之后，创建网络连接，请求服务器
                4、获取服务器返回结果，判断是否登录成功
                5、判断是普通管理员还是admin
                6、跳转界面
         */
        @Override
        public void onClick(View v) {
            username = Ename.getText().toString();//获得账号字符串
            password = Epwd.getText().toString();//获得密码字符串
            if (check_uaername_password(username, password)){
                new Thread(new Runnable() {
                    //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                    @Override
                    public void run() {
                        try {
                            //设置POST请求的body
                            String postBody = "username=" + username+"&password="+password;
                            MyHttp myHttp = new MyHttp();
                            Response response = myHttp.connect("login", postBody);
                            if (response.isSuccessful()) {  //如果返回200 OK
                                String res = response.body().string();
                                System.out.println(res);
                                if(res.equals("Successful")) {   //如果返回body的内容是“yes”
                                    //if(true) {
                                    //登录成功
                                    Intent Ilogin = new Intent();   //创建有一个 Intent对象，并指定启动程序Login

                                    if(username.equals("admin")) {
                                        Ilogin.setClass(MainActivity.this, AdminMain.class);
                                        MainActivity.this.startActivity(Ilogin); //启动意图
                                        MainActivity.this.finish();  //关闭MainActivity
                                    } else {
                                        //Ilogin.setClass(MainActivity.this, ChooseSpot.class);
                                        Ilogin.setClass(MainActivity.this, ChooseSpot.class);
                                        Ilogin.putExtra("username", username);
                                        MainActivity.this.startActivity(Ilogin); //启动意图
                                        MainActivity.this.finish();  //关闭MainActivity
                                    }
                                } else {
                                    //登录失败
                                    Looper.prepare();
                                    Toast t = Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT);
                                    t.show();
                                    Looper.loop();
                                }
                            } else{
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                        }catch (Exception e) {
                            //显示服务器未运行
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "服务器维护中", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                            e.printStackTrace();
                        }

                    }
                }).start();
            }
        }
    }

    public boolean check_uaername_password(String username, String password) {
        //检查用户名和密码的合法性
        CheckOut checkOut = new CheckOut();
        if(checkOut.check_username(username) == false) {
            Toast t = Toast.makeText(getApplicationContext(), "请输入正确的工号", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        if(checkOut.check_password(password) == false) {
            Toast t = Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }

        return true;
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

