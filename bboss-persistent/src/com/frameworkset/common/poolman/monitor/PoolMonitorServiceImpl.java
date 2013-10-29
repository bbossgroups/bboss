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
package com.frameworkset.common.poolman.monitor;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.util.TransferObjectFactory;

public class PoolMonitorServiceImpl implements PoolMonitorService {

	public List<PoolmanStatic> getAllPools() {
		List<PoolmanStatic> all = new ArrayList<PoolmanStatic>();
		List<String> pools = DBUtil.getAllPoolNames();
		JDBCPoolMetaData jdbcPoolMetaData = null;
		for (int i = 0; pools != null && i < pools.size(); i++) {
			String dbname = pools.get(i);
			jdbcPoolMetaData = DBUtil.getJDBCPoolMetaData(dbname);

			PoolmanStatic ps = new PoolmanStatic();
			TransferObjectFactory.createTransferObject(jdbcPoolMetaData, ps);
			// ps.setMaxconnections(jdbcPoolMetaData.getMaximumSize());
			
			ps.setActiveconnections(DBUtil.getNumActive(dbname));
			ps.setHeapconnections(DBUtil.getMaxNumActive(dbname));
			if(!jdbcPoolMetaData.isUsepool())
				ps.setIdleconnections(DBUtil.getNumIdle(dbname));	
			// ps.setMinconnections(jdbcPoolMetaData.getMinimumSize());
			ps.setStartTime(DBUtil.getStartTime(dbname));
			ps.setStopTime(DBUtil.getStopTime(dbname));
			ps.setStatus(DBUtil.getStatus(dbname));

			// ps.setValidationQuery(jdbcPoolMetaData.getValidationQuery());
			// ps.setDriver(jdbcPoolMetaData.getDriver());
			// ps.setExternal(jdbcPoolMetaData.isExternal());
			// if(jdbcPoolMetaData.isExternal()){
			// ps.setExternaljndiName(jdbcPoolMetaData.getExternaljndiName());
			// }
			// ps.setJNDIName(jdbcPoolMetaData.getJNDIName());

			ps.setDbname(dbname);

			all.add(ps);
		}
		return all;
	}

	public PoolmanStatic getPoolmanStatic(String dbname) {
		PoolmanStatic ps = new PoolmanStatic();
		// ps.setMaxconnections(DBUtil.getJDBCPoolMetaData(dbname).getMaximumSize());
		TransferObjectFactory.createTransferObject(DBUtil
				.getJDBCPoolMetaData(dbname), ps);
		ps.setActiveconnections(DBUtil.getNumActive(dbname));
		ps.setHeapconnections(DBUtil.getMaxNumActive(dbname));
		if(!ps.isUsepool())
			ps.setIdleconnections(DBUtil.getNumIdle(dbname));
		// ps.setMinconnections(DBUtil.getJDBCPoolMetaData(dbname).getMinimumSize());
		ps.setStartTime(DBUtil.getStartTime(dbname));
		ps.setStopTime(DBUtil.getStopTime(dbname));
		ps.setStatus(DBUtil.getStatus(dbname));
		ps.setDbname(dbname);
		return ps;
	}

	public boolean startPool(String dbname) {
		try {
			DBUtil.startPool(dbname);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String statusCheckPool(String dbname) {
		try {
			// stop、start、unknown
			String status = DBUtil.statusCheck(dbname);
			if ("start".equals(status)) {
				return "START";
			} else if ("stop".equals(status)) {
				return "STOP";
			} else if ("unknown".equals(status)) {
				return "UNKNOWN";
			}
			return "FAILED";
		} catch (Exception e) {
			return "FAILED";
		}
	}

	public boolean stopPool(String dbname) {
		try {
			DBUtil.stopPool(dbname);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<AbandonedTraceExt> getTraceObjects(String dbname) {
		return PoolMonitorUtil.converAbandonedTrace(DBUtil.getTraceObjects(dbname));
	}

}
