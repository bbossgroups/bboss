package com.frameworkset.common;

public class Test_p {
	private String test;
	private String name;
	private String name1;
	private int count = 0;
	private int nomatch;
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
		return new StringBuffer().append("name=").append(name).append(",name1=").append(name1).append(",test=").append(test).append(",nomatch=").append(nomatch).toString();
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getNomatch() {
		return nomatch;
	}
	public void setNomatch(int nomatch) {
		this.nomatch = nomatch;
	}

}
