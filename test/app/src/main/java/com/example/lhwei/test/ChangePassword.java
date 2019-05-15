package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Response;

/**
 * Created by lhwei on 2019/5/13.
 */
public class ChangePassword extends Activity{

    String nowpwd;
    String newpwd;
    String renewpdw;

    EditText Enowpwd;
    EditText Enewpwd;
    EditText Erenewpwd;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        Enowpwd = (EditText) findViewById(R.id.Enow_password);
        Enewpwd = (EditText) findViewById(R.id.Enew_password);
        Erenewpwd = (EditText) findViewById(R.id.Erenew_password);

        Button submit = (Button) findViewById(R.id.Bchange_password_submit);

        Intent  i = this.getIntent();
        username = i.getStringExtra("username");

        submit.setOnClickListener(new ButtonClickListener());
    }

    private boolean ChectoutPassword(String nowpwd, String newpwd, String renewpdw) {
        if(nowpwd.isEmpty()) {
            //提示当前密码不能为空
            Toast t = Toast.makeText(getApplicationContext(), "当前密码不能为空", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if(newpwd.isEmpty() || renewpdw.isEmpty()) {
            // 提示新密码不能为空
            Toast t = Toast.makeText(getApplicationContext(), "新密码不能为空", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if(newpwd.equals(renewpdw) == false) {
            // 提示两次密码不想等
            Toast t = Toast.makeText(getApplicationContext(), "两次密码不想等", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if(newpwd.length() < 3) {
            //提示密码太短
            Toast t = Toast.makeText(getApplicationContext(), "新密码太短", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if(newpwd.length() > 16) {
            //提示密码太长
            Toast t = Toast.makeText(getApplicationContext(), "新密码太长", Toast.LENGTH_SHORT);
            t.show();
            return false;
        } else if(nowpwd.equals(newpwd)) {
            //提示 新老密码不能相同
            Toast t = Toast.makeText(getApplicationContext(), "新密码和旧密码不能相同", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        return true;
    }

    class ButtonClickListener implements View.OnClickListener {    //登录按钮监听函数
        @Override
        public void onClick(View v) {
            nowpwd = Enowpwd.getText().toString();
            newpwd = Enewpwd.getText().toString();
            renewpdw = Erenewpwd.getText().toString();

            if(ChectoutPassword(nowpwd, newpwd, renewpdw) == true) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //设置POST请求的body
                            String postBody = "username=" + username +"&nowpassword=" + nowpwd+"&newpassword="+newpwd;
                            System.out.println(postBody);
                            MyHttp myHttp = new MyHttp();
                            Response response = myHttp.connect("change_password", postBody);
                            if (response.isSuccessful()) {  //如果返回200 OK
                                String res = response.body().string();
                                if(res.equals("Successful")) {
                                    //修改密码成功，返回ChooseSpot
                                    Intent Iret = new Intent();  //创建意图
                                    Iret.putExtra("username", username);
                                    Iret.setClass(ChangePassword.this, ChooseSpot.class);
                                    ChangePassword.this.startActivity(Iret);//启动意图
                                    ChangePassword.this.finish(); //关闭当前Activity
                                } else {
                                    System.out.println("==== Failed ====");
                                    Looper.prepare();
                                    Toast t = Toast.makeText(getApplicationContext(), "原密码不正确，修改失败", Toast.LENGTH_SHORT);
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
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "两次密码不相同！", Toast.LENGTH_SHORT);
                t.show();
            }

        }
    }
}
