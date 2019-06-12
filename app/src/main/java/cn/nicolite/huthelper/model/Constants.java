package cn.nicolite.huthelper.model;

/**
 * 常量类 放置一些常量, 如APPID、APPKEY等
 * Created by nicolite on 17-10-13.
 */

public class Constants {
    //API URL
    public static final String API_BASE_URL = "https://raw.githubusercontent.com/nicolite/HutHelper/";
    public static final String PICTURE_URL = API_BASE_URL;//"https://raw.githubusercontent.com/nicolite/HutHelper/";

    //SOME URL
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

}
