/*
 * Copyright (c) 17-8-28 上午1:36 Author@KewenC
 */

package com.kewenc.microword.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.kewenc.microword.util.Constants;

import static com.kewenc.microword.util.Constants.TABLE_ALL;
import static com.kewenc.microword.util.Constants.TABLE_ALL_DISORDER;
import static com.kewenc.microword.util.Constants.TABLE_CET4;

/**
 * Created by KewenC on 2017/8/28.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_MICROWORD, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("create table tb_me(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200))");
        //四级
        sqLiteDatabase.execSQL("create table "+TABLE_ALL[0]+"(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200),markenpath CHAR(150),markuspath CHAR(150),collect BOOLEAN(10))");
        //六级
        sqLiteDatabase.execSQL("create table "+TABLE_ALL[1]+"(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200),markenpath CHAR(150),markuspath CHAR(150),collect BOOLEAN(10))");
        //考研
        sqLiteDatabase.execSQL("create table "+TABLE_ALL[2]+"(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200),markenpath CHAR(150),markuspath CHAR(150),collect BOOLEAN(10))");
        //雅思
        sqLiteDatabase.execSQL("create table "+TABLE_ALL[3]+"(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200),markenpath CHAR(150),markuspath CHAR(150),collect BOOLEAN(10))");
        //收藏
        sqLiteDatabase.execSQL("create table "+TABLE_ALL[4]+"(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200),markenpath CHAR(150),markuspath CHAR(150),collect BOOLEAN(10),flag CHAR(50))");
        //乱序表
        sqLiteDatabase.execSQL("create table "+TABLE_ALL_DISORDER[0]+"(id INTEGER primary key,id_dis INTEGER)");
        sqLiteDatabase.execSQL("create table "+TABLE_ALL_DISORDER[1]+"(id INTEGER primary key,id_dis INTEGER)");
        sqLiteDatabase.execSQL("create table "+TABLE_ALL_DISORDER[2]+"(id INTEGER primary key,id_dis INTEGER)");
        sqLiteDatabase.execSQL("create table "+TABLE_ALL_DISORDER[3]+"(id INTEGER primary key,id_dis INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
