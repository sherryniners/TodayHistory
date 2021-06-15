package com.example.todayhistory;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todayhistory.Dao.UserDao;

public class activity_modify extends AppCompatActivity {
    EditText name = null;
    EditText oldpassword = null;
    EditText newpassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        name = findViewById(R.id.name);
        oldpassword = findViewById(R.id.password);
        newpassword = findViewById(R.id.newpassword);
    }
    public void modify(View view) {

        new Thread(){
            @Override
            public void run() {



                UserDao userDao = new UserDao();

                boolean aa = userDao.modify(newpassword.getText().toString(),name.getText().toString(), oldpassword.getText().toString());
                int msg = 0;
                if(aa){
                    msg = 1;
                }

                hand.sendEmptyMessage(msg);



            }
        }.start();



    }
    @SuppressLint("HandlerLeak")
    final Handler hand = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0)
            {
                Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_LONG).show();

            }
            if(msg.what == 1)
            {
                Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                //将想要传递的数据用putExtra封装在intent中
                setResult(RESULT_CANCELED,intent);
                finish();

            }


        }
    };
}