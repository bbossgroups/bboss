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

package org.frameworkset.spi.security;

/**
 * <p>Title: SecurityManager.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-5 下午04:32:22
 * @author biaoping.yin
 * @version 1.0
 */
public interface SecurityManager
{
	
	public static final String USER_ACCOUNT_KEY="user";
	public static final String USER_PASSWORD_KEY="password";
    public boolean checkUser(SecurityContext context) throws Exception;
    /**
     * 加密方法
     * @param value
     * @return
     */
    public byte[] encode(byte[] value)  throws Exception;
    /**
     * 解密方法
     * @param value
     * @return
     */
    public byte[] decode(byte[] value)  throws Exception;
    
    public boolean checkPermission(SecurityContext context)  throws Exception;
    
    public boolean enableEncrypt();
    public boolean enableAuthenticate();
    public boolean enableAuthority();
}
