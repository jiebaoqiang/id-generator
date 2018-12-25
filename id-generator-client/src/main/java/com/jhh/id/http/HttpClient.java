package com.jhh.id.http;


/**
 * Http请求工具类
 *
 * @author siguiyang
 */
public class HttpClient {

    /**
     * http get 请求
     *
     * @param url  请求地址
     * @param loop true 表示使用长连接请求，false 正常java 底层请求方式
     */
    public static String doGet(String url, boolean loop) {
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }

        if (loop) {
            try {
                return HttpClientForLoop.doGet(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return HttpURLClient.doGet(url);
        }
        return null;
    }

//    public static void main(String[] args) {
////        sendPostLoop("http", "192.168.1.90:9999", "/id/v1/BIZ_TEST2", null);
//        System.out.println(doGet("192.168.1.90:9999/id/v1/BIZ_TEST2", true));
//
//        String URL = "192.168.1.90:9999/id/v1/";
//
//        System.out.println(URL.substring(0, URL.indexOf("/")));
//        System.out.println(URL.substring(URL.indexOf("/"), URL.length()));
//    }
}
