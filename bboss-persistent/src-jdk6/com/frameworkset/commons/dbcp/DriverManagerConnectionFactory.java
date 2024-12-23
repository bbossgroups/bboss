/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frameworkset.commons.dbcp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A {@link DriverManager}-based implementation of {@link ConnectionFactory}.
 *
 * @author Rodney Waldhoff
 * @author Ignacio J. Ortega
 * @author Dirk Verbeeck
 * @version $Revision: 746827 $ $Date: 2009-02-22 16:41:52 -0500 (Sun, 22 Feb 2009) $
 */
public class DriverManagerConnectionFactory implements ConnectionFactory {

    static {
        // Related to DBCP-212
        // Driver manager does not sync loading of drivers that use the service
        // provider interface. This will cause issues is multi-threaded
        // environments. This hack makes sure the drivers are loaded before
        // DBCP tries to use them.
        DriverManager.getDrivers();
    }


    /**
     * Constructor for DriverManagerConnectionFactory.
     * @param connectUri a database url of the form 
     * <code> jdbc:<em>subprotocol</em>:<em>subname</em></code>
     * @param props a list of arbitrary string tag/value pairs as
     * connection arguments; normally at least a "user" and "password" 
     * property should be included.
     */
    public DriverManagerConnectionFactory(String connectUri, Properties props) {
        _connectUri = connectUri;
        _props = props;
    }

    /**
     * Constructor for DriverManagerConnectionFactory.
     * @param connectUri a database url of the form 
     * <code>jdbc:<em>subprotocol</em>:<em>subname</em></code>
     * @param uname the database user
     * @param passwd the user's password
     */
    public DriverManagerConnectionFactory(String connectUri, String uname, String passwd) {
        _connectUri = connectUri;
        _uname = uname;
        _passwd = passwd;
    }

    public Connection createConnection() throws SQLException {
        if(null == _props) {
            if((_uname == null) && (_passwd == null)) {
                return DriverManager.getConnection(_connectUri);
            } else {
                return DriverManager.getConnection(_connectUri,_uname,_passwd);
            }
        } else {
            return DriverManager.getConnection(_connectUri,_props);
        }
    }

    protected String _connectUri = null;
    protected String _uname = null;
    protected String _passwd = null;
    protected Properties _props = null;
}
