package com.example.lhwei.test;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

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

    public Response connect(String method, String postBody) throws IOException{
        /*
        * 功能：设置服务器参数
        * 参数：method：对应cgi程序
        *       parameter：传给cgi程序的参数
        */
        String urlStr = "http://101.200.63.71:8000/android/" + method;  //服务器地址+端口号+访问程序
        //String urlStr = "http://60.205.188.244:9999/" + method;  //服务器地址+端口号+访问程序

        //设置请求的数据类型
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        final OkHttpClient client = new OkHttpClient(); //得到okhttp的client

        //与服务建立连接，并请求数据
        Request request = new Request.Builder()
                .url(urlStr)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        //创建一个读取数据的句柄
        Response response = client.newCall(request).execute();

        return response;
    }

}
