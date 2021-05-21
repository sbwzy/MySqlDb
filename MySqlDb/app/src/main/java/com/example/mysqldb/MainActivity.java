package com.example.mysqldb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_query_count, btn_login;
    private TextView tv_user_count;

    private EditText et_uname, et_upass;    //用户名、密码框

    private UserDao dao;  //用户数据库操作类
    private Handler mainHandler ;   //主线程

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //super.handleMessage(msg);
            if(msg.what == 0){
                int count = (Integer)msg.obj;
                tv_user_count.setText("数据库中的用户数量为:" + count);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){
        btn_query_count = findViewById(R.id.btn_query_count);
        tv_user_count = findViewById(R.id.tv_user_count);

        et_uname = findViewById(R.id.et_uname);
        et_upass = findViewById(R.id.et_upass);

        btn_login = findViewById(R.id.btn_login);

        dao = new UserDao();
        mainHandler = new Handler(getMainLooper()); //获取主线程

        btn_query_count.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_query_count){
            doQueryCount();
        }
        else if(v.getId() == R.id.btn_login){
            doLogin();
        }
    }

    private void doQueryCount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = MySqlHelp.getUserSize();
                Message msg = Message.obtain();
                msg.what = 0;
                msg.obj = count;
                handler.sendMessage(msg);
            }
        }).start();
    }

    //执行登陆操作
    private void doLogin(){
        final String uname = et_uname.getText().toString().trim();
        final String upass = et_upass.getText().toString().trim();

        if(TextUtils.isEmpty(uname)){
            CommonUtils.showShortMsg(this, "请输入用户名!");
            et_uname.requestFocus();
        }
        else if(TextUtils.isEmpty(upass)){
            CommonUtils.showShortMsg(this, "请输入用户密码!");
            et_upass.requestFocus();
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Userinfo item = dao.getUserByUnameAndUpass(uname, upass);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run(){
                            if(item == null){
                                CommonUtils.showDlgMsg(MainActivity.this, "用户名或密码错误!");
                            }else{
                                //CommonUtils.showDlgMsg(MainActivity.this, "登陆成功,进入用户管理");
                                CommonUtils.showLongMsg(MainActivity.this, "登陆成功,进入用户管理");
                                //调用用户管理界面
                                Intent intent = new Intent(MainActivity.this, UserManagerActivity.class);
                                startActivity(intent);
                                //finish();   //结束当前界面
                            }
                        }
                    });
                }
            }).start();
        }
    }
}