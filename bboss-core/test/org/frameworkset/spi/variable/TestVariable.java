package org.frameworkset.spi.variable;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

public class TestVariable {
	@Test
	public void test()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/variable/ioc-var.xml");
		VariableBean variableBean = context.getTBeanObject("test.beans", VariableBean.class);
		System.out.println(variableBean.getExteral("string"));
		System.out.println(variableBean.getVarValue());
	}
	
	
	@Test
	public void testParent()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/variable/parent-var.xml");
		VariableBean variableBean = context.getTBeanObject("test.beans", VariableBean.class);
		System.out.println(variableBean.getExteral("string"));
		System.out.println(variableBean.getVarValue());
	}
	
	
	@Test
	public void testExternal()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/variable/parent-var.xml");
		System.out.println(context.getExternalProperty("varValue"));
		System.out.println(context.getExternalProperty("varValue1"));
		System.out.println(context.getExternalProperty("varValue2"));
		 
	}
	
	@Test
	public void testZH()
	{
//		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("F:/迅雷下载/dbinit-system/schema/assemble.xml");
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("F:/workspace/bboss-cms/dbinit-system/schema/assemble.xml");
		
		//F:\workspace\bboss-cms
//		VariableBean variableBean = context.getTBeanObject("test.beans", VariableBean.class);
//		System.out.println(variableBean.getExteral("string"));
//		System.out.println(variableBean.getVarValue());
	}
	
	//F:\迅雷下载\dbinit-system/schema/assemble.xml

}
