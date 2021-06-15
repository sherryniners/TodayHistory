package com.example.todayhistory;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todayhistory.bean.HistoryDescribeBean;
import com.example.todayhistory.common.BaseActivity;
import com.example.todayhistory.common.ContentURL;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class HistoryDescribeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack, imgShare, imgDesc;
    private TextView tvTitle, tvContent;
    private HistoryDescribeBean.ResultBean resultBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_describe);

        init();
    }

    public void init() {
        imgBack = findViewById(R.id.historyDesc_imgBack);
        imgShare = findViewById(R.id.historyDesc_imgShare);
        imgDesc = findViewById(R.id.historyDesc_imgDesc);
        tvTitle = findViewById(R.id.historyDesc_tvTitle);
        tvContent = findViewById(R.id.historyDesc_tvContent);

        imgBack.setOnClickListener(this);
        imgShare.setOnClickListener(this);

        String historyId = getIntent().getStringExtra("historyId");
        String historyDescURL = ContentURL.getHistoryDescURL("1.0", historyId);
        loadData(historyDescURL);
    }

    @Override
    public void onSuccess(String result) {
        //解析json
        HistoryDescribeBean historyDescribeBean = new Gson().fromJson(result, HistoryDescribeBean.class);
        resultBean = historyDescribeBean.getResult().get(0);
        tvTitle.setText(resultBean.getTitle());
        tvContent.setText(resultBean.getContent());
        String picURL = resultBean.getPic();
        if (TextUtils.isEmpty(picURL)) {
            imgDesc.setVisibility(View.GONE);
        } else {
            imgDesc.setVisibility(View.VISIBLE);
            Picasso.with(this).load(picURL).into(imgDesc);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.historyDesc_imgBack://返回上一界面
                finish();
                break;
            case R.id.historyDesc_imgShare://分享
                String text = "我发现一款好用的软件-历史上的今天，快来一起探索这个APP吧！";
                if (resultBean != null) {
                    text = "想要了解" + resultBean.getTitle() + "详情么？快来下载历史上的今天APP吧！";
                }
                //隐式意图
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, "历史上的今天"));
                break;
        }
    }
}
