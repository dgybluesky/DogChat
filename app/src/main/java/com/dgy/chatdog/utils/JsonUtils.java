package com.dgy.chatdog.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by DGY on 2017/2/17.
 */

public class JsonUtils {

    /**
     * json转换为mapentity
     * @param json
     * @return
     */

    public static MapEntity jsonToMap(String json) {
        if (is_json(json)) {
            JSONObject obj;
            try {
                obj = new JSONObject(json);
                return parseMap(obj);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * json转List<mapentity>
     * @param json
     * @return
     */

    public static List<MapEntity> jsonToList(String json) {
        if (is_json(json)) {
            try {
                JSONArray obj = new JSONArray(json);
                return parseList(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static MapEntity parseMap(JSONObject jsonObject) {
        if(jsonObject==null)return null;
        Iterator<String> ite = jsonObject.keys();
        try {
            MapEntity result = new MapEntity();
            while (ite.hasNext()) {
                String key = ite.next();
                Object o = jsonObject.get(key);
                if (o.getClass() == JSONObject.class) {
                    result.put(key, parseMap((JSONObject) o));
                } else if (o.getClass() == JSONArray.class) {
                    result.put(key, parseList((JSONArray) o));
                } else {
                    if(o!= JSONObject.NULL) {
                        result.put(key, o);
                    }
                }
            }
            return result;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static List<MapEntity> parseList(JSONArray jsonArray) {
        if(jsonArray==null)return null;
        List<MapEntity> result = new ArrayList<MapEntity>();
        for(int i=0; i<jsonArray.length(); i++){
            try {
                result.add(parseMap(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 判断是否json
     *
     * @param data
     *            {@link String} json字符串
     * @return
     */
    public static boolean is_json(String data) {
        try {
            if (data.startsWith("[")) {
                new JSONArray(data);
            } else {
                new JSONObject(data);
            }
            return true;
        } catch (Exception ex) {
        }
        return false;
    }


    /**
     * list集合转换成json
     *
     * @param list
     * @return json字符串
     */
    public static String listTojson(List<Map<String, Object>> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                json.append(new JSONObject(map));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    /**
     * list集合转换成 String(元素之前用逗号隔开)
     *
     * @param list
     * @return String字符串
     */
    public static String listToString(List<Integer> list ) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for(int i=0;i<list.size();i++){
                if(i<list.size()-1){
                    sb.append(list.get(i));
                    sb.append(",");
                }else{
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
}
