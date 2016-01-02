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

public abstract class DB2Base extends DB {

public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared,String orderby) {
		
//		return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
		StringBuilder newsql = new StringBuilder();
		if(prepared)
		{
			newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") row_number_ FROM (").append(sql)
			.append(") res) t where t.row_number_ <= ? and t.row_number_ >= ?");
			 
			
			/**
			 * StringBuilder ret = null;
    	if(prepared)
    		ret = new StringBuilder().append("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                ") tt1 where rownum <= ?) ss1 where ss1.rowno_ >= ?");
    	else
    		ret = new StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                  ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
                  (offset + 1));
        return new PagineSql(ret.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
			 */
			return new PagineSql(newsql.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared).setRebuilded(true);
		}
		else
		{
			newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") row_number_ FROM (").append(sql)
			.append(") res) t where t.row_number_ <= ").append(offset + maxsize).append(" and t.row_number_ >= ").append(offset + 1).append("");
			return new PagineSql(newsql.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared).setRebuilded(true);
		}
		 
	}
	
	  public String getStringPagineSql(String sql,String orderby)
	  {
		  StringBuilder newsql = new StringBuilder();
		  newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") row_number_ FROM (").append(sql)
			.append(") res) t where t.row_number_ <= ? and t.row_number_ >= ?");
			return newsql.toString();
	  }
	  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns,String orderby)
	    {
		  
		  StringBuilder newsql = new StringBuilder();
		  newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") row_number_ FROM (").append("SELECT ");
		 	if(columns != null && ! columns.equals(""))
		 	{
		 		newsql.append( columns);
		 	}
		 	else
		 		newsql.append("* ");
		 	newsql.append(" from   ");
		 	if(schema != null && !schema.equals(""))
		 		newsql.append(schema).append(".");
		 	newsql.append( tablename)
			.append(") res) t where t.row_number_ <= ? and t.row_number_ >= ?");
			return newsql.toString();
	    	
	    } 

}
