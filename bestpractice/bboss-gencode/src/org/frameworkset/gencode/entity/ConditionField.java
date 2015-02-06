package org.frameworkset.gencode.entity;

public class ConditionField {
	private String fieldName;
	private String columnName;
	private boolean or = false;
	private boolean like = false;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isOr() {
		return or;
	}
	public void setOr(boolean or) {
		this.or = or;
	}
	public boolean isLike() {
		return like;
	}
	public void setLike(boolean like) {
		this.like = like;
	}
}
