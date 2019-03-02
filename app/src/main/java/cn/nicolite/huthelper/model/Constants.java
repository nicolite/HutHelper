package cn.nicolite.huthelper.model;

import org.jetbrains.annotations.NotNull;

/**
 * 常量类 放置一些常量, 如APPID、APPKEY等
 * Created by nicolite on 17-10-13.
 */

public class Constants {
    //DataBase Name
    public static final String DBNAME = "huthelper-db";
    //API URL
    public static final String API_BASE_URL = "https://raw.githubusercontent.com/nicolite/HutHelper/";
    public static final String ARTICLE_BASE_URL = "http://218.75.197.121:8888";
    public static final String PICTURE_URL = API_BASE_URL;
    public static final String FRESHMANGUIDE_URL = ARTICLE_BASE_URL + "/home/post/19"; //新生攻略

    //SOME URL
    public static final String CHANGE_PWD = "不开放";
    public static final String HELP = ARTICLE_BASE_URL + "/home/post/39";
    public static final String PERMISSON = ARTICLE_BASE_URL + "/home/post/40";
    public static final String LIBRARY = "不开放";
    public static final String HOMEWORK = "不开放";
    public static final String BLOG = "http://nicolite.cn";
    public static final String OPEN_SOURCE = "https://github.com/nicolite/HutHelper";

    //Activity Request Result Code
    public static final int REQUEST = 100;
    public static final int DELETE = 300;
    public static final int PUBLISH = 400;
    public static final int CHANGE = 500;
    public static final int RESULT = 600;
    public static final int REFRESH = 700;

    //Action
    public static final String MainBroadcast = "cn.nicolite.huthelper.mainbroadcast"; //主页面广播actioin

    //Broadcast type
    public static final int BROADCAST_TYPE_NOTICE = 0;
    public static final int BROADCAST_TYPE_REFRESH_AVATAR = 1;


    public static final String BASE_API_URL = "";
    public static final String BASE_API_TEST_URL = "";
}
