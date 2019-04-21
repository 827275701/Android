package com.example.lhwei.test;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;


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
                            final MediaType MEDIA_TYPE_MARKDOWN
                                    = MediaType.parse("text/x-markdown; charset=utf-8");

                            final OkHttpClient client = new OkHttpClient();
                            String postBody = "username=" + username+"&password="+password;

                            Request request = new Request.Builder()
                                    .url("http://101.200.63.71:9999/")
                                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                                    .build();

                            Response response = client.newCall(request).execute();

                            if (response.isSuccessful()) {
                                if(response.body().string().equals("yes")) {
                                System.out.println("=====================");
                                //if(true) {
                                    //登录成功
                                    Intent Ilogin = new Intent();   //创建有一个 Intent对象，并指定启动程序Login

                                    Ilogin.setClass(MainActivity.this, ChooseSpot.class);
                                    Ilogin.putExtra("username", username);
                                    MainActivity.this.startActivity(Ilogin); //启动意图
                                    MainActivity.this.finish();  //关闭MainActivity
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

}

