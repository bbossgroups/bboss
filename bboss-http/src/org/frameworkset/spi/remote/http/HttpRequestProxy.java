/**
 *
 */
package org.frameworkset.spi.remote.http;

import com.frameworkset.util.SimpleStringUtil;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.frameworkset.spi.remote.http.proxy.ExceptionWare;
import org.frameworkset.spi.remote.http.proxy.HttpAddress;
import org.frameworkset.spi.remote.http.proxy.HttpProxyRequestException;
import org.frameworkset.spi.remote.http.proxy.NoHttpServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author yinbp
 * @Date:2016-11-20 11:39:59
 */
public class HttpRequestProxy {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestProxy.class);
    public static void startHttpPools(String configFile){
        HttpRequestUtil.startHttpPools(configFile);
    }
    public static void startHttpPools(Map<String,Object> configs){
        HttpRequestUtil.startHttpPools(configs);
    }







    public static String httpGetforString(String url) throws HttpProxyRequestException {
        return httpGetforString(url, (String) null, (String) null, (Map<String, String>) null);
    }

    public static String httpGetforString(String poolname, String url) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, (Map<String, String>) null);
    }

    public static <T> T httpGet(String poolname, String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, (Map<String, String>) null,responseHandler);
    }

    public static <T> T httpGet(String poolname, String url,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers,responseHandler);
    }

    public static <T> T httpGet(String url,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString("default", url, (String) null, (String) null, headers,responseHandler);
    }

    public static String httpGetforString(String url, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString(url, (String) null, (String) null, headers);
    }

    public static String httpGetforString(String poolname, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers);
    }

    public static <T> T httpGetforString(String poolname, String url, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers,responseHandler);
    }

    public static <T> T httpGetforString( String url, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString("default",  url, (String) null, (String) null, headers,responseHandler);
    }

    public static String httpGetforString(String url, String cookie, String userAgent, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString("default", url, cookie, userAgent, headers);
    }
    public static String httpGetforString(String poolname, String url, String cookie, String userAgent, Map<String, String> headers) throws HttpProxyRequestException{
        return  httpGetforString(poolname, url, cookie, userAgent, headers,new StringResponseHandler()) ;
    }
    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpGetforString(String poolname, String url, String cookie, String userAgent, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return httpGet(  poolname,   url,   cookie,   userAgent,   headers, responseHandler);
    }
    private static Exception getException(ResponseHandler responseHandler,ClientConfiguration configuration ){
        ExceptionWare exceptionWare =configuration.getHttpServiceHosts().getExceptionWareBean();
        if(exceptionWare != null) {
            return exceptionWare.getExceptionFromResponse(responseHandler);
        }
        return null;
    }
    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpGet(String poolname, String url, String cookie, String userAgent, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // String cookie = getCookie();
        // String userAgent = getUserAgent();

        HttpClient httpClient = null;
        HttpGet httpGet = null;

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Get call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpGet = HttpRequestUtil.getHttpGet(config, url, cookie, userAgent, headers);
                    responseBody = httpClient.execute(httpGet, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpGet != null)
                        httpGet.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Get call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpGet = HttpRequestUtil.getHttpGet(config, url, cookie, userAgent, headers);
                responseBody = httpClient.execute(httpGet, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpGet != null)
                    httpGet.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }

    /**
     * head请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  poolname,   url,   null, null, (Map<String, String>) null,responseHandler);

    }

    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  "default",   url,   null, null, (Map<String, String>) null,responseHandler);

    }

    /**
     * head请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url,Map<String, Object> params,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  poolname,   url,   null, null,params, (Map<String, String>) headers,responseHandler);

    }

    /**
     * get请求URL
     * ,Map<String, Object> params,Map<String, String> headers,
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, String cookie, String userAgent, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return httpHead(  poolname,   url,   cookie,   userAgent,(Map<String, Object> )null, headers,responseHandler);

    }

    /**
     * get请求URL
     * ,Map<String, Object> params,Map<String, String> headers,
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, String cookie, String userAgent,Map<String, Object> params, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // String cookie = getCookie();
        // String userAgent = getUserAgent();

        HttpClient httpClient = null;
        HttpHead httpHead = null;

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Head call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpHead = HttpRequestUtil.getHttpHead(config, url, cookie, userAgent, headers);
                    HttpParams httpParams = null;
                    if (params != null && params.size() > 0) {
                        httpParams = new BasicHttpParams();
                        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                        NameValuePair paramPair_ = null;
                        for (int i = 0; it.hasNext(); i++) {
                            Entry<String, Object> entry = it.next();
                            httpParams.setParameter(entry.getKey(), entry.getValue());
                        }
                        httpHead.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpHead, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpHead != null)
                        httpHead.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("Head call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpHead = HttpRequestUtil.getHttpHead(config, url, cookie, userAgent, headers);
                HttpParams httpParams = null;
                if (params != null && params.size() > 0) {
                    httpParams = new BasicHttpParams();
                    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                    NameValuePair paramPair_ = null;
                    for (int i = 0; it.hasNext(); i++) {
                        Entry<String, Object> entry = it.next();
                        httpParams.setParameter(entry.getKey(), entry.getValue());
                    }
                    httpHead.setParams(httpParams);
                }
                responseBody = httpClient.execute(httpHead, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpHead != null)
                    httpHead.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }


    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param files
     * @throws HttpProxyRequestException
     */
    public static String httpPostFileforString(String url, Map<String, Object> params, Map<String, File> files)
            throws HttpProxyRequestException {
        return httpPostFileforString("default", url, (String) null, (String) null, params, files);
    }

    public static String httpPostFileforString(String poolname, String url, Map<String, Object> params, Map<String, File> files)
            throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, (String) null, (String) null, params, files);
    }

    public static String httpPostforString(String url, Map<String, Object> params) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null);
    }

    public static <T> T httpPost(String url, Map<String, Object> params,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpPostFileforString("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String poolName,String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpPostFileforString(poolName, url, (String) null, (String) null, params, (Map<String, File>) null, headers);
    }



    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static  <T> T  httpPost(String url, Map<String, Object> params, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers, responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPostforString(String url, Map<String, Object> params, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers,responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPostforString(String poolName,String url, Map<String, Object> params, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost(poolName, url, (String) null, (String) null, params, (Map<String, File>) null, headers,responseHandler);
    }

    public static String httpPostforString(String poolname, String url, Map<String, Object> params) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, (String) null, (String) null, params, (Map<String, File>) null);
    }

    public static String httpPostforString(String url) throws HttpProxyRequestException {
        return httpPostforString("default", url);
    }

    public static <T> T  httpPost(String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String poolname, String url) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null);
    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T  httpPost(String poolname, String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPost(  poolname,   url, (String) null, (String) null, (Map<String, Object>) null,
    			 (Map<String, File>) null, (Map<String, String>)null,responseHandler) ;

    }

    public static String httpPostforString(String url, String cookie, String userAgent,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostforString("default", url, cookie, userAgent,
                files);
    }

    public static String httpPostforString(String poolname, String url, String cookie, String userAgent,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, cookie, userAgent, null,
                files);
    }

    public static String httpPostforString(String url, String cookie, String userAgent, Map<String, Object> params,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString("default", url, cookie, userAgent, params,
                files);
    }

    public static String httpPostFileforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, cookie, userAgent, params,
                files, null);
    }


    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPost(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // System.out.println("post_url==> "+url);
        // String cookie = getCookie(appContext);
        // String userAgent = getUserAgent(appContext);

        HttpClient httpClient = null;
        HttpPost httpPost = null;

//
//                .addPart("bin", bin)
//                .addPart("comment", comment)
//                .build();
//				 FileBody bin = new FileBody(new File(args[0]));
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
        HttpEntity httpEntity = null;
        List<NameValuePair> paramPair = null;
        if (files != null) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // post表单参数处理
            int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());

            int i = 0;
            boolean hasdata = false;

            if (params != null) {
                Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();
                    multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
                    hasdata = true;
                }
            }
            if (files != null) {
                Iterator<Entry<String, File>> it = files.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, File> entry = it.next();

//						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
                    File f = new File(String.valueOf(entry.getValue()));
                    if (f.exists()) {
                        FileBody file = new FileBody(f);
                        multipartEntityBuilder.addPart(entry.getKey(), file);
                        hasdata = true;
                    } else {

                    }

                    // System.out.println("post_key_file==> "+file);
                }
            }
            if (hasdata)
                httpEntity = multipartEntityBuilder.build();
        } else if (params != null && params.size() > 0) {
            paramPair = new ArrayList<NameValuePair>();
            Iterator<Entry<String, Object>> it = params.entrySet().iterator();
            NameValuePair paramPair_ = null;
            for (int i = 0; it.hasNext(); i++) {
                Entry<String, Object> entry = it.next();
                paramPair_ = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
                paramPair.add(paramPair_);
            }
        }

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Post call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = HttpRequestUtil.getHttpPost(config, url, cookie, userAgent, headers);


                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    } else if (paramPair != null && paramPair.size() > 0) {
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                        httpPost.setEntity(entity);

                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("Post call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = HttpRequestUtil.getHttpPost(config, url, cookie, userAgent, headers);


                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                } else if (paramPair != null && paramPair.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                    httpPost.setEntity(entity);

                }

                responseBody = httpClient.execute(httpPost, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPutforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers) throws HttpProxyRequestException{
    	return httpPut(  poolname,   url,   cookie,   userAgent,  params,
                  files,   headers,new StringResponseHandler());
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static String httpPutforString(  String url,Map<String, Object> params, Map<String, String> headers ) throws HttpProxyRequestException{
    	return httpPut(  "default",   url,   (String)null,   (String)null,  params,
    			( Map<String, File> )null,   headers,new StringResponseHandler());
    }
    public static <T> T httpPutforString(  String url,Map<String, Object> params, Map<String, String> headers ,ResponseHandler<T> responseHandler ) throws HttpProxyRequestException{
        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
                ( Map<String, File> )null,   headers ,responseHandler);
    }

    public static String httpPutforString(String poolname,  String url,Map<String, Object> params, Map<String, String> headers ) throws HttpProxyRequestException{
        return httpPut(  poolname,   url,   (String)null,   (String)null,  params,
                ( Map<String, File> )null,   headers,new StringResponseHandler());
    }

    public static <T> T httpPutforString(String poolname,  String url,Map<String, Object> params, Map<String, String> headers ,ResponseHandler<T> responseHandler) throws HttpProxyRequestException{
        return httpPut(  poolname,   url,   (String)null,   (String)null,  params,
                ( Map<String, File> )null,   headers,responseHandler);
    }

    /**
     * 公用post方法
     *

     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut("default", url, cookie, userAgent, params,
                                                    files, headers, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, Map<String, Object> params,  Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)params,
    							(Map<String, File>)null, headers, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, Map<String, Object> params,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)params,
    							(Map<String, File>)null, (Map<String, String>)null, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)null,
    							(Map<String, File>)null, (Map<String, String>)null, responseHandler) ;
    }
    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // System.out.println("post_url==> "+url);
        // String cookie = getCookie(appContext);
        // String userAgent = getUserAgent(appContext);

        HttpClient httpClient = null;
        HttpPut httpPut = null;

//
//                .addPart("bin", bin)
//                .addPart("comment", comment)
//                .build();
//				 FileBody bin = new FileBody(new File(args[0]));
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
        HttpEntity httpEntity = null;
        List<NameValuePair> paramPair = null;
        if (files != null) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // post表单参数处理
            int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());

            int i = 0;
            boolean hasdata = false;

            if (params != null) {
                Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();
                    multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
                    hasdata = true;
                }
            }
            if (files != null) {
                Iterator<Entry<String, File>> it = files.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, File> entry = it.next();

//						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
                    File f = new File(String.valueOf(entry.getValue()));
                    if (f.exists()) {
                        FileBody file = new FileBody(f);
                        multipartEntityBuilder.addPart(entry.getKey(), file);
                        hasdata = true;
                    } else {

                    }

                    // System.out.println("post_key_file==> "+file);
                }
            }
            if (hasdata)
                httpEntity = multipartEntityBuilder.build();
        } else if (params != null && params.size() > 0) {
            paramPair = new ArrayList<NameValuePair>();
            Iterator<Entry<String, Object>> it = params.entrySet().iterator();
            NameValuePair paramPair_ = null;
            for (int i = 0; it.hasNext(); i++) {
                Entry<String, Object> entry = it.next();
                paramPair_ = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
                paramPair.add(paramPair_);
            }
        }

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Put call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPut = HttpRequestUtil.getHttpPut(config, url, cookie, userAgent, headers);


                    if (httpEntity != null) {
                        httpPut.setEntity(httpEntity);
                    } else if (paramPair != null && paramPair.size() > 0) {
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                        httpPut.setEntity(entity);

                    }
                    responseBody = httpClient.execute(httpPut, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpPut != null)
                        httpPut.releaseConnection();
                    httpClient = null;
                }
//        } while (time < RETRY_TIME);
            } while (true);
        }
        else
        {
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Put call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPut = HttpRequestUtil.getHttpPut(config, url, cookie, userAgent, headers);
                if (httpEntity != null) {
                    httpPut.setEntity(httpEntity);
                } else if (paramPair != null && paramPair.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                    httpPut.setEntity(entity);

                }
                responseBody = httpClient.execute(httpPut, responseHandler);
            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPut != null)
                    httpPut.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostFileforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers) throws HttpProxyRequestException {

    	return httpPost(  poolname,   url,   cookie,   userAgent,   params,
                  files,  headers,new StringResponseHandler() );


    }


    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname, String url) throws HttpProxyRequestException{
       return httpDelete(  poolname,   url, (String) null, (String) null, (Map<String, Object>) null,
               (Map<String, String>) null);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete( String url) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, String>) null);

    }
    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDeleteWithbody( String url,String requestBody) throws HttpProxyRequestException{
        return httpDelete(  "default",   url,requestBody, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, String>) null);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete( String url,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, (String) null, (String) null, (Map<String, Object>) null,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete( String url,String requestBody,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  "default",   url,  requestBody, (String) null, (String) null, (Map<String, Object>) null,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDeleteWithbody( String url,String requestBody,Map<String, Object> params,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, requestBody, (String) null, (String) null, params,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete( String url,Map<String, Object> params,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, (String) null, (String) null, params,
                headers);

    }



    public static <T> T httpDelete( String url,Map<String, Object> params,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, (String)null,(String) null, (String) null, params,
                headers, responseHandler);

    }

    public static <T> T httpDeleteWithBody (String url,String requestBody,Map<String, Object> params,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException{
        return httpDelete(  "default",   url, requestBody,(String) null, (String) null, params,
                headers, responseHandler);

    }

    public static String httpDelete( String poolname,String url,Map<String, Object> params,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  poolname,   url, (String) null, (String) null, params,
                headers);

    }

    public static String httpDelete ( String poolname,String url,String requestBody,Map<String, Object> params,Map<String, String> headers) throws HttpProxyRequestException{
        return httpDelete(  poolname,   url,  requestBody,(String)null, (String) null, params,
                headers);

    }

    public static <T> T httpDelete( String poolname,String url,Map<String, Object> params,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException{
        return httpDelete(  poolname,   url,(String)null, (String) null, (String) null, params,
                headers,responseHandler);

    }

    public static <T> T httpDelete( String poolname,String url,String requestBody,Map<String, Object> params,Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException{
        return httpDelete(  poolname,     url, requestBody,(String) null, (String) null, params,
                headers,responseHandler);

    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                                Map<String, String> headers) throws HttpProxyRequestException {
    	return httpDelete(  poolname,   url, (String)null  ,cookie,   userAgent,   params,
                  headers,new StringResponseHandler());
    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname,String url,String requestBody,  String cookie, String userAgent, Map<String, Object> params,
                                    Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  poolname,   url,  requestBody, cookie,   userAgent,   params,
                headers,new StringResponseHandler());
    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpDelete(String poolname, String url, String requestBody, String cookie, String userAgent, Map<String, Object> params,
                                                Map<String, String> headers,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {


        HttpClient httpClient = null;
        HttpDeleteWithBody httpDeleteWithBody = null;
        HttpDelete httpDelete = null;


        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        HttpEntity httpEntity = requestBody == null?null:new StringEntity(
                requestBody,
                ContentType.APPLICATION_JSON);
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Delete call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    HttpParams httpParams = null;
                    if (params != null && params.size() > 0) {
                        httpParams = new BasicHttpParams();
                        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                        NameValuePair paramPair_ = null;
                        for (int i = 0; it.hasNext(); i++) {
                            Entry<String, Object> entry = it.next();
                            httpParams.setParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    if (httpEntity != null) {
                        httpDeleteWithBody = HttpRequestUtil.getHttpDeleteWithBody(config, url, cookie, userAgent, headers);
                        httpDeleteWithBody.setEntity(httpEntity);
                        if (httpParams != null) {

                            httpDeleteWithBody.setParams(httpParams);
                        }
                        responseBody = httpClient.execute(httpDeleteWithBody, responseHandler);
                    } else {
                        httpDelete = HttpRequestUtil.getHttpDelete(config, url, cookie, userAgent, headers);
                        if (httpParams != null) {
                            httpDelete.setParams(httpParams);
                        }
                        responseBody = httpClient.execute(httpDelete, responseHandler);
                    }

                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpDelete != null)
                        httpDelete.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Delete call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                HttpParams httpParams = null;
                if (params != null && params.size() > 0) {
                    httpParams = new BasicHttpParams();
                    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                    NameValuePair paramPair_ = null;
                    for (int i = 0; it.hasNext(); i++) {
                        Entry<String, Object> entry = it.next();
                        httpParams.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                if (httpEntity != null) {
                    httpDeleteWithBody = HttpRequestUtil.getHttpDeleteWithBody(config, url, cookie, userAgent, headers);
                    httpDeleteWithBody.setEntity(httpEntity);
                    if (httpParams != null) {

                        httpDeleteWithBody.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpDeleteWithBody, responseHandler);
                } else {
                    httpDelete = HttpRequestUtil.getHttpDelete(config, url, cookie, userAgent, headers);
                    if (httpParams != null) {
                        httpDelete.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpDelete, responseHandler);
                }


            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpDelete != null)
                    httpDelete.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }


    public static String sendStringBody(String poolname,String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return  sendBody(poolname,  requestBody,   url,   headers,ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendJsonBody(String poolname,String requestBody, String url) throws HttpProxyRequestException {

        return  sendBody(   poolname, requestBody,   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBody(String poolname,Object requestBody, String url) throws HttpProxyRequestException {

        return  sendBody(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendStringBody(String poolname,String requestBody, String url) throws HttpProxyRequestException {
        return  sendBody(  poolname,  requestBody,   url,   null,ContentType.create(
                "text/plain", Consts.UTF_8));
    }


    public static String sendJsonBody(String poolname, Object requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {

        return  sendBody(  poolname, SimpleStringUtil.object2json(requestBody),   url,   headers,ContentType.APPLICATION_JSON);
    }
    public static String sendJsonBody(String poolname, String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {

        return  sendBody(  poolname, requestBody,   url,   headers,ContentType.APPLICATION_JSON);
    }
    public static String sendStringBody(String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   headers,ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendJsonBody(String requestBody, String url) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBody( String url) throws HttpProxyRequestException {

        return  sendBody( "default", (String)null,   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBodyWithPool(String poolName, String url) throws HttpProxyRequestException {

        return  sendBody( poolName, (String)null,   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBody(Object requestBody, String url) throws HttpProxyRequestException {

        return  sendBody( "default", SimpleStringUtil.object2json(requestBody),   url,   null,ContentType.APPLICATION_JSON);
    }

    public static String sendStringBody(String requestBody, String url) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   null,ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendStringBody(String requestBody, String url, String mimeType, Charset charSet) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   null,ContentType.create(
                mimeType, charSet));
    }

    public static String sendJsonBody(String requestBody, String url, Map<String, String> headers ) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   headers,ContentType.APPLICATION_JSON);
    }
    public static <T> T sendJsonBody(String requestBody, String url, Map<String, String> headers  ,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   headers,ContentType.APPLICATION_JSON, responseHandler);
    }

    public static <T> T sendJsonBody(String poolname,String requestBody, String url, Map<String, String> headers  ,ResponseHandler<T> responseHandler) throws HttpProxyRequestException {

        return  sendBody( poolname, requestBody,   url,   headers,ContentType.APPLICATION_JSON, responseHandler);
    }
    public static <T> T sendBody(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;


        HttpEntity httpEntity = new StringEntity(
                requestBody,
                contentType);
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("sendBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = HttpRequestUtil.getHttpPost(config, url, "", "", headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                }  finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }


            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("sendBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = HttpRequestUtil.getHttpPost(config, url, "", "", headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    private static HttpRequestBase getHttpEntityEnclosingRequestBase(String action,ClientConfiguration config, String url,  Map<String, String> headers){
        if(action.equals(HttpRequestUtil.HTTP_POST)){
            return HttpRequestUtil.getHttpPost(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_GET)){
            return HttpRequestUtil.getHttpGet(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_PUT)){
            return HttpRequestUtil.getHttpPut(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_DELETE)){
            return HttpRequestUtil.getHttpDelete(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_HEAD)){
            return HttpRequestUtil.getHttpHead(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_TRACE)){
            return HttpRequestUtil.getHttpTrace(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_OPTIONS)){
            return HttpRequestUtil.getHttpOptions(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_PATCH)){
            return HttpRequestUtil.getHttpPatch(config, url, null, null, headers);
        }
        throw new IllegalArgumentException("not support http action:"+action);
    }
    private static HttpProxyRequestException handleSocketTimeoutException(ClientConfiguration configuration,SocketTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Socket Timeout for ").append(configuration.getTimeoutSocket()).append("ms.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    private static HttpProxyRequestException handleConnectionPoolTimeOutException(ClientConfiguration configuration,ConnectionPoolTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Wait timeout for ").append(configuration.getConnectionRequestTimeout()).append("ms for idle http connection from http connection pool.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    private static HttpProxyRequestException handleConnectionTimeOutException(ClientConfiguration configuration,ConnectTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Build a http connection timeout for ").append(configuration.getTimeoutConnection()).append("ms.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    public static <T> T sendBody(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType,
                                 ResponseHandler<T> responseHandler,String action) throws HttpProxyRequestException {
        CloseableHttpClient httpClient = null;
        HttpEntityEnclosingRequestBase httpPost = null;


        HttpEntity httpEntity = requestBody != null?new StringEntity(
                requestBody,
                contentType):null;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
//        int time = 0;
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(),endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("sendBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = (HttpEntityEnclosingRequestBase) getHttpEntityEnclosingRequestBase(action, config, url, headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }

//        } while (time < RETRY_TIME);
            }while(true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("sendBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = (HttpEntityEnclosingRequestBase) getHttpEntityEnclosingRequestBase(action, config, url, headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);

            } catch (Exception ex) {
                 e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    
    public static String sendBody(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType) throws HttpProxyRequestException {
    	return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    HttpEntity entity = response.getEntity();
                    if (entity != null )
                        return EntityUtils.toString(entity);
                    else
                        throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        });
        
    }
    
    public static <T> T putBody(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        CloseableHttpClient httpClient = null;
        HttpPut httpPost = null;


        HttpEntity httpEntity = new StringEntity(
                requestBody,
                contentType);
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
//        int time = 0;
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
            do {

                try {

                    httpAddress = config.getHttpServiceHosts().getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("putBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = HttpRequestUtil.getHttpPut(config, url, "", "", headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,config );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!config.getHttpServiceHosts().reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new NoHttpServerException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                }  finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }

            } while (true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("putBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = HttpRequestUtil.getHttpPut(config, url, "", "", headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    
    public static String putBody(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType) throws HttpProxyRequestException {
    	return putBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    HttpEntity entity = response.getEntity();
                    if (entity != null )
                        return EntityUtils.toString(entity);
                    else
                        throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        });
        
    }
    
    public static <T> T putBody(String requestBody, String url, Map<String, String> headers,ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( "default", requestBody,   url,  headers,  contentType,  responseHandler) ;
    }
    
    public static String putBody(String requestBody, String url, Map<String, String> headers,ContentType contentType) throws HttpProxyRequestException {
    	return putBody( "default",requestBody,   url,   headers,  contentType) ;
        
    }
    
    

    public static <T> T putJson(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return putJson(  poolname,  requestBody,   url,   headers,ContentType.APPLICATION_JSON,  responseHandler);
    }
    
    public static String putJson(String poolname,String requestBody, String url, Map<String, String> headers,ContentType contentType) throws HttpProxyRequestException {
    	return putJson(  poolname,  requestBody,   url, headers, ContentType.APPLICATION_JSON);
        
    }
    
    public static <T> T putJson(String requestBody, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( "default", requestBody,   url,  headers,   ContentType.APPLICATION_JSON,  responseHandler) ;
    }

    public static <T> T putJson(String poolName,String requestBody, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( poolName, requestBody,   url,  headers,   ContentType.APPLICATION_JSON,  responseHandler) ;
    }


    public static String putJson(String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
    	return putBody( "default",requestBody,   url,   headers,  ContentType.APPLICATION_JSON) ;
        
    }

    public static String putJson(String poolName,String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return putBody(poolName,requestBody,   url,   headers,  ContentType.APPLICATION_JSON) ;

    }
    
    

}
