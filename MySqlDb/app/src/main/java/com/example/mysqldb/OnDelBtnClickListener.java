package com.example.mysqldb;

/*
 *删除按钮的点击事件监听接口
 * */

import android.view.View;

public interface OnDelBtnClickListener {
    void onDelBtnClick(View view, int position);    //修改按钮的删除事件
}
