/**
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
package com.frameworkset.common.poolman.hibernate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.util.JDBCExceptionReporter;

import com.frameworkset.common.poolman.util.SQLManager;

/**
 * <p> SimpleDatasourceConnectionProvider.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-10 ÉÏÎç8:27:39
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleDatasourceConnectionProvider  implements ConnectionProvider {

	private DataSource dataSource;

	private DataSource dataSourceToUse;
	private String dbname;


	public void configure(Properties props) throws HibernateException {
		this.dataSource = SQLManager.getDatasourceByDBName(dbname);
		// absolutely needs thread-bound DataSource to initialize
		if (this.dataSource == null) {
			throw new HibernateException("No local DataSource found for configuration - " +
			    "'dataSource' property must be set on LocalSessionFactoryBean");
		}
		this.dataSourceToUse = getDataSourceToUse(this.dataSource);
	}

	/**
	 * Return the DataSource to use for retrieving Connections.
	 * <p>This implementation returns the passed-in DataSource as-is.
	 * @param originalDataSource the DataSource as configured by the user
	 * on LocalSessionFactoryBean
	 * @return the DataSource to actually retrieve Connections from
	 * (potentially wrapped)
	 * @see LocalSessionFactoryBean#setDataSource
	 */
	protected DataSource getDataSourceToUse(DataSource originalDataSource) {
		return originalDataSource;
	}

	/**
	 * Return the DataSource that this ConnectionProvider wraps.
	 */
	public DataSource getDataSource() {
		return this.dataSource;
	}

	/**
	 * This implementation delegates to the underlying DataSource.
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		try {
			return this.dataSourceToUse.getConnection();
		}
		catch (SQLException ex) {
			JDBCExceptionReporter.logExceptions(ex);
			throw ex;
		}
	}

	/**
	 * This implementation simply calls <code>Connection.close</code>.
	 * @see java.sql.Connection#close()
	 */
	public void closeConnection(Connection con) throws SQLException {
		try {
			con.close();
		}
		catch (SQLException ex) {
			JDBCExceptionReporter.logExceptions(ex);
			throw ex;
		}
	}

	/**
	 * This implementation does nothing:
	 * We're dealing with an externally managed DataSource.
	 */
	public void close() {
	}

	/**
	 * This implementation returns <code>false</code>: We cannot guarantee
	 * to receive the same Connection within a transaction, not even when
	 * dealing with a JNDI DataSource.
	 */
	public boolean supportsAggressiveRelease() {
		return false;
	}

}
