package com.example.todayhistory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todayhistory.Dao.UserDao;
import com.example.todayhistory.MD5Utils;



public class LoginActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void reg(View view){

        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));

    }
    public void login(View view){

        final EditText EditTextname = (EditText)findViewById(R.id.name);
        final EditText EditTextpassword = (EditText)findViewById(R.id.password);

        new Thread(){
            @Override
            public void run() {

                UserDao userDao = new UserDao();

                boolean aa = userDao.login(EditTextname.getText().toString(),EditTextpassword.getText().toString());
                int msg = 0;
                if(aa){
                    msg = 1;
                }

                hand1.sendEmptyMessage(msg);


            }
        }.start();


    }
    @SuppressLint("HandlerLeak")
    final Handler hand1 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1)
            {
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                //登录成功后关闭此页面进入主页
                Intent data=new Intent();
                //datad.putExtra( ); name , value ;
                data.putExtra("isLogin",true);
                //RESULT_OK为Activity系统常量，状态码为-1
                // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                setResult(RESULT_OK,data);
                //销毁登录界面
                LoginActivity.this.finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
            else
            {
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_LONG).show();
            }
        }
    };
    public void cancel(View view) {
        final EditText EditTextname = (EditText)findViewById(R.id.name);
        final EditText EditTextpassword = (EditText)findViewById(R.id.password);
        new Thread(){
            @Override
            public void run() {

                UserDao userDao = new UserDao();

                boolean aa = userDao.cancel(EditTextname.getText().toString(),EditTextpassword.getText().toString());
                int msg = 0;
                if(aa){
                    msg = 1;
                }

                hand2.sendEmptyMessage(msg);


            }
        }.start();
    }
    @SuppressLint("HandlerLeak")
    final Handler hand2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 1)
            {
                Toast.makeText(getApplicationContext(),"注销成功",Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"注销失败",Toast.LENGTH_LONG).show();
            }
        }
    };
    public void modify(View view) {
        startActivity(new Intent(getApplicationContext(),activity_modify.class));
    }

}

