package com.example.mysqldb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

/*
 * 货物管理界面
 * */

public class GoodsManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_add, btn_return, btn_search;  //增加图片按钮，返回图片按钮

    private GoodsDao goodsDao;    //货物数据库操作实例

    private List<Goodsinfo> goodsinfoList;    //货物数据集合
    private LvGoodsinfoAdapter lvGoodsinfoAdapter;    //货物信息数据适配器

    private ListView lv_goods;   //货物列表组件
    private Handler mainHandler;    //主线程

    public GoodsManagerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_manager);

        initView();
        loadGoodsDb();
    }

    private void initView(){
        btn_add = findViewById(R.id.btn_gAdd);
        btn_return = findViewById(R.id.btn_gReturn);
        btn_search = findViewById(R.id.btn_gSearch);

        lv_goods = findViewById(R.id.lv_goods);

        goodsDao = new GoodsDao();

        mainHandler = new Handler(getMainLooper()); //获取主线程

        btn_return.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    private void loadGoodsDb(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                goodsinfoList = goodsDao.getAllGoodsList();    //获取所有的货物数据
                Log.i("管理界面的数据", "货物种类:" + goodsinfoList.size());
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
    private void showLvData(){
        if(lvGoodsinfoAdapter == null){   //首次加载时的操作
            lvGoodsinfoAdapter = new LvGoodsinfoAdapter(this, goodsinfoList);
            lv_goods.setAdapter(lvGoodsinfoAdapter);
        }else{  //更新数据时的操作
            lvGoodsinfoAdapter.setGoodsinfoList(goodsinfoList);
            lvGoodsinfoAdapter.notifyDataSetChanged();
        }

        //修改按钮的操作
        lvGoodsinfoAdapter.setOnEditButtonClickListener(new OnEditButtonClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                //修改方法
                Goodsinfo item = goodsinfoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goodsEdit", item);
                Intent intent = new Intent(GoodsManagerActivity.this, GoodsEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        //删除按钮的操作
        lvGoodsinfoAdapter.setOnDelBtnClickListener(new OnDelBtnClickListener() {
            @Override
            public void onDelBtnClick(View view, int position) {
                //删除方法
                final Goodsinfo item = goodsinfoList.get(position);
                new AlertDialog.Builder(GoodsManagerActivity.this)
                        .setTitle("删除确认")
                        .setMessage("您确定要删除:id:[" + item.getId() + "],商品名为:[" + item.getGname() + "]的货物信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doDelGoods(item.getId());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }

    /*
     * 执行删除商品的方法
     * id 要删除商品的id
     * */
    private void doDelGoods(final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int iRow = goodsDao.delGoods(id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadGoodsDb();   //重新加载数据
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_gReturn){
            finish();
        }
        else if(v.getId() == R.id.btn_gAdd){
            //打开添加货物界面
            Intent intent = new Intent(this, GoodsAddActivity.class);
            startActivityForResult(intent, 1);
        }else if(v.getId() == R.id.btn_gSearch){
            //打开搜索货物界面
            Intent intent = new Intent(this, GoodsSearchActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){    //操作成功
            loadGoodsDb();   //重新加载数据
        }
    }
}