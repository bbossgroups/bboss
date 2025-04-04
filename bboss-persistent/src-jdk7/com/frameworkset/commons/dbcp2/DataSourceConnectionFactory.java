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
package com.frameworkset.commons.dbcp2;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * A {@link DataSource}-based implementation of {@link ConnectionFactory}.
 *
 * @author Rodney Waldhoff
 * @version $Id: DataSourceConnectionFactory.java 1649430 2015-01-04 21:29:32Z tn $
 * @since 2.0
 */
public class DataSourceConnectionFactory implements ConnectionFactory {
    public DataSourceConnectionFactory(DataSource source) {
        this(source,null,null);
    }

    public DataSourceConnectionFactory(DataSource source, String uname, String passwd) {
        _source = source;
        _uname = uname;
        _passwd = passwd;
    }

    @Override
    public Connection createConnection() throws SQLException {
        if(null == _uname && null == _passwd) {
            return _source.getConnection();
        }
        return _source.getConnection(_uname,_passwd);
    }

    private final String _uname;
    private final String _passwd;
    private final DataSource _source;
}
