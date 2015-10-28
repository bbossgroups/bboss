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
package com.frameworkset.commons.dbcp;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 
 * <p>Title: NativeDataSource.java</p>
 * 
 * <p>Description: 用户非连接池模式的数据源管理，当用户在连接池的配置文件指定
 * usepool=false时，系统创建NativeDataSource类型的数据源
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2011-3-4 下午02:06:01
 * @author biaoping.yin
 * @version 1.0
 */
public class NativeDataSource implements DataSource {
	private StaticCount staticCount;
	public static class StaticCount
	{
		private List<AbandonedTrace> traces = new ArrayList<AbandonedTrace>();
		private boolean logAbandoned = false;
		public StaticCount(boolean logAbandoned)
		{
			this.logAbandoned = logAbandoned;
		}
		
		/**
		 * 非链接池数据源活动链接数
		 */
		private volatile int activeConnections = 0;
		/**
		 * 非链接池数据源高峰链接数
		 */
		private volatile int heapConnections = 0;
		private Object lock = new Object();
		public void increments(AbandonedTrace trace)
		{
			synchronized(lock)
			{
				if(logAbandoned)
					addTraceObject(trace);
				activeConnections ++;
				if(heapConnections < activeConnections)
					heapConnections = activeConnections;
			}
		}
		
		public void decrements(AbandonedTrace trace)
		{
			synchronized(lock)
			{
				if(logAbandoned)
					removeTraceObject(trace);
				activeConnections --;
				
			}
		}

		protected int getActiveConnections() {
			return activeConnections;
		}

		protected int getHeapConnections() {
			return heapConnections;
		}
		
		private void addTraceObject(AbandonedTrace trace)
		{
			this.traces.add(trace);
		}
		
		private void removeTraceObject(AbandonedTrace trace)
		{
			this.traces.remove(trace);
		}
		
		
		public List<AbandonedTrace> getTraceObjects()
		{
			
			List dest = new ArrayList();
			try
			{
				if(traces != null)
					dest.addAll(traces);
			}
			catch(Exception e)
			{
				
			}
			return dest;
			
		}
	}
	private String driver;
	private String url;
	private String user;
	private String password;
	private boolean defaultAutoCommit;
	private boolean defaultReadOnly;
	private int defaultTransactionIsolation;
	private String defaultCatalog;
	private DriverConnectionFactory driverConnectionFactory;
	protected AbandonedConfig abandonedConfig = null;
	private boolean logAbandoned = false;
	
	/**
	 * The connection properties that will be sent to our JDBC driver when
	 * establishing new connections. <strong>NOTE</strong> - The "user" and
	 * "password" properties will be passed explicitly, so they do not need to
	 * be included here.
	 */
	protected Properties connectionProperties = new Properties();

	private PrintWriter logWriter;
	
	
	

	public Connection getConnection() throws SQLException {
		this.createDataSource();
//		Connection connection = this.driverConnectionFactory.createConnection();
		StaticDelegateConnection connection = new StaticDelegateConnection(this.driverConnectionFactory.createConnection(),abandonedConfig,this.staticCount);
		this.staticCount.increments(connection);
		return connection;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		throw new UnsupportedOperationException("Not supported by BasicDataSource");
		
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return this.logWriter;
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		this.logWriter = out;

	}

	public void setLoginTimeout(int seconds) throws SQLException {

	}

	private void log(String message) {
		if (logWriter != null) {
			logWriter.println(message);
		}
	}

	private boolean created = false;

	protected DataSource createDataSource() throws SQLException {
		if (created)
			return this;

		synchronized (this) {
			if (created)
				return this;
			// Load the JDBC driver class
			if (driver != null) {
				try {
					Class.forName(driver);
				} catch (Throwable t) {
					String message = "Cannot load JDBC driver class '" + driver
							+ "'";
					logWriter.println(message);
					t.printStackTrace(logWriter);
					throw new SQLNestedException(message, t);
				}
			}

			// Create a JDBC driver instance
			Driver driver = null;
			try {
				driver = DriverManager.getDriver(url);
			} catch (Throwable t) {
				String message = "Cannot create JDBC driver of class '"
						+ (driver != null ? driver : "")
						+ "' for connect URL '" + url + "'";
				logWriter.println(message);
				t.printStackTrace(logWriter);
				throw new SQLNestedException(message, t);
			}

			// Set up the driver connection factory we will use
			if (user != null) {
				connectionProperties.put("user", user);
			} else {
				log("DBCP DataSource configured without a 'username'");
			}

			if (password != null) {
				connectionProperties.put("password", password);
			} else {
				log("DBCP DataSource configured without a 'password'");
			}

			driverConnectionFactory = new DriverConnectionFactory(driver, url,
					connectionProperties);
			staticCount = new StaticCount(this.isLogAbandoned());
			this.created = true;
		}
		return this;
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the defaultAutoCommit
	 */
	public boolean isDefaultAutoCommit() {
		return defaultAutoCommit;
	}

	/**
	 * @param defaultAutoCommit
	 *            the defaultAutoCommit to set
	 */
	public void setDefaultAutoCommit(boolean defaultAutoCommit) {
		this.defaultAutoCommit = defaultAutoCommit;
	}

	/**
	 * @return the defaultReadOnly
	 */
	public boolean isDefaultReadOnly() {
		return defaultReadOnly;
	}

	/**
	 * @param defaultReadOnly
	 *            the defaultReadOnly to set
	 */
	public void setDefaultReadOnly(boolean defaultReadOnly) {
		this.defaultReadOnly = defaultReadOnly;
	}

	/**
	 * @return the defaultTransactionIsolation
	 */
	public int getDefaultTransactionIsolation() {
		return defaultTransactionIsolation;
	}

	/**
	 * @param defaultTransactionIsolation
	 *            the defaultTransactionIsolation to set
	 */
	public void setDefaultTransactionIsolation(int defaultTransactionIsolation) {
		this.defaultTransactionIsolation = defaultTransactionIsolation;
	}

	/**
	 * @return the defaultCatalog
	 */
	public String getDefaultCatalog() {
		return defaultCatalog;
	}

	/**
	 * @param defaultCatalog
	 *            the defaultCatalog to set
	 */
	public void setDefaultCatalog(String defaultCatalog) {
		this.defaultCatalog = defaultCatalog;
	}

	/**
	 * @return the logAbandoned
	 */
	public boolean isLogAbandoned() {
		if (abandonedConfig != null) {
            return abandonedConfig.getLogAbandoned();
        }
        return false;
	}

	/**
	 * @param logAbandoned the logAbandoned to set
	 */
	public void setLogAbandoned(boolean logAbandoned) {
		if (abandonedConfig == null) {
            abandonedConfig = new AbandonedConfig();
        }
		if(logAbandoned)
		{
			abandonedConfig.setRemoveAbandoned(true);
			 abandonedConfig.setLogAbandoned(logAbandoned);
		}
		this.logAbandoned = logAbandoned;
	}

	public void addConnectionProperty(String propertyName, String propertyValue) {
		 connectionProperties.put(propertyName, propertyValue);
		
	}
	
	
	public int getNumActive()
	{
		if(this.staticCount != null)
		{
			return this.staticCount.getActiveConnections();
		}
		return 0;
	}
	public int getMaxNumActive()
	{
		if(this.staticCount != null)
		{
			return this.staticCount.getHeapConnections();
		}
		return 0;
	}
	
	public List getTraces()
	{
		if(this.staticCount != null)
		{
			return this.staticCount.getTraceObjects();
		}
		return new ArrayList();
	}
	
	
	
	   

}
