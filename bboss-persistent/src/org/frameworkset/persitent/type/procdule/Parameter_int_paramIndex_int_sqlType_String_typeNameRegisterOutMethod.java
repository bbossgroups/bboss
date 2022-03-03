package org.frameworkset.persitent.type.procdule;
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

import com.frameworkset.common.poolman.CallableParam;
import com.frameworkset.common.poolman.StatementInfo;
import org.frameworkset.persitent.type.BaseTypeMethod;

import java.sql.CallableStatement;
import java.sql.SQLException;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/3
 * @author biaoping.yin
 * @version 1.0
 */
public class Parameter_int_paramIndex_int_sqlType_String_typeNameRegisterOutMethod  extends BaseTypeMethod {
	@Override
	public void action(StatementInfo stmtInfo, CallableParam param, CallableStatement cstmt) throws SQLException {
		cstmt.registerOutParameter(param.getIndex(), param.getSqlType(), param.getTypeName());
	}
}
