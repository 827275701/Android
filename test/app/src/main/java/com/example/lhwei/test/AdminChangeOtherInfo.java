package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/17.
 */
public class AdminChangeOtherInfo extends Activity {
    EditText Ename;
    EditText Eno;
    //EditText Esex;
    Spinner Ssex;
    EditText Ephone;
    EditText Epwd;

    String o_name;
    String o_no;
    String o_sex;
    String o_phone;
    String o_pwd;

    String n_name;
    String n_no;
    String n_sex;
    String n_phone;
    String n_pwd;

    Button select;
    Button submit;

    boolean FLAG_SELECT = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_change_other_info);

        Ename = (EditText) findViewById(R.id.Tadmin_change_other_info_name);
        Eno = (EditText) findViewById(R.id.Tadmin_change_other_info_no);
        Ssex = (Spinner) findViewById(R.id.Sadmin_change_other_info_sex);
        Ephone = (EditText) findViewById(R.id.Tadmin_change_other_info_phone);
        Epwd = (EditText) findViewById(R.id.Tadmin_change_other_info_password);

        submit = (Button) findViewById(R.id.Badmin_change_other_info_submit);
        select = (Button) findViewById(R.id.Badmin_change_other_info_select_user_info);


        Ssex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                //拿到被选择项的值
                n_sex = (String)Ssex.getSelectedItem();
//                //把该值传给 TextView
//                tv.setText(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        submit.setOnClickListener(new ButtonClickListener_submit());   //用户管理按键监听
        select.setOnClickListener(new ButtonClickListener_select());
    }

    class ButtonClickListener_submit implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(FLAG_SELECT == false) {
                Toast t = Toast.makeText(getApplicationContext(), "请先查询", Toast.LENGTH_SHORT);
                t.show();
                return;
            }
            n_name = Ename.getText().toString();
            n_no = Eno.getText().toString();
            //n_sex = Ssex.getItemAtPosition(0).toString();
            n_phone =  Ephone.getText().toString();
            n_pwd = Epwd.getText().toString();

            CheckOut checkOut = new CheckOut();
            if(checkOut.check_no(n_no) == false) {
                //提示 工号格式错误
                Toast t = Toast.makeText(getApplicationContext(), "工号格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_name(n_name) == false) {
                //提示 姓名格式错误
                Toast t = Toast.makeText(getApplicationContext(), "姓名格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_sex(n_sex) == false) {
                //提示 性别格式错误
                Toast t = Toast.makeText(getApplicationContext(), "性别格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_phone(n_phone) == false) {
                //提示 手机号格式错误
                Toast t = Toast.makeText(getApplicationContext(), "手机号格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }

            if(checkOut.check_password(n_pwd) == false) {
                //提示 密码格式错误
                Toast t = Toast.makeText(getApplicationContext(), "密码格式错误", Toast.LENGTH_SHORT);
                t.show();
                return;
            }
            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                //校验 输入信息
                MyHttp myHttp = new MyHttp();
                String postBody = "username=admin" + "&name=" + n_name +
                        "&no=" + n_no+ "&sex=" + n_sex +
                        "&phone=" + n_phone+"&password=" + n_pwd;

                @Override
                public void run() {
                    System.out.println("n_on ====》" + n_no);
                    System.out.println("o_on ====》" + o_no);

                    if(o_no.equals(n_no) == false) {
                        //显示 工号不能修改
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "工号不能修改", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                        return;
                    }

                    System.out.println("n_sex ====》" + n_sex);
                    System.out.println("o_sex ====》" + o_sex);
                    if(o_name.equals(n_name) &&o_sex.equals(n_sex) &&o_phone.equals(n_phone) &&o_pwd.equals(n_pwd)) {
                        //显示 没有任何修改，并返回
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "没有任何修改", Toast.LENGTH_SHORT);
                        t.show();
                        Intent Iret = new Intent();  //创建意图
                        Iret.setClass(AdminChangeOtherInfo.this, AdminUserManagement.class);
                        AdminChangeOtherInfo.this.startActivity(Iret);//启动意图
                        AdminChangeOtherInfo.this.finish(); //关闭当前Activity
                        Looper.loop();
                        return;
                    }

                    try {
                        Response response = myHttp.connect("change_other_info", postBody);
                        String res = response.body().string();

                        System.out.println("==== Change Info start ====");
                        if (response.isSuccessful()) {  //如果返回200 OK
                            System.out.println(res);
                            if(res.equals("Successful")){
                                System.out.println("==== Successful ====");
                                //成功 返回好AdminUserManagement界面
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT);
                                t.show();
                                Intent Iret = new Intent();  //创建意图
                                Iret.setClass(AdminChangeOtherInfo.this, AdminUserManagement.class);
                                AdminChangeOtherInfo.this.startActivity(Iret);//启动意图
                                AdminChangeOtherInfo.this.finish(); //关闭当前Activity
                                Looper.loop();
                            } else {
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                            }
                            System.out.println("==== Change Info end ====");
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "请求失败", Toast.LENGTH_SHORT);
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

    public void initSpinner_m() {
        //原始string数组
        final String[] spinnerItems = {"男","女"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        Ssex.setAdapter(spinnerAdapter);
    }

    public void initSpinner_w() {
        //原始string数组
        final String[] spinnerItems = {"女","男"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        Ssex.setAdapter(spinnerAdapter);
    }

    class ButtonClickListener_select implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FLAG_SELECT = true;
            new Thread(new Runnable() {
                //在android中，主线程用来显示界面，所以与网络通信只能在创建一个线程
                MyHttp myHttp = new MyHttp();
                String no = Eno.getText().toString();

                @Override
                public void run() {
                    CheckOut checkOut = new CheckOut();
                    //检查工号格式
                    if(checkOut.check_no(no) == false) {
                        //提示 工号格式错误
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "工号格式错误", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                        return;
                    }

                    String postBody = "username=admin&no=" + no;

                    try {
                        Response response = myHttp.connect("obtain_person", postBody);
                        final String res = response.body().string();
                        System.out.println(res);

                        System.out.println("==== Get Person start ====");
                        if (response.isSuccessful()) {  //如果返回200 OK
                            if(res.equals("False")){
                                System.out.println("==== Failed ====");
                                Looper.prepare();
                                Toast t = Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT);
                                t.show();
                                Looper.loop();
                                return;
                            }

                            System.out.println("==== Successful ====");
                                //成功 返回好AdminUserManagement界面
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //更新UI
                                    String[] buff = res.split("[&]");
                                    o_name = buff[0].split("[=]")[1];
                                    o_no = buff[1].split("[=]")[1];
                                    System.out.println("o_on ====》" + o_no);


                                    o_sex = buff[2].split("[=]")[1];
                                    o_phone = buff[3].split("[=]")[1];
                                    o_pwd = buff[4].split("[=]")[1];
                                    Ename.setText(o_name);
                                    if(o_sex.equals("男")) {
                                        initSpinner_m();
                                    }else if(o_sex.equals("女")) {
                                        initSpinner_w();
                                    } else {
                                        //TODO
                                        //未知性别
                                    }
                                    Ephone.setText(o_phone);
                                    Epwd.setText(o_pwd);
                                }

                            });
                            System.out.println("==== Get Person end ====");
                        } else {
                            //失败 提示失败信息，页面不跳转
                            Looper.prepare();
                            Toast t = Toast.makeText(getApplicationContext(), "查询管理员失败", Toast.LENGTH_SHORT);
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
            Iret.setClass(AdminChangeOtherInfo.this, AdminUserManagement.class);
            AdminChangeOtherInfo.this.startActivity(Iret);
            AdminChangeOtherInfo.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
