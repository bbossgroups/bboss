package org.frameworkset.spi.beans;

import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;

/**
 * 
 * <p>Title: TestInit.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-4 下午05:05:23
 * @author biaoping.yin
 * @version 1.0
 */
public class TestInit implements InitializingBean,DisposableBean
{

	public void afterPropertiesSet() throws Exception
	{

		System.out.println("TestInit initialized.");
		
	}

	public void destroy() throws Exception
	{

		System.out.println("TestInit destroyed.");
		
	}
	
	public void helloWorld()
	{
		System.out.println("TestInit helloWorld.");
		
	}

}
