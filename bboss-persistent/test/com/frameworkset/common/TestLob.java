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

import javax.transaction.RollbackException;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

import org.junit.BeforeClass;
import org.junit.Test;

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestLob {
    @BeforeClass
    public static void createTable()
    {
        String proce = 
//            "CREATE   OR   REPLACE   PROCEDURE   tt"+   
//                    " AS  " +
                    "begin "+
                        "execute immediate 'drop table test'; "+
                        "execute immediate 'create table test(id number(10),blobname blob,clobname clob)'; "+
                        "execute immediate 'delete from TABLEINFO where table_name=''test'''; "+
                        "execute immediate 'INSERT INTO TABLEINFO ( TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE,"+
                        "TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX ) VALUES ("+
                        "''test'', ''id'', 1, 0, NULL, ''int'', NULL)'; "+
                        "commit; "+
                        "EXCEPTION "+
                        "when others then "+
                        "execute immediate 'create table test(id number(10),blobname blob,clobname clob)'; " +
                        "execute immediate 'delete from TABLEINFO where table_name=''test'''; "+
                        "execute immediate 'INSERT INTO TABLEINFO ( TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE,"+
                        "TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX ) VALUES ("+
                        "''test'', ''id'', 1, 0, NULL, ''int'', NULL)'; "+
                        "commit;"+
                        "END;";
        
//        String proce = "{begin "+
//                                "execute immediate 'drop table test'; "+
//                                "execute immediate 'create table test(id number(10),blobname blob,clobname clob)'; "+
//                                "EXCEPTION "+
//                                "when others then "+
//                                "execute immediate 'create table test(id number(10),blobname blob,clobname clob)'; "+
//                                "END;}";
      
        CallableDBUtil dbutil = new CallableDBUtil();
        try
        {
            dbutil.prepareCallable(proce);
            
            dbutil.executeCallable();
//            dbutil.execute(proce);
//            CallableDBUtil dbutil_ = new CallableDBUtil();
//            dbutil_.prepareCallable("{call tt()}");
//         
//            dbutil_.executeCallable();
          
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        dbutil.prepareCallable(proce);
//        try
//        {
//            dbutil.executeCallable();
//        }
//        catch (SQLException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
//  @Test
//  public void testBlobWrite()
//  {
//      
//  }
	/**
     * 第一种插入blob字段的方法，通用的模式
	 */
    @Test
	public void testBlobWrite()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedInsert( "insert into test(id,blobname) values(?,?)");
			
			dbUtil.setString(1, DBUtil.getNextStringPrimaryKey("test"));
			dbUtil.setBlob(2, new java.io.File("d:/dominspector.rar"));//直接将文件存储到大字段中			
			dbUtil.executePrepared();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
		}
		
	}
	
	
	/**
	 * 针对oracle Blob字段的插入操作
	 */
    @Test
	public void testBigBlobWrite()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			//启动事务
			tm.begin();
			//先插入一条记录,blob字段初始化为empty_lob
			dbUtil.preparedInsert( "insert into test(id,blobname) values(?,?)");
			String id = DBUtil.getNextStringPrimaryKey("test");
			dbUtil.setString(1, id);
			dbUtil.setBlob(2,BLOB.empty_lob());//先设置空的blob字段
			
			
			dbUtil.executePrepared();
			
			//查找刚才的插入的记录，修改blob字段的值为一个文件
			dbUtil = new PreparedDBUtil();
			dbUtil.preparedSelect("select blobname from test where id = ?");
			dbUtil.setString(1, id);
			dbUtil.executePrepared();
			
			BLOB blob = (BLOB)dbUtil.getBlob(0, "blobname");
			if(blob != null)
			{				
				DBUtil.updateBLOB(blob, new java.io.File("d:/dominspector.rar"));
			}
			tm.commit();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally
		{
			tm = null;
			dbUtil = null;
		}
		
	}
	
	
	/**
	 * 大字段的读取
	 */
    @Test
	public void testBlobRead()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect( "select id,blobname from test");
			dbUtil.executePrepared();
			
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				
				dbUtil.getFile(i, "blobname", new java.io.File("e:/dominspector.rar"));//将blob字段的值转换为文件
//				Blob blob = dbUtil.getBlob(i, "blobname");//获取blob字段的值到blob变量中。
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
		}
		
	}
	
	/**
	 * clob字段的写入
	 */
    @Test
	public void testClobWrite()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedInsert( "insert into test(id,clobname) values(?,?)");
			
			dbUtil.setString(1, DBUtil.getNextStringPrimaryKey("test"));
			dbUtil.setClob(2,"clobvalue");//直接将字符串存储到clob字段中
			dbUtil.executePrepared();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			
		}
		
	}
	
	/**
	 * 针对oracle Clob字段的插入操作
	 */
    @Test
	public void testBigClobWrite()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			//启动事务
			tm.begin();
			//先插入一条记录,blob字段初始化为empty_lob
			dbUtil.preparedInsert( "insert into test(id,clobname) values(?,?)");
			String id = DBUtil.getNextStringPrimaryKey("test");
			dbUtil.setString(1, id);
			dbUtil.setClob(2,CLOB.empty_lob());//先设置空的blob字段
			
			
			dbUtil.executePrepared();
			
			//查找刚才的插入的记录，修改blob字段的值为一个文件
			dbUtil = new PreparedDBUtil();
			dbUtil.preparedSelect("select clobname from test where id = ?");
			dbUtil.setString(1, id);
			dbUtil.executePrepared();
			
			CLOB clob = (CLOB)dbUtil.getClob(0, "clobname");
			if(clob != null)
			{				
				DBUtil.updateCLOB(clob, new java.io.File("d:\\route.txt"));
			}
			tm.commit();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally
		{
			tm = null;
			dbUtil = null;
		}
		
	}
	
	/**
	 * clob字段的读取
	 */
    @Test
	public void testClobRead()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect( "select id,clobname from test");
			dbUtil.executePrepared();
			
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				
				dbUtil.getFile(i, "clobname", new java.io.File("d:/route" + i + ".txt")); //读取clob字段到文件中
//				String clobvalue = dbUtil.getString(i, "clobname");//获取clob字段到字符串变量中
//				Clob clob = dbUtil.getClob(i, "clobname");//获取clob字段值到clob类型变量中
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			dbUtil = null;
		}
		
	}
	
	public void main(String[] args)
	{
		System.out.print("start.........");
		DBUtil.debugMemory();
		for(int i = 0; i < 1; i ++)
		{
			testBlobWrite();
//			testBigBlobWrite();
//			testClobWrite();
//			testBigClobWrite();
			
		}
		
//		testBlobWrite();
//		testBigBlobWrite();
		DBUtil.debugMemory();
		testBlobRead();
//		testClobRead();
		System.out.print("end.........");
		DBUtil.debugMemory();
	}
	
	

}
