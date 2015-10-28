package com.frameworkset.common;

public class Test_f {
	private String name;
	private String name1;
	private String ret ;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName1() {
		return name1;
	}
	public void setName1(String name1) {
		this.name1 = name1;
	}
	public String toString()
	{
		return new StringBuffer("ret=").append(ret).append(",name=").append(name).append(",name1=").append(name1).toString();
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}

}
