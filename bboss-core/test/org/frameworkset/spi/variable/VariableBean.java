package org.frameworkset.spi.variable;

import org.frameworkset.spi.BeanInfoAware;

public class VariableBean extends BeanInfoAware{
	private String varValue;
	private String varValue1;
	private String varValue2;
	private int intValue;
	public VariableBean(String varValue1,String varValue2)
	{
		System.out.println("varValue1:"+varValue1);
		System.out.println("varValue2:"+varValue2);
	}

}
