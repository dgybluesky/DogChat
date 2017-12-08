package com.dgy.chatdog.datebase;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dgy.chatdog.AppConstants;
import com.dgy.chatdog.utils.MapEntity;

public class DBManager
{
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context)
    {
        helper = new DatabaseHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    /**
     * 添加一条聊天记录
     * 
     * @param chat
     * @return chartid 返回插入的聊天记录Id
     */
    public int addChart(MapEntity chat,List<MapEntity> articlelist)
    {
        int chartid =0;
        // 采用事务处理，确保数据完整性
        db.beginTransaction(); // 开始事务
        try
        {

            db.execSQL("INSERT INTO " + DatabaseHelper.MAIN_TABLE_NAME
                    + " VALUES(null, ?, ?, ? ,?)", new Object[] { chat.getString("code",null),
                    chat.getString("text",null), chat.getString("url",null),chat.getString("createtime",null)});
            Cursor c = db.rawQuery("SELECT last_insert_rowid()", null);
            c.moveToFirst();
            chartid =c.getInt(0);
            if (articlelist!=null && articlelist.size()>0){
                for(MapEntity map:articlelist){
                    db.execSQL("INSERT INTO " + DatabaseHelper.ARTICLE_TABLE_NAME
                            + " VALUES(null, ?, ?, ? ,? ,?)", new Object[] { chartid,map.getString("article",null),
                            map.getString("source",null), map.getString("detailurl",null),map.getString("icon",null)});
                }
            }
            db.setTransactionSuccessful(); // 设置事务成功完成
        }
        finally
        {
            db.endTransaction(); // 结束事务
        }
        return chartid;
    }



    /**
     * query all persons, return list
     * 
     * @return List<Person>
     */
    public List<MapEntity> query()
    {
        Log.d(AppConstants.LOG_TAG, "DBManager --> query");
        ArrayList<MapEntity> charts = new ArrayList<MapEntity>();
        Cursor c = queryChatCursor();
        while (c.moveToNext())
        {
            MapEntity chart = new MapEntity();
            int chartid=c.getInt(c.getColumnIndex("_id"));
            chart.put("id",chartid);
            chart.put("code",c.getString(c.getColumnIndex("code")));
            chart.put("text",c.getString(c.getColumnIndex("text")));
            chart.put("url",c.getString(c.getColumnIndex("url")));
            chart.put("createtime",c.getString(c.getColumnIndex("createtime")));
            List<MapEntity> articlelist=queryArticleByChartid(chartid);
            if (articlelist!=null && articlelist.size()>0){
                chart.put("list",articlelist);
            }
            charts.add(chart);
        }
        c.close();
        return charts;
    }

    /**
     * 根据聊天id查询新闻详情
     * @param chartid
     * @return
     */
    public List<MapEntity> queryArticleByChartid(int chartid){
        List<MapEntity> articlelist=new ArrayList<MapEntity>();
        Cursor articlecursor=queryArticleCursor(chartid);
        while (articlecursor.moveToNext()){
            MapEntity article = new MapEntity();
            article.put("article",articlecursor.getString(articlecursor.getColumnIndex("article")));
            article.put("source",articlecursor.getString(articlecursor.getColumnIndex("source")));
            article.put("detailurl",articlecursor.getString(articlecursor.getColumnIndex("detailurl")));
            article.put("icon",articlecursor.getString(articlecursor.getColumnIndex("icon")));
            articlelist.add(article);
        }
        return articlelist;
    }

    /**
     * query all persons, return cursor
     * 
     * @return Cursor
     */
    private Cursor queryChatCursor()
    {
        Log.d(AppConstants.LOG_TAG, "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.MAIN_TABLE_NAME,
                null);
        return c;
    }

    /**
     * 查询新闻信息
     * @param chartid
     * @return
     */
    private Cursor queryArticleCursor(int chartid)
    {
        Log.d(AppConstants.LOG_TAG, "DBManager --> queryTheCursor");
        Cursor c = db.rawQuery("SELECT * FROM " + DatabaseHelper.ARTICLE_TABLE_NAME+ " WHERE chartid = "+chartid,
                null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB()
    {
        Log.d(AppConstants.LOG_TAG, "DBManager --> closeDB");
        // 释放数据库资源
        db.close();
    }

}