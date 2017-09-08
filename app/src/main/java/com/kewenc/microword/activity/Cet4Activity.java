/*
 * Copyright (c) 17-8-24 下午5:18 Author@KewenC
 */

package com.kewenc.microword.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.kewenc.microword.R;
import com.kewenc.microword.adapter.RecyclerAdapter;
import com.kewenc.microword.helper.DatabaseHelper;
import com.kewenc.microword.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kewenc.microword.util.Constants.LIB_NAME;
import static com.kewenc.microword.util.Constants.TABLE_ALL;
import static com.kewenc.microword.util.Constants.TABLE_ALL_DISORDER;

public class Cet4Activity extends AppCompatActivity {

    private ArrayList<Map<String,Object>> list;
    private Map<String, Object> map;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ProgressBar progressBar;
    private TextView textView;
    private TextView tv_count;
    private int mFlag =0;//词库标志
    private Boolean isFirstCount = true;
    private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 1:
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, int position) {
                        //设置点击动画
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                            view.animate().translationZ(15F).setDuration(300).setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
                                        view.animate().translationZ(1f).setDuration(500).start();
                                    }
                                }
                            }).start();
                        }
                        Snackbar.make(view,position+"",Snackbar.LENGTH_LONG).show();
                    }
                });
                break;
            case 2:
                textView.setText(msg.arg1*100/msg.arg2+"%");
                break;
            case 3:
                tv_count.setText("词库总数："+msg.arg1);
                break;
        }

    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet4);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mFlag = bundle.getInt("Activity_Flag");
        progressBar = (ProgressBar) findViewById(R.id.pb_home);
        textView = (TextView) findViewById(R.id.tv_p);
        tv_count = (TextView) findViewById(R.id.tv_count);
        InitTitleLayout();
        //设置RecycleView
        recyclerView = (RecyclerView) findViewById(R.id.lv_child_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        new Thread(new Runnable() {
            @Override
            public void run() {
                recyclerAdapter = new RecyclerAdapter(LoadData(0),new String[]{"word","translates","num"});
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 为adapter加载数据
     * @param flag
     * @return
     */
    private ArrayList<Map<String,Object>> LoadData(int flag) {
        list = new ArrayList<Map<String,Object>>();
//        Map<String, Object> map = null;
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase =databaseHelper.getReadableDatabase();
        switch (flag){
            case 0:
                Cursor cursor = sqLiteDatabase.rawQuery("select id,word,marken,markus,translate from "+TABLE_ALL[mFlag]+" where id=id", null);
                if (isFirstCount){
                    Message message = new Message();
                    message.what = 3;
                    message.arg1 = cursor.getCount();
                    handler.sendMessage(message);
                    isFirstCount = false;
                }
                while (cursor.moveToNext()) {
                    AddData(cursor);
                }
                cursor.close();
                break;
            case 1:
                Cursor cursor1 = sqLiteDatabase.rawQuery("select id,word,marken,markus,translate from "+TABLE_ALL[mFlag]+" where id=id", null);
                if (isFirstCount){
                    Message message = new Message();
                    message.what = 3;
                    message.arg1 = cursor1.getCount();
                    handler.sendMessage(message);
                    isFirstCount = false;
                }
                cursor1.moveToPosition(cursor1.getCount());
                while (cursor1.moveToPrevious()) {
                    AddData(cursor1);
                }
                cursor1.close();
                break;
            case 2:
                int p = 0;
                Cursor cursor_dis = sqLiteDatabase.rawQuery("select id,id_dis from "+TABLE_ALL_DISORDER[mFlag]+" where id=id",null);
                if (isFirstCount){
                    Message message = new Message();
                    message.what = 3;
                    message.arg1 = cursor_dis.getCount();
                    handler.sendMessage(message);
                    isFirstCount = false;
                }
                while (cursor_dis.moveToNext()){
                    String  id = cursor_dis.getString(cursor_dis.getColumnIndex("id_dis"));
                    Cursor cursor2 = sqLiteDatabase.query(TABLE_ALL[mFlag],new String[] {"id","word","marken","markus","translate"},"id=?"
                    ,new String[] {id},null,null,null);
                    cursor2.moveToFirst();
                    AddData(cursor2);
                    cursor2.close();
                    Message msg = new Message();
                    msg.arg1 = ++p;
                    msg.arg2 = cursor_dis.getCount();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                cursor_dis.close();
                break;
        }
        sqLiteDatabase.close();
        databaseHelper.close();
        return list;
    }

    /**
     * 为list赋值
     * @param cursor
     */
    private void AddData(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex("id"));
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
        map = new HashMap<String, Object>();
        map.put("word", word);
        map.put("translates", translates);
        map.put("num", id);
        list.add(map);
    }

    /**
     * 初始化TitleLayout
     */
    private void InitTitleLayout() {
        //设置共享元素
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            findViewById(R.id.iv_appbar).setTransitionName(Constants.IMAGE_SHARE_ELEMENTS[0]);
        }
        //处理status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(//通过判断当前sdk_int大于4.4(kitkat),则通过代码的形式设置status bar为透明
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //设置toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        //取图片主题色
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if(null != swatch) {
                    appBarLayout.setBackgroundColor(swatch.getRgb());
                }
            }
        });
        //处理展开收缩变化效果
        TextView textView = (TextView) findViewById(R.id.tv_appbar);
//        tv_count = (TextView) findViewById(R.id.tv_count);
        textView.setText(LIB_NAME[mFlag]);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -appBarLayout.getHeight()/2){
                    collapsingToolbarLayout.setTitle(LIB_NAME[mFlag]);
                }else {
                    collapsingToolbarLayout.setTitle("");
                }
                linearLayout.setAlpha(1+verticalOffset/(float)appBarLayout.getTotalScrollRange());
            }
        });
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu,menu);
        return true;
    }

    /**
     * 处理菜单选项
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.tb_az:
//                recyclerView.setVisibility(View.GONE);
//                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerAdapter = new RecyclerAdapter(LoadData(0),new String[]{"word","translates","num"});
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.tb_za:
//                recyclerView.setVisibility(View.GONE);
//                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerAdapter = new RecyclerAdapter(LoadData(1),new String[]{"word","translates","num"});
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.tb_mess:
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerAdapter = new RecyclerAdapter(LoadData(2),new String[]{"word","translates","num"});
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
