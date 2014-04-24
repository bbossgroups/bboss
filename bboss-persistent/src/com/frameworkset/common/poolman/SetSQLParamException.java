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
package com.frameworkset.common.poolman;


/**
 * <p>Title: SetSQLParamException.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-3-11 上午10:08:07
 * @author biaoping.yin
 * @version 1.0
 */
public class SetSQLParamException extends NestedSQLException
{

    public SetSQLParamException(String reason, String SQLState, int vendorCode, Throwable cause)
    {
        super(reason, SQLState, vendorCode, cause);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(String reason, String SQLState, int vendorCode)
    {
        super(reason, SQLState, vendorCode);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(String reason, String SQLState, Throwable cause)
    {
        super(reason, SQLState, cause);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(String reason, String SQLState)
    {
        super(reason, SQLState);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(String msg, Throwable cause)
    {
        super(msg, cause);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(String msg)
    {
        super(msg);
        // TODO Auto-generated constructor stub
    }

    public SetSQLParamException(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    

}
