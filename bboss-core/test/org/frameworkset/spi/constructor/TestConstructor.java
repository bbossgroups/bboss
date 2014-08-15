package org.frameworkset.spi.constructor;

import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.SPIException;

public class TestConstructor {
    @org.junit.Test
	public void testConstructor()
	{
		try {
			ConstructorInf b = (ConstructorInf)BaseSPIManager.getProvider("constructor.b");
			ConstructorInf a = (ConstructorInf)BaseSPIManager.getProvider("constructor.a");
			try {
				a.testHelloworld();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			
			try {
				BaseSPIManager.getProvider("interceptor.a");
			} catch (SPIException e1) {
				// TODO Auto-generated catch block
				throw e;
			}
			throw e;
		}
	}
	
	public static void main(String[] args)
	{
//		testConstructor();
	}
}
