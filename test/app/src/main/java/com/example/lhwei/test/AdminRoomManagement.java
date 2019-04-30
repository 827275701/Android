package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminRoomManagement extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_room_management);
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
