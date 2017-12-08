package com.dgy.chatdog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dgy.chatdog.adapte.ChatAdapter;
import com.dgy.chatdog.datebase.DBManager;
import com.dgy.chatdog.utils.ClientUtil;
import com.dgy.chatdog.utils.HideIMEUtil;
import com.dgy.chatdog.utils.JsonUtils;
import com.dgy.chatdog.utils.MapEntity;
import com.dgy.chatdog.utils.OtherUtils;
import com.dgy.chatdog.utils.PasswordHasher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private ListView listChat;
    private EditText content1;
    private Button submit;
    private List<MapEntity> lists;
    private ChatAdapter adapter;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {//
                    errorProgressDialog(msg.getData().getString("message"));
                }
                break;
                case 1: {//成功
                    initLists();
                    content1.setText("");
                }
                break;
                case 2: {
                    endProgressDialog();
                    listChat.setAdapter(adapter);
                    listChat.setSelection(adapter.getCount() - 1);
                }
                break;
                case -500: {
                    Toast.makeText(MainActivity.this, "网络错误，请求超时！", Toast.LENGTH_LONG).show();
                    endProgressDialog();
                }
                break;
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HideIMEUtil.wrap(this);//键盘管理，点击除editText外区域收起键盘
        ButterKnife.bind(this);
        setTitleBar("小丁丁",false);
        initView();
        initLists();
        //handler.postDelayed(runnable, 1000 * 60 * 2);
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 1000 * 60 * 2);
                initLists();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void initView(){
        listChat= (ListView) findViewById(R.id.list_chat);
        content1= (EditText) findViewById(R.id.content1);
        content1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    questionSub();
                    return true;
                }
                return false;
            }
        });
        submit= (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                questionSub();
            }
        });
    }

    private void initLists() {
        lists = dbManager.query();
        adapter = new ChatAdapter(MainActivity.this, lists);
        listChat.setAdapter(adapter);
        listChat.setSelection(adapter.getCount() - 1);
    }


    /**
     * 提交
     */
    private void questionSub() {
        final String contenttext = content1.getText().toString().trim();
        if (contenttext.equals("")) {
            //showtipsWarning("请输入您的内容！");
            return;
        }
        content1.setText("");
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MapEntity selfmap=new MapEntity();
        selfmap.put("text",contenttext);
        selfmap.put("createtime",sdf.format(d));
        dbManager.addChart(selfmap,null);
        lists.add(selfmap);
        listChat.setAdapter(adapter);
        listChat.setSelection(adapter.getCount() - 1);
        final ClientUtil client = new ClientUtil(MainActivity.this, getApplication());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", AppConstants.API_KEY);
                params.put("userid", PasswordHasher.Md5Encoder(OtherUtils.getDeviceId(MainActivity.this)));
                params.put("info",contenttext);
                final MapEntity map = JsonUtils.jsonToMap(client.getServerJson("", params));
                Bundle bundle = new Bundle();
                Message msg = new Message();
                if (map != null) {
                    final List<MapEntity> articlelist=map.getAnyObject("list", ArrayList.class);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Date d = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            map.put("createtime",sdf.format(d));
                            int chatid=dbManager.addChart(map,articlelist);
                            map.put("id",chatid);
                            lists.add(map);
                            listChat.setAdapter(adapter);
                            listChat.setSelection(adapter.getCount() - 1);
                        }
                    });
                } else {
                    bundle.putString("message", "请求出错！");
                    msg.setData(bundle);
                    msg.what = 0;
                }

            }
        }).start();

    }

}
