package com.jhh.id.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientForLoop {
    // 最大连接数
    private final static int maxTotal = 100;
    // 并发数
    private final static int defaultMaxPerRoute = 20;
    // 创建连接的最长时间
    private final static int connectTimeout = 60000;
    // 从连接池中获取到连接的最长时间
    private final static int connectionRequestTimeout = 60000;
    // 数据传输的最长时间
    private final static int socketTimeout = 60000;

    private static final CloseableHttpClient closeableHttpClient;

    private static final RequestConfig requestConfig;

    static {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = getHttpClientConnectionManager();
        HttpClientBuilder httpClientBuilder = getHttpClientBuilder(poolingHttpClientConnectionManager);

        closeableHttpClient = getCloseableHttpClient(httpClientBuilder);
        RequestConfig.Builder builder = getBuilder();
        requestConfig = getRequestConfig(builder);
    }

    /**
     * 实例化一个连接池管理器，设置最大连接数、并发连接数
     */
    private static PoolingHttpClientConnectionManager getHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //最大连接数
        httpClientConnectionManager.setMaxTotal(maxTotal);
        //并发数
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    /**
     * 实例化连接池，设置连接池管理器。
     * 这里需要以参数形式注入上面实例化的连接池管理器
     */
    private static HttpClientBuilder getHttpClientBuilder(PoolingHttpClientConnectionManager httpClientConnectionManager) {

        //HttpClientBuilder中的构造方法被protected修饰，所以这里不能直接使用new来实例化一个HttpClientBuilder，可以使用HttpClientBuilder提供的静态方法create()来获取HttpClientBuilder对象
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        httpClientBuilder.setConnectionManager(httpClientConnectionManager);

        return httpClientBuilder;
    }

    /**
     * 注入连接池，用于获取httpClient
     */
    private static CloseableHttpClient getCloseableHttpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    /**
     * Builder是RequestConfig的一个内部类
     * 通过RequestConfig的custom方法来获取到一个Builder对象
     * 设置builder的连接信息
     * 这里还可以设置proxy，cookieSpec等属性。有需要的话可以在此设置
     *
     */
    private static RequestConfig.Builder getBuilder() {
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout);
    }

    /**
     * 使用builder构建一个RequestConfig对象
     */
    private static RequestConfig getRequestConfig(RequestConfig.Builder builder) {
        return builder.build();
    }


    public static CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public static RequestConfig getRequestConfig() {
        return requestConfig;
    }


    /**
     * 发送get请求
     *
     */
    public static String doGet(String url) {
        // 声明 http get 请求
        HttpGet httpGet = new HttpGet(url);

        // 装载配置信息
        httpGet.setConfig(requestConfig);

        try {
            // 发起请求
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);

            // 判断状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 返回响应体的内容
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送json格式数据
     *
     * @param url           请求地址
     * @param ableSerialize 参数
     */
    public static String doPost(String url, Object ableSerialize) {
        // 声明httpPost请求
        HttpPost httpPost = new HttpPost(url);
        // 加入配置信息
        httpPost.setConfig(requestConfig);
        try {
            //设置请求头
            StringEntity entity = new StringEntity(JSON.toJSONString(ableSerialize), "UTF-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            httpPost.setHeader("Accept", "*/*");
            // 发起请求
            CloseableHttpResponse response = closeableHttpClient.execute(httpPost);

            // 判断状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                // 返回响应体的内容
                return EntityUtils.toString(response.getEntity(), "UTF-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        String URL = "192.168.1.90:9999/id/v1/";
//
//        System.out.println(URL.substring(0, URL.indexOf("/")));
//        System.out.println(URL.substring(URL.indexOf("/"), URL.length()));
//
//        for(int i = 0;i<1000;i++) {
//            long time = System.currentTimeMillis();
//            System.out.println("开始耗时" + time);
//            System.out.println(HttpClientForLoop.doGet("http://192.168.1.90:9999/id/v1/BIZ_TEST2"));
//            System.out.println("结束耗时" + (System.currentTimeMillis() - time) + " ms");
//        }
//
//    }

}
