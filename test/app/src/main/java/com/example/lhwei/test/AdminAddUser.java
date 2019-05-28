package com.example.lhwei.test;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by lhwei on 2019/4/27.
 */
public class AdminAddUser extends AppCompatActivity {
    EditText Ename;
    EditText Eno;
    //EditText sex;
    Spinner Ssex;
    EditText Ephone;
    Button submit;

    String name;
    String no;
    String sex;
    String phone;
    private Context instance;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_user);

        Ename = (EditText) findViewById(R.id.Tadmin_adduser_name);
        Eno = (EditText) findViewById(R.id.Tadmin_adduser_no);
        //sex = (EditText) findViewById(R.id.Tadmin_adduser_sex);
        Ssex = (Spinner) findViewById(R.id.Sadmin_adduser_sex);
        Ephone = (EditText) findViewById(R.id.Tadmin_adduser_phone);
        submit = (Button) findViewById(R.id.Badmin_adduser_submit);

        initSpinner();  //设置监听和数据

        submit.setOnClickListener(new ButtonClickListener_submit());   //用户管理按键监听
    }

    public void initSpinner() {
        Ssex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //拿到被选择项的值
                sex = (String)Ssex.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //原始string数组
        final String[] spinnerItems = {"请选择","男","女"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        Ssex.setAdapter(spinnerAdapter);
    }

    class ButtonClickListener_submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            name = Ename.getText().toString();
            no = Eno.getText().toString();
            phone = Ephone.getText().toString();

            CheckOut checkOut = new CheckOut();
            if(checkOut.check_no(no) == false) {
                //提示 工号格式错误
                Toast t = Toast.makeText(getApplicationContext(), "工号格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_name(name) == false) {
                //提示 姓名格式错误
                Toast t = Toast.makeText(getApplicationContext(), "姓名格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_sex(sex) == false) {
                //提示 性别格式错误
                Toast t = Toast.makeText(getApplicationContext(), "性别格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_phone(phone) == false) {
                //提示 手机号格式错误
                Toast t = Toast.makeText(getApplicationContext(), "手机号格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                MyHttp myHttp = new MyHttp();
                String postBody = "username=admin" + "&name=" + name +
                        "&no=" + Eno.getText().toString() + "&sex="+ sex +
                        "&phone=" + Ephone.getText().toString();
                @Override
                public void run() {
                    try {
                        Response response = myHttp.connect("add_user", postBody);
                        String res = response.body().string();

                        System.out.println("==== Change Password start ====");
                        if (response.isSuccessful()) {  //如果返回200 OK
                            System.out.println(res);
                            if(res.equals("Successful")){
                                System.out.println("==== Successful ====");

                                //成功 返回好AdminUserManagement界面
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "添加管理员成功", Toast.LENGTH_SHORT);
                                t.show();
                                Intent Iret = new Intent();  //创建意图
                                Iret.setClass(AdminAddUser.this, AdminUserManagement.class);
                                AdminAddUser.this.startActivity(Iret);//启动意图
                                AdminAddUser.this.finish(); //关闭当前Activity
                                Looper.loop();
                            } else {
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "添加管理员失败", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                            System.out.println("==== Change Password end ====");
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "添加管理员失败", Toast.LENGTH_SHORT);
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
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回到NowData Activity
            Intent Iret = new Intent();
            Iret.setClass(AdminAddUser.this, AdminUserManagement.class);
            AdminAddUser.this.startActivity(Iret);
            AdminAddUser.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}