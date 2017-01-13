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
import java.util.Map;

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
		return HttpRequestUtil.httpGetforString(url, cookie, userAgent,null);
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
	public static String httpPostforFile(String url,
			Map<String, File> files) throws Exception
	{
		return httpPostforString(url, (String)null,
				(String) null,null,
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
	 * 公用post方法:文件上传方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url, String cookie,
			String userAgent, Map<String, Object> params,
			Map<String, File> files) throws Exception {
		return HttpRequestUtil.httpPostforString(url, cookie, userAgent,params,files);
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
	
	
	/**
	 * 公用post方法:文件上传方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @throws AppException
	 */
	public static String httpPostforString(String url, String cookie,
			String userAgent,
			Map<String, File> files) throws Exception {
		return HttpRequestUtil.httpPostforString(url, cookie, userAgent,files);
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
