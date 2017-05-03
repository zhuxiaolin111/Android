package com.huafa.lixianjicha;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.huafa.model.SQLite_Jicha_Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chensiqi on 2016/11/7.
 * 通过这个方法来封装所有的数据库操作方法
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context){
        helper = new DBHelper(context);
        //getWritableDatabase内部调用了openorcreateDatabase()方法
        db = helper.getWritableDatabase();
    }

    public void add(List<SQLite_Jicha_Model> jichashuju){
        //开始事务
        db.beginTransaction();
        try {
            for (SQLite_Jicha_Model sqLite_jicha_model : jichashuju) {
                db.execSQL("insert into jicha values(null , ? ,? ,? ,? ,? ,?, ?, ?, ?, ?)",
                        new String[]{
                                sqLite_jicha_model.RoomID,
                                sqLite_jicha_model.Num,
                                sqLite_jicha_model.Type,
                                sqLite_jicha_model.Item,
                                sqLite_jicha_model.Remark,
                                sqLite_jicha_model.media1,
                                sqLite_jicha_model.media2,
                                sqLite_jicha_model.media3,
                                sqLite_jicha_model.Operator,
                                sqLite_jicha_model.shifoujicha});
                //设置事务成功完成
                db.setTransactionSuccessful();
            }
        }catch (SQLiteException se){
            se.printStackTrace();
        }finally {
            //结束事物
            db.endTransaction();
        }
    }


    //将数据库里的所有数据放入一个Curse中,以便后续的数据使用
    public List<SQLite_Jicha_Model> query(){
        ArrayList<SQLite_Jicha_Model> jicha_models = new ArrayList<>();
        Cursor c = queryTheCursor();

        while (c.moveToNext()){
            SQLite_Jicha_Model jicha = new SQLite_Jicha_Model();
            jicha._id = c.getInt(c.getColumnIndex("_id"));
            jicha.RoomID = c.getString(c.getColumnIndex("RoomID"));
            jicha.Num = c.getString(c.getColumnIndex("Num"));
            jicha.Type = c.getString(c.getColumnIndex("Type"));
            jicha.Item = c.getString(c.getColumnIndex("Item"));
            jicha.Remark = c.getString(c.getColumnIndex("Remark"));
            jicha.media1 = c.getString(c.getColumnIndex("media1"));
            jicha.media2 = c.getString(c.getColumnIndex("media2"));
            jicha.media3 = c.getString(c.getColumnIndex("media3"));
            jicha.Operator = c.getString(c.getColumnIndex("Operator"));
            jicha.shifoujicha = c.getString(c.getColumnIndex("shifoujicha"));
            jicha_models.add(jicha);
        }
        c.close();
        return jicha_models;
    }

    //返回一个Curse对象
    public Cursor queryTheCursor(){
        Cursor c = db.rawQuery("select * from jicha",null);
        return c;
    }


    //搜索数据库方法
    public Cursor search(String string,String lieming){
        String sql = "select * from jicha where " + lieming + " = ?";
        Cursor c = db.rawQuery(sql,new String[]{string});
        return c;
    }

    //删除行
    public void deleteOldPerson(SQLite_Jicha_Model sqLite_jicha_model) {
        db.delete("jicha","RoomID = ?", new String[]{sqLite_jicha_model.RoomID});
    }


    //更改列数据
    public void update(){
        db.execSQL("update jicha set shifoujicha = 'yes' where shifoujicha = 'isflag'");
    }

    //更改成上一次稽查记录
    public void update_isflag(){
        db.execSQL("update jicha set shifoujicha = 'isflag' where shifoujicha = 'no'");
    }
    //更改成上一次稽查记录
    public void delete(){
        db.execSQL("delete from jicha ");
    }


    //关闭数据库
    public void closeDB(){
        db.close();
    }
}
