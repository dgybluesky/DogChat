package com.dgy.chatdog.utils;

/**
 * Created by DGY on 2017/2/17.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

/**
 * Map扩展类 extends of HashMap &lt;String, Object&gt;
 * @author Aidy
 *
 */
public class MapEntity extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 取得int值，无值则返回0
     * @param key String MapKey
     * @return  int int型值
     */
    public int getInt(String key){
        return getInt(key, 0);
    }
    /**
     * 取得int值，无值返回defval
     * @param key String MapKey
     * @param defval	int 默认值
     * @return	int int型值
     */
    public int getInt(String key, int defval){
        return getInteger(key, defval);
    }
    /**
     * 取得Integer值，无值则返回null
     * @param key String MapKey
     * @return  Integer Integer值
     */
    public int getInteger(String key){
        return getInteger(key, null);
    }
    /**
     * 取得Integer值
     * Integer getInteger
     * @author Aidy 2016年11月3日上午9:42:51
     * @param key {@link String} MapKey
     * @param defval	{@link Integer} 默认值(可以为null)
     * @return	{@link Integer}
     */
    public Integer getInteger(String key, Integer defval){
        Object v = get(key);
        if(v!=null){
            if(ValidateUtils.isInteger(v.toString(), true)){
                return Integer.parseInt(v.toString());
            }else if(v.getClass().equals(Double.class)){
                return new BigDecimal(getString(key, "0")).intValue();
            }
        }
        return defval;
    }
    /**
     * 取得double值，无值返回0.0
     * @param key	String MapKey
     * @return	double double型值
     */
    public double getDouble(String key){
        return getDouble(key, 0.0);
    }
    /**
     * 取得double值，无值返回defval
     * @param key	String MapKey
     * @param defval	double 默认值
     * @return	double double型值
     */
    public double getDouble(String key, double defval){
        Object v = get(key);
        if(v!=null){
            if(ValidateUtils.isDouble(v.toString())){
                return Double.parseDouble(get(key).toString());
            }
            if(v.getClass().equals(Double.class)){
                return new BigDecimal(v.toString()).doubleValue();
            }
        }
        return defval;
    }


    /**
     * 取得Double值，无值返回defval(允许为null)
     * @param key	String MapKey
     * @param defval	{@link Double} 默认值
     * @return	{@link Double} Double型值
     */
    public Double getDouble(String key, Double defval){
        Object v = get(key);
        if(v!=null){
            if(ValidateUtils.isDouble(v.toString())){
                return Double.parseDouble(get(key).toString());
            }
            if(v.getClass().equals(Double.class)){
                return new BigDecimal(v.toString()).doubleValue();
            }
        }
        return defval;
    }


    /**
     * 取得Long值
     * @param key	String MapKey
     * @return	{@link Long} long型值
     */
    public long getLong(String key){
        return getLong(key,0L);
    }
    /**
     * 取得Long值，无值返回defval
     * @param key	String MapKey
     * @param defval	{@link Long} 默认值
     * @return	{@link Long} long型值
     */
    public long getLong(String key, Long defval){
        Object v = get(key);
        if(v!=null){
            if(ValidateUtils.isInteger(v.toString(), true)){
                return Long.valueOf(get(key).toString());
            }
            if(v.getClass().equals(Long.class) || v.getClass().equals(Integer.class) || v.getClass().equals(Double.class)){
                return new BigDecimal(v.toString()).longValue();
            }
        }
        return defval;
    }

    /**
     * 取得String值,无值返回EmptyString
     * @param key	String MapKey
     * @return	{@link String} string型值
     */
    public String getString(String key){
        return getString(key, "");
    }
    /**
     * 取得String值，无值返回defval
     * @param key	String MapKey
     * @param defval	{@link String} 默认值
     * @return	{@link String} String型值
     */
    public String getString(String key, String defval){
        if(get(key)!=null){
            return get(key).toString();
        }
        return defval;
    }
    /**
     * 取得Boolean值，无则返回false
     * @param key	String MapKey
     * @return	{@link Boolean} Boolean型值
     */
    public boolean getBoolean(String key){
        return getBoolean(key, false);
    }
    /**
     * 取得Boolean值，无值返回defval
     * @param key	String MapKey
     * @param defval	{@link Boolean} 默认值
     * @return	{@link Boolean} Boolean型值
     */
    public boolean getBoolean(String key, boolean defval){
        if(get(key)!=null && (get(key).toString().trim().equalsIgnoreCase("true") || get(key).toString().trim().equalsIgnoreCase("false"))){
            return Boolean.parseBoolean(get(key).toString().toLowerCase());
        }
        return false;
    }

    /**
     * 取得List对象
     * @param key	String MapKey
     * @param clz	{@link Class}&lt;T&gt; 转换类型
     * @param <T>	类型
     * @return	{@link List}&lt;T&gt;
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> clz){
        if(get(key)!=null){
            return (List<T>)get(key);
        }
        return null;
    }



    /**
     * 取出Timestame对象
     * @param key	String MapKey
     * @return	{@link Timestamp}
     */
    public Timestamp getTimestamp(String key){
        if(get(key)!=null){
            return (Timestamp)get(key);
        }
        return null;
    }

    /**
     * 根据传入类型取出对象
     * @param key	String key
     * @param T		Class&lt;T&gt; 类型
     * @param <T> 类型
     * @return	T
     */
    @SuppressWarnings("unchecked")
    public <T> T getAnyObject(String key, Class<T> T){
        System.out.println(get(key));
        if(get(key)!=null){
            return (T)get(key);
        }
        return null;
    }




}
