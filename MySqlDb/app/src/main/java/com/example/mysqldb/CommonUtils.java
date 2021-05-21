package com.example.mysqldb;

/*
*自定义通用工具类
* */

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    //获取当前时间的字符串
    public static String getDateStringFromNow(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    //从日期时间中获取时间字符串
    public static String getStrFromDate(Date dt){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(dt);
    }

    /*
    * 显示短消息
    * context 上下文
    * msg 要显示的消息
    * */
    public static void showShortMsg(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /*
     * 显示长消息
     * context 上下文
     * msg 要显示的消息
     * */
    public static void showLongMsg(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /*
    * 显示消息对话框
    * context 上下文
    * msg 要显示的消息
    * */
    public static void showDlgMsg(Context context, String msg){
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage(msg)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .create().show();
    }
}
