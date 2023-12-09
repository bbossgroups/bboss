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
package com.frameworkset.orm.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSQLiteXerial extends DBMM{

	public DBSQLiteXerial() {
		// TODO Auto-generated constructor stub
	}

    public Statement createStatement(Connection con, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        //SQLite only supports TYPE_FORWARD_ONLY cursors
        Statement stmt = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                resultSetConcurrency);

        return stmt;
    }

}
