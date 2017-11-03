package com.zuma.util;

import com.zuma.dto.LogMessage;
import com.zuma.enums.ServiceEnum;
import com.zuma.factory.PatternFactory;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 14:18
 * 日志存储路径生成
 */
@Component
@Slf4j
public class LogPathUtil {
    //提取时间正则, 年月日时
    public static final String DATE_REGEXP = "[1-2]\\d{3}(-|\\\\|\\s)[0-1]\\d(-|\\\\|\\s)[0-3]\\d\\s+[0-2]\\d";

    //日期格式
    private static final String DATE_FORMAT = "yyyy-MM-dd HH";

    //基础路径前缀,根据win和linux区分
    private static final String BASE_PATH_PRE = System.getProperty("os.name").contains("Windows") ? "E:" + File.separator : File.separator;

    //文件后缀
    private static final String FILE_SUF = ".log";

    //基础路径  "D:/log/项目名/模块名/年份/月份/日/24小时.log"
    private static final String BASE_PATH = BASE_PATH_PRE + "log" +  File.separator;

    //正则工厂

    private static   PatternFactory patternFactory;

    @Autowired
    public  void setPatternFactory(PatternFactory patternFactory) {
        LogPathUtil.patternFactory = patternFactory;
    }

    /**
     * 生成存储路径
     * @param logMessage
     * @return
     */
    public static   File generate(LogMessage logMessage) {

        //提取时间路径
        StringBuilder datePath = extractDatePath(logMessage);
        //如果时间路径提取失败，
        if (datePath == null)
            return null;
        //获取
        String path = new StringBuilder()
                .append(BASE_PATH)//基础路径
                .append(EnumUtil.getByCode(logMessage.getServiceId(), ServiceEnum.class).getMessage())//服务名，根据服务id枚举返回
                .append(File.separator)
                .append(logMessage.getModuleName())//模块名
                .append(File.separator)
                .append(datePath)//年月日时路径
                .toString();
        return new File(path);
    }

    /**
     * 提取日志string时间
     */
    private static StringBuilder extractDatePath(LogMessage logMessage){
        //日期字符串.1:日志消息中截取。2.LogMessage中的date不为空
        String date = null;
        //返回路径
        StringBuilder resultPath = new StringBuilder();

        //从LogMessage的date中获取日期
        if (logMessage.getDate() != null) {
            DateTime dateTime = new DateTime(new Date());
            date = dateTime.toString(DATE_FORMAT);
        }else{//从日志消息中截取日期
            Matcher matcher = patternFactory.build().matcher(logMessage.getContent());
            //如果匹配
            if(matcher.find())
                //年月日时 字符
                date = matcher.group();
        }

        //如果日期截取失败
        if (StringUtils.isEmpty(date)) {
            log.error("从日志消息中匹配时间异常。logMessage={}",logMessage.getContent());
            return null;
        }
        //构造路径
        resultPath.append(date.substring(0, 4))//年
                .append(File.separator)
                .append(date.substring(5, 7))//月
                .append(File.separator)
                .append(date.substring(8, 10))//日
                .append(File.separator)
                .append(date.substring(date.length() - 2, date.length()) + FILE_SUF);//倒序取小时，因为日和小时间的间隔不确定有几位
        return resultPath;
    }

    public static void main(String[] args) {
//        String a = "2017-11-01 14:08:21.122  INFO 7848 --- [ntLoopGroup-1-2] com.zuma.handler.ServerHandler           : server接收到bba283b0消息:LogMessage(serviceId=null, channel=null, moduleId=null, time=Wed Nov 01 14:08:21 CST 2017, content=2  17:19:39.663 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.pageSize: 8192)";
//        LogMessage logMessage = LogMessage.builder()
//                .content(a)
//                .channel(1)
//                .moduleName("登录")
//                .serviceId(1)
//                .build();
//        System.out.println(generate(logMessage));

        DateTime dateTime = new DateTime(new Date());
        String s = dateTime.toString();
        System.out.println(s);
    }

}
