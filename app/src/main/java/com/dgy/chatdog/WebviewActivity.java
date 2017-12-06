package com.dgy.chatdog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


import com.dgy.chatdog.utils.OtherUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * dgy 20170209
 */
public class WebviewActivity extends BaseActivity {

    private String url;
    private LinearLayout titlebar;
    private LinearLayout back;
    private boolean needrefresh=true;
    private String nowurl;
    private WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_webview);
        titlebar= (LinearLayout) findViewById(R.id.titlebar);
        back= (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        nowurl=url;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(needrefresh){
            startLoad(nowurl);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        webView.loadUrl("");
    }

    /**
     *
     * @param url webview 加载url
     */
    public void startLoad(String url) {
        webView = (WebView) findViewById(R.id.webview);
        webView=initWebview(webView);
        webView.addJavascriptInterface(new WebViewInterface(this),"webviewInstance");
        final Map<String,String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("DEVICE-UNIONID", OtherUtils.getDeviceId(WebviewActivity.this));
        extraHeaders.put("APP-SOURCE", "CHATDOG-APP");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //System.out.println("###############on Page Finished");
                endProgressDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                endProgressDialog();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                endProgressDialog();
                if (url.indexOf("chinapnr")>-1){
                    titlebar.setVisibility(View.VISIBLE);
                    needrefresh=false;
                }else{
                    titlebar.setVisibility(View.GONE);
                    needrefresh=true;
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //System.out.println("###############shouldOverrideUrlLoading");
                if(checkurl(url)){
                    startProgressDialog();
                    view.loadUrl(url,extraHeaders);
                    nowurl=url;
                    if (url.indexOf("chinapnr")>-1){
                        titlebar.setVisibility(View.VISIBLE);
                        needrefresh=false;
                    }else{
                        titlebar.setVisibility(View.GONE);
                        needrefresh=true;
                    }
                }
                return true;
            }


        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                needrefresh=false;

               /* final DownloadUtils download=new DownloadUtils(WebviewActivity.this,null);
                download.showDownloadDialog(url);*/
            }
        });
        startProgressDialog();
        webView.loadUrl(url,extraHeaders);
        if (webView.getUrl()!=null && webView.getUrl().indexOf("chinapnr")>-1){
            titlebar.setVisibility(View.VISIBLE);
            needrefresh=false;
        }else{
            titlebar.setVisibility(View.GONE);
            needrefresh=true;
        }

    }

    /**
     * 检测连接是否需要跳转
     * @param url
     * checks -cert 认证中心 -member 会员中心
     * @return
     */
    private boolean checkurl(String url){
        if (url.indexOf("mobile=app")>-1){
            doUrl("close");
        }
        String[] checks=new String[]{"close","login","realnamecert","regist","cert","member"};
        for(String u:checks){
            if(("app://"+u).equals(url.trim())){
                doUrl(u);
                return false;
            }

        }
        return true;
    }

    /**
     * 做相应的动作
     */
    private void doUrl(String urlval){
        switch (urlval){
            case "login":{
                needrefresh=true;
                //startActivity(new Intent(WebviewActivity.this,LoginActivity.class));
            }break;
            case "close":{
                finish();
            }break;
            case "realnamecert" :{
                needrefresh=true;
                //startActivity(new Intent(WebviewActivity.this,MemberAccountAuthentieActivity.class));
            }break;
            case "regist":{
                needrefresh=true;
                //startActivity(new Intent(WebviewActivity.this,RegisterActivity.class));
            }break;
            case "cert":{
                needrefresh=true;
                //startActivity(new Intent(WebviewActivity.this,MemberAccountAuthentieActivity.class));
            }break;
            case "member":{
                finish();
            }break;
            default:break;
        }
    }

    /**
     * 加载js
     * @param jsurl
     */
    public void loadjs(String jsurl){
        startLoad(jsurl);
    }
    class WebViewInterface{

        private Context context;
        public WebViewInterface(Context context){
            this.context = context;
        }
        @JavascriptInterface//这个是android 17之后必须的
        public void endPage(){   //此方法为js调用的方法
//            Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}