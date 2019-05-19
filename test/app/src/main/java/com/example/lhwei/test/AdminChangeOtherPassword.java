package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/17.
 */
public class AdminChangeOtherPassword extends Activity{

    EditText Eno;
    EditText Enew_password;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_other_password);

        Eno = (EditText) findViewById(R.id.Echange_other_password_no);
        Enew_password = (EditText) findViewById(R.id.Echange_other_password_new_password);
        submit = (Button) findViewById(R.id.Bchange_other_password_submit);

        submit.setOnClickListener(new ButtonClickListener_submit());   //用户管理按键监听
    }

    class ButtonClickListener_submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                String no = Eno.getText().toString();
                String new_password =Enew_password.getText().toString();

                MyHttp myHttp = new MyHttp();
                String postBody = "username=admin" + "&no=" +  no +
                        "&new_password=" + new_password;
                @Override
                public void run() {
                    CheckOut checkOut = new CheckOut();

                    //检查工号格式
                    if(checkOut.check_no(no) == false) {
                        //提示 工号格式错误
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "工号格式错误", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                        return;
                    }

                    //检查输入的新密码格式
                    if(checkOut.check_password(new_password) == false) {
                        //提示密码格式不正确
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "密码格式错误", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                        return;
                    }

                    //如果工号格式和新密码格式都正确进行访问服务器
                    try {
                        Response response = myHttp.connect("change_other_password", postBody);
                        String res = response.body().string();

                        System.out.println("==== Change Other Password start ====");
                        if (response.isSuccessful()) {  //如果返回200 OK
                            System.out.println(res);
                            if(res.equals("Successful")){
                                System.out.println("==== Successful ====");
                                //成功 返回好AdminUserManagement界面
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT);
                                t.show();
                                Intent Iret = new Intent();  //创建意图
                                Iret.setClass(AdminChangeOtherPassword.this, AdminUserManagement.class);
                                AdminChangeOtherPassword.this.startActivity(Iret);//启动意图
                                AdminChangeOtherPassword.this.finish(); //关闭当前Activity
                                Looper.loop();
                            } else {
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "工号不存在", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                            System.out.println("==== Change Other Password end ====");
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT);
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
            Iret.setClass(AdminChangeOtherPassword.this, AdminUserManagement.class);
            AdminChangeOtherPassword.this.startActivity(Iret);
            AdminChangeOtherPassword.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
