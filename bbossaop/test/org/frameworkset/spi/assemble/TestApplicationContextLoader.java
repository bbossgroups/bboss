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

package org.frameworkset.spi.assemble;

import org.frameworkset.netty.NettyRPCServer;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.ClientProxyContext;
import org.frameworkset.spi.assemble.callback.WebDocbaseAssembleCallback;
import org.frameworkset.spi.remote.RPCTestInf;
import org.junit.Test;

/**
 * <p>Title: TestApplicationContextLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-4 下午06:42:54
 * @author biaoping.yin
 * @version 1.0
 */
public class TestApplicationContextLoader {
	@Test
	public void testWebApplicationcontext()
	{
		AssembleUtil.registAssembleCallback(new WebDocbaseAssembleCallback("F:\\workspace\\bbossgroups-3.5\\bbossaop"));
		ApplicationContext context = ApplicationContext.getApplicationContext();
	}
	@Test
	public void testWebApplicationClient()
	{
			//单独运行服务端时一定要在初始化ApplicationContext之前运行这条语句，用来指定webbase路径,否则系统将无法找到通过docbase导入的组件
	    	AssembleUtil.registAssembleCallback(new WebDocbaseAssembleCallback("F:\\workspace\\bbossgroups-3.5\\bbossaop"));
	        NettyRPCServer.getNettyRPCServer().start();
	      //单独运行客服端时一定要在初始化ApplicationContext之前运行这条语句，用来指定webbase路径,否则系统将无法找到通过docbase导入的组件
//	        AssembleUtil.registAssembleCallback(new WebDocbaseAssembleCallback("D:/workspace/bbossgroup-2.0-RC2/bbossaop/")); 

			RPCTestInf testInf = ClientProxyContext.getApplicationClientBean("(netty::10.25.192.142:12347)/rpc.test?user=admin&password=123456&server_uuid=多多",RPCTestInf.class);
			
			for(int i = 0; i < 10; i ++)
			{
				Object ret = testInf.getParameter();
				
				
					System.out.println("ret_1186: = "+ret);
				
//				Object ret_1186 = context.getRPCResult(0, ret);
//		        Object ret_1187 = context.getRPCResult(1, ret);
//		        Object ret_1188 = context.getRPCResult(2, ret);
//		        Object ret_1189 = context.getRPCResult(3, ret,Target.BROADCAST_TYPE_JRGOUP);
				
//				System.out.println("ret_1187:" + i + " = "+ret_1187);
//				System.out.println("ret_1188:" + i + " = "+ret_1188);
//				System.out.println("ret_1189:" + i + " = "+ret_1189);
			}
	    
	}
	@Test
	public void testApplicationcontext()
	{
		ApplicationContext context = ApplicationContext.getApplicationContext();
		
	}

}
