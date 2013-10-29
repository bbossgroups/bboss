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
package org.frameworkset.web.demo;

import org.frameworkset.spi.ApplicationContext;
import org.junit.Test;

/**
 * <p>Title: TestUserService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-1-10
 * @author biaoping.yin
 * @version 1.0
 */
public class TestUserService {
	
	private UserService userService;
	@org.junit.Before
	public void initUserService()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/web" +
				"/demo/bboss-demo.xml");
		userService = (UserService)context.getBeanObject("userService");
	}
	@Test
	public void updateUser() {
		try {
			
			User user = new User();
			user.setUserId(1);
			user.setUserName("lisi");
			user.setUserPassword("wwwwwww");
			userService.updateUser(user);			
		} catch (UserManagerException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void getUser()  {
		try {
			int userId = 1;
			
			getUser( userId);
			
		} catch (UserManagerException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getUser(int userId) throws UserManagerException  {
		try {			
			User user = userService.getUser(userId);
			if(user != null)
			{
				System.out.println("UserId:"+user.getUserId());
				System.out.println("UserName:"+user.getUserName());
				System.out.println("UserPassword:"+user.getUserPassword());
			}
			
		} catch (UserManagerException e) {
			throw e;
		}
		
	}
	@Test
	public void deleteUser() throws UserManagerException {
		try {
			int userId = 1;
			
			userService.deleteUser(userId);
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	@Test
	public void addUser()  {
		try {
			User user = new User();
			
			user.setUserName("lisiaaa");
			user.setUserPassword("123456");
			
			User newuser = userService.addUser(user);	
			getUser(newuser.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
