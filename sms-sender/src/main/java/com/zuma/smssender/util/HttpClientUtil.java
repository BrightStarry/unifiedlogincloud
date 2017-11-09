package com.zuma.smssender.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.factory.CommonFactory;
import com.zuma.smssender.factory.GsonFactory;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 15:43
 * httpClient工具类
 */
@Slf4j
public class HttpClientUtil {

    private CommonFactory<Gson> gsonFactory = GsonFactory.getInstance();

    //连接池对象
    private PoolingHttpClientConnectionManager pool = null;

    //请求配置
    private RequestConfig requestConfig;

    //连接池连接最大数
    private final Integer MAX_CONNECTION_NUM = 10;
    //最大路由，
    //这里route的概念可以理解为 运行环境机器 到 目标机器的一条线路。
    // 举例来说，我们使用HttpClient的实现来分别请求 www.baidu.com 的资源和 www.bing.com 的资源那么他就会产生两个route。
    //如果设置成200.那么就算上面的MAX_CONNECTION_NUM设置成9999，对同一个网站，也只会有200个可用连接
    private final Integer MAX_PER_ROUTE = 10;
    //握手超时时间
    private final Integer SOCKET_TIMEOUT = 10000;
    //连接请求超时时间
    private final Integer CONNECTION_REQUEST_TIMEOUT = 10000;
    //连接超时时间
    private final Integer CONNECTION_TIMEOUT = 10000;


    /**
     * 发送post请求，返回String
     */
    public <T> String doPostForString(String url, T obj){
        //发送请求返回response
        CloseableHttpResponse response = doPost(url, obj);
        //response 转 string
        String result = responseToString(response);
        //关闭
        closeResponseAndIn(null,response);

        return result;
    }

    /**
     * 发起post请求,根据url，参数实体
     */
    public <T> CloseableHttpResponse doPost(String url, T obj) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(gsonFactory.build().toJson(obj), Charsets.UTF_8));
        CloseableHttpResponse response = null;
        try {
            response = getHttpClient().execute(httpPost);
        } catch (IOException e) {
            log.error("【httpClient】发送请求失败.IOException={}", e.getMessage(), e);
            throw new SmsSenderException(ErrorEnum.HTTP_ERROR);
        }
        return response;
    }


    /**
     * 从response 中取出 html String
     * 如果没有访问成功，返回null
     */
    private String responseToString(CloseableHttpResponse response) {
        if (isSuccess(response)) {
            try {
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            } catch (IOException e) {
                log.error("【httpClient】response转String,发生io异常.error={}",e.getMessage(),e);
                throw new SmsSenderException(ErrorEnum.HTTP_RESPONSE_IO_ERROR);
            }
        }
        //这句不可能执行到...，返回值不会为null
        return null;
    }

    /**
     * 校验是否请求成功
     */
    private boolean isSuccess(CloseableHttpResponse response) {
        boolean flag = null != response && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
        //成功直接返回
        if(flag)
            return flag;

        //如果失败，记录日志，关闭response，抛出异常
        log.error("【httpClient】请求成功，但状态码非200，状态码:{}",response.getStatusLine().getStatusCode());
        closeResponseAndIn(null, response);
        throw new SmsSenderException(ErrorEnum.HTTP_STATUS_CODE_ERROR);
    }

    /**
     * 发起get请求，返回 response
     */
    public CloseableHttpResponse sendGetRequestForResponse(String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        response = getHttpClient().execute(httpget);
        /**
         * 如果失败，关闭连接
         */
        if (!isSuccess(response)) {
            httpget.abort();
            closeResponseAndIn(null, response);
        }
        return response;
    }


    /**
     * 发起get请求，返回 html string
     */
    public String sendGetRequestForHtml(String url) throws Exception {
        CloseableHttpResponse response = sendGetRequestForResponse(url);
        String html = responseToString(response);
        closeResponseAndIn(null, response);
        return html;
    }


    /**
     * 关闭  in 和 response
     */
    public void closeResponseAndIn(InputStream inputStream, CloseableHttpResponse response) {
        try {
            @Cleanup
            InputStream temp1 = inputStream;
            @Cleanup
            CloseableHttpResponse temp2 = response;
        } catch (Exception e) {
            log.error("【httpClient】关闭response失败.error={}",e.getMessage(),e);
            //不抛出异常
        }
//        if (inputStream != null) {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                inputStream = null;
//            }
//        }
//        if (response != null) {
//            try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                response = null;
//            }
//        }
    }

    /**
     * 获取HttpClient
     */
    public CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                //设置连接池
                .setConnectionManager(pool)
                //请求配置
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 私有化构造方法，构造时，创建对应的连接池实例
     * 使用连接池管理HttpClient可以提高性能
     */
    private HttpClientUtil() {
        try {
            /**
             * 初始化连接池
             */
            SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
            sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    sslContextBuilder.build());
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", socketFactory)
                    .register("http", new PlainConnectionSocketFactory())
                    .build();
            pool = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            pool.setMaxTotal(MAX_CONNECTION_NUM);
            pool.setDefaultMaxPerRoute(MAX_PER_ROUTE);

            /**
             * 初始化请求配置
             */
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .setConnectTimeout(CONNECTION_TIMEOUT)
                    .build();
        } catch (Exception e) {
            log.error("【httpClient】连接池创建失败!");
        }
    }

    /**
     * 单例
     */
    private static HttpClientUtil instance;
    private static ReentrantLock lock = new ReentrantLock();
    public static HttpClientUtil getInstance() {
        if (instance == null) {
            lock.lock();
            instance = new HttpClientUtil();
            lock.unlock();
        }
        return instance;
    }
}
