package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminMain extends AppCompatActivity{

    Button admin_user;
    Button admin_room;
    Button admin_log;

    Button admin_exitLogin;   //退出登录
    Button admin_personInfo;  //个人信息
    Button admin_chang_epassword;    //修改密码
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        admin_user = (Button) findViewById(R.id.Badmin_user);
        admin_room = (Button) findViewById(R.id.Badmin_room);
        admin_log = (Button) findViewById(R.id.Badmin_log);
        admin_exitLogin = (Button)findViewById(R.id.Badmin_exit_login);
        admin_personInfo = (Button)findViewById(R.id.Badmin_person_info);
        admin_chang_epassword = (Button)findViewById(R.id.Badmin_change_password);


        admin_user.setOnClickListener(new ButtonClickListener_user());   //用户管理按键监听
        admin_room.setOnClickListener(new ButtonClickListener_room());   //博览室管理按键监听
        admin_log.setOnClickListener(new ButtonClickListener_log());   //日志管理按键监听

        admin_exitLogin.setOnClickListener(new ButtonClickListener_ExitLogin());   //退出按键监听
        admin_personInfo.setOnClickListener(new ButtonClickListener_PersonInfo());   //个人信息按键监听
        admin_chang_epassword.setOnClickListener(new ButtonClickListener_ChangePassword());

    }

    //个人信息按钮监听函数
    class ButtonClickListener_PersonInfo implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", "admin");
            IToInfo.setClass(AdminMain.this, PersonInfo.class);
            AdminMain.this.startActivity(IToInfo);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_ChangePassword implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent IToInfo = new Intent();  //创建意图, 用于跳转至用户信息界面
            IToInfo.putExtra("username", "admin");
            IToInfo.setClass(AdminMain.this, ChangePassword.class);
            AdminMain.this.startActivity(IToInfo);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }
    //退出登录安按钮监听
    class ButtonClickListener_ExitLogin implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminMain.this, MainActivity.class);
            AdminMain.this.startActivity(Iret);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_user implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminMain.this, AdminUserManagement.class);
            AdminMain.this.startActivity(Iret);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_room implements View.OnClickListener {
        @Override
        public void onClick(View v) {            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminMain.this, AdminRoomManagement.class);
            AdminMain.this.startActivity(Iret);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }

    class ButtonClickListener_log implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //创建有一个 Intent对象，并指定启动程序Iret
            Intent Iret = new Intent();  //创建意图
            Iret.setClass(AdminMain.this, AdminLogManagement.class);
            AdminMain.this.startActivity(Iret);//启动意图
            AdminMain.this.finish(); //关闭当前Activity
        }
    }

    //对返回键进行监听，两次返回键之间小于2秒，程序退出
    private long mExitTime;     //退出时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(AdminMain.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}
