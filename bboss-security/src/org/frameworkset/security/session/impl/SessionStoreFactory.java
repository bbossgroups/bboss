/*
 *  Copyright 2008 bbossgroups
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
package org.frameworkset.security.session.impl;

import org.frameworkset.security.session.SessionStore;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: SessionStoreFactory.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月15日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class SessionStoreFactory {
	public static SessionStore getSessionStore(Object sessionStore,SessionManager sessionManager)
	{
		if(sessionStore == null)
			return null;
		
		else
		{
			if(sessionStore instanceof String)
			{
				String str = (String)sessionStore;
				if(StringUtil.isEmpty(str)||str.equals("session"))
				{
					return null;
				}
				try {
					SessionStore sessionStore_ = (SessionStore)Class.forName(str.trim()).newInstance();
					sessionStore_.setSessionManager(sessionManager);
					return new DelegateSessionStore(sessionStore_);
				} catch (Exception e) {
					throw new SessionManagerException("获取sessionstore失败："+sessionStore,e);
				}
			}
			else
			{
				SessionStore sessionStore_ = (SessionStore)sessionStore;
				sessionStore_.setSessionManager(sessionManager);
				return new DelegateSessionStore(sessionStore_);
			}
			
		}
			
	}
}
