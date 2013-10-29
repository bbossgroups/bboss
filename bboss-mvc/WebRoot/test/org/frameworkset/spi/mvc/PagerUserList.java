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
package org.frameworkset.spi.mvc;

import java.sql.SQLException;
import java.util.List;

import test.pager.TableInfo;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: PagerUserList.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-18
 * @author biaoping.yin
 * @version 1.0
 */
public class PagerUserList extends DataInfoImpl {

	
	protected ListInfo getDataList(String sortKey, boolean desc) {
		
		String sql = "select * from tableinfo";
		String tablename = request.getParameter("TABLE_NAME");
		boolean usecondition = tablename != null && !tablename.equals("");
		if(usecondition)
			sql += " where TABLE_NAME like %?%'";
		
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql);
			if(usecondition)
				db.setString(1, tablename);
			List tables = db.executePreparedForList(TableInfo.class);
			ListInfo datas = new ListInfo();
			datas.setTotalSize(db.getTotalSize());//设置总记录数
			datas.setDatas(tables);//设置当页数据
			return datas;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		String sql = "select * from tableinfo";
		String tablename = request.getParameter("TABLE_NAME");
		boolean usecondition = tablename != null && !tablename.equals("");
		if(usecondition)
			sql += " where TABLE_NAME like %?%'";
		
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect(sql,(int)offSet,pageItemsize);
			if(usecondition)
				db.setString(1, tablename);
			List<TableInfo> tables = db.executePreparedForList(TableInfo.class);
			ListInfo datas = new ListInfo();
			datas.setTotalSize(db.getTotalSize());//设置总记录数
			datas.setDatas(tables);//设置当页数据
			return datas;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
