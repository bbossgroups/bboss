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

import java.io.Reader;
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
public class Clob_int_StringTypeMethod extends BaseTypeMethod{
	@Override
	public void action(StatementInfo stmtInfo, Param param, PreparedStatement statement, PreparedStatement statement_count, List resources) throws SQLException {

		String data = null;
		Reader reader = null;
		try
		{
			data = (String)param.getData();
			if(data == null)
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
				reader = new java.io.StringReader(data);
				statement.setCharacterStream(param.getIndex(), reader,data.length());
				if(statement_count != null)
				{
					//					Reader reader = (Reader)data[0];
					statement_count.setCharacterStream(param.getIndex(), reader,data.length());

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
			data = null;
//					if(reader != null)
//					{
//						try {
//							reader.close();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						reader = null;
//					}
			if(reader != null && resources != null)
				resources.add(reader);

		}

	}
}
