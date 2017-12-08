package com.dgy.chatdog;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.dgy.chatdog.datebase.DBManager;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by DGY on 2017/5/3.
 */

public class BaseActivity extends AppCompatActivity {

    private SweetAlertDialog pDialog;
    protected TranslateAnimation showAnim;
    protected TranslateAnimation hiddenAmin;
    protected DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new DBManager(this);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        //控件显示的动画
        showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
                ,-1.0f,Animation.RELATIVE_TO_SELF,0.0f);
        showAnim.setDuration(100);

        //控件隐藏的动画
        hiddenAmin = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,Animation.RELATIVE_TO_SELF
                ,0.0f,Animation.RELATIVE_TO_SELF,-1.0f);
        hiddenAmin.setDuration(100);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    /**
     * 初始化webview
     * @param webview
     * @param html 要加载的html
     * @return
     */

    public void initWebview(WebView webview, String html){
        webview=initWebview(webview);
        StringBuilder sb = new StringBuilder();
        //拼接一段HTML代码
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>dgy的机器汪</title>");
        sb.append("<meta content=\"width=device-width,height=device-height, initial-scale=1.0, maximum-scale=1.0, user-scalable=1\" name=\"viewport\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(html);
        sb.append("</body>");
        sb.append("</html>");
        //使用简单的loadData方法会导致乱码，可能是Android API的Bug
        //show.loadData(sb.toString(), "text/html", "utf-8");
        //加载、并显示HTML代码
        webview.loadDataWithBaseURL(null,sb.toString(), "text/html", "utf-8", null);
    }

    /**
     * 初始化webview
     * @param webview
     * @return
     */
    protected WebView initWebview(WebView webview){
        webview.getSettings().setDefaultFontSize(20);
        webview.getSettings().setTextZoom(100);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setUseWideViewPort(true);//设定支持viewport
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setDisplayZoomControls(false);
        return webview;
    }

    /**
     * 用webview打开页面
     * @param url
     */
    protected void gowebview(String url){
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        Intent intent = new Intent(BaseActivity.this, WebviewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 调用外部浏览器打开链接
     * @param url
     */
    public void openWebForOutside(String url){
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            showtipsWarning("该链接有误~暂不支持打开~~");
        }
    }



    public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);
    }

    /**
     *
     * @param gridView
     */
    public void setGridViewHeightBasedOnChildren(GridView gridView, int chirdcount, int marginone) {

        ListAdapter listAdapter = gridView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int heitcount= 1;
        if (listAdapter.getCount()>chirdcount){
            String count=String.valueOf(listAdapter.getCount()/(chirdcount+1));
            if (count.indexOf(".")>-1){
                count=count.substring(0,count.indexOf("."));
            }
            heitcount= Integer.parseInt(count)+1;
        }

        for (int i = 0; i < heitcount; i++) {
            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight()+marginone;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();

        //int height=gridView.getHeight();
        int height=0;
        params.height = totalHeight
                + (height * (listAdapter.getCount() - 1));

        //((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        gridView.setLayoutParams(params);
    }



    /**
     * 复制内容到剪切板
     * @param content
     */
    public void copytext(String content){
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
        Toast.makeText(this,"已成功复制内容！",Toast.LENGTH_SHORT).show();
    }


    private int i = -1;
    Handler progresshandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
                        i=-1;
                        break;
                }
            }
        }
    };
    Timer timer ;
    TimerTask task ;
    /**
     * 进度条开始
     */
    public void startProgressDialog(String msg){
        try {
            if (!pDialog.isShowing()){
                pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText((msg==null || msg.trim().equals(""))?getResources().getString(R.string.loading):msg);
                pDialog.setCancelable(false);
                pDialog.show();
                timer = new Timer();
                task = new TimerTask() {

                    @Override
                    public void run() {
                        // 需要做的事:发送消息
                        Message message = new Message();
                        message.what = 1;
                        progresshandler.sendMessage(message);
                    }
                };
                timer.schedule(task,800,800);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 进度条开始
     */
    public void startProgressDialog(){
        startProgressDialog(null);
    }

    /**
     * 进度条结束
     */
    public void endProgressDialog(){
        if(pDialog!=null){
            if (pDialog.isShowing()) {
                pDialog.dismiss();
                timer.cancel();
                i=-1;
            }
        }
    }

    /**
     * 进度条加载成功
     * @param msg
     */
    public void successProgressDialog(String msg, boolean closeactivity, final SweetAlertDialog.OnSweetClickListener listener){
            if (listener!=null){
                pDialog.setConfirmClickListener(listener);
            }else{
                if (closeactivity) {
                    pDialog.setCancelable(false);
                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    });
                }else{
                    pDialog.setCancelable(true);
                }
        }
        pDialog.setTitleText("成功")
                .setContentText(msg)
                .setConfirmText("确定")
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        if (timer!=null) {
            timer.cancel();
        }
        i=-1;
    }

    public void successProgressDialog(String msg){
        successProgressDialog(msg,false,null);
    }

    public void successProgressDialog(String msg,boolean closeactivity){
        successProgressDialog(msg,closeactivity,null);
    }

    public void errorProgressDialog(String msg){
        errorProgressDialog(msg,false);
    }

    /**
     * 进度条加载失败
     * @param msg
     */
    public void errorProgressDialog(String msg ,boolean closeactivity){
        if (closeactivity){
            pDialog.setCancelable(false);
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            });
        }else{
            pDialog.setCancelable(true);
        }
        pDialog.setTitleText("失败")
                .setContentText(msg)
                .setConfirmText("关闭")
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
        if (timer!=null) {
            timer.cancel();
        }
        i=-1;
    }

    public void showtipsError(String msg){
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("错误提示")
                .setConfirmText("确定")
                .setContentText((msg))
                .show();
    }

    public void showtipsWarning(String msg){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示信息")
                .setConfirmText("确定")
                .setContentText((msg))
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        endProgressDialog();
        dbManager.closeDB();
    }

    /**
     * 设置标题
     */

    private TextView title_title;
    private ImageView title_back;
    private LinearLayout title_titlelayout;
    public void setTitleBar(String title,boolean showback){
        title_title= (TextView) findViewById(R.id.title_title);
        title_back= (ImageView) findViewById(R.id.title_back);
        title_titlelayout= (LinearLayout) findViewById(R.id.titlelayout);
        title_titlelayout.setVisibility(View.VISIBLE);
        if (showback){
            title_back.setVisibility(View.VISIBLE);
        }else{
            title_back.setVisibility(View.GONE);
        }
        title_title.setText(title);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
