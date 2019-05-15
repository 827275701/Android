package com.example.lhwei.test;

import java.util.regex.Pattern;
/**
 * Created by lhwei on 2019/5/15.
 */
public class CheckOut {
    public static  final  int NO_LENGTH = 6; //no的长度
    public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"; //手机校验正则
    public static final String NO_IS_NUMBER = "^[-\\+]?[\\d]*$"; //工号检验正则
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";//验证汉字
    public boolean check_name(String name){
        //名字为都为汉字

        //如果是汉字最短2个字符，最长4个字符

        return true;
    }

    public  boolean check_no(String no) {
        //工号长度为6位
        if (no.length() != NO_LENGTH) {
            return false;
        }
        //工号必须由数字组成
        return Pattern.matches(NO_IS_NUMBER, no);
    }

    public boolean check_age(int age) {
        //年龄大于18 小于 60
        if(age < 18 || age > 60) {
            return false;
        }
        return true;
    }

    public boolean check_phone(String phone) {
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    public boolean check_username(String username) {
        //不为空
        if(username.equals("")) {
            return false;
        }
        //工号  调用 check_no
        return check_no(username);
    }

    public boolean check_password(String password){
        //不为空
        if(password.equals("")) {
            return false;
        }
        //校验长度
        if(password.length() < 6 || password.length() > 18) {
            return false;
        }
        return true;
    }
}
