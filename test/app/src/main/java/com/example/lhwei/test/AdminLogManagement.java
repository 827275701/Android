package com.example.lhwei.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

/**
 * Created by lhwei on 2019/4/26.
 */
public class AdminLogManagement extends AppCompatActivity {
    TextView logs;
    String[] buff = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_log_management);

        //关联Textview
        logs = (TextView) findViewById(R.id.Techo_log);
        //请求服务器

        get_log();
    }

    public void get_log(){
        new Thread(new Runnable() {
            //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody = "username=admin";
                    MyHttp myHttp = new MyHttp();
                    Response response = myHttp.connect("echo_log", postBody);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        String res = response.body().string();
                        buff = res.split("[\n]");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //更新UI
                                String log = "";
                                for (int i = buff.length-1; i >= 0 ; --i) {
                                    log += buff[i];
                                    log += '\n';
                                }
                                logs.setText(log);
                            }
                        });
                    } else{
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                    }
                }catch (Exception e) {
                    //显示服务器未运行
                    Looper.prepare();
                    Toast t = Toast.makeText(getApplicationContext(), "服务器维护中", Toast.LENGTH_SHORT);
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
            Iret.setClass(AdminLogManagement.this, AdminMain.class);
            AdminLogManagement.this.startActivity(Iret);
            AdminLogManagement.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
