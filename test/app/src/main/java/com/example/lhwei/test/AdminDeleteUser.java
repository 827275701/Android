package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/14.
 */
public class AdminDeleteUser extends Activity {
    EditText username;
    EditText no;
    Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_delete_user);

        username = (EditText) findViewById(R.id.Tadmin_deleteuser_name);
        no = (EditText) findViewById(R.id.Tadmin_deleteuser_no);
        submit = (Button) findViewById(R.id.Badmin_deleteuser_submit);

        submit.setOnClickListener(new ButtonClickListener_submit());   //用户管理按键监听
    }


    class ButtonClickListener_submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                MyHttp myHttp = new MyHttp();
                String postBody = "username=admin" + "&name=" + username.getText().toString() +
                        "&no=" + no.getText().toString();
                @Override
                public void run() {
                    try {
                        Response response = myHttp.connect("delete_user", postBody);
                        String res = response.body().string();

                        System.out.println("==== Change Password start ====");
                        if (response.isSuccessful()) {  //如果返回200 OK
                            System.out.println(res);
                            if(res.equals("Successful")){
                                System.out.println("==== Successful ====");
                                //成功 返回好AdminUserManagement界面
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "删除管理员成功", Toast.LENGTH_SHORT);
                                t.show();
                                Intent Iret = new Intent();  //创建意图
                                Iret.setClass(AdminDeleteUser.this, AdminUserManagement.class);
                                AdminDeleteUser.this.startActivity(Iret);//启动意图
                                AdminDeleteUser.this.finish(); //关闭当前Activity
                                Looper.loop();
                            } else {
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "姓名与工号不匹配", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                            System.out.println("==== Change Password end ====");
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "删除管理员失败", Toast.LENGTH_SHORT);
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
            Iret.setClass(AdminDeleteUser.this, AdminUserManagement.class);
            AdminDeleteUser.this.startActivity(Iret);
            AdminDeleteUser.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
