package com.zuma.config;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:59
 * 配置类
 */
public class Config {
    //保存在session中的账号信息的key
    public static final String SESSION_USER_ATTRIBUTE_NAME = "account";
    //分页条数
    public static final Integer PAGE_SIZE = 3;
    //异常页面路径
    public static final String ERROR_URL = "common/error";

    //一级菜单
    public static final String MEMU_TOP1_A = "用户管理";
    public static final String MEMU_TOP1_B = "平台管理";

    //二级菜单
    public static final String MEMU_TOP2_A = "用户列表";
    public static final String MEMU_TOP2_B = "平台列表";


}
