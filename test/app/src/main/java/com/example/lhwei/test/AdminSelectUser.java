package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Looper;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bin.david.form.annotation.SmartTable;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhwei on 2019/5/14.
 */

public class AdminSelectUser extends Activity {
    //TextView users;

    ListView Lusers;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_select_user);
        //users = (TextView) findViewById(R.id.Tselectuser_users);

        Lusers = (ListView) findViewById(R.id.Ladmin_select_user_users);
        Lusers.setCacheColorHint(666666);

        get_users();  //查询的用户列表中不包括admin
    }

    private void get_users(){
        new Thread(new Runnable() {
            //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
            MyHttp myHttp = new MyHttp();
            String postBody = "username=admin";
            @Override
            public void run() {
                try {
                    Response response = myHttp.connect("select_user", postBody);
                    final String res_body = response.body().string();

                    System.out.println("==== Select Users start ====");
                    if (response.isSuccessful()) {  //如果返回200 OK
                        System.out.println(res_body);
                        if(res_body.equals("Nobody")) {
                            //提示此时还没有用户
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "目前还没有任何用户", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                        } else {
                            //取出用户信息保存到info的字符串数组
                            String[] buff = res_body.split("[&]");

                            int length = buff.length/4;
                            final String[] users = new String[length];
                            users[0] = "";
                            int j = 0;
                            for(int i = 1; i <= buff.length; ++i){
                                String[] info;
                                info = buff[i-1].split("[=]");
                                users[j] = users[j] + "       " + info[1];
                                if(i%4==0 && j<length-1) {
                                    ++j;
                                    users[j] = "";
                                }
                            }


                            //显示到UI上
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新UI
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdminSelectUser.this,android.R.layout.simple_list_item_1,users);//listdata和str均可
                                    Lusers.setAdapter(arrayAdapter);
                                }

                            });
                        }
                        System.out.println("==== Select Users end ====");
                    } else {
                        //失败 提示失败信息，页面不跳转
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "查询管理员失败", Toast.LENGTH_SHORT);
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

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminSelectUser.this, AdminUserManagement.class);
            AdminSelectUser.this.startActivity(Iret);
            AdminSelectUser.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

