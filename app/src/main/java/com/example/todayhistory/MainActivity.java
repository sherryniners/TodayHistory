package com.example.todayhistory;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.todayhistory.bean.HistoryAdapter;
import com.example.todayhistory.bean.HistoryBean;
import com.example.todayhistory.bean.LaoHuangLiBean;
import com.example.todayhistory.common.BaseActivity;
import com.example.todayhistory.common.ContentURL;
import com.google.gson.Gson;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    //主界面控件
    private ListView mainLv;
    private ImageButton imgBtn;
    private List<HistoryBean.ResultBean> datas;
    private HistoryAdapter adapter;
    private Calendar calendar;
    private HistoryBean historyBean;
    private Date date;
    //头布局的控件
    private TextView tvSolar, tvDay, tvWeek, tvLunar, tvBaiJi, tvWuXin, tvChongSha, tvJiShen, tvXiongShen, tvYi, tvJi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //初始化控件
    public void init() {
        //实例化控件
        mainLv = findViewById(R.id.mainLv);
        imgBtn = findViewById(R.id.mainImgBtn);
        imgBtn.setOnClickListener(this);
        datas = new ArrayList<>();
        adapter = new HistoryAdapter(this, datas);
        mainLv.setAdapter(adapter);
        //添加点击事件
        mainLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, HistoryDescribeActivity.class);
                //由于添加了头布局，位置要减1
                //HistoryBean.ResultBean resultBean = datas.get(position - 1);
                //或者使用下面的方法获取正确位置的数据
                HistoryBean.ResultBean resultBean = (HistoryBean.ResultBean) parent.getAdapter().getItem(position);
                String beanId = resultBean.get_id();
                intent.putExtra("historyId", beanId);
                startActivity(intent);
            }
        });

        //获取日历对象
        calendar = Calendar.getInstance();
        //当前时间
        date = new Date();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //添加头布局和尾布局
        addHeaderAndFooterView();

        //获取历史上的今天URL
        String todayHistoryURL = ContentURL.getTodayHistoryURL("1.0", month, day);
        //加载历史上的今天接口的数据
        loadData(todayHistoryURL);
    }

    //添加界面的头布局和尾布局
    private void addHeaderAndFooterView() {
        //将头布局和尾布局xml布局文件转换成View对象
        View headerView = LayoutInflater.from(this).inflate(R.layout.main_header, null);
        View footerView = LayoutInflater.from(this).inflate(R.layout.main_footer, null);
        //给ListView添加头布局和尾布局
        // false代表头布局或者脚布局不可点击,第二个参数代表与listview绑定的数据可传null
        //添加了头布局后头布局也算索引
        mainLv.addHeaderView(headerView,null,false);
        mainLv.addFooterView(footerView,null,false);
        //初始化头布局和加载数据
        initHeaderView(headerView);
        //给尾布局添加点击事件
        footerView.setOnClickListener(this);
    }

    /**
     * 初始化头布局的控件和加载数据
     *
     * @param headerView
     */
    public void initHeaderView(View headerView) {
        tvSolar = headerView.findViewById(R.id.main_header_tvSolar);
        tvDay = headerView.findViewById(R.id.main_header_tvDay);
        tvWeek = headerView.findViewById(R.id.main_header_tvWeek);
        tvLunar = headerView.findViewById(R.id.main_header_tvLunar);
        tvBaiJi = headerView.findViewById(R.id.main_header_tvBaiJi);
        tvWuXin = headerView.findViewById(R.id.main_header_tvWuXin);
        tvChongSha = headerView.findViewById(R.id.main_header_tvChongSha);
        tvJiShen = headerView.findViewById(R.id.main_header_tvJiShen);
        tvXiongShen = headerView.findViewById(R.id.main_header_tvXiongShen);
        tvYi = headerView.findViewById(R.id.main_header_tvYi);
        tvJi = headerView.findViewById(R.id.main_header_tvJi);
        //将日期对象格式化成指定对象的字符串
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String time = sdf.format(date);
        //获取老黄历URL
        String laoHaungLiURL = ContentURL.getLaohuangliURL(time);
        //获取老黄历接口数据
        loadHeaderData(laoHaungLiURL);
    }

    /**
     * 获取老黄历接口的数据
     *
     * @param laoHaungLiURL
     */
    private void loadHeaderData(String laoHaungLiURL) {
        RequestParams params = new RequestParams(laoHaungLiURL);
        x.http().get(params, new CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("laohuangli", "success");
                LaoHuangLiBean laoHuangLiBean = new Gson().fromJson(result, LaoHuangLiBean.class);
                LaoHuangLiBean.ResultBean resultBean = laoHuangLiBean.getResult();
                //给TextView赋值
                tvLunar.setText("农历" + resultBean.getYinli() + "(阴历)");
                String[] yangliArr = resultBean.getYangli().split("-");
                String week = getWeek(Integer.parseInt(yangliArr[0]), Integer.parseInt(yangliArr[1]), Integer.parseInt(yangliArr[2]));
                tvSolar.setText("公历 " + yangliArr[0] + "年" + yangliArr[1] + "月" + yangliArr[2] + "日 " + week + "(阳历)");
                tvDay.setText(yangliArr[2]);
                tvWeek.setText(week);
                tvBaiJi.setText("彭祖百忌:" + resultBean.getBaiji());
                tvWuXin.setText("五行:" + resultBean.getWuxing());
                tvChongSha.setText("冲煞:" + resultBean.getChongsha());
                tvJiShen.setText("吉神宜趋:" + resultBean.getJishen());
                tvXiongShen.setText("凶神宜忌:" + resultBean.getXiongshen());
                tvYi.setText("宜:" + resultBean.getYi());
                tvJi.setText("忌:" + resultBean.getJi());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    // 根据年月日获取对应的星期
    private String getWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        String[] weeks = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (index < 0) { //以防万一
            index = 0;
        }
        return weeks[index];
    }


    @Override
    public void onSuccess(String result) {
        Log.i("Json", "success");
        datas.clear();
        historyBean = new Gson().fromJson(result, HistoryBean.class);
        List<HistoryBean.ResultBean> list = historyBean.getResult();
        for (int i = 0; i < 5; i++) {
            datas.add(list.get(i));
        }
        //更新数据
        adapter.notifyDataSetChanged();
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainImgBtn:
                //Toast.makeText(this, "点击日历", Toast.LENGTH_SHORT).show();
                //弹出日期对话框，选择日期后更新显示的数据
                popCalendarDialog();
                break;
            case R.id.footer:
                //跳转到历史上的今天你更多的界面
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                if (historyBean != null) {//历史上的今天不为空
                    Bundle bundle = new Bundle();//用Bundle存储数据
                    bundle.putSerializable("history", historyBean);
                    intent.putExtras(bundle);//把bundle添加到intent中
                }
                //跳转界面
                startActivity(intent);
                break;
        }
    }

    //弹出日历对话框，选择日期后更新显示的数据
    private void popCalendarDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            /**
             * 选择日期，然后加载选择的日期的老黄历和历史上的今年的数据
             * @param view
             * @param year
             * @param month
             * @param dayOfMonth
             */
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String time = year + "-" + (month + 1) + "-" + dayOfMonth;
                //更新老黄历显示内容
                String laoHuangLiURL = ContentURL.getLaohuangliURL(time);
                loadHeaderData(laoHuangLiURL);

                //更新历史上的今天的数据
                String todayHistoryURL = ContentURL.getTodayHistoryURL("1.0", month + 1, dayOfMonth);
                loadData(todayHistoryURL);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void jumptodiary(View view) {
        Intent intent = new Intent();
        intent.setClass(this,DiaryActivity.class);
        startActivity(intent);
    }
}
