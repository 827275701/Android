package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/26.
 */
public class AdminSelectRoom extends Activity{

    ListView Lrooms;

    String res = new String();

    String[] room_name;
    String[] room_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_select_rooms);

        Lrooms = (ListView) findViewById(R.id.Ladmin_select_room_rooms);

        get_rooms();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        get_room_name_and_room_id();

        initListView();
    }


    public void initListView() {
        Lrooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent Iret = new Intent();
                Iret.setClass(AdminSelectRoom.this, AdminUpdateRoomInfo.class);
                Iret.putExtra("room_name", room_name[position]);
                Iret.putExtra("room_id", room_id[position]);
                AdminSelectRoom.this.startActivity(Iret);
                AdminSelectRoom.this.finish();

            }
        });
    }

    public void get_room_name_and_room_id() {
        String[] room_info = res.split("[&]");
        System.out.println("==== get_room_name_and_room_id ====");

        for(int i = 0; i < room_info.length; ++i) {
            System.out.println(room_info[i]);
        }

        room_name = new String[room_info.length/2 + 1];
        room_id = new String[room_info.length/2 + 1];

        int index = 0;
        for(int i = 0; i < room_info.length; i+=3) {
            room_id[index] = room_info[i + 1].split("[=]")[1];
            room_name[index] = room_info[i].split("[=]")[1];
            ++index;
        }

        System.out.println("==== get_room_name_and_room_id ====");
    }

    private void get_rooms(){
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                res = msg.obj.toString();
            }
        };
        new Thread(new Runnable() {
            //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
            MyHttp myHttp = new MyHttp();
            String postBody = "username=admin";
            @Override
            public void run() {
                try {
                    Response response = myHttp.connect("select_room", postBody);
                    final String res_body = response.body().string();

                    System.out.println("==== Select Rooms start ====");
                    if (response.isSuccessful()) {  //如果返回200 OK
                        System.out.println(res_body);
                        if(res_body.equals("Noroom")) {
                            //提示此时还没有博览室
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "目前还没有任何博览室", Toast.LENGTH_SHORT);
                            t.show();
                            Looper.loop();
                        } else {
                            //取出用户信息保存到info的字符串数组
                            String[] buff = res_body.split("[&]");

                            int length = buff.length/3;
                            final String[] rooms = new String[length];
                            rooms[0] = "";
                            int j = 0;
                            for(int i = 1; i <= buff.length; ++i){
                                String[] info;
                                info = buff[i-1].split("[=]");
                                rooms[j] = rooms[j] + "                " + info[1];
                                if(i%3==0 && j<length-1) {
                                    ++j;
                                    rooms[j] = "";
                                }
                            }

                            //显示到UI上
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新UI
                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AdminSelectRoom.this,android.R.layout.simple_list_item_1,rooms);//listdata和str均可
                                    Lrooms.setAdapter(arrayAdapter);
                                }

                            });


                            //向主线程发送消息
                            Message msg = new Message();
                            msg.obj = res_body;
                            handler.sendMessage(msg);

                            Looper.prepare();
                            res = res_body;
                            Looper.loop();
                        }
                        System.out.println("==== Select Rooms end ====");
                    } else {
                        //失败 提示失败信息，页面不跳转
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "查询博览室失败", Toast.LENGTH_SHORT);
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
            Iret.setClass(AdminSelectRoom.this, AdminRoomManagement.class);
            AdminSelectRoom.this.startActivity(Iret);
            AdminSelectRoom.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
