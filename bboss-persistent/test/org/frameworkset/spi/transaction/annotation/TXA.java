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

package org.frameworkset.spi.transaction.annotation;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.annotation.RollbackExceptions;
import com.frameworkset.orm.annotation.Transaction;
import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * <p>Title: TXA.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-19 下午05:17:43
 * @author biaoping.yin
 * @version 1.0
 */
public class TXA 
{ 
    @Transaction(TransactionType.REQUIRED_TRANSACTION)
    @RollbackExceptions({"java.sql.SQLException","org.frameworkset.spi.transaction.Exception1"})
    public void executeTXDBFailed() throws SQLException
    {
        DBUtil db = new DBUtil();
        try
        {
            db.executeInsert("insert into char_table(id) values(2) ");
            db.executeDelete("delete from char_table where id=4");
            System.out.println("executeTXDBFailed:"+TransactionManager.getTransaction());
//            throw new SQLException();
        }
        catch (SQLException e)
        {
            throw e;
        }

    }
    
    public void executeDefualtFailed() throws SQLException
    {
        DBUtil db = new DBUtil();
        try
        {
            db.executeInsert("insert into char_table(id) values(2) ");
            db.executeDelete("delete from char_table where id=4");
            System.out.println("executeTXDBFailed:"+TransactionManager.getTransaction());
//            throw new SQLException();
        }
        catch (SQLException e)
        {
            throw e;
        }

    }
    
    @Transaction    
    public void executeDefualtTXDB() throws SQLException
    {
        DBUtil db = new DBUtil();
        try
        {
            db.executeInsert("insert into char_table(id) values(2) ");
            db.executeDelete("delete from char_table where id=4");
            System.out.println("executeTXDBFailed:"+TransactionManager.getTransaction());
//            throw new SQLException();
        }
        catch (SQLException e)
        {
            throw e;
        }

    }
    
    @Transaction(TransactionType.REQUIRED_TRANSACTION)
    @RollbackExceptions({"java.sql.SQLException","org.frameworkset.spi.transaction.Exception1"})
    public void executeTxDB() throws SQLException
    {
        DBUtil db = new DBUtil();
        try
        {
            db.executeSelect("select * from char_table where id=2");
            System.out.println("db.size()="+ db.size());
            db.executeInsert("insert into char_table(id) values(3) ");
            db.executeInsert("insert into char_table(id) values(4) ");
            db.executeDelete("delete from char_table where id=3");
            System.out.println("executeTxDB:"+TransactionManager.getTransaction());
            
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

}
