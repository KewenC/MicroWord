/*
 * Copyright (c) 17-8-26 下午9:45 Author@KewenC
 */

package com.kewenc.microword.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kewenc.microword.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by KewenC on 2017/8/26.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Map<String,Object>> mData;
    private String[] mFlags;

    public RecyclerAdapter(ArrayList<Map<String,Object>> data , String[] flags) {
        mData=data;
        mFlags=flags;
    }

    public OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //将布局转化为Viewb并传递给RecyclerView封装好的ViewHolder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_child_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        //建立起ViewHolder中试图与数据的关联
        holder.tv_word.setText(mData.get(position).get(mFlags[0]).toString());
        holder.tv_translate.setText(mData.get(position).get(mFlags[1]).toString());
        holder.tv_num.setText(mData.get(position).get(mFlags[2]).toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView cardView;
        public TextView tv_word;
        public TextView tv_translate;
        public TextView tv_num;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            cardView.setOnClickListener(this);
            tv_word = itemView.findViewById(R.id.tv_word_item_child_home);
            tv_translate = itemView.findViewById(R.id.tv_translates_item_child_home);
            tv_num = itemView.findViewById(R.id.tv_num_item_child_home);
        }

        //通过接口回调来实现RecyclerView的点击事件
        @Override
        public void onClick(View view) {
            if (itemClickListener!=null){
                itemClickListener.onItemClick(view,getAdapterPosition());//getPosition
            }
        }
    }
}
