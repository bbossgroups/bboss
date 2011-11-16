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
package org.frameworkset.event;

import org.frameworkset.spi.remote.JGroupHelper;
import org.junit.Test;

/**
 * 
 * <p>Title: TestRun.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright (c) 2009</p>
 *
 * <p>bboss workgroup</p>
 * @Date May 17, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class TestRun {
	public static void main(String args[])
	{
		JGroupHelper.getJGroupHelper().start();
//		ExampleListener listener = new ExampleListener();
//		//调用init方法注册监听器，这样就能收到事件发布器发布的事件
//		listener.init();
//		
////		ExampleEventPublish.publishEventtype1();
////		ExampleEventPublish.publishEventtype2();
////		ExampleEventPublish.publishEventtype1();
////		ExampleEventPublish.publishAsynEventtype1();
//		ExampleEventPublish.publishEventtype2Withtarget();
//		TestRun run = new TestRun();
////		run.testAsyEvent();
//		run.testAsyEvents();
	}
	
	@Test
	public void testAsyEvent()
	{
		ExampleListener listener = new ExampleListener();
		listener.init();
		
		ExampleEventPublish.publishAsynEventtype1();
	}
	
	
	@Test
	public void testAsyEvents()
	{
		ExampleListener listener = new ExampleListener();
		listener.init();
		T t;
		t = new T();
		t.start();
		t = new T();
		t.start();
		
		t = new T();
		t.start();
		
		t = new T();
		t.start();
		
		
	}
	
	static class T extends Thread
	{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while(true)
			{
				
				ExampleEventPublish.publishAsynEventtype1();
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
