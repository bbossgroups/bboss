/*
 *  PoolMan Java Object Pooling and Caching Library
 *  Copyright (C) 1999-2001 The Code Studio
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  The full license is located at the root of this distribution
 *  in the LICENSE file.
 */
package com.frameworkset.common.poolman.sql;

// The JDK and Extensions

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.naming.spi.ObjectFactory;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.util.DatasourceUtil;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.transaction.TXDataSource;

/**
 * 
 * 
 * <p>Title: PoolManDataSource.java</p>
 *
 * <p>Description: </p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class PoolManDataSource
        implements DataSource, ConnectionPoolDataSource, Referenceable, ObjectFactory {
    private static final Logger log = Logger.getLogger(PoolManDataSource.class);
    private String poolName;
    private String jndiName;
    private PrintWriter logger;
    private int loginTimeout;
    private DataSource datasource;


    public PoolManDataSource() {
    }
    

    public PoolManDataSource(String poolName, String jndiName) {
        this();
        this.poolName = poolName;
        this.jndiName = jndiName;
    }
    
    public PoolManDataSource(DataSource datasource,String poolName, String jndiName) {
        this();
        this.poolName = poolName;
        this.jndiName = jndiName;
        this.datasource = datasource;
    }

    public String getPoolName() {
        return this.poolName;
    }
    
    
    public DataSource getInnerDataSource()
    {
    	DataSource datasource_ = datasource;
    	if(datasource_ == null)
    		return null;
    	while(true)
    	{
	    	if(datasource_ instanceof PoolManDataSource)
	    	{
	    		PoolManDataSource temp_ = (PoolManDataSource)datasource_;

	    		if(temp_.getInnerDataSource() == null)
	    		{
	    			return datasource_;
	    		}
	    		else
	    		{
	    			datasource_ = temp_.getInnerDataSource();
	    			if(datasource_ == temp_)
	    			{
	    				return datasource_;
	    			}

	    		}
	    	}
	    	else if(datasource_ instanceof TXDataSource)
	    	{
	    		TXDataSource temp_ = (TXDataSource)datasource_;

	    		if(temp_.getSRCDataSource() == null)
	    		{
	    			return datasource_;
	    		}
	    		else
	    		{
	    			datasource_ = temp_.getSRCDataSource();
	    			if(datasource_ == temp_)
	    			{
	    				return datasource_;
	    			}

	    		}
	    	}
	    	else
	    	{
	    		return datasource_;
	    	}
    	}
//    	return this.datasource;
    }

    public String getJNDIName() {
        return this.jndiName;
    }

    /*
      DATASOURCE METHODS
    */

    public Connection getConnection() throws SQLException {
    	if(this.datasource == null)
    		return SQLManager.getInstance().requestConnection(this.poolName);
    	else
    		return datasource.getConnection();
    }

    public Connection getConnection(String user, String password) throws SQLException {
        SQLManager.getInstance().checkCredentials(this.poolName, user, password);
        return getConnection();
    }

    /*
      CONNECTIONPOOL DATASOURCE METHODS
    */

    public javax.sql.PooledConnection getPooledConnection() throws SQLException {
        return (PooledConnection) getConnection();
    }

    public javax.sql.PooledConnection getPooledConnection(String user, String password) throws SQLException {
        return (PooledConnection) getConnection(user, password);
    }

    /*
      COMMON DATASOURCE METHODS
    */

    public void setLoginTimeout(int seconds) throws SQLException {
//    	if(this.datasource == null)
    		this.loginTimeout = seconds;
//    	else
//    		this.datasource.setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException {
//        return this.datasource == null?this.loginTimeout:this.datasource.getLoginTimeout();
    	return this.loginTimeout;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
//    	if(this.datasource == null)
    		this.logger = out;
//    	else
//    		this.datasource.setLogWriter(out);
    }

    public PrintWriter getLogWriter() throws SQLException {
//        return this.datasource == null?this.logger:this.datasource.getLogWriter();
    	 return this.logger;
    }

    /* OBJECT METHODS */

    public String toString() {
        return "PoolManDataSource:[JNDIName:" + jndiName + ",PoolName:" + poolName + "]";
    }

    /* JNDI REFERENCE METHODS */

    public Reference getReference() {
        Reference ref = new Reference("com.frameworkset.common.poolman.sql.PoolManDataSource", "com.frameworkset.common.poolman.sql.PoolManDataSource", null);
        ref.add(new StringRefAddr("dbname", this.poolName));
        ref.add(new StringRefAddr("jndiname", this.jndiName));
        return ref;
    }

//    public Object getObjectInstance(Object RefObj, Name Nm, Context Ctx, Hashtable Env)
//            throws Exception {
//
//        Object result = null;
//        Reference ref = (Reference) RefObj;
//        if (ref.getClassName().equals("com.frameworkset.common.poolman.sql.PoolManDataSource")) {
//            String poolname = (String) ref.get("dbname").getContent();
//            String jndiname = (String) ref.get("jndiname").getContent();
//            PoolManDataSource pds = new PoolManDataSource(poolname, jndiname);
//            result = pds;
//        }
//        return result;
//    }

    public Serializable getPrimaryKey(String tableName) throws SQLException
    {
        
//        Long key = new Long(PrimaryKeyCacheManager.getInstance()
//                             .getPrimaryKeyCache(poolName)
//                             .getIDTable(tableName.toLowerCase()).generateKey());
        return getPrimaryKey(tableName,null);
    }
    
    public Serializable getPrimaryKey(String tableName,Connection con) throws SQLException
    {
        
//        Long key = new Long(PrimaryKeyCacheManager.getInstance()
//                             .getPrimaryKeyCache(poolName)
//                             .getIDTable(tableName.toLowerCase()).generateKey());
        return PrimaryKeyCacheManager.getInstance()
		      .getPrimaryKeyCache(poolName)
		      .getIDTable(tableName.toLowerCase()).generateObjectKey(con).getPrimaryKey();
    }


	public void close() {
//		if(datasource != null && this.datasource instanceof BasicDataSource)
//		{
//			log.debug("Close  datasource[jndiName="+this.jndiName+",dbname=" + this.poolName + "] begin.");
////			System.out.println("Close  datasource[jndiName="+this.jndiName+",dbname=" + this.poolName + "] begin.");
//			try {
//				((BasicDataSource)datasource).close();
//				log.debug("Close  datasource[jndiName="+this.jndiName+",dbname=" + this.poolName + "] end.");
////				System.out.println("Close  datasource[jndiName="+this.jndiName+",dbname=" + this.poolName + "] end.");
//			} catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
		DatasourceUtil.closeDS(datasource);
		
	}


	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	public Object getObjectInstance(Object RefObj, Name name, Context nameCtx,
			Hashtable<?, ?> environment) throws Exception {
		 Object result = null;
	        Reference ref = (Reference) RefObj;
	        if (ref.getClassName().equals("com.frameworkset.common.poolman.sql.PoolManDataSource")) {
	            String poolname = (String) ref.get("dbname").getContent();
	            String jndiname = (String) ref.get("jndiname").getContent();
	            PoolManDataSource pds = new PoolManDataSource(poolname, jndiname);
	            result = pds;
	        }
	        return result;
	}


	@Override
	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return datasource.getParentLogger();
	}
}











