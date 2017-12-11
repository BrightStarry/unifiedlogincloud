package com.zuma.smssender.config;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:09
 * 配置类
 */
public class Config {
    //单次最多发送手机号数
    public static final Integer MAX_PHONE_NUM = 30;


    //畅想
    //畅想流水号处理前缀
    public static final String CHANGXIANG_PRE = "changxiang";


    //筑望
    //前缀
    public static final String ZHUWANG_PRE = "zhuwang";
    //业务代码
    public static final String ZHUWANG_SERVICE_CODE = "HELP";
    //发送速率,每秒100条
    public static final Integer ZHUWANG_SEND_SPEED = 100;
    //字数
    public static final Integer ZHUWANG_SMS_MAX_LEN = 70;
    //对接ip
    public static final String ZHUWANG_IP = "115.231.73.206";
//    public static final String ZHUWANG_IP = "118.178.35.191";
    //端口号
//    public static final Integer ZHUWANG_PORT = 7892;
    public static final Integer ZHUWANG_PORT = 8855;
    //接入号
//    public static final String ZHUWANG_JOIN_NUMBER = "10689082";
    public static final String ZHUWANG_JOIN_NUMBER = "1069026427";
    //连接重试次数
    public static final Integer ZHUWANG_CONNECT_MAX_RETRY_NUMBER = 3;
    //断开后x秒后再次尝试重连
    public static final Integer ZHUWANG_RETRY_CONNECT_TIME = 3600;
    //心跳检测频率
    public static final Integer ZHUWANG_ACTIVE_TEST_SECOND = 180;
    //每个socket的并发量,也就是每秒的并发量
    public static final Integer ZHUWANG_SOCKET_QPS = 1000;



    //掌游
    //掌游短信内容最大长度 TODO 短信字数校验
    public static final Integer ZHANGYOU_SMS_MAX_LEN = 210;
    //掌游接口前缀
    private static final String ZHANGYOU_URL_PRE = "http://ysms.game2palm.com:8899/smsAccept";
    //掌游流水号处理前缀
    public static final String ZHANGYOU_PRE = "zhangyou";

    //宽信
    //宽信流水号处理前缀
    public static final String KUANXIN_PRE = "kuanxin";
    //宽信接口前缀
    private static final String KUANXIN_URL_PRE = "http://114.55.90.98:8808/api";
    //宽信短信内容最大长度
    public static final Integer KUANXIN_SMS_MAX_LEN = 350;

    //群正
    //群正流水号处理前缀
    public static final String QUNZHENG_PRE = "qunzheng";
    //群正接口前缀
    private static final String QUNZHENG_URL_PRE = "http://sms.uninets.com.cn/Modules/Interface/http/IservicesBSJY.aspx";

    //regxep
    //移动手机号正则
    public static final String YIDONG_PHONE_NUMBER_REGEXP = "(^((13[4-9])|147|(15[0|1|2|7|8|9])|178|(18[2|3|4|7|8]))\\d{8}$)|(^1705\\d{7}$)";
    //联通手机号正则
    public static final String LIANTONG_PHONE_NUMBER_REGEXP = "(^((13[0-2])|145|(15[5|6])|17[5|6]|(18[5|6]))\\d{8}$)|(^1709\\d{7}$)";
    //电信手机号正则
    public static final String DIANXIN_PHONE_NUMBER_REGEXP = "(^(133|153|173|177|(18[0|1|9]))\\d{8}$)|(^1(349|700)\\d{7}$)";

    //URL
    //掌游发送短信接口url
    public static final String ZHANGYOU_SEND_SMS_URL = ZHANGYOU_URL_PRE + "/sendSms.action";
    //宽信发送短信接口url
    public static final String KUANXIN_SEND_SMS_URL = KUANXIN_URL_PRE + "/sms/send";
    //群正发送短信接口url
    public static final String QUNZHENG_SEND_SMS_URL = QUNZHENG_URL_PRE + "";
    //畅想发送短信接口url
    public static final String CHANGXIANG_SEND_SMS_URL = "http://api.cxton.com:8080/eums/utf8/send_strong.do";

    //分隔符
    //手机号数组分隔符
    public static final String PHONES_SEPARATOR = ",";
    //短信内容分隔符
    public static final String SMS_MESSAGE_SEPARATOR = "!&";



}
