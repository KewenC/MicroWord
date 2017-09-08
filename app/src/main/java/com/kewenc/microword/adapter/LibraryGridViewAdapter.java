/*
 * Copyright (c) 17-8-23 下午11:12 Author@KewenC
 */

package com.kewenc.microword.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kewenc.microword.util.Constants;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by KewenC on 2017/8/23.
 */

public class LibraryGridViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
//    private Drawable mDefaultBitmapDrawable;
    private ArrayList<Map<String, Object>> list;
    private int mResource;
    private int[] mTo;
    private String[] mFrom;
    public LibraryGridViewAdapter(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to) {
        this.list=data;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.mFrom = from;
        this.mTo = to;
//        mDefaultBitmapDrawable = context.getResources().getDrawable(R.mipmap.ic_launcher_round);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        // 判断是否缓存
        if (view == null) {
            holder = new ViewHolder();
            // 通过LayoutInflater实例化布局
            view = mInflater.inflate(mResource, null);
            holder.img = view.findViewById(mTo[0]);
            holder.title = view.findViewById(mTo[1]);
            view.setTag(holder);
        } else {
            // 通过tag找到缓存的布局
            holder = (ViewHolder) view.getTag();
        }
        // 设置布局中控件要显示的视图
        holder.img.setBackgroundResource((int)list.get(i).get(mFrom[0]));
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            holder.img.setTransitionName(Constants.IMAGE_SHARE_ELEMENTS[i]);
        }
        holder.title.setText(list.get(i).get(mFrom[1]).toString());
        return view;
    }

    private final class ViewHolder {
        private ImageView img;
        private TextView title;
    }
}
