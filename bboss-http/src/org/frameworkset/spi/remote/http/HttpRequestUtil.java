/**
 * 
 */
package org.frameworkset.spi.remote.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @author yinbp
 *
 * @Date:2016-11-20 11:39:59
 */
public class HttpRequestUtil {

	public static final String UTF_8 = "UTF-8";
	// public static final String DESC = "descend";
	// public static final String ASC = "ascend";

//	private final static int TIMEOUT_CONNECTION = 20000;
//	private final static int TIMEOUT_SOCKET = 20000;
//	private final static int RETRY_TIME = 3;

	private static HttpClient getHttpClient() throws Exception {
		return ClientConfiguration.getDefaultHttpclient();
	}
	private static HttpClient getHttpClient(String poolname) throws Exception {
		return ClientConfiguration.getClientConfiguration(poolname).getHttpClient();
	}

	private static String getCookie() {
		// if(appCookie == null || appCookie == "") {
		// appCookie = appContext.getProperty("cookie");
		// }
		// return appCookie;
		return null;
	}

	private static String getUserAgent() {
		// if(appUserAgent == null || appUserAgent == "") {
		// StringBuilder ua = new StringBuilder("OSChina.NET");
		// ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App版本
		// ua.append("/Android");//手机系统平台
		// ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
		// ua.append("/"+android.os.Build.MODEL); //手机型号
		// ua.append("/"+appContext.getAppId());//客户端唯一标识
		// appUserAgent = ua.toString();
		// }
		// return appUserAgent;
		return null;
	}
	private static HttpGet getHttpGet(String url, String cookie, String userAgent,Map<String,String> headers){
		return  getHttpGet("default",  url,   cookie,   userAgent, headers);
	}
	private static HttpGet getHttpGet(String httppoolname,String url, String cookie, String userAgent,Map<String,String> headers) {

		HttpGet httpget = new HttpGet(url);
		// Request configuration can be overridden at the request level.
		// They will take precedence over the one set at the client level.
		RequestConfig requestConfig = ClientConfiguration.getClientConfiguration(httppoolname).getRequestConfig();	
				
				 
		httpget.setConfig(requestConfig);
		httpget.addHeader("Host", "www.bbossgroups.com");
		httpget.addHeader("Connection", "Keep-Alive");
		if (cookie != null)
			httpget.addHeader("Cookie", cookie);
		if (userAgent != null)
			httpget.addHeader("User-Agent", userAgent);
		if(headers != null && headers.size() > 0){
			Iterator<Entry<String, String>> entries = headers.entrySet().iterator();
			while(entries.hasNext()){
				Entry<String, String> entry = entries.next();
				httpget.addHeader(entry.getKey(),entry.getValue());
			}
		}
		return httpget;
	}
	private static HttpPost getHttpPost(String url, String cookie, String userAgent){
		return getHttpPost("default",url, cookie, userAgent,null);
	}
	private static HttpPost getHttpPost(String httppoolname,String url, String cookie, String userAgent,Map<String,String> headers) {
		HttpPost httpPost = new HttpPost(url);
		RequestConfig requestConfig = ClientConfiguration.getClientConfiguration(httppoolname).getRequestConfig();
		httpPost.setConfig(requestConfig);
		httpPost.addHeader("Host", "www.bbossgroups.com");
		httpPost.addHeader("Connection", "Keep-Alive");
		if (cookie != null)
			httpPost.addHeader("Cookie", cookie);
		if (userAgent != null)
			httpPost.addHeader("User-Agent", userAgent);
		if(headers != null && headers.size() > 0){
			Iterator<Entry<String, String>> entries = headers.entrySet().iterator();
			while(entries.hasNext()){
				Entry<String, String> entry = entries.next();
				httpPost.addHeader(entry.getKey(),entry.getValue());
			}
		}
		
		
		return httpPost;
	}
	public static String httpGetforString(String url) throws Exception {
		return httpGetforString(url, (String) null, (String) null,  (Map<String,String>)null);
	}
	public static String httpGetforString(String url,Map<String,String> headers) throws Exception {
		return httpGetforString(url, (String) null, (String) null,  headers);
	}
	public static String httpGetforString(String poolname,String url,Map<String,String> headers) throws Exception {
		return httpGetforString(poolname,url, (String) null, (String) null, headers);
	}
	public static String httpGetforString(String url, String cookie, String userAgent,Map<String,String> headers) throws Exception {
		return httpGetforString("default",url, cookie, userAgent,  headers);
	}

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	public static String httpGetforString(String poolname,String url, String cookie, String userAgent,Map<String,String> headers) throws Exception {
		// String cookie = getCookie();
		// String userAgent = getUserAgent();

		HttpClient httpClient = null;
		HttpGet httpGet = null;

		String responseBody = "";
		int time = 0;
		int RETRY_TIME = ClientConfiguration.getClientConfiguration(poolname).getRetryTime();
		do {
			try {
				httpClient = getHttpClient(poolname);
				httpGet = getHttpGet(poolname,url, cookie, userAgent,  headers);

				// Create a custom response handler
				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

					@Override
					public String handleResponse(final HttpResponse response)
							throws ClientProtocolException, IOException {
						int status = response.getStatusLine().getStatusCode();
						if (status >= 200 && status < 300) {
							HttpEntity entity = response.getEntity();
							return entity != null ? EntityUtils.toString(entity) : null;
						} else {
							throw new ClientProtocolException("Unexpected response status: " + status);
						}
					}

				};
				responseBody = httpClient.execute(httpGet, responseHandler);
				break;
			} catch (ClientProtocolException e) {
				throw new HttpRuntimeException("请求异常：", e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				throw new HttpRuntimeException("请求异常：", e);
			} catch (Exception e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
				throw new HttpRuntimeException("请求异常：", e);
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		return responseBody;
		// //responseBody = responseBody.replaceAll("\\p{Cntrl}", "\r\n");
		// if(responseBody.contains("result") &&
		// responseBody.contains("errorCode") &&
		// appContext.containsProperty("user.uid")){
		// try {
		// Result res = Result.parse(new
		// ByteArrayInputStream(responseBody.getBytes()));
		// if(res.getErrorCode() == 0){
		// appContext.Logout();
		// appContext.getUnLoginHandler().sendEmptyMessage(1);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// return new ByteArrayInputStream(responseBody.getBytes());
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostFileforString(String url, Map<String, Object> params, Map<String, File> files)
			throws Exception {
		return httpPostFileforString("default",url, (String) null, (String) null, params, files);
	}
	
	public static String httpPostFileforString(String poolname,String url, Map<String, Object> params, Map<String, File> files)
			throws Exception {
		return httpPostFileforString(poolname,url, (String) null, (String) null, params, files);
	}
	public static String httpPostforString(String url, Map<String, Object> params) throws Exception {
		return httpPostforString(  url,  params,  (Map<String,String>)null) ;
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url, Map<String, Object> params,Map<String,String> headers) throws Exception {
		return httpPostFileforString("default",url, (String) null, (String) null, params, (Map<String, File>) null,  headers);
	}
	
	public static String httpPostforString(String poolname,String url, Map<String, Object> params) throws Exception {
		return httpPostFileforString(poolname,url, (String) null, (String) null, params, (Map<String, File>) null);
	}

	public static String httpPostforString(String url) throws Exception {
		return httpPostforString("default",url);
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String poolname,String url) throws Exception {
		return httpPostFileforString(poolname,url, (String) null, (String) null, (Map<String, Object>) null,
				(Map<String, File>) null);
	}
	public static String httpPostforString( String url, String cookie, String userAgent,
			Map<String, File> files) throws Exception{
		return httpPostforString("default",  url,   cookie,   userAgent,
				 files) ;
	}
	public static String httpPostforString(String poolname,String url, String cookie, String userAgent,
			Map<String, File> files) throws Exception {
		return httpPostFileforString(poolname,url, cookie, userAgent, null,
				files);
	}
	public static String httpPostforString(String url, String cookie, String userAgent, Map<String, Object> params,
			Map<String, File> files) throws Exception {
		return httpPostFileforString("default",url,   cookie,   userAgent,   params,
				 files) ;
	}
	
	public static String httpPostFileforString(String poolname,String url, String cookie, String userAgent, Map<String, Object> params,
			Map<String, File> files) throws Exception {
		return httpPostFileforString(poolname,url,   cookie,   userAgent,   params,
				  files,null);
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostFileforString(String poolname,String url, String cookie, String userAgent, Map<String, Object> params,
			Map<String, File> files,Map<String,String> headers) throws Exception {
		// System.out.println("post_url==> "+url);
		// String cookie = getCookie(appContext);
		// String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		HttpPost httpPost = null;
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
//				
//                .addPart("bin", bin)
//                .addPart("comment", comment)
//                .build();
//				 FileBody bin = new FileBody(new File(args[0]));
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
		HttpEntity httpEntity = null;
		List<NameValuePair> paramPair = null;
		if (files != null) {
			// post表单参数处理
			int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
			 
			int i = 0;
			boolean hasdata = false;
			 
			if (params != null) {
				Iterator<Entry<String, Object>> it = params.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()),ClientConfiguration.TEXT_PLAIN_UTF_8);
					hasdata = true;
				}
			}
			if (files != null) {
				Iterator<Entry<String, File>> it = files.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, File> entry = it.next();
					 
//						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
						File f = new File(String.valueOf(entry.getValue()));
						if(f.exists())
						{
							 FileBody file = new FileBody(f);
							multipartEntityBuilder.addPart(entry.getKey(), file);
							hasdata = true;
						}
						else
						{
							
						}
					 
					// System.out.println("post_key_file==> "+file);
				}
			}
			if(hasdata)
				httpEntity = multipartEntityBuilder.build();
		} else if (params != null && params.size() > 0) {
			paramPair = new ArrayList<NameValuePair>();
			Iterator<Entry<String, Object>> it = params.entrySet().iterator();
			NameValuePair paramPair_ = null;
			for (int i = 0; it.hasNext(); i++) {
				Entry<String, Object> entry = it.next();
				paramPair_ = new BasicNameValuePair(entry.getKey(),String.valueOf(entry.getValue()));			 
				paramPair.add(paramPair_);
			}
		}

		String responseBody = "";
		int time = 0;
		int RETRY_TIME = ClientConfiguration.getClientConfiguration(poolname).getRetryTime();
		do {
			try {
				httpClient = getHttpClient(poolname);
				httpPost = getHttpPost(poolname,url, cookie, userAgent,headers);
			


				
				if (httpEntity != null ) {
					httpPost.setEntity(httpEntity);
				} else if(paramPair != null && paramPair.size() > 0){
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

					httpPost.setEntity(entity);

				}
				// Create a custom response handler
				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

					@Override
					public String handleResponse(final HttpResponse response)
							throws ClientProtocolException, IOException {
						int status = response.getStatusLine().getStatusCode();
						if (status >= 200 && status < 300) {
							HttpEntity entity = response.getEntity();
							return entity != null ? EntityUtils.toString(entity) : null;
						} else {
							throw new ClientProtocolException("Unexpected response status: " + status);
						}
					}

				};
				responseBody = httpClient.execute(httpPost, responseHandler);				
				break;
			}
			catch(ClientProtocolException e)
			{
				throw new HttpRuntimeException("请求异常：", e);
			}
			catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				throw new HttpRuntimeException("请求异常：", e);
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				throw new HttpRuntimeException("请求异常：", e);
			} finally {
				// 释放连接
				//httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return responseBody;
		
	}

}
