package org.frameworkset.spi.constructor;

import org.frameworkset.spi.interceptor.AI;

public class ConstructorImpl implements ConstructorInf {
	private String message;
	private AI ai;
	private Test test;
	public ConstructorImpl(String message, AI ai,Test test)
	{
		this.message = message;
		this.ai = ai;
		this.test = test;
	}
	
	public ConstructorImpl()
	{
		
	}

	public void testHelloworld() {
		System.out.println("testHelloworld");
		
	}
	
	
	
}
