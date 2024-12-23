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

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.transaction.TransactionManager;

import oracle.sql.BLOB;
import oracle.sql.CLOB;
/**
 * 
 * <p>Title: TestLob.java</p>
 *
 * <p>Description: 
 * CREATE
    TABLE TEST
    (
        BLOBNAME BLOB,
        CLOBNAME CLOB,
        ID VARCHAR(100)
    )
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2011-12-27 下午2:56:03
 * @author biaoping.yin
 * @version 1.0
 */
public class TestLob {
	
	public static class LobBean
	{
		private String id;		
		@Column(type="blob")//指示属性的值按blob类型写入或者读取
		private String blobname;
		@Column(type="clob")//指示属性的值按clob类型写入或者读取
		private String clobname; 

        @Column(name="name_")//指示属性名称与表字段名称映射关系，name属性对应于表中的name_字段
		private String name; 
        @Column(dataformat="yyyy-mm-dd")//指示日期类型属性值的存储和读取转换日期格式
		private String regdate; 
        
//        。。。。。。


	}
	@Test
	public void testNewSQLParamInsert() throws Exception
	{
		SQLParams params = new SQLParams();
		params.addSQLParam("id", "1", SQLParams.STRING);
		// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
		params.addSQLParam("blobname", "abcdblob",
				SQLParams.BLOB);
		params.addSQLParam("clobname", "abcdclob",
				SQLParams.CLOB);
		SQLExecutor.insertBean("insert into test(id,blobname,clobname) values(#[id],#[blobname],#[clobname])", params);
	}
	@Test
	public void testNewBeanInsert() throws Exception
	{
		LobBean bean = new LobBean();
		bean.id = "2";
		bean.blobname = "abcdblob";
		bean.clobname = "abcdclob";
		SQLExecutor.insertBean("insert into test(id,blobname,clobname) values(#[id],#[blobname],#[clobname])", bean);
	}
	
	@Test
	public void testNewOrMappingQuery() throws Exception
	{
//		SQLParams params = new SQLParams();
//		params.addSQLParam("id", "1", SQLParams.STRING);
//		// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
//		params.addSQLParam("blobname", "abcdblob",
//				SQLParams.BLOB);
//		params.addSQLParam("clobname", "abcdclob",
//				SQLParams.CLOB);
		LobBean bean = SQLExecutor.queryObject(LobBean.class,"select * from test");
		System.out.println();
	}
	
	
	@Test
	public void testNewOrMappingsQuery() throws Exception
	{
//		SQLParams params = new SQLParams();
//		params.addSQLParam("id", "1", SQLParams.STRING);
//		// ID,HOST_ID,PLUGIN_ID,CATEGORY_ID,NAME,DESCRIPTION,DATASOURCE_NAME,DRIVER,JDBC_URL,USERNAME,PASSWORD,VALIDATION_QUERY
//		params.addSQLParam("blobname", "abcdblob",
//				SQLParams.BLOB);
//		params.addSQLParam("clobname", "abcdclob",
//				SQLParams.CLOB);
		List<LobBean> bean = SQLExecutor.queryList(LobBean.class,"select * from test");
		System.out.println();
	}
	
	
	
	/**
	 * CREATE
    TABLE CLOBFILE
    (
        FILEID VARCHAR(100),
        FILENAME VARCHAR(100),
        FILESIZE BIGINT,
        FILECONTENT CLOB
    )
	 */
	public @Test void uploadClobFile() throws Exception
	{
		File file = new File("D:\\1");
		
		String sql = "";
		try {
			sql = "INSERT INTO CLOBFILE (FILENAME,FILECONTENT,fileid,FILESIZE) VALUES(#[filename],#[FILECONTENT],#[FILEID],#[FILESIZE])";
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", file.getName(), SQLParams.STRING);
			sqlparams.addSQLParamWithCharset("FILECONTENT", file,SQLParams.CLOBFILE,"UTF-8");
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", file.length(),SQLParams.LONG);
			SQLExecutor.insertBean(sql, sqlparams);			
			
		} catch (Exception ex) {
		
			
			throw new Exception("上传附件关联临控指令布控信息附件失败：" + ex);
		} 
		
		
	}
	
		/**
	 * CREATE
    TABLE CLOBFILE
    (
        FILEID VARCHAR(100),
        FILENAME VARCHAR(100),
        FILESIZE BIGINT,
        FILECONTENT CLOB(2147483647)
    )
	 */
	public @Test void updateClobFile() throws Exception
	{
		File file = new File("D:\\bbossgroups-3.5.1\\bboss-taglib\\readme.txt");
		String sql = "";
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			SQLExecutor.queryField("select 1 as ss from CLOBFILE where fieldid=? for update nowait","11");//锁定记录
			sql = "update CLOBFILE set FILECONTENT=#[FILECONTENT]) where fileid = #[FILEID])";
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParamWithCharset("FILECONTENT", file,SQLParams.CLOBFILE,"UTF-8");
			sqlparams.addSQLParam("FILEID", "11",SQLParams.STRING);
			SQLExecutor.updateBean(sql, sqlparams);			
			tm.commit();
		} catch (Exception ex) {
			throw new Exception("上传附件关联临控指令布控信息附件失败：" + ex);
		} 
		finally
		{
			tm.release();
		}
		
		
	}
	
	/**
	 * 上传附件
	 * @param inputStream
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public boolean uploadFile(InputStream inputStream,long size, String filename) throws Exception {
		boolean result = true;
		String sql = "";
		try {
			sql = "INSERT INTO filetable (FILENAME,FILECONTENT,fileid,FILESIZE) VALUES(#[filename],#[FILECONTENT],#[FILEID],#[FILESIZE])";
			SQLParams sqlparams = new SQLParams();
			sqlparams.addSQLParam("filename", filename, SQLParams.STRING);
			sqlparams.addSQLParam("FILECONTENT", inputStream, size,SQLParams.BLOBFILE);
			sqlparams.addSQLParam("FILEID", UUID.randomUUID().toString(),SQLParams.STRING);
			sqlparams.addSQLParam("FILESIZE", size,SQLParams.LONG);
			SQLExecutor.insertBean(sql, sqlparams);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			result = false;
			throw new Exception("上传附件关联临控指令布控信息附件失败：" + ex);
		} finally {
			if(inputStream != null){
				inputStream.close();
			}
		}
		return result;
	}
	
	public File getDownloadFile(String fileid) throws Exception
	{
		try
		{
			return SQLExecutor.queryTField(
											File.class,
											new FieldRowHandler<File>() {

												@Override
												public File handleField(
														Record record)
														throws Exception
												{

													// 定义文件对象
													File f = new File("d:/",record.getString("filename"));
													// 如果文件已经存在则直接返回f
													if (f.exists())
														return f;
													// 将blob中的文件内容存储到文件中
													record.getFile("filecontent",f);
													return f;
												}
											},
											"select * from filetable where fileid=?",
											fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	public File getDownloadClobFile(String fileid) throws Exception
	{
		try
		{
			return SQLExecutor.queryTField(
											File.class,
											new FieldRowHandler<File>() {

												@Override
												public File handleField(
														Record record)
														throws Exception
												{

													// 定义文件对象
													File f = new File("d:/",record.getString("filename"));
													// 如果文件已经存在则直接返回f
													if (f.exists())
														return f;
													// 将blob中的文件内容存储到文件中
													record.getFile("filecontent",f);
													return f;
												}
											},
											"select * from CLOBFILE where fileid=?",
											fileid);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	
	public void deletefiles() throws Exception
	{

		SQLExecutor.delete("delete from filetable ");	
		SQLExecutor.delete("delete from CLOBFILE ");	
	}

	
	public List<HashMap> queryfiles() throws Exception
	{

		return SQLExecutor.queryList(HashMap.class, "select FILENAME,fileid,FILESIZE from filetable");
		
	}
	
	public List<HashMap> queryclobfiles()throws Exception
	{

		return SQLExecutor.queryList(HashMap.class, "select FILENAME,fileid,FILESIZE from CLOBFILE");
		
	}

//	
//	public void downloadFileFromBlob(String fileid, final HttpServletRequest request,
//			final HttpServletResponse response) throws Exception
//	{
//		try
//		{
//			SQLExecutor.queryByNullRowHandler(new NullRowHandler() {
//				
//				public void handleRow(Record record) throws Exception
//				{
//
//					StringUtil.sendFile(request, response, record
//							.getString("filename"), record
//							.getBlob("filecontent"));
//				}
//			}, "select * from filetable where fileid=?", fileid);
//		}
//		catch (Exception e)
//		{
//			throw e;
//		}
//	}
//	
////	
//	public void downloadFileFromClob(String fileid, final HttpServletRequest request,
//			final HttpServletResponse response) throws Exception
//	{
//
//		try
//		{
//			SQLExecutor.queryByNullRowHandler(new NullRowHandler() {
//				@Override
//				public void handleRow(Record record) throws Exception
//				{
//
//					StringUtil.sendFile(request, response, record
//							.getString("filename"), record
//							.getClob("filecontent"));
//				}
//			}, "select * from CLOBFILE where fileid=?", fileid);
//		}
//		catch (Exception e)
//		{
//			throw e;
//		}
//		
//	}
//    @BeforeClass
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
