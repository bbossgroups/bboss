package org.frameworkset.persitent.type;
/**
 * Copyright 2022 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.Param;
import com.frameworkset.common.poolman.StatementInfo;
import com.frameworkset.util.FileUtil;
import org.frameworkset.util.BigFile;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import static com.frameworkset.common.poolman.DBOptionsPreparedDBUtil.getString;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/2
 * @author biaoping.yin
 * @version 1.0
 */
public class Clob_int_FileTypeMethod extends BaseTypeMethod{
	@Override
	public void action(StatementInfo stmtInfo, Param param, PreparedStatement statement, PreparedStatement statement_count, List resources) throws SQLException {
		StringReader reader  = null;
		InputStream in = null;
		InputStream dataf = null;
		try
		{

			if(param.getData() == null)
			{
				statement.setNull(param.getIndex(), Types.CLOB);
				if(statement_count != null)
				{
					//					Reader reader = (Reader)data[0];
					statement_count.setNull(param.getIndex(), Types.CLOB);
				}
			}
			else
			{
				if(param.getData() instanceof File)
				{
					File data = null;
					data = (File)param.getData();
					if(param.getCharset() == null)
					{

						in = new java.io.BufferedInputStream(new java.io.FileInputStream(data));
						long len = data.length();
						statement.setAsciiStream(param.getIndex(), in,(int)len);
						if(statement_count != null)
						{
							//					Reader reader = (Reader)data[0];
							statement_count.setAsciiStream(param.getIndex(), in,(int)len);

						}
					}
					else
					{
						String content = FileUtil.getFileContent(data, param.getCharset());
						reader  = new java.io.StringReader(content);
						statement.setCharacterStream(param.getIndex(), reader,content.length());
						if(statement_count != null)
						{
							//					Reader reader = (Reader)data[0];
							statement_count.setCharacterStream(param.getIndex(), reader,content.length());

						}
					}
				}
				else if(param.getData() instanceof BigFile)
				{
					BigFile f = (BigFile)param.getData();
					long len = f.getSize();
					in = new java.io.BufferedInputStream(f.getInputStream());
					statement.setAsciiStream(param.getIndex(), in,(int)len);
					if(statement_count != null)
					{
						//					Reader reader = (Reader)data[0];
						statement_count.setAsciiStream(param.getIndex(), in,(int)len);

					}
				}
				else if(param.getData() instanceof Object[])
				{
					Object[] values = ( Object[])param.getData();
					dataf = (InputStream)values[0];
					long len = (Long)values[1];
					in = new java.io.BufferedInputStream(dataf);
					statement.setAsciiStream(param.getIndex(), in,(int)len);
					if(statement_count != null)
					{
						//					Reader reader = (Reader)data[0];
						statement_count.setAsciiStream(param.getIndex(), in,(int)len);

					}
				}
				else
				{
					dataf = (InputStream)param.getData();
					String d = null;
					if(param.getCharset() == null)
						d = getString( dataf,(String)null);
					else
						d = getString( dataf,param.getCharset());
					reader  = new java.io.StringReader(d);
					statement.setCharacterStream(param.getIndex(), reader,d.length());
					if(statement_count != null)
					{
						//					Reader reader = (Reader)data[0];
						statement_count.setCharacterStream(param.getIndex(), reader,d.length());

					}
				}
			}
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}

		finally
		{
//					data = null;
//					if(reader != null)
//					{
//						try {
//							reader.close();
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						reader = null;
//					}
			if(reader != null)
				resources.add(reader);
			if(in != null)
			{
				resources.add(in);
			}
//						try
//						{
//							in.close();
//						}
//						catch (Exception e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
			if( dataf != null)
			{
//						try
//						{
//							dataf.close();
//						}
//						catch (Exception e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
				resources.add(dataf);
			}

		}
	}
}
