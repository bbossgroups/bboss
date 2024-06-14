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

import com.frameworkset.common.poolman.util.DatasourceUtil;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.transaction.TXDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Hashtable;

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
public class ExtenerDataSource
        implements DataSource {
    private static final Logger log = LoggerFactory.getLogger(ExtenerDataSource.class);
 
    private DataSource datasource;


    public ExtenerDataSource(DataSource datasource) {
        this.datasource = datasource;
    }
    

 
    
 
    public DataSource getInnerDataSource()
    {
    	return datasource;
    }

 

    /*
      DATASOURCE METHODS
    */

    public Connection getConnection() throws SQLException {
     
    	return datasource.getConnection();
    }

    public Connection getConnection(String user, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return datasource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return datasource.getLoginTimeout();
    }


    public void close() {

		 
		
	}


	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return datasource.unwrap(iface);
	}


	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return datasource.isWrapperFor(iface);
	}

 

	@Override
	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return datasource.getParentLogger();
	}
}











