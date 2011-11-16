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

import org.frameworkset.spi.ApplicationContext;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestAnnotationTx.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-19 ÏÂÎç05:12:03
 * @author biaoping.yin
 * @version 1.0
 */
public class TestAnnotationTx
{
	private ApplicationContext context ;
	@Before
	public void init()
	{
		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/transaction/" +
				"annotation/annotationtx.xml");
	}
    @Test
    public void testTxfailed()
    {
        TXA ai = (TXA)context.getBeanObject("annotation.test");
        try
        {
            ai.executeTXDBFailed();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    @Test
    public void testTx()
    {
        
//        TXA ai = (TXA)BaseSPIManager.getBeanObject("(failover(rmi://172.16.17.144:1098,rmi://172.16.17.144:1099))/annotation.test");
    	TXA ai = (TXA)context.getBeanObject("(rmi://172.16.17.144:1098)/annotation.test");
        try
        {
            ai.executeTxDB();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testDefualtTx()
    {
        
        TXA ai = (TXA)context.getBeanObject("annotation.test");
        try
        {
            ai.executeDefualtTXDB();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
}
