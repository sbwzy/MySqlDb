package com.example.mysqldb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class GoodsSearchActivity extends AppCompatActivity {

    private EditText et_gname;

    private List<Goodsinfo> goodsinfoList;

    private Handler mainHandler;

    private LvGoodsinfoAdapter lvGoodsinfoAdapter;    //货物信息数据适配器

    private ListView lv_goods;   //货物列表组件

    private GoodsDao goodsDao;    //用户数据操作类实例

    public GoodsSearchActivity(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_search);

        et_gname = findViewById(R.id.et_gname);

        initView();
    }

    private void initView(){
        lv_goods = findViewById(R.id.lv_goods);

        goodsDao = new GoodsDao();

        mainHandler = new Handler(getMainLooper()); //获取主线程
    }


    public void btn_ok_click(View view){
        LoadGoodsDb();
    }

    private void LoadGoodsDb(){

        final String gname = et_gname.getText().toString().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                goodsinfoList = goodsDao.getGoodsBygname(gname);    //获取所有的用户数据
                Log.i("管理界面的数据", "用户数量:" + goodsinfoList.size());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showLvData();
                    }
                });
            }
        }).start();
    }

    //显示列表数据的方法
    private void showLvData() {
        if (lvGoodsinfoAdapter == null) {   //首次加载时的操作
            lvGoodsinfoAdapter = new LvGoodsinfoAdapter(this, goodsinfoList);
            lv_goods.setAdapter(lvGoodsinfoAdapter);
        } else {  //更新数据时的操作
            lvGoodsinfoAdapter.setGoodsinfoList(goodsinfoList);
            lvGoodsinfoAdapter.notifyDataSetChanged();
        }
    }
}