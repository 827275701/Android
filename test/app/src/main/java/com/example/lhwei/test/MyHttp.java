package com.example.lhwei.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lhwei on 2019/3/30.
 */
public class MyHttp {

    HttpURLConnection conn;

    public HttpURLConnection connect(String method, String parameter) throws IOException{
        /*
        * 功能：设置服务器参数
        * 参数：method：对应cgi程序
        *       parameter：传给cgi程序的参数
        */
        String urlStr = "http://101.200.63.71:9999/";  //服务器地址+端口号+访问程序
        urlStr = urlStr+ method + "/" + parameter;
        URL url = new URL(urlStr); //创建URL对象
        conn = (HttpURLConnection) url.openConnection(); //获取HttpURLConnection连接（尝试连接服务器）
        conn.setRequestMethod("GET"); //设置为GET请求
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.connect(); //连接服务器
        return conn;
    }

    public String get_response() throws IOException {
        InputStream in= conn.getInputStream();
        //读取输入流
        byte[] b=new byte[1024*512]; //定义一个byte数组读取输入流

        ByteArrayOutputStream baos = new ByteArrayOutputStream(); //定义缓存流来保存输入流的数据

        int len=0;
        while((len=in.read(b))>-1){  //每次读的len>-1 说明是是有数据的
            baos.write(b,0,len);  //三个参数  输入流byte数组   读取起始位置  读取终止位置
        }

        conn.disconnect();//关闭HTTP连接
        String msg = baos.toString();
        return msg;
    }
}
