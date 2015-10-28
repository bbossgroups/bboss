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
package com.frameworkset.common;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;

public class Test20081205 {
	public static void main(String[] args)
	{
		String sql = "select * from td_sm_organization t where t.parent_id='0' order by t.org_sn asc";
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(sql);
			for(int i = 0; i < dbUtil.size(); i ++)
				System.out.println(dbUtil.getString(i, "ORG_ID"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
