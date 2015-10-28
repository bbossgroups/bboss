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
package com.frameworkset.orm.adaptors;

import com.frameworkset.orm.adapter.DBOracle;

public class MyOracle extends DBOracle {

    public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared)
    {
        // StringBuffer ret = new
        // StringBuffer("select ss1.* from (select tt1.*,rownum rowno_ from (")
        // .append(sql)
        // .append(") tt1) ss1 where ss1.rowno_ between ")
        // .append((offset + 1) + "")
        // .append(" and ")
        // .append((offset + maxsize) + "");
//        StringBuffer ret = new StringBuffer("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
//                ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
//                (offset + 1));
//        return ret.toString();
    	StringBuffer ret = null;
    	if(prepared)
    		ret = new StringBuffer().append("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                ") tt1 where rownum <= ?) ss1 where ss1.rowno_ >= ?");
    	else
    		ret = new StringBuffer("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                  ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
                  (offset + 1));
        return new PagineSql(ret.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
    }
	
	

}
