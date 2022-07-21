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
package com.frameworkset.common.poolman.util;

import java.io.Serializable;

/**
 * 通过模板启动数据源配置参数
 * <p>Title: TempConf.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2015年10月8日 下午3:41:22
 * @author biaoping.yin
 * @version 1.0
 */
public class DBConf implements Serializable {
	private String poolname;
	private String driver;
	private String dbtype;
	private boolean enablejta;
	/**
	 * https://doc.bbossgroups.com/#/persistent/encrypt
	 * 同时如果想对账号、口令、url之间的任意两个组合加密的话，用户可以自己继承 com.frameworkset.common.poolman.security.BaseDBInfoEncrypt类，参考默认插件，实现相应的信息加密方法并配置到aop.properties中即可。
	 */
	private String dbInfoEncryptClass;
	private String dbAdaptor;
	private String jdbcurl;
	private String username;
	private String password;
	private String readOnly;
	private String txIsolationLevel;
	private String validationQuery;
	private String jndiName;   
	private int initialConnections;
	private int minimumSize;
	private int maximumSize;
	private boolean usepool;
	private boolean  external;

	public boolean isColumnLableUpperCase() {
		return columnLableUpperCase;
	}

	public void setColumnLableUpperCase(boolean columnLableUpperCase) {
		this.columnLableUpperCase = columnLableUpperCase;
	}

	private boolean columnLableUpperCase = true;
	private String externaljndiName ;
	private boolean showsql ;
	private boolean encryptdbinfo  ;
	private Integer queryfetchsize;
	public DBConf() {
		// TODO Auto-generated constructor stub
	}
	public String getPoolname() {
		return poolname;
	}
	public void setPoolname(String poolname) {
		this.poolname = poolname;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getJdbcurl() {
		return jdbcurl;
	}
	public void setJdbcurl(String jdbcurl) {
		this.jdbcurl = jdbcurl;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public String getTxIsolationLevel() {
		return txIsolationLevel;
	}
	public void setTxIsolationLevel(String txIsolationLevel) {
		this.txIsolationLevel = txIsolationLevel;
	}
	public String getValidationQuery() {
		return validationQuery;
	}
	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}
	public String getJndiName() {
		return jndiName;
	}
	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}
	public int getInitialConnections() {
		return initialConnections;
	}
	public void setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
	}
	public int getMinimumSize() {
		return minimumSize;
	}
	public void setMinimumSize(int minimumSize) {
		this.minimumSize = minimumSize;
	}
	public int getMaximumSize() {
		return maximumSize;
	}
	public void setMaximumSize(int maximumSize) {
		this.maximumSize = maximumSize;
	}
	public boolean isUsepool() {
		return usepool;
	}
	public void setUsepool(boolean usepool) {
		this.usepool = usepool;
	}
	public boolean isExternal() {
		return external;
	}
	public void setExternal(boolean external) {
		this.external = external;
	}
	public String getExternaljndiName() {
		return externaljndiName;
	}
	public void setExternaljndiName(String externaljndiName) {
		this.externaljndiName = externaljndiName;
	}
	public boolean isShowsql() {
		return showsql;
	}
	public void setShowsql(boolean showsql) {
		this.showsql = showsql;
	}
	public boolean isEncryptdbinfo() {
		return encryptdbinfo;
	}
	public void setEncryptdbinfo(boolean encryptdbinfo) {
		this.encryptdbinfo = encryptdbinfo;
	}
	public Integer getQueryfetchsize() {
		return queryfetchsize;
	}
	public void setQueryfetchsize(Integer queryfetchsize) {
		this.queryfetchsize = queryfetchsize;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getDbAdaptor() {
		return dbAdaptor;
	}

	public void setDbAdaptor(String dbAdaptor) {
		this.dbAdaptor = dbAdaptor;
	}

	public String getDbInfoEncryptClass() {
		return dbInfoEncryptClass;
	}

	/**
	 * https://doc.bbossgroups.com/#/persistent/encrypt
	 * 同时如果想对账号、口令、url之间的任意两个组合加密的话，用户可以自己继承 com.frameworkset.common.poolman.security.BaseDBInfoEncrypt类，参考默认插件，实现相应的信息加密方法并配置到aop.properties中即可。
	 */
	public void setDbInfoEncryptClass(String dbInfoEncryptClass) {
		this.dbInfoEncryptClass = dbInfoEncryptClass;
	}

	public boolean isEnablejta() {
		return enablejta;
	}

	public void setEnablejta(boolean enablejta) {
		this.enablejta = enablejta;
	}
}
