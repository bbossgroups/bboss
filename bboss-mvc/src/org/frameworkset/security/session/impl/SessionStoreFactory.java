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
	public static SessionStore getTokenStore(String sessionStore)
	{
		if(StringUtil.isEmpty(sessionStore)||sessionStore.equals("session"))
		{
			return new HttpSessionStore();
		}
		else
		{
			
			try {
				SessionStore sessionStore_ = (SessionStore)Class.forName(sessionStore).newInstance();
				return sessionStore_;
			} catch (Exception e) {
				return new HttpSessionStore();
			}
		}
			
	}
}
