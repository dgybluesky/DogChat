package com.dgy.chatdog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;


import com.dgy.chatdog.BaseActivity;
import com.dgy.chatdog.R;
import com.dgy.chatdog.adapte.NewsAdapter;
import com.dgy.chatdog.utils.MapEntity;

import java.util.List;

/**
 * Created by DGY on 2017/8/31.
 * 新闻动态
 */

public class NewsCenterActivity extends BaseActivity {

    private ListView list_news;
    private List<MapEntity> lists;
    private NewsAdapter newad;
    private int chartid;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:{//加载数据列表
                    endProgressDialog();
                }break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setTitleBar("新闻详情",true);
        chartid=getIntent().getIntExtra("chartid",0);
        initview();
        initlists();
    }

    private void initview() {
        list_news= (ListView) findViewById(R.id.list_news);
    }

    private void initlists() {
        lists = dbManager.queryArticleByChartid(chartid);
        newad = new NewsAdapter(NewsCenterActivity.this, lists);
        list_news.setAdapter(newad);
    }

}
