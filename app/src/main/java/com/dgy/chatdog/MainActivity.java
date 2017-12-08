package com.dgy.chatdog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dgy.chatdog.adapte.ChatAdapter;
import com.dgy.chatdog.utils.ClientUtil;
import com.dgy.chatdog.utils.HideIMEUtil;
import com.dgy.chatdog.utils.JsonUtils;
import com.dgy.chatdog.utils.MapEntity;
import com.dgy.chatdog.utils.OtherUtils;
import com.dgy.chatdog.utils.PasswordHasher;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity {

    private ListView listChat;
    private EditText content1;
    private Button submit;
    private List<MapEntity> lists;
    private ChatAdapter adapter;
    private TextView title_titleright;
    private ResideMenu resideMenu;

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
        initMenu();
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

    private void initMenu(){
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.mipmap.menubg);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        // create menu items;
        String titles[] = { "联系作者", "清理聊天记录" };
        int icon[] = { R.mipmap.contact, R.mipmap.clean };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            if (i==0){
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showtipsWarning("作者QQ：807524357，微信邮箱同号，欢迎骚扰~");
                    }
                });
            }
            if (i==1){
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("提示信息")
                                .setContentText("您确定要清空聊天记录么~")
                                .setCancelText("取消")
                                .setConfirmText("确定")
                                .showCancelButton(true)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        dbManager.clean();
                                        lists.clear();
                                        adapter=new ChatAdapter(MainActivity.this,lists);
                                        listChat.setAdapter(adapter);
                                        resideMenu.closeMenu();
                                        sweetAlertDialog.cancel();
                                    }
                                })
                                .show();
                    }
                });
            }
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
        }
    }

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
        title_titleright= (TextView) findViewById(R.id.title_righttitle);
        title_titleright.setText("菜单");
        title_titleright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
        title_titleright.setVisibility(View.VISIBLE);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

}
