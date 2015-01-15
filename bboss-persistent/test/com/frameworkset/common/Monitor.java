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
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.common.poolman.monitor.PoolmanStatic;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.commons.dbcp2.PoolableConnection;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.TransferObjectFactory;


public class Monitor
{
    @Test
    public void stopPool()
    {
        try {
			DBUtil.stopPool(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
    public void startPool()
    {
        try {
        	DBUtil.getJDBCPoolMetaData(null);
			DBUtil.startPool(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testExternalMeta()
    {
        try {
        	DBUtil.startPool(null);
        	JDBCPoolMetaData meta = DBUtil.getJDBCPoolMetaData("mq");
        	JDBCPool pool = DBUtil.getPool("mq");
        	PoolmanStatic sss = new PoolmanStatic();
        	TransferObjectFactory.createTransferObject(meta, sss);
        	System.out.println(pool.getStartTime());
        	System.out.println(pool.getStopTime());
        	System.out.println(pool.getStatus());
        	sss.isExternal();
        	DBUtil.stopPool("bspf");
        	System.out.println(pool.getStartTime());
        	System.out.println(pool.getStopTime());
        	System.out.println(pool.getStatus());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Test
    public void testMeta()
    {
        try {
        	DBUtil.startPool(null);
        	JDBCPoolMetaData meta = DBUtil.getJDBCPoolMetaData("bspf");
        	JDBCPool pool = DBUtil.getPool("bspf");
        	PoolmanStatic sss = new PoolmanStatic();
        	TransferObjectFactory.createTransferObject(meta, sss);
        	System.out.println(pool.getStartTime());
        	System.out.println(pool.getStopTime());
        	System.out.println(pool.getStatus());
        	sss.isExternal();
        	DBUtil.stopPool("bspf");
        	PreparedDBUtil db = new PreparedDBUtil();
    		db.preparedSelect("Select 1 from dual");
    		db.executePrepared();
        	System.out.println(pool.getStartTime());
        	System.out.println(pool.getStopTime());
        	System.out.println(pool.getStatus());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
     
    
    @Test
    public void startstopExterPool()
    {
        try {
        	
//			DBUtil.stopPool("mq");
			System.out.println("DBUtil.statusCheck(\"mq\"):"+DBUtil.statusCheck("mq"));
			System.out.println("DBUtil.statusCheck(\"bspf\"):"+DBUtil.statusCheck("bspf"));
			System.out.println("DBUtil.statusCheck(\"kettle\"):"+DBUtil.statusCheck("kettle"));
			DBUtil dbutil= new DBUtil();
			try {
				
				dbutil.executeSelect("mq", "Select 1 from dual");
				System.out.println("dbutil.size():" + dbutil.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			DBUtil.startPool("mq");
			dbutil = new DBUtil();
			dbutil.executeSelect("mq","Select 1 from dual");
    		System.out.println("dbutil.size():" + dbutil.size());
			System.out.println("DBUtil.statusCheck(\"mq\"):"+DBUtil.statusCheck("mq"));
			System.out.println("DBUtil.statusCheck(\"bspf\"):"+DBUtil.statusCheck("bspf"));
			System.out.println("DBUtil.statusCheck(\"kettle\"):"+DBUtil.statusCheck("kettle"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
	public void testStartPool()
	{
    	TransactionManager tm = new TransactionManager();
    	try
    	{
    		DBUtil dbutil = new DBUtil();
    		String name = "db", driver="oracle.jdbc.driver.OracleDriver", jdbcurl="jdbc:oracle:thin:@//172.16.25.219:1521/orcl", username="baseline", password="baseline", readOnly="true", validationQuery="select 1 from dual";
    		DBUtil.startPool("db", driver, jdbcurl, username, password, readOnly, validationQuery);
    		tm.begin();
    		
    		dbutil.executeSelect("db","Select 1 from dual");
    		tm.commit();
    		System.out.println("dbutil.size():" + dbutil.size());
    		DBUtil.stopPool("db");
    		PreparedDBUtil db = new PreparedDBUtil();
    		db.preparedSelect("db","Select 1 from dual");
    		db.executePrepared();
    		
//    		dbutil.executeSelect("db","Select 1 from dual");
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
    @Test
	public void testEncryptStartPool()
	{
    	TransactionManager tm = new TransactionManager();
    	try
    	{
    		DBUtil dbutil = new DBUtil();
    		String name = "db", driver="oracle.jdbc.driver.OracleDriver", jdbcurl="jdbc:oracle:thin:@//10.0.15.134:1521/orcl", username="sanygcmp", password="sanygcmp", readOnly="true", validationQuery="select 1 from dual";
    		com.frameworkset.common.poolman.security.DESCipher aa = new com.frameworkset.common.poolman.security.DESCipher();
    		password = aa.encrypt(password);
    		DBUtil.startPool("db", driver, jdbcurl, username, password, readOnly, validationQuery,true);
    		tm.begin();
    		
    		dbutil.executeSelect("db","Select 1 from dual");
    		tm.commit();
    		System.out.println("dbutil.size():" + dbutil.size());
    		DBUtil.stopPool("db");
    		PreparedDBUtil db = new PreparedDBUtil();
    		db.preparedSelect("db","Select 1 from dual");
    		db.executePrepared();
    		
//    		dbutil.executeSelect("db","Select 1 from dual");
    		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
	}
    @Test
    public void testDBStatusStatic() throws SQLException
    {
    	int numactive = DBUtil.getNumActive("mysql");
    	int numIdle = DBUtil.getNumIdle("mysql");
    	DBUtil.getConection("mysql");
    	List<AbandonedTraceExt> objects = (List<AbandonedTraceExt>)DBUtil.getTraceObjects("mysql");
    	for(int i = 0;  objects != null && i < objects.size(); i ++)
    	{
    		AbandonedTraceExt con = objects.get(i);
    		 
    		System.out.println(con.getDburl());
    	}
    	System.out.println();
//    	int numactive = DBUtil.getNumActive("bspf");
    	
    }
    @Test
    public void testStartPoolFromConf()
    {
    	String configfile = "custom_poolman.xml";
    	SQLUtil.startPoolFromConf(configfile);
    	DBUtil dbutil = new DBUtil();
    	try {
			dbutil.executeSelect("bspf_custom_1","Select * from tableinfo");
			System.out.println("bspf_custom_1:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbutil.executeSelect("bspf_custom","Select * from tableinfo");
			System.out.println("bspf_custom:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
    public void startPoolFromConf()
	{
    	String configfile = "custom_poolman.xml";
    	String dbnamespace = "test";
    	SQLUtil.startPoolFromConf(configfile,dbnamespace);
    	DBUtil dbutil = new DBUtil();
    	try {
			dbutil.executeSelect("test:bspf_custom_1","Select * from tableinfo");
			System.out.println("test:bspf_custom_1:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbutil.executeSelect("test:bspf_custom","Select * from tableinfo");
			System.out.println("test:bspf_custom:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbutil.stopPool("test:bspf_custom_1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			dbutil.stopPool("test:bspf_custom");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    @Test
	public void startPoolFromConfWithAllParams()
	{
    	String configfile = "custom_poolman.xml";
    	String dbnamespace = "test";
    	String[] dbnames = new String[]{"bspf_custom_1"};
    	SQLUtil.startPoolFromConf(configfile,dbnamespace,dbnames);
    	DBUtil dbutil = new DBUtil();
    	try {
			dbutil.executeSelect("test:bspf_custom_1","Select * from tableinfo");
			System.out.println("test:bspf_custom_1:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbutil.executeSelect("test:bspf_custom","Select * from tableinfo");
			System.out.println("test:bspf_custom:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void startPoolFromConfNulldbnamespace() {
		String configfile = "custom_poolman.xml";
    	String dbnamespace = null;
    	String[] dbnames = new String[]{"bspf_custom_1"};
    	SQLUtil.startPoolFromConf(configfile,dbnamespace,dbnames);
    	DBUtil dbutil = new DBUtil();
    	try {
			dbutil.executeSelect("bspf_custom_1","Select * from tableinfo");
			System.out.println("bspf_custom_1:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbutil.executeSelect("bspf_custom","Select * from tableinfo");
			System.out.println("bspf_custom:"+dbutil.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStartPoolFromTempate() throws SQLException
	{
		DBUtil.getConection();
		String poolname = "bspf-custom";
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		String jdbcurl = "jdbc:derby:D:/workspace/bbossgroups-3.1/bboss-mvc/database/cimdb";
		String username = ""; 
		String password = "";
		String readOnly = "false";
		String txIsolationLevel = "READ_COMMITTED";
		String validationQuery = "select 1 from dual";
		String jndiName = "jdbc/derby-ds-custom";
		int initialConnections = 2;
		int minimumSize = 0;
		int maximumSize = 10;
		boolean usepool = true;
		boolean  external = true;
		String externaljndiName = "jdbc/derby-ds";        
		boolean showsql = true;		
		SQLUtil.startPool( poolname, driver, jdbcurl, username, password,
        		 readOnly,
        		 txIsolationLevel,
        		 validationQuery,
        		 jndiName,   
        		 initialConnections,
        		 minimumSize,
        		 maximumSize,
        		 usepool,
        		  external,
        		 externaljndiName        , showsql		
        		);
	}
    
    public static void main(String[] args)
    {
    	Monitor monitor = new Monitor();
    	monitor.stopPool();
    	monitor.startPool();
    	DBUtil dbutil = new DBUtil();
    	try {
			dbutil.executeSelect("select 1 from dual");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(dbutil.size());
    }
    
    
}
