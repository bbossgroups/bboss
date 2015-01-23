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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;
import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * <p>
 * Title: UserService.java
 * </p>
 * <p>
 * Description:
 * 
 * CREATE
    TABLE TB_USER
    (
        userId BIGINT NOT NULL,
        userName VARCHAR(100),
        userPassword VARCHAR(100),
       
        PRIMARY KEY (userId)
    )
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2011-1-5
 * @author biaoping.yin
 * @version 1.0
 */
public class UserService {
	
	public void updateUser(User user) throws UserManagerException {
		try {
			String sql = "update tb_user set userId=#[userId],userName=#[userName],userPassword=#[userPassword]" +
					" where userId=#[userId]";
			SQLParams params = new SQLParams();
			params.addSQLParam("userId", user.getUserId(), SQLParams.INT);
			params
					.addSQLParam("userName", user.getUserName(),
							SQLParams.STRING);
			params.addSQLParam("userPassword", user.getUserPassword(),
					SQLParams.STRING);
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedUpdate(params, sql);
			db.executePrepared();
			
		} catch (Exception e) {
			throw new UserManagerException("用户更新失败",e);
		}
		
	}

	public User getUser(int userId) throws UserManagerException {
		try {
			String sql = "select * from tb_user where userId=#[userId]";
			SQLParams params = new SQLParams();
			params.addSQLParam("userId", userId, SQLParams.INT);
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(params, sql);
			return (User)db.executePreparedForObject(User.class);
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	
	public List<User> getUsers() throws UserManagerException {
		try {
			String sql = "select userid from tb_user ";
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect( sql);
			return (List<User>)db.executePreparedForList(User.class);
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	public List<User> getUsersNullRowHandler() throws UserManagerException {
		try {
			String sql = "select userid from tb_user ";
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect( sql);
			final List<User> users=new ArrayList<User>();
			db.executePreparedWithRowHandler(new NullRowHandler() {
				@Override
				public void handleRow(Record record) throws Exception {
					User user = new User();
					user.setUserId(record.getInt("userId"));
					
					users.add(user);
					
				}
			});
			return users;
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	
	public ListInfo getUsersNullRowHandler(String userName,long offset,int pagesize) throws UserManagerException {
		try {
			String sql = "select * from tb_user where 1=1 " +
					"#if($userName && !$userName.equals(\"\")) and userName like #[userName] #end";

			PreparedDBUtil db = new PreparedDBUtil();
			SQLParams params = new SQLParams();
			if(userName != null && !userName.equals(""))
				userName = "%" + userName + "%";
			params.addSQLParam("userName", userName, SQLParams.STRING);
			db.preparedSelect(params,  sql, offset, pagesize);
			final List<User> users=new ArrayList<User>();
			db.executePreparedWithRowHandler( new NullRowHandler() {
				public void handleRow(Record record) throws Exception {
					User user = new User();
					user.setUserId(record.getInt("userId"));
					user.setUserName(record.getString("userName"));
					user.setUserPassword(record.getString("userPassword"));
					users.add(user);
					
				}
			});
			ListInfo listinfo = new ListInfo(); 
			long totalsize = db.getTotalSize();//总记录数
			listinfo.setDatas(users);
			listinfo.setTotalSize(totalsize);
			return listinfo;
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	
	public List<User> getUsersRowHandler() throws UserManagerException {
		TransactionManager tm = new TransactionManager();
		try {
			String sql = "select userid from tb_user ";
			tm.begin();
			
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect( sql);
			List<User> users = db.executePreparedForList(User.class,new RowHandler<User>() {

				@Override
				public void handleRow(User user, Record record) throws Exception {
					user.setUserId(record.getInt("userid"));
					PreparedDBUtil db = new PreparedDBUtil();
					db.preparedSelect( "");
				}

				
			});
			tm.commit();
			return users;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	public void deleteUser(int userId) throws UserManagerException {
		try {
			String sql = "delete from tb_user where userId=#[userId]";
			SQLParams params = new SQLParams();
			params.addSQLParam("userId", userId, SQLParams.INT);
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedDelete(params, sql);
			db.executePrepared();
		} catch (Exception e) {
			throw new UserManagerException("用户查找失败：用户不存在",e);
		}
		
	}
	
	public User addUser(User user) throws UserManagerException {
		try {
			int userId = SQLExecutor.queryObject(int.class, "select max(userId)+1 from tb_user");//获取用户的主键信息
			String sql = "insert into tb_user(userId,userName,userPassword) " +
					"values(#[userId],#[userName],#[userPassword])";
			SQLParams params = new SQLParams();
			params.addSQLParam("userId", userId, SQLParams.INT);
			params
					.addSQLParam("userName", user.getUserName(),
							SQLParams.STRING);
			params.addSQLParam("userPassword", user.getUserPassword(),
					SQLParams.STRING);
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedInsert(params, sql);
			db.executePrepared();
			user.setUserId(userId);
			return user;
			
		} catch (Exception e) {
			throw new UserManagerException("添加用户失败",e);
		}
		
	}

	public void deleteAllUser() throws SQLException
	{

		SQLExecutor.delete("delete from tb_user");
		
	}
}