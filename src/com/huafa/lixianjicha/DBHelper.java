package com.huafa.lixianjicha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chensiqi on 2016/11/7.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jicha.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    //第一次创建数据库时会调用这个方法
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE jicha (_id integer primary key autoincrement,"
                + "RoomID varchar(50),"
                + "Num varchar(50),"
                + "Type varchar(50),"
                + "Item varchar(50),"
                + "Remark varchar(255),"
                + "media1 varchar(255),"
                + "media2 varchar(255),"
                + "media3 varchar(255),"
                + "Operator varchar(50),"
                + "shifoujicha varchar(50))");
    }


    //更新表
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE jicha ADD COLUMN other STRING");
    }
}
