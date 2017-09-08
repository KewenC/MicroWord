/*
 * Copyright (c) 17-8-22 下午9:19 Author@KewenC
 */

package com.kewenc.microword.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.kewenc.microword.R;
import com.kewenc.microword.activity.Cet4Activity;
import com.kewenc.microword.adapter.LibraryGridViewAdapter;
import com.kewenc.microword.util.Constants;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL("http://www.baidu.com");
                    URLConnection uc = url.openConnection();// 生成连接对象
                    uc.connect(); // 发出连接
                    long ld = uc.getDate(); // 取得网站日期时间
                    Date date = new Date(ld); // 转换为标准时间对象
                    // 分别取得时间中的小时，分钟和秒，并输出
                    Log.d("multicast",
                            date + ", " + date.getHours() + "时" + date.getMinutes()
                                    + "分" + date.getSeconds() + "秒"+(date.getMonth()+1)+"月"+date.getDate());
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }// 取得资源对象
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        InitGridView(view);
        return view;
    }

    /**
     * 初始化GridView
     * @param view
     */
    private void InitGridView(View view) {
        GridView gridView = view.findViewById(R.id.gv_library_home);
        LibraryGridViewAdapter adapter = new LibraryGridViewAdapter(getContext(),LoadData(),R.layout.item_gridview
        ,new String[]{"img","title"},new int[]{R.id.iv_item_gridview,R.id.tv_item_gridview});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new MyGridViewListener());
    }

    /**
     * 加载GridView数据
     * @return
     */
    private ArrayList<Map<String,Object>> LoadData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        String[] titles = {"四级","六级","考研","雅思","更多"};
        for (int i=0;i<5;i++){
            map = new HashMap<String, Object>();
            map.put("img", R.mipmap.ic_launcher);
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }

    private class MyGridViewListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(),Cet4Activity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("Activity_Flag",i);
            intent.putExtras(bundle);
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                        Pair.create(view,Constants.IMAGE_SHARE_ELEMENTS[0])).toBundle());
            }else {
                startActivity(intent);
            }
        }
    }
}
