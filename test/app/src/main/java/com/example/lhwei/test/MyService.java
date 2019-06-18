package com.example.lhwei.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by lhwei on 2019/5/30.
 */
public class MyService extends Service{
    String username;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind");
        return null;
    }

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("服务启动了");

        //pushMonitorMessage();

        mHandler.postDelayed(r, 5000);//延时1000毫秒
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //一个定时器
    Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //do something
            //每隔1s循环执行run方法
            System.out.println("定时程序执行了");
            //System.out.println("usernaem====>" + username);

            getData();

//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            mHandler.postDelayed(r, 5000);//延时1000毫秒
        }
    };

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置POST请求的body
                    String postBody_get_room_id = "username=admin";
                    MyHttp myHttp_get_room_id = new MyHttp();
                    Response response = myHttp_get_room_id.connect("select_room", postBody_get_room_id);
                    if (response.isSuccessful()) {  //如果返回200 OK
                        final String res_body_get_room_id = response.body().string();

                        //获取已存在所有博览室的ID
                        String[] rooms_info = res_body_get_room_id.split("[&]");
                        String[] room_ids = new String[rooms_info.length/3];
                        int j = 0;
                        System.out.println("room_info==================");
                        for(int i = 1; i < rooms_info.length; i+=3) {
                            room_ids[j++] = rooms_info[i];
                            System.out.println(room_ids[j - 1]);
                        }

                        //循环使用博览室ID调用now_data接口，记录有异常数据的博览室个数
                        MyHttp myHttp_now_data = new MyHttp();
                        String postBody_now_data = "username=admin&where=";
                        CheckOut checkout  = new CheckOut();
                        int _count = 0;
                        System.out.println("now_data==================");
                        for(int i = 0; i < room_ids.length; ++i) {
                            Response response_now_data = myHttp_now_data.connect("now_data", postBody_now_data + room_ids[i].split("[=]")[1]);
                            if(response_now_data.isSuccessful()) {
                                final String res_body_now_data = response_now_data.body().string();
                                String[] now_data = res_body_now_data.split("[&]");
                                //温度 湿度  气体  时间
                                if(checkout.check_now_t(Float.parseFloat(now_data[0].split("[=]")[1])) == false) {
                                    _count++;
                                    continue;
                                } else if (checkout.check_now_h(Float.parseFloat(now_data[1].split("[=]")[1])) == false) {
                                    _count++;
                                    continue;
                                } else if (checkout.check_now_g(Float.parseFloat(now_data[2].split("[=]")[1])) == false) {
                                    _count++;
                                    continue;
                                }

                            }
                        }
                        System.out.println("_count==================" + _count);

                        //如果博览室数据有异常，则通知栏显示
                        if(_count > 0) {
                            pushMonitorMessage("有" + _count + "间博览室室内数据异常，请立即查看");
                        }

                    } else {
                        Looper.prepare();
                        Toast t = Toast.makeText(getApplicationContext(), "服务器错误!", Toast.LENGTH_SHORT);
                        t.show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     *  通知到前台的消息：登录时启动
     * @param
     * @param
     */
    public void messagelogin(){
        NotificationCompat.Builder builder =new NotificationCompat.Builder(this);
        Intent intent=new Intent(MyService.this, MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentTitle("新通知") //消息标题
                .setContentText("一条新通知来了")//消息内容
                .setTicker("您有一条新通知") //未下拉时 浮动显示
                .setWhen(System.currentTimeMillis())// 时间
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //系统默认的铃声
                .setSmallIcon(R.mipmap.ic_launcher)//显示的图标
                .setOngoing(true)//禁止滑动删除
                .setAutoCancel(false)//点击后不消失
                .setContentIntent(pi);//点击后进入的页面
        Notification notification=  builder.build();
        startForeground(1, notification);
    }

    /**
     *  通知到前台的消息 有新的消息
     *  参数可自己修改
     * @param title
     * @param msg
     */
    public void message(String title,String msg,int countMsg) {
        Intent intent = new Intent();

        //消息通知
        //PendingIntent.FLAG_UPDATE_CURRENT：携带参数，点进去以后可以得到这个intent携带的数据
        //但是，intent跳进去的Activity在manifest里设置android:launchMode="singleTask"
        //如果不需要intent携带数据，直接为0即可
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        PendingIntent pi = PendingIntent.getActivity(this, countMsg, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title) //消息标题
                .setContentText(msg)//消息内容
                .setTicker(msg) //未下拉时 浮动显示
                .setWhen(System.currentTimeMillis())// 时间
                .setDefaults(Notification.DEFAULT_LIGHTS) //灯光
                .setDefaults(Notification.DEFAULT_VIBRATE)// 震动
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //系统默认的铃声
                .setSmallIcon(R.mipmap.aa) //小图标
                .setOngoing(false)//可以删除
                .setAutoCancel(true)//点击后消失
                .setContentIntent(pi); //点击跳转到

        builder.setDefaults(Notification.FLAG_INSISTENT);//消息一直不停的响，知道用户点击消息才停止
        Notification notification= builder.build();
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(countMsg, notification);

        startForeground(1, notification);

    }

    private  void pushMonitorMessage(String msg){
        message("博览室室内数据异常", msg, 1);
    }

    /**
     * 服务销毁时
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("服务退出了");
    }

}
