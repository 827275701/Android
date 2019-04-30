package com.example.lhwei.test;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/4/27.
 */
public class AdminAddUser extends AppCompatActivity {
    EditText username;
    EditText no;
    EditText sex;
    EditText phone;
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user);

        username = (EditText) findViewById(R.id.Tadmin_adduser_name);
        no = (EditText) findViewById(R.id.Tadmin_adduser_no);
        sex = (EditText) findViewById(R.id.Tadmin_adduser_sex);
        phone = (EditText) findViewById(R.id.Tadmin_adduser_phone);
        submit = (Button) findViewById(R.id.Badmin_adduser_submit);


        submit.setOnClickListener(new ButtonClickListener_submit());   //用户管理按键监听

    }

    class ButtonClickListener_submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //连接服务器

            //查看返回码

            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                MyHttp myHttp = new MyHttp();
                String postBody = "username=admin" + "&name=" + username.getText().toString() +
                        "&no=" + no.getText().toString() + "&sex=" + no.toString().toString() +
                        "&phone=" + phone.toString().toString();
                @Override
                public void run() {
                    try {
                        Response response = myHttp.connect("add_user", postBody);
                        if (response.isSuccessful()) {  //如果返回200 OK
                            //成功 返回好AdminUserManagement界面
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "添加管理员成功", Toast.LENGTH_SHORT);
                            t.show();

                            Intent Iret = new Intent();  //创建意图
                            Iret.setClass(AdminAddUser.this, AdminUserManagement.class);
                            AdminAddUser.this.startActivity(Iret);//启动意图
                            AdminAddUser.this.finish(); //关闭当前Activity
                            Looper.loop();
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "添加管理员失败", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                        }
                    } catch (IOException e) {
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminAddUser.this, AdminUserManagement.class);
            AdminAddUser.this.startActivity(Iret);
            AdminAddUser.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
