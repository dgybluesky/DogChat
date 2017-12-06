package com.dgy.chatdog.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.dgy.chatdog.App;
import com.dgy.chatdog.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by swj on 14-3-31.
 */
public class ClientUtil {

    private Application app;
    private Context context;

    public ClientUtil(Context ctx, Application application){
        this.context=ctx;
        this.app=application;
    }

    /**
     *
     * 函数名称: parseData
     * 函数描述: 将json字符串转换为 Map<String, Object>
     * @param json
     * @return
     */
    public Map<String, Object> getMapjson(String json){
        JSONObject jsonObject ;
        try {

            jsonObject = new JSONObject(json);

            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
        return null;
    }


    /**
     * 获取List<Map<String, Object>>
     * @param json
     * @return
     */
    public List<Map<String,Object>> getMapList(String json){
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObj ;
            list = new ArrayList<Map<String,Object>>();
            for(int i = 0 ; i < jsonArray.length() ; i ++){
                jsonObj = (JSONObject)jsonArray.get(i);
                list.add(getMapjson(jsonObj.toString()));
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 获得服务器Json数据带参版本
     * @param url
     * @param map
     * @return
     */
    public String getServerJson(String url, Map<String,String> map){
        String result  = getJson(url,map);
        return result;
    }
    /**
     * 获得服务器Json数据
     * @param url
     * @return
     */
    public String getServerJson(String url){
        String result  = getJson(url,null);
        return result;
    }

    /**
     * 获取网络JSON数据
     * @param url
     * @param map
     * @return
     */
    private String getJson(String url, Map<String,String> map){
        HttpClient client = new DefaultHttpClient();
        HttpPost request;
        String beanListToJson=null;
        try {
            String requesturl=app.getString(R.string.mianurl)+url;
            request = new HttpPost(new URI(requesturl));
            App app=(App)context.getApplicationContext();
            request.setHeader("User-Agent"," "+";DOGCHAT-APP/ANDROID/"+ OtherUtils.getVersion(context)+"/"+OtherUtils.getDeviceId(context)+"/"+OtherUtils.getPhoneModel()+"/default/");
            SharedPreferences sp = context.getSharedPreferences("SP", MODE_PRIVATE);
            request.setHeader("DEVICE-UNIONID", OtherUtils.getDeviceId(context));
            if(map!=null && map.size()>0){
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for(String key : map.keySet())
                {
                    //封装请求参数
                    String val = map.get(key);
                    System.out.println("send:" + key + "=" + val);
                    params.add(new BasicNameValuePair(key , val));
                }
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            HttpResponse response  =  client.execute(request);
            //判断请求是否成功
            if (response.getStatusLine().getStatusCode() == 200) {// 200表示请求成功
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    beanListToJson = EntityUtils.toString(entity, HTTP.UTF_8);
                }else{
                    beanListToJson="{\"success\":\"false\",\"message\":\"网络错误，请求超时！\"}";
                }
            }else{
                beanListToJson="{\"success\":\"false\",\"message\":\"网络错误，请求超时！\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            beanListToJson="{\"success\":\"false\",\"message\":\"服务器错误！\"}";
        }
        return  beanListToJson;
    }
}

