/*
 * Copyright (c) 17-8-31 下午9:58 Author@KewenC
 */

package com.kewenc.microword.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.kewenc.microword.R;
import com.kewenc.microword.helper.DatabaseHelper;
import static com.kewenc.microword.util.Constants.APPWIDGET_Android;
import static com.kewenc.microword.util.Constants.APPWIDGET_CHANGE;
import static com.kewenc.microword.util.Constants.LIB_NAME;
import static com.kewenc.microword.util.Constants.PCT;
import static com.kewenc.microword.util.Constants.SP_LIBRARY_SORT;
import static com.kewenc.microword.util.Constants.SP_LIBRARY_WHICH;
import static com.kewenc.microword.util.Constants.SP_MICROWORD;
import static com.kewenc.microword.util.Constants.SP_PROGRESS_ID;
import static com.kewenc.microword.util.Constants.TABLE_ALL;
import static com.kewenc.microword.util.Constants.TABLE_ALL_DISORDER;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MyAppWidgetConfigureActivity MyAppWidgetConfigureActivity}
 */
public class MyAppWidget extends AppWidgetProvider {

    private static final String M ="MyAppWidget";
    private String mWord;
    private String mTranslates;
    private int mNum;
    private static int mCount;
    private Context context;
    private SharedPreferences sp;
    private boolean isPreviousEnable = true;
    private boolean isNextEnable = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
                    RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
                    remoteViews.setTextViewText(R.id.tv_word_appwidget,mWord);
                    remoteViews.setTextViewText(R.id.tv_translates_appwidget,mTranslates);
                    remoteViews.setTextViewText(R.id.tv_num_appwidget,mNum+"/"+mCount+"-"+mNum*100/mCount+"%-"+LIB_NAME[sp.getInt(SP_LIBRARY_WHICH,0)]);
                    if (isPreviousEnable){
                        remoteViews.setViewVisibility(R.id.tv_previous_appwidget, View.VISIBLE);
                        Intent intent_previous = new Intent(APPWIDGET_CHANGE);
                        Bundle bundle_previous =new Bundle();
                        bundle_previous.putInt(PCT,0);
                        intent_previous.putExtras(bundle_previous);
                        PendingIntent pendingIntent_previous =PendingIntent.getBroadcast(context,0,intent_previous,0);
                        remoteViews.setOnClickPendingIntent(R.id.tv_previous_appwidget,pendingIntent_previous);
                    }else {
                        remoteViews.setViewVisibility(R.id.tv_previous_appwidget, View.GONE);
                    }
                    Intent intent_collect = new Intent(APPWIDGET_CHANGE);
                    Bundle bundle_collect =new Bundle();
                    bundle_collect.putInt(PCT,1);
                    intent_collect.putExtras(bundle_collect);
                    PendingIntent pendingIntent_collect =PendingIntent.getBroadcast(context,1,intent_collect,0);
                    remoteViews.setOnClickPendingIntent(R.id.tv_collect_appwidget,pendingIntent_collect);
                    if (isNextEnable){
                        remoteViews.setViewVisibility(R.id.tv_next_appwidget,View.VISIBLE);
                        Intent intent_next = new Intent(APPWIDGET_CHANGE);
                        Bundle bundle_next =new Bundle();
                        bundle_next.putInt(PCT,2);
                        intent_next.putExtras(bundle_next);
                        PendingIntent pendingIntent_next =PendingIntent.getBroadcast(context,2,intent_next,0);
                        remoteViews.setOnClickPendingIntent(R.id.tv_next_appwidget,pendingIntent_next);
                    }else {
                        remoteViews.setViewVisibility(R.id.tv_next_appwidget,View.GONE);
                    }
                    appWidgetManager.updateAppWidget(new ComponentName(context,MyAppWidget.class),remoteViews);
                    break;
            }
        }
    };
    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        this.context = context;
        sp = context.getSharedPreferences(SP_MICROWORD, Activity.MODE_PRIVATE);
//        int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
//        LoadData(sp.getInt(SP_LIBRARY_WHICH,0),sp.getInt(SP_LIBRARY_SORT,0),mId);
        Log.d(M ,"onReceive:"+intent.getAction());
        if (intent.getAction().equals(APPWIDGET_Android)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
                    LoadData(sp.getInt(SP_LIBRARY_WHICH,0),sp.getInt(SP_LIBRARY_SORT,0),mId);
                }
            }).start();
        }
        if (intent.getAction().equals(APPWIDGET_CHANGE)){
//            int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
            Bundle bundle = intent.getExtras();
            int s = bundle.getInt(PCT);
            if (s==0){
                int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
                if (mId!=1){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],--mId);
                    editor.apply();
                }else {
                    Toast.makeText(context,"已经是第一个！",Toast.LENGTH_LONG).show();
                }
            }else if (s==2){
                int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
                Log.d("MTA",mCount+"COUNT");
                if (mId!=mCount){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],++mId);
                    editor.apply();
                }else {
                    Toast.makeText(context,"恭喜您，词库已经记完！",Toast.LENGTH_LONG).show();
                }
            }
            Log.d(M ,":::::"+s);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int mId = sp.getInt(SP_PROGRESS_ID[sp.getInt(SP_LIBRARY_SORT,0)+3*sp.getInt(SP_LIBRARY_WHICH,0)],1);
                    LoadData(sp.getInt(SP_LIBRARY_WHICH,0),sp.getInt(SP_LIBRARY_SORT,0),mId);
                }
            }).start();
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = MyAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(M ,"onUpdate:");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MyAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private void LoadData(int mFlag,int mSort,int mId) {
        isPreviousEnable = mId!=1;
        isNextEnable = mId!=mCount;
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase =databaseHelper.getReadableDatabase();
        mCount = sqLiteDatabase.rawQuery("select id,word,marken,markus,translate from "+TABLE_ALL[mFlag]+" where id=id", null).getCount();
        switch (mSort){
            case 0:
                Cursor cursor = sqLiteDatabase.query(TABLE_ALL[mFlag],new String[] {"id","word","marken","markus","translate"},"id=?"
                        ,new String[] {mId+""},null,null,null);
                cursor.moveToFirst();
                AddData(cursor);
                cursor.close();
                sqLiteDatabase.close();
                databaseHelper.close();
                break;
            case 1:
                int id_tmp = mCount-mId+1;
                Cursor cursor1 = sqLiteDatabase.query(TABLE_ALL[mFlag],new String[] {"id","word","marken","markus","translate"},"id=?"
                        ,new String[] {id_tmp+""},null,null,null);
                cursor1.moveToFirst();
                AddData(cursor1);
                cursor1.close();
                break;
            case 2:
                Cursor cursor_dis = sqLiteDatabase.query(TABLE_ALL_DISORDER[mFlag],new String[] {"id","id_dis"},"id=?"
                        ,new String[] {mId+""},null,null,null);
                cursor_dis.moveToFirst();
                int id_tmp2 = cursor_dis.getInt(cursor_dis.getColumnIndex("id_dis"));
                cursor_dis.close();
                //在正序表里寻找对应的乱序的数据
                Cursor cursor2 = sqLiteDatabase.query(TABLE_ALL[mFlag],new String[] {"id","word","marken","markus","translate"},"id=?"
                        ,new String[] {id_tmp2+""},null,null,null);
                cursor2.moveToFirst();
                AddData(cursor2);
                cursor2.close();
                break;
                }
        sqLiteDatabase.close();
        databaseHelper.close();
        Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
        }

    private void AddData(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String word = cursor.getString(cursor.getColumnIndex("word"));
        String marken = cursor.getString(cursor.getColumnIndex("marken"));
        String markus = cursor.getString(cursor.getColumnIndex("markus"));
        String translate = cursor.getString(cursor.getColumnIndex("translate"));
        String tmp = "";
        String tmp_en = marken;
        String tmp_us = markus;
        if (!tmp_en.equals("")){
            if (!tmp_us.equals("")){
                tmp="英 "+tmp_en+"  美 "+tmp_us+"\n";
            }else{
                tmp="英 "+tmp_en+"\n";
            }
        }else{
            if (!tmp_us.equals("")){
                tmp="美 "+tmp_us+"\n";
            }else{

            }
        }
        String translates= tmp+translate;
        mWord = word;
        mTranslates = translates;
        mNum = id;
    }
}

