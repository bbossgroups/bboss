package bboss.org.apache.velocity.test.sql;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class TestDataSource implements DataSource
{

    private final String url;
    private final String user;
    private final String password;

    private PrintWriter logWriter = null;

    private int loginTimeout = 0;

    public TestDataSource(final String driverClass, final String url, final String user, final String password) throws Exception
    {
        this.url = url;
        this.user = user;
        this.password = password;
        Class.forName(driverClass);
    }

    @Override
    public Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Connection getConnection(final String username, final String password)
        throws SQLException
    {
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException
    {
        return logWriter;
    }

    @Override
    public int getLoginTimeout() throws SQLException
    {
        return loginTimeout;
    }

    @Override
    public void setLogWriter(final PrintWriter logWriter) throws SQLException
    {
        this.logWriter = logWriter;
    }

    @Override
    public void setLoginTimeout(final int loginTimeout) throws SQLException
    {
        this.loginTimeout = loginTimeout;
    }

    @Override
    public boolean isWrapperFor(final Class iface) throws SQLException
    {
        return false;
    }

    @Override
    public Object unwrap(final Class iface) throws SQLException
    {
        throw new SQLException("Not implemented");
    }

    /* added to be able to compile with jdk 1.7+ */
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException
    {
        throw new SQLFeatureNotSupportedException();
    }

}
