package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.UiThread;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/24.
 */
public class AdminChooseSpot extends Activity {
    Button t_history;
    Button h_history;
    Button g_history;
    Spinner Srooms;

    TextView now_t;
    TextView now_h;
    TextView now_g;

    String where = null;  //哪里？  大厅？   博览室？

//    String[] room_name = new String[2];
//    String[] room_id = new String[2];

    String[] room_name;
    String[] room_id;
    //原始string数组
    final String[] spinnerItems = {"博览室1","博览室2"};


    String res = new String();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_choose_spot);

        t_history = (Button)findViewById(R.id.Bt_admin_history);
        h_history = (Button)findViewById(R.id.Bh_admin_history);
        g_history = (Button)findViewById(R.id.Bg_admin_history);
        now_t = (TextView)findViewById(R.id.Tadmin_now_t);
        now_h = (TextView)findViewById(R.id.Tadmin_now_h);
        now_g = (TextView)findViewById(R.id.Tadmin_now_g);
        Srooms = (Spinner) findViewById(R.id.Sadmin_choose_spot_choose_room);


        t_history.setOnClickListener(new ButtonClickListener_T());  //温度按钮监听
        h_history.setOnClickListener(new ButtonClickListener_H());  //湿度按钮监听
        g_history.setOnClickListener(new ButtonClickListener_G());  //有害气体浓度按钮监听



        get_room();   //从服务器获取 博览室的name和id
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main res ====>" + res);

        get_room_name_and_room_id();  //将从服务器获取取得name和id  设置到对应的字符串数组中

        initSpinner();  //设置监听和数据
    }


    public void get_room_name_and_room_id() {
        String[] room_info = res.split("[&]");
        System.out.println("==== get_room_name_and_room_id ====");

        for(int i = 0; i < room_info.length; ++i) {
            System.out.println(room_info[i]);
        }

        room_name = new String[room_info.length/3];
        room_id = new String[room_info.length/3];

        int index = 0;
        for(int i = 0; i < room_info.length; i+=3) {
            room_id[index] = room_info[i].split("[=]")[1];
            room_name[index] = room_info[i + 1].split("[=]")[1];
            ++index;
        }

        System.out.println("==== get_room_name_and_room_id ====");
    }

    public void initSpinner() {
        Srooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       final int position, long id) {

                //((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(20);

                System.out.println(" 下拉列表监听 ");

                System.out.println(" position = " + position);

                where = room_name[position];

                //TODO 连接服务器
                new Thread(new Runnable() {
                    //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                    MyHttp myHttp = new MyHttp();
                    String postBody = "username=admin&where=" + room_id[position];
                    @Override
                    public void run() {
                        try {
                            Response response = myHttp.connect("now_data", postBody);
                            final String res_body = response.body().string();

                            System.out.println("==== Select Rooms start ====");
                            if (response.isSuccessful()) {  //如果返回200 OK
                                System.out.println(res_body);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //更新UI
                                        String[] buff = res_body.split("[&]");
                                        String t = buff[0].split("[=]")[1];
                                        String h = buff[1].split("[=]")[1];
                                        String g = buff[2].split("[=]")[1];

                                        if (new CheckOut().check_now_t(Float.parseFloat(t))) {
                                            now_t.setTextColor(Color.GREEN);  //设置为绿色
                                        } else {
                                            now_t.setTextColor(Color.RED);
                                        }

                                        if (new CheckOut().check_now_h(Float.parseFloat(h))) {
                                            now_h.setTextColor(Color.GREEN);
                                        } else {
                                            now_h.setTextColor(Color.RED);
                                        }

                                        if (new CheckOut().check_now_g(Float.parseFloat(g))) {
                                            now_g.setTextColor(Color.GREEN);
                                        } else {
                                            now_g.setTextColor(Color.RED);
                                        }
                                        now_t.setText(t);
                                        now_h.setText(h);
                                        now_g.setText(g);
                                    }

                                });

                            }else {
                                //失败 提示失败信息，页面不跳转
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "查询博览室失败", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                            System.out.println("==== Select Rooms end ====");
                        }catch (IOException e) {
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                            e.printStackTrace();
                        }

                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // TODO 将博览室name设置到spinner中
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, room_name);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        Srooms.setAdapter(spinnerAdapter);
    }


    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            res = msg.obj.toString();
        }
    };
    public void get_room() {
        //用来获取服务器内的博览室信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //设置POST请求的body
                    String postBody = "username=admin";
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("select_room", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        final String res_body = response.body().string();

                        System.out.println("==== Get Room start ====");
                        //此时服务器的数据已经存到了res_body中
                        System.out.println(res_body);

                        //向主线程发送消息
                        Message msg = new Message();
                        msg.obj = res_body;
                        handler.sendMessage(msg);

                        Looper.prepare();
                        res = res_body;
                        Looper.loop();
                        System.out.println("==== Get Room end ====");

                    } else {
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    //1、提示服务器未运行 2、并返回到ChooseSpot
                    Looper.prepare();
                    Toast t = Toast.makeText(getApplicationContext(), "数据不可访问", Toast.LENGTH_SHORT);
                    t.show();
                    Looper.loop();
                    Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
                    IToInfo.setClass(AdminChooseSpot.this, AdminRoomManagement.class);
                    AdminChooseSpot.this.startActivity(IToInfo);//启动意图
                    AdminChooseSpot.this.finish(); //关闭当前Activity
                }
            }
        }).start();
    }

    //查看当前室内历史温度
    class ButtonClickListener_T implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(AdminChooseSpot.this,THistory.class);
            Iret.putExtra("username", "admin");
            Iret.putExtra("where", where);
            AdminChooseSpot.this.startActivity(Iret);
            AdminChooseSpot.this.finish();
        }
    }

    //查看当前室内历史湿度
    class ButtonClickListener_H implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(AdminChooseSpot.this, HHistory.class);
            Iret.putExtra("username", "admin");
            Iret.putExtra("where", where);
            AdminChooseSpot.this.startActivity(Iret);
            AdminChooseSpot.this.finish();
        }
    }

    //查看当前室内历史有害气体浓度
    class ButtonClickListener_G implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();
            Iret.setClass(AdminChooseSpot.this, GHistory.class);
            Iret.putExtra("username", "admin");
            Iret.putExtra("where", where);
            AdminChooseSpot.this.startActivity(Iret);
            AdminChooseSpot.this.finish();
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminChooseSpot.this, AdminRoomManagement.class);
            AdminChooseSpot.this.startActivity(Iret);
            AdminChooseSpot.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
