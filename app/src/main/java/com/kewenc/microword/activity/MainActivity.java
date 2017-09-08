/*
 * Copyright (c) 17-8-22 下午8:44 Author@KewenC
 */

package com.kewenc.microword.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.kewenc.microword.R;
import com.kewenc.microword.fragment.CollectFragment;
import com.kewenc.microword.fragment.HomeFragment;
import com.kewenc.microword.fragment.SearchFragment;
import com.kewenc.microword.fragment.SettingFragment;
import com.kewenc.microword.helper.BottomNavigationViewHelper;
import com.kewenc.microword.helper.DataBaseLoader;
import com.kewenc.microword.helper.DatabaseHelper;

import java.util.Random;

import static com.kewenc.microword.util.Constants.DATABASE_ALL_CACHE;
import static com.kewenc.microword.util.Constants.DATABASE_CACHE_PATH;
import static com.kewenc.microword.util.Constants.PACKAGE_NAME;
import static com.kewenc.microword.util.Constants.SP_MICROWORD;
import static com.kewenc.microword.util.Constants.SP_VERSIONCODE;
import static com.kewenc.microword.util.Constants.TABLE_ALL;
import static com.kewenc.microword.util.Constants.TABLE_ALL_CACHE;
import static com.kewenc.microword.util.Constants.TABLE_ALL_DISORDER;
import static com.kewenc.microword.util.Constants.TABLE_COUNTS;

public class MainActivity extends AppCompatActivity {

//    private TextView mTextMessage;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ProgressDialog progressDialog = null;
    DatabaseHelper databaseHelper_native;
    SQLiteDatabase sqLiteDatabase_native;
    String Show="";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
//                    databaseHelper_native = new DatabaseHelper(getApplicationContext());
//                    sqLiteDatabase_native =databaseHelper_native.getReadableDatabase();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeValue(0);
                            Message message = new Message();
                            message.what=1;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeValue(1);
                            Message message = new Message();
                            message.what=2;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeValue(2);
                            Message message = new Message();
                            message.what=3;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeValue(3);
                            Message message = new Message();
                            message.what=4;
                            handler.sendMessage(message);
                        }
                    }).start();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeDisorderValue(0);
                            Message message = new Message();
                            message.what=6;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeDisorderValue(1);
                            Message message = new Message();
                            message.what=7;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeDisorderValue(2);
                            Message message = new Message();
                            message.what=8;
                            handler.sendMessage(message);
                        }
                    }).start();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            LoadNativeDisorderValue(3);
                            Message message = new Message();
                            message.what=9;
                            handler.sendMessage(message);
                        }
                    }).start();
                    break;
                case 1:
                    Show+="四级,";
                    progressDialog.setMessage(Show);
                    break;
                case 2:
                    Show+="六级,";
                    progressDialog.setMessage(Show);
                    break;
                case 3:
                    Show+="考研,";
                    progressDialog.setMessage(Show);
                    break;
                case 4:
                    Show+="雅思,";
                    progressDialog.setMessage(Show);
                    databaseHelper_native.close();
                    sqLiteDatabase_native.close();
                    break;
                case 5:
                    progressDialog.setTitle("初始化数据中...");
                    progressDialog.setMax(msg.arg2);
                    progressDialog.setProgress(msg.arg1);
//                    progressDialog.show();
                    break;
                case 6:
                    Show+="乱4,";
                    progressDialog.setMessage(Show);
                    break;
                case 7:
                    Show+="乱6,";
                    progressDialog.setMessage(Show);
                    break;
                case 8:
                    Show+="乱考,";
                    progressDialog.setMessage(Show);
                    break;
                case 9:
                    Show+="乱雅,";
                    progressDialog.setMessage(Show);
                    break;
            }
            progressDialog.show();
        }
    };

    private void LoadNativeDisorderValue(int flag) {
        int no = TABLE_COUNTS[flag];
        int[] sequence = new int[no];
        for(int i = 0; i < no; i++){
            sequence[i] = i+1;
        }
        Random random = new Random();
        for(int i = 0; i < no; i++){
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }
        ContentValues contentValues;
        for (int i=0;i<no;i++){
            contentValues = new ContentValues();
            contentValues.put("id_dis",sequence[i]);
            sqLiteDatabase_native.insert(TABLE_ALL_DISORDER[flag],null,contentValues);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.content,homeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_search:
                    SearchFragment searchFragment = new SearchFragment();
                    fragmentTransaction.replace(R.id.content,searchFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_collect:
                    CollectFragment collectFragment = new CollectFragment();
                    fragmentTransaction.replace(R.id.content,collectFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_setting:
                    SettingFragment settingFragment = new SettingFragment();
                    fragmentTransaction.replace(R.id.content,settingFragment);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSequence(4590);
        InitData();
        InitFragment();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * 初始化数据库
     */
    private void InitData() {
        int versionCode = GetAppVersionCode();
        SharedPreferences sp=getSharedPreferences(SP_MICROWORD, Activity.MODE_PRIVATE);
        int oldVersionCode = sp.getInt(SP_VERSIONCODE,0);
        if (versionCode>oldVersionCode){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("SP_VERSIONCODE",versionCode);
            editor.apply();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //加载本地数据库
                    databaseHelper_native = new DatabaseHelper(getApplicationContext());
                    sqLiteDatabase_native = databaseHelper_native.getReadableDatabase();
//                    databaseHelper.close();
                    //加载外部数据库
                    DataBaseLoader dataBaseLoader = new DataBaseLoader(getApplicationContext());
                    for (int i=0;i<4;i++){
                        Boolean isSucceed = dataBaseLoader.openDatabase(i);
                        Log.d("MainActivity",i+":"+isSucceed);
                    }
                    //为本地数据库赋值
//                    LoadNativeValue();
                    //为乱序数据表赋值
                    Message message = new Message();
                    message.what=0;
                    handler.sendMessage(message);
                }
            }).start();


        }
    }

    /**
     * 将外部数据库的值赋予本地数据库
     */
    private void LoadNativeValue(int i) {
        //                Cursor cursor = sqLiteDatabase.query(TABLE_ALL_CACHE[0],new String[]{"id","word","marken","markus","translate"},
//                        "id=?",new String[]{"?"},null,null,null);
//        for (int i=0;i<1;i++){
            SQLiteDatabase sqLiteDatabase_cache = SQLiteDatabase.openOrCreateDatabase(DATABASE_CACHE_PATH+"/"+DATABASE_ALL_CACHE[i],null);
            Cursor cursor = sqLiteDatabase_cache.rawQuery("select id,word,marken,markus,translate from "+TABLE_ALL_CACHE[i]+" where id=id", null);
//            databaseHelper_native = new DatabaseHelper(getApplicationContext());
//            sqLiteDatabase_native =databaseHelper_native.getReadableDatabase();
            int index = 0;
            while (cursor.moveToNext()){
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String marken = cursor.getString(cursor.getColumnIndex("marken"));
                String markus = cursor.getString(cursor.getColumnIndex("markus"));
                String translate = cursor.getString(cursor.getColumnIndex("translate"));
                ContentValues contentValues = new ContentValues();
                contentValues.put("word",word);
                contentValues.put("marken",marken);
                contentValues.put("markus",markus);
                contentValues.put("translate",translate);
                contentValues.put("markenpath","");
                contentValues.put("markuspath","");
                contentValues.put("collect",false);
                sqLiteDatabase_native.insert(TABLE_ALL[i],null,contentValues);
                if (i==3){
                    Message message = new Message();
                    message.arg1 = ++index;
                    message.what = 5;
                    message.arg2 = cursor.getCount();
                    handler.sendMessage(message);
                }
            }
//            databaseHelper_native.close();
//            sqLiteDatabase_native.close();
            cursor.close();
            sqLiteDatabase_cache.close();
//        }
    }

    /**
     * 获取应用的VersionCode号
     * @return
     */
    private int GetAppVersionCode() {
        try {
            return getPackageManager().getPackageInfo(PACKAGE_NAME,0).versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 初始化Fragment,设置默认Fragment
     */
    private void InitFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.content,homeFragment);
        fragmentTransaction.commit();
    }

    public static void getSequence(int no) {
        int[] sequence = new int[no];
        String s ="";
        for(int i = 0; i < no; i++){
            sequence[i] = i;
            s+=sequence[i]+" ";
        }
        Log.d("MainActivity",s);
        Random random = new Random();
        for(int i = 0; i < no; i++){
            int p = random.nextInt(no);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }
        random = null;
        String ss ="";
        for (int i=0;i<no;i++){
            ss+=sequence[i]+" ";
        }
        Log.d("MainActivity",ss);
    }
}
