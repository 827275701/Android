package com.example.lhwei.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Created by lhwei on 2019/6/5.
 */
public class Welcome extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
//        Intent intent=new Intent(Welcome.this, MyService.class);
//        Bundle bundle=new Bundle();
//        intent.putExtras(bundle);
//        startService(intent);

        handler.sendEmptyMessageDelayed(0, 3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            gotoLogin();
            super.handleMessage(msg);
        }
    };

    public void gotoLogin(){
        Intent intent = new Intent(Welcome.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回到NowData Activity
        return true;
    }
}