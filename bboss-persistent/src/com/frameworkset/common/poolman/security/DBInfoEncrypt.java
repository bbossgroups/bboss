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
package com.frameworkset.common.poolman.security;

/**
 * 
 * <p>Title: DBInfoEncrypt.java</p>
 *
 * <p>Description: 用来对数据库信息进行加密的抽象类，应用系统可以通过该插件对数据库地址，账号，口令信息进行加密
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2012-3-14 下午3:08:09
 * @author biaoping.yin
 * @version 1.0
 */
public interface DBInfoEncrypt {
	/**
	 * 加密数据接口
	 * @param data
	 * @return
	 */
	public abstract String encrypt(String data);
	/**
	 * 解密数据接口
	 * @param data
	 * @return
	 */
	public abstract String decrypt(String data);
	
	/**
	 * 加密数据接口
	 * @param data
	 * @return
	 */
	public abstract String encryptDBUrl(String url);
	/**
	 * 解密数据接口
	 * @param data
	 * @return
	 */
	public abstract String decryptDBUrl(String url);
	
	/**
	 * 加密数据接口
	 * @param data
	 * @return
	 */
	public abstract String encryptDBPassword(String password);
	/**
	 * 解密数据接口
	 * @param data
	 * @return
	 */
	public abstract String decryptDBPassword(String password);
	
	/**
	 * 加密数据接口
	 * @param data
	 * @return
	 */
	public abstract String encryptDBUser(String user);
	/**
	 * 解密数据接口
	 * @param data
	 * @return
	 */
	public abstract String decryptDBUser(String user);
	
	

}
