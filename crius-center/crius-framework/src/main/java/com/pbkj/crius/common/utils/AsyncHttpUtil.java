package com.pbkj.crius.common.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author yaoyuan
 * @createTime 2019/12/12 2:10 PM
 */
@Component
public class AsyncHttpUtil {
    private static ConnectingIOReactor ioReactor;
    private static PoolingNHttpClientConnectionManager cm;
    private static CloseableHttpAsyncClient httpAsyncClient;

    private static CloseableHttpAsyncClient notFusingHttpAsyncClient;

    @PostConstruct
    private void init() throws Exception {
        ioReactor = new DefaultConnectingIOReactor();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                //连接超时,连接建立时间,三次握手完成时间
                .setSocketTimeout(5000)
                //请求超时,数据传输过程中数据包之间间隔的最大时间
                .setConnectionRequestTimeout(5000)
                //使用连接池来管理连接,从连接池获取连接的超时时间
                .build();
        cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(2000);
        httpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
        httpAsyncClient.start();
    }

    @PostConstruct
    private void initNotFusingHttpAsyncClient() throws Exception {
        ioReactor = new DefaultConnectingIOReactor();
        cm = new PoolingNHttpClientConnectionManager(ioReactor);
        cm.setMaxTotal(1000);
        notFusingHttpAsyncClient = HttpAsyncClients.custom().setConnectionManager(cm).build();
        notFusingHttpAsyncClient.start();
    }

    public void sendPost(HttpPost post, FutureCallback<HttpResponse> callback) {
        httpAsyncClient.execute(post, callback);
    }
    public void sendPostNotFusing(HttpPost post, FutureCallback<HttpResponse> callback) {
        notFusingHttpAsyncClient.execute(post, callback);
    }
}
