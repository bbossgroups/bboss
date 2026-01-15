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

import com.frameworkset.common.poolman.CallableParam;
import com.frameworkset.common.poolman.Param;
import com.frameworkset.common.poolman.StatementInfo;
import org.frameworkset.persitent.type.procdule.RegisterOutMethod;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/3
 * @author biaoping.yin
 * @version 1.0
 */
public class BaseTypeMethod implements RegisterOutMethod {
    private boolean nullable;
    public BaseTypeMethod(){
        
    }
    public BaseTypeMethod(boolean nullable){
        this.nullable = nullable;
    }

    public boolean isNullable() {
        return nullable;
    }

    @Override
	public void action(StatementInfo stmtInfo, CallableParam param, CallableStatement cstmt) throws SQLException {

	}

	@Override
	public void action(StatementInfo stmtInfo, Param param, PreparedStatement statement, PreparedStatement statement_count, List resources) throws SQLException {

	}
	@Override
	public String toString(){
		return this.getClass().getName();
	}
}
