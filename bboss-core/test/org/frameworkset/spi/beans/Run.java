package org.frameworkset.spi.beans;

import org.frameworkset.spi.DefaultApplicationContext;


public class Run
{
	public static void main(String[] args)
	{
		
		DefaultApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/beans/manager-beans.xml");
		DestroyBean testInit = context.getTBeanObject("test.destorybeans",DestroyBean.class);
//		testInit.helloWorld();
	}
}
