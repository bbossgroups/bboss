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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DBHive extends DBOracle{
	public boolean isWritebable(java.sql.ResultSetMetaData other,int idx) throws SQLException
    {
    	return false;
    }
	
	public boolean isSearchable(ResultSetMetaData other, int i) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isSigned(ResultSetMetaData other, int rc) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isDefinitelyWritable(ResultSetMetaData other, int rc) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
