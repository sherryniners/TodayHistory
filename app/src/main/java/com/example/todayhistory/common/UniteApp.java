package com.example.todayhistory.common;

import android.app.Application;

import org.xutils.x;

/**
 * 初始化程序
 */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化
        x.Ext.init(this);
        //x.Ext.setDebug(true); // 是否输出debug日志...
    }
}
