package org.frameworkset.spi;



public class BeanDestroyHook implements Runnable
{
	BaseApplicationContext applicationContext;
	
	public BeanDestroyHook(BaseApplicationContext applicationContext)
	{
		this.applicationContext = applicationContext;
	}

	public void run()
	{

		
		applicationContext.destroySingleBeans();
		
	}

}
