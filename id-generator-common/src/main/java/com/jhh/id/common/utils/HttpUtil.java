package com.jhh.id.common.utils;

import com.jhh.id.common.constant.Charset;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public abstract class HttpUtil {

  private static final int CONNECT_TIMEOUT = 50000;

  private static final int SOCKET_TIMEOUT = 50000;

  private static final int CONNECTION_REQUEST_TIMEOUT = 3000;

  private static final String DEFAULT_CONTENT_TYPE = "plain/text";

  private HttpUtil() {
    throw new IllegalStateException("不允许此操作.");
  }

  public static String get(String url) throws IOException {
    return get(url, CONNECT_TIMEOUT, SOCKET_TIMEOUT,
        CONNECTION_REQUEST_TIMEOUT);
  }


  /**
   * 发送http get请求
   * @param url 请求地址
   * @param connectTimeout 连接超时时间，单位毫秒
   * @param socketTimeout 请求获取数据的超时时间，单位毫秒
   * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒
   * @return
   * @throws IOException
   */
  public static String get(String url, int connectTimeout, int socketTimeout,
      int connectionRequestTimeout) throws IOException {
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(createRequestConfig(connectTimeout, socketTimeout,
        connectionRequestTimeout));
    return request(httpGet);
  }


  /**
   * 发送http post请求
   *
   * @param url 请求地址
   * @param paramMap 表单参数
   */
  public static String post(String url, Map<String, String> paramMap)
      throws IOException {
    return post(url, paramMap, CONNECT_TIMEOUT, SOCKET_TIMEOUT,
        CONNECTION_REQUEST_TIMEOUT);
  }


  /**
   * 发送http post请求
   *
   * @param url 网络地址
   * @param text 请求参数
   */
  public static String post(String url, String text) throws IOException {
    return post(url, text, CONNECT_TIMEOUT, SOCKET_TIMEOUT,
        CONNECTION_REQUEST_TIMEOUT);
  }


  /**
   * 发送http post请求
   *
   * @param url 网络地址
   * @param text 请求参数
   * @param connectTimeout 连接超时时间，单位毫秒
   * @param socketTimeout 请求获取数据的超时时间，单位毫秒
   * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒
   */
  public static String post(String url, String text,
      int connectTimeout,
      int socketTimeout,
      int connectionRequestTimeout) throws IOException {
    return post(url, new StringEntity(text,
            ContentType.create(DEFAULT_CONTENT_TYPE, Charset.UTF_8)),
        connectTimeout,
        socketTimeout, connectionRequestTimeout);
  }


  /**
   * 发送http post请求
   *
   * @param url 网络地址
   * @param paramMap 请求参数
   * @param connectTimeout 连接超时时间，单位毫秒
   * @param socketTimeout 请求获取数据的超时时间，单位毫秒
   * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒
   */
  public static String post(String url, Map<String, String> paramMap,
      int connectTimeout,
      int socketTimeout,
      int connectionRequestTimeout) throws IOException {
    return post(url, createUrlEncodedFormEntity(paramMap), connectTimeout,
        socketTimeout, connectionRequestTimeout);
  }


  /**
   * 发送http post请求
   *
   * @param url 网络地址
   * @param httpEntity org.apache.http.HttpEntity
   * @param connectTimeout 连接超时时间，单位毫秒
   * @param socketTimeout 请求获取数据的超时时间，单位毫秒
   * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒
   */
  private static String post(String url, HttpEntity httpEntity,
      int connectTimeout,
      int socketTimeout,
      int connectionRequestTimeout) throws IOException {
    HttpPost httpPost = new HttpPost(url);
    httpPost.setEntity(httpEntity);
    httpPost.setConfig(createRequestConfig(connectTimeout, socketTimeout,
        connectionRequestTimeout));
    return request(httpPost);
  }


  /**
   * 发送http请求
   *
   * @param request org.apache.http.client.methods.HttpUriRequest
   */
  private static String request(HttpUriRequest request) throws IOException {
    try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
      return httpclient.execute(request, (HttpResponse response) -> {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
          throw new HttpResponseException(
              statusLine.getStatusCode(),
              statusLine.getReasonPhrase());
        }
        if (entity == null) {
          throw new ClientProtocolException("Response contains no content");
        }
        return EntityUtils.toString(entity, Charset.UTF_8);
      });
    }
  }

  /**
   * 构造RequestConfig对象
   *
   * @param connectTimeout 连接超时时间，单位毫秒
   * @param socketTimeout 请求获取数据的超时时间，单位毫秒
   * @param connectionRequestTimeout 设置从connect Manager获取Connection 超时时间，单位毫秒
   */
  private static RequestConfig createRequestConfig(int connectTimeout,
      int socketTimeout, int connectionRequestTimeout) {
    RequestConfig defaultConfig = RequestConfig.custom()
        .build();
    return RequestConfig.copy(defaultConfig)
        .setConnectTimeout(connectTimeout)
        .setSocketTimeout(socketTimeout)
        .setConnectionRequestTimeout(connectionRequestTimeout)
        .build();
  }


  /**
   * 根据传入的Map类型请求参数创建org.apache.http.client.entity.UrlEncodedFormEntity对象
   *
   * @param paramMap 请求参数
   */
  private static HttpEntity createUrlEncodedFormEntity(
      Map<String, String> paramMap) throws UnsupportedEncodingException {
    List<NameValuePair> nameValuePairs = new ArrayList<>();
    for (Entry<String, String> entity : paramMap.entrySet()) {
      nameValuePairs
          .add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
    }
    return new UrlEncodedFormEntity(
        nameValuePairs, Charset.UTF_8);
  }


}
