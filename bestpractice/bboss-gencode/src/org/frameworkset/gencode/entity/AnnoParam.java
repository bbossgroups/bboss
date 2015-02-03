package org.frameworkset.gencode.entity;

public class AnnoParam {
	private String name;
	private String value;
	public static final int V_CONTAST = 0;//常量
	public static final int V_STRING = 1;//字符串
	public static final int V_NUMBER = 2;//数字
	private int vtype = V_STRING;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getVtype() {
		return vtype;
	}
	public void setVtype(int vtype) {
		this.vtype = vtype;
	}

}
