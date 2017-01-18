package org.frameworkset.spi.variable;

import org.frameworkset.spi.BeanInfoAware;

public class VariableBean extends BeanInfoAware{
	private String varValue;
	private String varValue1;
	private String varValue2;
	private String varValue3;
	private int intValue;
	private int intValue2;
	public VariableBean(String varValue1,String varValue2)
	{
		System.out.println("varValue1:"+varValue1);
		System.out.println("varValue2:"+varValue2);
	}
	
	public String getExteral(String attr)
	{
		return super.beaninfo.getStringExtendAttribute(attr);
	}

	public String getVarValue() {
		return varValue;
	}

	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}

	public String getVarValue1() {
		return varValue1;
	}

	public void setVarValue1(String varValue1) {
		this.varValue1 = varValue1;
	}

	public String getVarValue2() {
		return varValue2;
	}

	public void setVarValue2(String varValue2) {
		this.varValue2 = varValue2;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getVarValue3() {
		return varValue3;
	}

	public void setVarValue3(String varValue3) {
		this.varValue3 = varValue3;
	}

	public int getIntValue2() {
		return intValue2;
	}

	public void setIntValue2(int intValue2) {
		this.intValue2 = intValue2;
	}

}
