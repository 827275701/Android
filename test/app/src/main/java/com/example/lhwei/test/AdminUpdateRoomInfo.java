package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

/**
 * Created by lhwei on 2019/5/26.
 */
public class AdminUpdateRoomInfo extends Activity{

    String room_name = new String();
    String room_id = new String();

    TextView Troom_id;
    EditText Troom_name;
    EditText Troom_pos;
    EditText Troom_description;
    Button Bsubmit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_room_info);

        Intent i = this.getIntent();
        room_name = i.getStringExtra("room_name");
        room_id = i.getStringExtra("room_id");

        Troom_id = (TextView) findViewById(R.id.Tadmin_change_room_info_no);
        Troom_name = (EditText) findViewById(R.id.Tadmin_change_room_info_name);
        Troom_pos = (EditText) findViewById(R.id.Tadmin_change_room_info_pos);
        Troom_description  = (EditText) findViewById(R.id.Tadmin_change_room_info_description);
        Bsubmit  = (Button) findViewById(R.id.Badmin_change_other_info_submit);

        Troom_id.setText(room_name);

        get_the_room_info();

        Bsubmit.setOnClickListener(new ButtonClickListener());
        //url为change_room_info  返回Successful
    }

    public void get_the_room_info(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=admin&room_id=" + room_id;
                    System.out.println(postBody);
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("room_info", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        final String res = response.body().string();
                        if(res.equals("Failed")) {
                            System.out.println("==== Failed ====");
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "博览室信息获取失败", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();

                        } else {
                            System.out.println("==== Successful ====");

                            //处理服务器返回的信息，并显示到界面
                            final String[] buff = res.split("[&]");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新UI
                                    Troom_name.setText(buff[0].split("[=]")[1]);
                                    Troom_pos.setText(buff[2].split("[=]")[1]);
                                    Troom_description.setText(buff[4].split("[=]")[1]);
                                }

                            });
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

    class ButtonClickListener implements View.OnClickListener {    //登录按钮监听函数
        @Override
        public void onClick(View v) {
            final String n_name = Troom_name.getText().toString();
            final String n_pos = Troom_pos.getText().toString();
            final String n_des = Troom_description.getText().toString();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //设置POST请求的body
                        String postBody = "username=admin&room_id=" + room_id + "&room_name=" + n_name +
                                "&pos=" + n_pos + "&description=" + n_des;
                        System.out.println(postBody);
                        MyHttp myHttp = new MyHttp();
                        Response response = myHttp.connect("update_room", postBody);
                        if (response.isSuccessful()) {  //如果返回200 OK
                            String res = response.body().string();
                            System.out.println("res_bodt =====>" + res);

                            if(res.equals("Successful")) {
                                System.out.println("==== Successful ====");
                                //修改成功，提示成功，返回AdminRoomManagement
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "修改博览室信息成功", Toast.LENGTH_SHORT);
                                t.show();
                                Intent Iret = new Intent();  //创建意图
                                Iret.setClass(AdminUpdateRoomInfo.this, AdminSelectRoom.class);
                                AdminUpdateRoomInfo.this.startActivity(Iret);//启动意图
                                AdminUpdateRoomInfo.this.finish(); //关闭当前Activity
                                Looper.loop();

                            } else {
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "修改博览室信息失败", Toast.LENGTH_SHORT);
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

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminUpdateRoomInfo.this, AdminSelectRoom.class);
            AdminUpdateRoomInfo.this.startActivity(Iret);//启动意图
            AdminUpdateRoomInfo.this.finish(); //关闭当前Activity
        }
        return super.onKeyDown(keyCode, event);
    }
}
