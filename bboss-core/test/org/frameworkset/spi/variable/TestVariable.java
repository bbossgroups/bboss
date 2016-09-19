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
	}

}
