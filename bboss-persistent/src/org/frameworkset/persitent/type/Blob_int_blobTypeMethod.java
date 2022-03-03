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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
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
public class Blob_int_blobTypeMethod extends BaseTypeMethod{
	@Override
	public void action(StatementInfo stmtInfo, Param param, PreparedStatement statement, PreparedStatement statement_count, List resources) throws SQLException {
		if(param.getData() == null)
		{
			statement.setNull(param.getIndex(), Types.BLOB);
//					statement.setBlob(param.index, (Blob)param.data);
			if(statement_count != null)
			{
				statement_count.setNull(param.getIndex(), Types.BLOB);
			}
		}
		else
		{
			if(param.getData() instanceof String)
			{
				InputStream in = null;

				try
				{

					String data_str = (String)param.getData();
					int len =data_str.length();
					in = new ByteArrayInputStream(data_str.getBytes());
					statement.setBinaryStream(param.getIndex(), in, (int)len);
					if(statement_count != null)
					{
						statement_count.setBinaryStream(param.getIndex(), in, (int)len);
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

					if(in != null && resources != null)
						resources.add(in);

				}

			}
			else
			{
				statement.setBlob(param.getIndex(), (Blob)param.getData());
				if(statement_count != null)
				{
					statement_count.setBlob(param.getIndex(), (Blob)param.getData());
				}
			}
		}
	}
}
