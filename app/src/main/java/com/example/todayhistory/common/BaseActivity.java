package com.example.todayhistory.common;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class BaseActivity extends AppCompatActivity implements Callback.CommonCallback<String> {

    public void loadData(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, this);
    }

    @Override
    public void onSuccess(String result) {
        Log.i("json", "success");
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Log.i("json", "Error");
    }

    @Override
    public void onCancelled(CancelledException cex) {
        Log.i("json", "Cancelled");
    }

    @Override
    public void onFinished() {
        Log.i("json", "Finished");
    }
}
