package com.dgy.chatdog.utils;

import java.text.DecimalFormat;

/**
 * Created by DGY on 2017/2/17.
 */

public class ListUtils {

   /* public static List<MapEntity> getlist(String url, Context ctx, Application app){
    }*/

    /**
     * 格式化值
     * @param types
     * @param fotmatvals
     * @param val 要格式化的参数
     * @return
     */
    public static String formatData(int[] types, String[] fotmatvals, int val){
        try {
            if (types != null && types.length > 0) {
                for (int i = 0; i < types.length; i++) {
                    if (types[i] == val) {
                        return fotmatvals[i];
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 时间截取
     * @param time
     * @return YYYY-MM-dd
     */
    public static String formatTime(String time){
        if(time!=null && !time.equals("")){
            if(time.indexOf(" ")!=-1){
                String[] str=time.split(" ");
                return str[0];
            }

        }
        return time;
    }

    /**
     * 金额格式化
     * @param total
     * @param type 0不换算，1 换算成万,2,全部转换成万
     * @return
     */
    public static String formatTotal(String total, int type){
        if(total!=null && !total.trim().equals("") && !total.trim().equals("null")){
            Double value= Double.parseDouble(total);
            if(type==1 && value>=10000){
                value=value/10000;
            }
            if(type==2){
                value=value/10000;
            }
            if(total.indexOf(".")!=-1){
                DecimalFormat fmt=new DecimalFormat("##,###,###,###,###.##");
                total=fmt.format(value);
            }else{
                DecimalFormat fmt=new DecimalFormat("##,###,###,###,###");
                total=fmt.format(value);
            }
            if(type==1 && value>=10000){
                total=total+"万";
            }
        }else {
            total="0.0";
        }
        return total;

    }

    /**
     * 格式化金额，去掉最后一个0
     * @param total
     * @return
     */
    public static String formatTotal(Double total){
        String val="0";
        if(total!=null ){
            if (total%1==0.0){
                val= String.valueOf(total.intValue());
            }else{
                val=total.toString();
            }
            if(val.indexOf(".")!=-1){
                DecimalFormat fmt=new DecimalFormat("##,###,###,###,###.##");
                val=fmt.format(total);
            }else{
                DecimalFormat fmt=new DecimalFormat("##,###,###,###,###");
                val=fmt.format(total);
            }
        }else {
            return val;
        }
        return val;

    }


}
