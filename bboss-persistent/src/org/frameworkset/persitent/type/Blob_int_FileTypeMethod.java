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
import org.frameworkset.util.BigFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/2
 * @author biaoping.yin
 * @version 1.0
 */
public class Blob_int_FileTypeMethod extends BaseTypeMethod{
	@Override
	public void action(StatementInfo stmtInfo, Param param, PreparedStatement statement, PreparedStatement statement_count, List resources) throws SQLException {
		File x = null;
		InputStream in = null;
//				try
//				this.setBinaryStream(i, getInputStream(x), Integer.MAX_VALUE);

		try
		{

			if(param.getData() == null)
			{
				statement.setNull(param.getIndex(), Types.BLOB);
				if(statement_count != null)
				{
					//					Reader reader = (Reader)data[0];
					statement_count.setNull(param.getIndex(), Types.BLOB);
				}
			}
			else
			{
				long len =Integer.MAX_VALUE;
				if(param.getData() instanceof File)
				{
					x = (File)param.getData();

					in = new java.io.BufferedInputStream(new FileInputStream(x));
					len = x.length();
				}
				else if(param.getData() instanceof BigFile)
				{
					BigFile f = (BigFile)param.getData();
					len = f.getSize();
					in = new java.io.BufferedInputStream(f.getInputStream());
				}
				else if(param.getData() instanceof Object[])
				{
					Object[] values = ( Object[])param.getData();
					in = new java.io.BufferedInputStream(( InputStream)values[0]);
					len = (Long)values[1];
				}
				else
					in = new java.io.BufferedInputStream(( InputStream)param.getData());


				statement.setBinaryStream(param.getIndex(), in, (int)len);
				if(statement_count != null)
				{
					statement_count.setBinaryStream(param.getIndex(), in, (int)len);
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
			x = null;
//					if(in != null)
//					{
//						try {
////							in.close();
//
//
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					in = null;
			if(in != null)
				resources.add(in);

		}
	}
}
