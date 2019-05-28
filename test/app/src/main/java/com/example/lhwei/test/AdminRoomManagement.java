package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminRoomManagement extends AppCompatActivity {
    Button Bchoose_spot;
    Button Bselect_rooms;
    Button Bupdate_room_info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_room_management);

        Bchoose_spot = (Button) findViewById(R.id.Badmin_choose_spot);
        Bselect_rooms = (Button) findViewById(R.id.Badmin_select_room);
        Bupdate_room_info = (Button) findViewById(R.id.Badmin_update_room_info);

        Bchoose_spot.setOnClickListener(new ButtonClickListener_Choose_spot());
        Bselect_rooms.setOnClickListener(new ButtonClickListener_Select_rooms());
        Bupdate_room_info.setOnClickListener(new ButtonClickListener_Update_room_info());

    }

    //查看室内信息按钮监听函数
    class ButtonClickListener_Choose_spot implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.setClass(AdminRoomManagement.this, AdminChooseSpot.class);
            AdminRoomManagement.this.startActivity(IToInfo);//启动意图
            AdminRoomManagement.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_Select_rooms implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.setClass(AdminRoomManagement.this, AdminSelectRoom.class);
            AdminRoomManagement.this.startActivity(IToInfo);//启动意图
            AdminRoomManagement.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_Update_room_info implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.setClass(AdminRoomManagement.this, AdminUpdateRoomInfo.class);
            AdminRoomManagement.this.startActivity(IToInfo);//启动意图
            AdminRoomManagement.this.finish(); //关闭当前Activity
        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminRoomManagement.this, AdminMain.class);
            AdminRoomManagement.this.startActivity(Iret);
            AdminRoomManagement.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
