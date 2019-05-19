package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminUserManagement extends AppCompatActivity {
    Button add_user;
    Button delete_user;
    Button select_user;
//    Button change_other_password;
//    Button change_other_name;
//    Button change_other_no;
//    Button change_other_sex;
    Button change_other_info;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user_management);

        add_user = (Button) findViewById(R.id.Badmin_add_user);
        delete_user = (Button) findViewById(R.id.Badmin_delete_user);
        select_user = (Button) findViewById(R.id.Badmin_select_user);
//        change_other_password = (Button) findViewById(R.id.Badmin_change_other_passeord);
//        change_other_name = (Button) findViewById(R.id.Badmin_change_other_name);
//        change_other_no = (Button) findViewById(R.id.Badmin_change_other_no);
//        change_other_sex = (Button) findViewById(R.id.Badmin_change_other_sex);
        change_other_info = (Button) findViewById(R.id.Badmin_change_other_info);


        add_user.setOnClickListener(new ButtonClickListener_add_user());   //添加用户按键监听
        delete_user.setOnClickListener(new ButtonClickListener_delete_user());   //添加用户按键监听
        select_user.setOnClickListener(new ButtonClickListener_select_user());   //添加用户按键监听
//        change_other_password.setOnClickListener(new ButtonClickListener_change_other_password());
//        change_other_name.setOnClickListener(new ButtonClickListener_change_other_name());
//        change_other_no.setOnClickListener(new ButtonClickListener_change_other_no());
//        change_other_sex.setOnClickListener(new ButtonClickListener_change_other_sex());
        change_other_info.setOnClickListener(new ButtonClickListener_change_other_info());   //添加用户按键监听

    }

    class ButtonClickListener_add_user implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminUserManagement.this, AdminAddUser.class);
            AdminUserManagement.this.startActivity(Iret);//启动意图
            AdminUserManagement.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_delete_user implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminUserManagement.this, AdminDeleteUser.class);
            AdminUserManagement.this.startActivity(Iret);//启动意图
            AdminUserManagement.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_select_user implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminUserManagement.this, AdminSelectUser.class);
            AdminUserManagement.this.startActivity(Iret);//启动意图
            AdminUserManagement.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_change_other_info implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminUserManagement.this, AdminChangeOtherInfo.class);
            AdminUserManagement.this.startActivity(Iret);//启动意图
            AdminUserManagement.this.finish(); //关闭当前Activity
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminUserManagement.this, AdminMain.class);
            AdminUserManagement.this.startActivity(Iret);
            AdminUserManagement.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
