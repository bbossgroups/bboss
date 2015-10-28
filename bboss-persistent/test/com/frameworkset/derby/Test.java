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
package com.frameworkset.derby;

import java.sql.SQLException;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;

/**
 * <p>Title: Test.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-1-20 下午05:34:07
 * @author biaoping.yin
 * @version 1.0
 */
public class Test
{
    @org.junit.Test
    public void test()
    {
        PreparedDBUtil db = new PreparedDBUtil();
        try
        {
            org.apache.derby.jdbc.EmbeddedDriver s;
            
            db.preparedSelect("derby","select * from airlines");
            db.executePrepared();
            for(int i = 0; i < db.size(); i ++)
            {
                System.out.println(db.getString(i,"AIRLINE"));
            }
            PoolManResultSetMetaData meta = (PoolManResultSetMetaData)db.getMeta();
            System.out.println(meta.toDetailString());
            db.executeInsert("derby","insert into airlines(AIRLINE ) values('NE')");
            
            db.executeDelete("derby","delete from  airlines where AIRLINE = 'NE'");
            db.preparedSelect("derby","select * from airlines");
            db.executePrepared();
            for(int i = 0; i < db.size(); i ++)
            {
                System.out.println(db.getString(i,"AIRLINE"));
            }
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
