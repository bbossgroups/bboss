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
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.handle.ValueExchange;

/**
 * 测试如下方法是否正常工作
 * dbUtil.getBinaryStream
 * dbUtil.getUnicodeStream(
 * dbUtil.getAsciiStream(
 * getCharacterStream(
 * <p>Title: TestStream.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date Dec 5, 2008 3:12:22 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestStream {
	public static void testClob()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect("select clobname from test where id='test472'");
			System.out.println("BinaryStream:" + ValueExchange.getStringFromStream(dbUtil.getBinaryStream(0, 0)));
			System.out.println("UnicodeStream:" + ValueExchange.getStringFromStream(dbUtil.getUnicodeStream(0, 0)));
			System.out.println("AsciiStream:" + ValueExchange.getStringFromStream(dbUtil.getAsciiStream(0, 0)));
			System.out.println("Reader:" + ValueExchange.getStringFromReader(dbUtil.getCharacterStream(0, 0)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testBlob()
	{
		PreparedDBUtil pdb = new PreparedDBUtil();
		
		
		DBUtil dbUtil = new DBUtil();
		try {
			pdb.preparedUpdate("update test set blobname = ? where id = 'test472'");
			pdb.setBlob(1, "blob".getBytes());
			pdb.executePrepared();
			dbUtil.executeSelect("select blobname from test where id='test472'");
			System.out.println("BinaryStream:" + ValueExchange.getStringFromStream(dbUtil.getBinaryStream(0, 0)));
			System.out.println("UnicodeStream:" + ValueExchange.getStringFromStream(dbUtil.getUnicodeStream(0, 0)));
			System.out.println("AsciiStream:" + ValueExchange.getStringFromStream(dbUtil.getAsciiStream(0, 0)));
			/**
			 * blob类型的字段不能直接转换为Reader对象,执行下述代码，系统会包以下异常
			 * java.sql.SQLException: Error type cast column index[0]:From [oracle.sql.BLOB] to [java.io.Reader]
				at com.frameworkset.common.poolman.Record.getCharacterStream(Record.java:780)
				at com.frameworkset.common.poolman.DBUtil.getCharacterStream(DBUtil.java:3033)
				at com.frameworkset.common.TestStream.testBlob(TestStream.java:74)
				at com.frameworkset.common.TestStream.main(TestStream.java:87)
			 */
			System.out.println("Reader:" + ValueExchange.getStringFromReader(dbUtil.getCharacterStream(0, 0)));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public static void main(String[] args)
	{
		DBUtil.debugMemory();
		testClob();
		testBlob();
		DBUtil.debugMemory();
		
		java.util.Timer timer = new java.util.Timer();
		
		
		
	}

}
