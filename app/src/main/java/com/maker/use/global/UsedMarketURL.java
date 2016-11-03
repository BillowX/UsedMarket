package com.maker.use.global;

/**
 * 服务器端网址
 * Created by XT on 2016/9/24.
 */

public class UsedMarketURL {
    public static String url_heart = "http://119.29.213.119:8080/UsedMarketDir";
    public static String splash_url = url_heart + "/Splash/splash.jpg";
    public static String VPImgUrl = url_heart + "/VPImg/img.json";


    //后台项目地址
    public static String server_heart = "http://119.29.213.119:8080/UsedMarket";

    //注册
    public static String REGISTER = server_heart + "/UserInfo/insert";
    //登陆
    public static String LOGIN = server_heart + "/UserInfo/login";
    //修改密码
    public static String CHANGE_PASSWORD = server_heart + "/UserInfo/editPassword";
    //修改头像
    public static String CHANGE_HEAD = server_heart + "/UserInfo/editHeadPortrait";
    //修改用户信息
    public static String CHANGE_USER_INFO = server_heart + "/UserInfo/edit";
    //头像地址
    public static String HEAD = server_heart + "/";

    //上传商品
    public static String UPLOAD_COMMODITY = server_heart + "/Commodity/upload";
    //删除商品
    public static String DELETE_COMMODITY = server_heart + "/Commodity/delete";
    //查询商品
    public static String SEARCH_COMMODITY = server_heart + "/Commodity/search";

}
