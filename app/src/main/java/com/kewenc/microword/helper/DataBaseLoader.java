/*
 * Copyright (c) 17-8-28 下午6:30 Author@KewenC
 */

package com.kewenc.microword.helper;

import android.content.Context;
import android.util.Log;

import com.kewenc.microword.R;
import com.kewenc.microword.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.Vector;


/**
 * Created by KewenC on 2017/8/28.
 */

public class DataBaseLoader {
    private static final int BUFFER_SIZE = 400000;
    private Context context;
    public DataBaseLoader(Context context){
        this.context = context;
    }
    public Boolean openDatabase(int flag) {
        File cache = new File(Constants.DATABASE_CACHE_PATH);
        if (!cache.exists()){
            boolean isSucceed = cache.mkdirs();
            if (!isSucceed)
                Log.e("Database", "Path not create");
        }
        String dbFile=Constants.DATABASE_CACHE_PATH+"/"+Constants.DATABASE_ALL_CACHE[flag];
        try {
            Log.d("MainActivity","try_DataBaseLOader");
            if (!(new File(dbFile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                Log.d("MainActivity","DataBaseLOader");
                InputStream is = null;
                switch (flag) {
                    case 0:
                        is = context.getResources().openRawResource(R.raw.cet4); // 欲导入的数据库
                        break;
                    case 1:
                        is = context.getResources().openRawResource(R.raw.cet6); // 欲导入的数据库
                        break;
                    case 2:
                        is = context.getResources().openRawResource(R.raw.teefps); // 欲导入的数据库
                        break;
                    case 3:
                        is = context.getResources().openRawResource(R.raw.ielts); // 欲导入的数据库
                        break;
                }
                FileOutputStream fos = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return false;
    }
}
