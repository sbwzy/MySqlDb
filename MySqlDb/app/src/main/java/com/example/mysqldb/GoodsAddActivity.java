package com.example.mysqldb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
 *添加商品的逻辑代码
 * */

public class GoodsAddActivity extends AppCompatActivity {

    private EditText et_gname, et_gnumber;

    private Handler mainHandler;

    private GoodsDao goodsDao;    //商品数据操作类实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_add);

        et_gname = findViewById(R.id.et_gname);
        et_gnumber = findViewById(R.id.et_gnumber);

        goodsDao = new GoodsDao();
        mainHandler = new Handler(getMainLooper());
    }

    //确定按钮的点击事件处理
    public void btn_ok_click(View view){
        final String gname = et_gname.getText().toString().trim();
        final String gnumber = et_gnumber.getText().toString().trim();

        if(TextUtils.isEmpty(gname)){
            CommonUtils.showShortMsg(this, "请输入商品名!");
            et_gname.requestFocus();
        }
        else if(TextUtils.isEmpty(gnumber)){
            CommonUtils.showShortMsg(this, "请输入商品数量!");
            et_gnumber.requestFocus();
        }else{
            final Goodsinfo item = new Goodsinfo();
            item.setGname(gname);
            item.setGnumber(gnumber);
            item.setCreateDt(CommonUtils.getDateStringFromNow());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int iRow = goodsDao.addGoods(item);

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run(){
                            setResult(1);   //当前使用参数表示当前页面操作成功，并返回管理界面
                            finish();
                        }
                    });
                }
            }).start();
        }
    }
}