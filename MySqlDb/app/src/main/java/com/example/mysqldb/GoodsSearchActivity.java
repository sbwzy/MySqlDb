package com.example.mysqldb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        //修改按钮的操作
        lvGoodsinfoAdapter.setOnEditButtonClickListener(new OnEditButtonClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                //修改方法
                Goodsinfo item = goodsinfoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goodsEdit", item);
                Intent intent = new Intent(GoodsSearchActivity.this, GoodsEditActivity.class);
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
                new AlertDialog.Builder(GoodsSearchActivity.this)
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
                        LoadGoodsDb();   //重新加载数据
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){    //操作成功
            LoadGoodsDb();   //重新加载数据
        }
    }
}