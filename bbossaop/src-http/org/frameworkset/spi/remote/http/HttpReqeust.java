/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.spi.remote.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * <p>
 * Title: HttpReqeust.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2014年4月9日 上午10:02:37
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpReqeust {
	public static final String UTF_8 = "UTF-8";
	// public static final String DESC = "descend";
	// public static final String ASC = "ascend";

	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;

	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
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

	private static GetMethod getHttpGet(String url, String cookie,
			String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", "www.bbossgroups.com");
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		if (cookie != null)
			httpGet.setRequestHeader("Cookie", cookie);
		if (userAgent != null)
			httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	public static String httpGetforString(String url) throws Exception {
		return httpGetforString(url, (String)null,
				(String)null);
	}
	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws AppException
	 */
	public static String httpGetforString(String url, String cookie,
			String userAgent) throws Exception {
		// String cookie = getCookie();
		// String userAgent = getUserAgent();

		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpRuntimeException("请求异常：" + statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				break;
			} catch (HttpException e) {
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

	private static PostMethod getHttpPost(String url, String cookie,
			String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", "www.bbossgroups.com");
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		if (cookie != null)
			httpPost.setRequestHeader("Cookie", cookie);
		if (userAgent != null)
			httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url,Map<String, Object> params,
			Map<String, File> files) throws Exception
	{
		return httpPostforString(url, (String)null,
				(String) null,params,
				files) ;
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url,Map<String, Object> params) throws Exception
	{
		return httpPostforString(url, (String)null,
				(String) null,params,
				(Map<String, File>)null) ;
	}
	
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url) throws Exception
	{
		return httpPostforString(url, (String)null,
				(String) null,(Map<String, Object>)null,
				(Map<String, File>)null) ;
	}
	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url, String cookie,
			String userAgent, Map<String, Object> params,
			Map<String, File> files) throws Exception {
		// System.out.println("post_url==> "+url);
		// String cookie = getCookie(appContext);
		// String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		PostMethod httpPost = null;
		Part[] parts = null;
		NameValuePair[] paramPair = null;
		if (files != null)
		{
			// post表单参数处理
			int length = (params == null ? 0 : params.size())
					+ (files == null ? 0 : files.size());
			parts = new Part[length];
			int i = 0;
			if (params != null)
			{
				Iterator<Entry<String, Object>> it = params.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, Object> entry = it.next();
					parts[i++] = new StringPart(entry.getKey(), String.valueOf(entry.getValue()), UTF_8);
					// System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
				}
			}
			if (files != null)
			{
				Iterator<Entry<String, File>> it = files.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, File> entry = it.next();
					try {
						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					// System.out.println("post_key_file==> "+file);
				}
			}
		}
		else if(params != null && params.size() > 0)
		{
			paramPair = new NameValuePair[params.size()];
			Iterator<Entry<String, Object>> it = params.entrySet().iterator();
			NameValuePair paramPair_  = null;
			for(int i = 0; it.hasNext();i ++)
			{
				Entry<String, Object> entry = it.next();
				paramPair_ = new NameValuePair();
				paramPair_.setName(entry.getKey());
				paramPair_.setValue(String.valueOf(entry.getValue()));
				paramPair[i] = paramPair_;
			}
		}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				if (files != null)
				{
					httpPost.setRequestEntity(new MultipartRequestEntity(parts,
							httpPost.getParams()));
				}
				else
				{
					httpPost.addParameters(paramPair);
					
				}
				
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpRuntimeException("请求异常：" + statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// //保存cookie
					// if(appContext != null && tmpcookies != ""){
					// appContext.setProperty("cookie", tmpcookies);
					// appCookie = tmpcookies;
					// }
				}
				responseBody = httpPost.getResponseBodyAsString();
				// System.out.println("XMLDATA=====>"+responseBody);
				break;
			} catch (HttpException e) {
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
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return responseBody;
		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
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

}
