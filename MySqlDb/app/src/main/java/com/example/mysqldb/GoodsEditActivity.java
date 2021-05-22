package com.example.mysqldb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
 * 修改商品界面业务逻辑
 * */

public class GoodsEditActivity extends AppCompatActivity {

    private EditText et_gname, et_gnumber;

    private TextView tv_createDt;

    private Handler mainHandler;

    private Goodsinfo goodsEdit;  //当前要修改的用户信息

    private GoodsDao goodsDao;    //用户数据操作类实例

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_edit);

        et_gname = findViewById(R.id.et_gname);
        et_gnumber = findViewById(R.id.et_gnumber);
        tv_createDt = findViewById(R.id.tv_createDt);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            goodsEdit = (Goodsinfo)bundle.getSerializable("goodsEdit");

            et_gname.setText(goodsEdit.getGname());
            et_gnumber.setText(goodsEdit.getGnumber());
            tv_createDt.setText(goodsEdit.getCreateDt());
        }

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
            goodsEdit.setGname(gname);
            goodsEdit.setGnumber(gnumber);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int iRow = goodsDao.editGoods(goodsEdit);

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