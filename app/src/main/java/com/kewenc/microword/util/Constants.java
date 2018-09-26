/*
 * Copyright (c) 17-8-22 下午9:11 Author@KewenC
 */

package com.kewenc.microword.util;

import android.os.Environment;

/**
 * Created by KewenC on 2017/8/22.
 */

public class Constants {
    /**
     * 应用中文名称
     */
    public static final String APPNAME = "微词";
    /**
     * 应用包名
     */
    public static final String PACKAGE_NAME = "com.kewenc.microword";
    /**
     * Activity共享元素参数标志
     */
    public static final String[] IMAGE_SHARE_ELEMENTS = {"cet4_img","cet6_img","teefps_img","ielts_img","more_img"};
    public static final String[] TEXTVIEW_SHARE_ELEMENTS = {"cet4_tv","cet6_tv","teefps_tv","ielts_tv","more_tv"};
    /**
     * 数据库
     */
    public static final int DATABASE_VERSION = 2;//版本号

    public static final String DATABASE_CET4_CACHE = "cet4.db";// 临时数据库
    public static final String DATABASE_CET6_CACHE = "cet6.db";
    public static final String DATABASE_TEEFPS_CACHE = "teefps.db";
    public static final String DATABASE_IELTS_CACHE = "ielts.db";
    public static final String[] DATABASE_ALL_CACHE = { DATABASE_CET4_CACHE , DATABASE_CET6_CACHE , DATABASE_TEEFPS_CACHE , DATABASE_IELTS_CACHE };
    public static final String[] TABLE_ALL_CACHE = { "cet4" , "cet6" , "teefps" , "ielts" };

    public static final String DATABASE_MICROWORD = "microword.db";//数据库名称

    public static final String DATA_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; // 在手机里存放数据库的位置
    public static final String DATABASE_CACHE_PATH = DATA_PATH +"/"+"databases_cache";

    public static final String TABLE_CET4 = "tb_cet4";//表名
    public static final String TABLE_CET6 = "tb_cet6";
    public static final String TABLE_TEEFPS = "tb_teefps";
    public static final String TABLE_IELTS = "tb_ielts";
    public static final String TABLE_COLLECT = "tb_collect";
    public static final String[] TABLE_ALL = {TABLE_CET4,TABLE_CET6,TABLE_TEEFPS,TABLE_IELTS,TABLE_COLLECT};

    public static final String[] TABLE_ALL_DISORDER = {"tb_cet4_dis","tb_cet6_dis","tb_teefps_dis","tb_ielts_dis"};//乱序表
    /**
     * SharedPreferences
     */
    public static final String SP_MICROWORD = "SP_MICROWORD";//总SP name
    public static final String SP_VERSIONCODE = "SP_VERSIONCODE";//应用versionCode
    public static final String SP_LIBRARY_WHICH = "SP_LIBRARY_WHICH";//桌面那个词库
    public static final String SP_LIBRARY_SORT = "SP_LIBRARY_SORT";//桌面那个词库的排序
    public static final String[] SP_PROGRESS_ID ={"SP_PG_ID00","SP_PG_ID01","SP_PG_ID02",
                                                  "SP_PG_ID10","SP_PG_ID11","SP_PG_ID12",
                                                  "SP_PG_ID20","SP_PG_ID21","SP_PG_ID22",
                                                  "SP_PG_ID30","SP_PG_ID31","SP_PG_ID32"};
    /**
     * 数据库总数
     */
    public static final int[] TABLE_COUNTS= {4590,2089,5492,7930};
    /**
     * 词库中文名称
     */
    public static final String[] LIB_NAME = {"四级","六级","考研","雅思"};
    /**
     * AppWidget广播信号
     */
    public static final String APPWIDGET_CHANGE = "microword.appwidget.action.APPWIDGET_CHANGE";
    public static final String APPWIDGET_Android = "android.appwidget.action.APPWIDGET_UPDATE";
    public static final String PCT = "Previous_Collect_Next";
}
