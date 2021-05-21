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
* 用户管理界面
* */

public class UserManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn_add, btn_return;  //增加图片按钮，返回图片按钮

    private UserDao userDao;    //用户数据库操作实例

    private List<Userinfo> userinfoList;    //用户数据集合
    private LvUserinfoAdapter lvUserinfoAdapter;    //用户信息数据适配器

    private ListView lv_user;   //用户列表组件
    private Handler mainHandler;    //主线程

    public UserManagerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manager);

        initView();
        loadUserDb();
    }

    private void initView(){
        btn_add = findViewById(R.id.btn_add);
        btn_return = findViewById(R.id.btn_return);

        lv_user = findViewById(R.id.lv_user);

        userDao = new UserDao();

        mainHandler = new Handler(getMainLooper()); //获取主线程

        btn_return.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    private void loadUserDb(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                userinfoList = userDao.getAllUserList();    //获取所有的用户数据
                Log.i("管理界面的数据", "用户数量:" + userinfoList.size());
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
        if(lvUserinfoAdapter == null){   //首次加载时的操作
            lvUserinfoAdapter = new LvUserinfoAdapter(this, userinfoList);
            lv_user.setAdapter(lvUserinfoAdapter);
        }else{  //更新数据时的操作
            lvUserinfoAdapter.setUserinfoList(userinfoList);
            lvUserinfoAdapter.notifyDataSetChanged();
        }

        //修改按钮的操作
        lvUserinfoAdapter.setOnEditButtonClickListener(new OnEditButtonClickListener() {
            @Override
            public void onEditBtnClick(View view, int position) {
                //修改方法
                Userinfo item = userinfoList.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userEdit", item);
                Intent intent = new Intent(UserManagerActivity.this, UserEditActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        //删除按钮的操作
        lvUserinfoAdapter.setOnDelBtnClickListener(new OnDelBtnClickListener() {
            @Override
            public void onDelBtnClick(View view, int position) {
                //删除方法
                final Userinfo item = userinfoList.get(position);
                new AlertDialog.Builder(UserManagerActivity.this)
                        .setTitle("删除确认")
                        .setMessage("您确定要删除:id:[" + item.getId() + "],用户名为:[" + item.getUname() + "]的用户信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doDelUser(item.getId());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create().show();
            }
        });
    }

    /*
    * 执行删除用户的方法
    * id 要删除用户的id
    * */
    private void doDelUser(final int id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int iRow = userDao.delUser(id);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadUserDb();   //重新加载数据
                    }
                });
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_return){
            finish();
        }
        else if(v.getId() == R.id.btn_add){
            //打开添加用户界面
            Intent intent = new Intent(this, UserAddActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 1){    //操作成功
            loadUserDb();   //重新加载数据
        }
    }
}