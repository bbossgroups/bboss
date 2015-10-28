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
package com.frameworkset.mysql;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;


public class mysql
{
	public static void main(String[] args)
	{
		createTable();
	}
	
    public static void createTable()
    {
        String droptableinfo = "" +
        		" drop table TABLEINFO ";

        String createtableinfo = " CREATE TABLE TABLEINFO "+
        "("+
        " TABLE_NAME          VARCHAR(255)        NOT NULL,"+
        "TABLE_ID_NAME       VARCHAR(255),"+
        "TABLE_ID_INCREMENT  int                 DEFAULT 1,"+
        "TABLE_ID_VALUE      int              DEFAULT 0,"+
        "TABLE_ID_GENERATOR  VARCHAR(255),"+
        "TABLE_ID_TYPE       VARCHAR(255),"+
        "TABLE_ID_PREFIX     VARCHAR(255)"+
        ")";
        String addpk = "ALTER TABLE TABLEINFO ADD   CONSTRAINT PK_TABLEINFO0 PRIMARY KEY (TABLE_NAME)";
        String insertsql = "INSERT INTO TABLEINFO ( TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE,"+
                      "TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX ) VALUES ("+
                      "'test', 'id', 1, 0, NULL, 'int', NULL) ";
        
        String insertsql1_ = "INSERT INTO TABLEINFO ( TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE,"+
        "TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX ) VALUES ("+
        "'cim_dbpool', 'id', 1, 0, 'seq_dbpool', 'sequence', NULL) ";
        
        String insertsql2_ = "INSERT INTO sequence.sequence_data (sequence_name) VALUE ('seq_dbpool')  ";
 
      
        PreparedDBUtil dbutil = new PreparedDBUtil();
        try
        {

            
            
            
            dbutil.preparedDelete("mysql",droptableinfo);
            
            dbutil.executePrepared();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            try
            {
                dbutil.preparedUpdate("mysql",createtableinfo);
                
                dbutil.executePrepared();
                
                dbutil.preparedUpdate("mysql",addpk);                
                
                dbutil.executePrepared();
                
                dbutil.preparedInsert("mysql",insertsql);                
                
                dbutil.executePrepared();
                
                dbutil.preparedInsert("mysql",insertsql1_);                
                
                dbutil.executePrepared();
                
                dbutil.preparedInsert("mysql",insertsql2_);                
                
                dbutil.executePrepared();
                
                
                try
                {
                    String droptest = "drop table test";
                    dbutil.preparedDelete("mysql",droptest);                
                    
                    dbutil.executePrepared();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String createtest = "create table test(id int,blobname longblob,clobname MEDIUMTEXT)";
//                String proce_tt = 
//                    "CREATE  PROCEDURE   tt"+   
//                            "" +
//                            "begin null; end;";
//                
                dbutil.preparedUpdate("mysql",createtest);                
                
                dbutil.executePrepared();
            }
            catch (SQLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }
//  @Test
//  public void testBlobWrite()
//  {
//      
//  }
	
	
	/**
     * 第一种插入blob字段的方法，通用的模式
	 * @throws Exception 
	 */
    @Test
	public void testBlobWrite() throws Exception
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedInsert("mysql", "insert into test(id,blobname) values(?,?)");
			
			dbUtil.setString(1, DBUtil.getNextStringPrimaryKey("test"));
			dbUtil.setBlob(2, new java.io.File("resources/lob/manager.rar"));//直接将文件存储到大字段中
			
			dbUtil.executePrepared();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		finally
		{
			
		}
		
	}
//	
	
	/**
	 * 针对oracle Blob字段的插入操作
	 */
    @Test
	public void testBigBlobWrite() throws Exception
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			//启动事务
			tm.begin();
			//先插入一条记录,blob字段初始化为empty_lob
			dbUtil.preparedInsert("mysql", "insert into test(id,blobname) values(?,?)");
			String id = DBUtil.getNextStringPrimaryKey("test");
			dbUtil.setString(1, id);
			dbUtil.setNull(2,java.sql.Types.BLOB);//先设置空的blob字段
			
			
			dbUtil.executePrepared();
			
			//查找刚才的插入的记录，修改blob字段的值为一个文件
			dbUtil = new PreparedDBUtil();
			dbUtil.preparedUpdate("mysql","update test set  blobname =? where id = ?");
			dbUtil.setBlob(1, new java.io.File("resources/lob/manager.rar"));
			dbUtil.setString(2, id);
			
			dbUtil.executePrepared();
			
//			BLOB blob = (BLOB)dbUtil.getBlob(0, "blobname");
//			if(blob != null)
//			{				
//				DBUtil.updateBLOB(blob, new java.io.File("resources/lob/manager.rar"));
//			}
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
			throw e;
		}
		finally
		{
			tm = null;
			dbUtil = null;
		}
		
	}
//	
//	
	/**
	 * 大字段的读取
	 */
    @Test
	public void testBlobFileRead()  throws Exception
	{
//        TransactionManager tm = new TransactionManager(); 
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {

			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect("mysql", "select id,blobname from test");

			dbUtil.executePreparedWithRowHandler(new NullRowHandler(){

                @Override
                public void handleRow(Record origine) throws Exception
                {
                    
                    origine.getFile("blobname", new java.io.File("resources/lob/reader/dominspector_" + origine.getRowid() +".rar"));
                }});
			
			System.out.println("testClobRead dbUtil.size():"+dbUtil.size());
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		finally
		{
			
		}
		
	}
    
    /**
     * 大字段的读取
     */
    @Test
    public void testBlobRead()  throws Exception
    {
        createTable();
        this.testBigBlobWrite();
//        TransactionManager tm = new TransactionManager(); 
        PreparedDBUtil dbUtil = new PreparedDBUtil();
        try {

            //查询大字段内容并且将大字段存放到文件中
            dbUtil.preparedSelect("mysql", "select id,blobname from test");

            dbUtil.executePreparedWithRowHandler(new NullRowHandler(){

                @Override
                public void handleRow(Record origine) throws Exception
                {
                    Blob blob = origine.getBlob("blobname");
//                    origine.getFile("blobname", new java.io.File("resources/lob/reader/dominspector_" + origine.getRowid() +".rar"));
                }});
            
            System.out.println("testClobRead dbUtil.size():"+dbUtil.size());
            

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw e;
        }
        finally
        {
            
        }
        
    }
//	
	/**
	 * clob字段的写入
	 */
    @Test
	public void testClobWrite()  throws Exception
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedInsert("mysql",  "insert into test(id,clobname) values(?,?)");
			
			dbUtil.setString(1, DBUtil.getNextStringPrimaryKey("test"));
			dbUtil.setClob(2,"clobvalue");//直接将字符串存储到clob字段中
			dbUtil.executePrepared();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		    throw e;
		}
		finally
		{
			
		}
		
	}
	
	/**
	 * 针对oracle Clob字段的插入操作
	 */
    @Test
	public void testBigClobWrite()  throws Exception
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
			//启动事务
			tm.begin();
			//先插入一条记录,blob字段初始化为empty_lob
			dbUtil.preparedInsert("mysql",  "insert into test(id,clobname) values(?,?)");
			String id = DBUtil.getNextStringPrimaryKey("test");
			dbUtil.setString(1, id);
			dbUtil.setNull(2,java.sql.Types.CLOB);//先设置空的blob字段
			
			
			dbUtil.executePrepared();
			
			//查找刚才的插入的记录，修改blob字段的值为一个文件
			dbUtil = new PreparedDBUtil();
			dbUtil.preparedUpdate("mysql", "update test set clobname =? where id = ?");
			dbUtil.setClob(1, new java.io.File("resources/lob/route.txt"));
			dbUtil.setString(2, id);
			dbUtil.executePrepared();
			
//			CLOB clob = (CLOB)dbUtil.getClob(0, "clobname");
//			if(clob != null)
//			{				
//				DBUtil.updateCLOB(clob, new java.io.File("d:\\route.txt"));
//			}
			tm.commit();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			throw e;
		}
		finally
		{
			tm = null;
			dbUtil = null;
		}
		
	}
//	
	/**
	 * clob字段的读取
	 */
    @Test
	public void testClobFileRead()  throws Exception
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			//查询大字段内容并且将大字段存放到文件中
			dbUtil.preparedSelect( "mysql", "select id,clobname from test");
			dbUtil.executePreparedWithRowHandler(new NullRowHandler(){

                @Override
                public void handleRow(Record origine) throws Exception
                {
                    origine.getFile("clobname", new java.io.File("resources/lob/reader/route" + origine.getRowid() + ".txt"));
                    
                }});
			System.out.println("testClobRead dbUtil.size():"+dbUtil.size());
//			for(int i = 0; i < dbUtil.size(); i ++)
//			{
//				
//				dbUtil.getFile(i, "clobname", new java.io.File("resources/lob/route" + i + ".txt")); //读取clob字段到文件中
////				String clobvalue = dbUtil.getString(i, "clobname");//获取clob字段到字符串变量中
////				Clob clob = dbUtil.getClob(i, "clobname");//获取clob字段值到clob类型变量中
//			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		    throw e ;
		}
		finally
		{
			dbUtil = null;
		}
		
	}
	
    
    /**
     * clob字段的读取
     */
    @Test
    public void testClobRead()  throws Exception
    {
        this.createTable();
        this.testBigClobWrite();
        PreparedDBUtil dbUtil = new PreparedDBUtil();
        try {
            //查询大字段内容并且将大字段存放到文件中
            dbUtil.preparedSelect( "mysql", "select id,clobname from test");
            dbUtil.executePreparedWithRowHandler(new NullRowHandler(){

                @Override
                public void handleRow(Record origine) throws Exception
                {
                    Clob clob = origine.getClob("clobname");
//                    origine.getFile("clobname", new java.io.File("resources/lob/reader/route" + origine.getRowid() + ".txt"));
                    
                }});
            System.out.println("testClobRead dbUtil.size():"+dbUtil.size());
//          for(int i = 0; i < dbUtil.size(); i ++)
//          {
//              
//              dbUtil.getFile(i, "clobname", new java.io.File("resources/lob/route" + i + ".txt")); //读取clob字段到文件中
////                String clobvalue = dbUtil.getString(i, "clobname");//获取clob字段到字符串变量中
////                Clob clob = dbUtil.getClob(i, "clobname");//获取clob字段值到clob类型变量中
//          }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
//          e.printStackTrace();
            throw e ;
        }
        finally
        {
            dbUtil = null;
        }
        
    }
	
}
