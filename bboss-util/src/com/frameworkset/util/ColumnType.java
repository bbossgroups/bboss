package com.frameworkset.util;

public class ColumnType implements java.io.Serializable{
	
	/**
	 * 如果值为null，必须指定值对应的类型，否则数据库没办法识别对应的java类型
	 */
	private Class type;
	public ColumnType(Class type) {
		this.type = type;
	}
	 
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}

}
