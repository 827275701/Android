package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminLogManagement extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_log_management);
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminLogManagement.this, AdminMain.class);
            AdminLogManagement.this.startActivity(Iret);
            AdminLogManagement.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
