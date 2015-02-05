package org.frameworkset.gencode.entity;

public class ConditionField {
	private String fieldName;
	private String columnName;
	private boolean isor = false;
	private boolean islike = false;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public boolean isIsor() {
		return isor;
	}
	public void setIsor(boolean isor) {
		this.isor = isor;
	}
	public boolean isIslike() {
		return islike;
	}
	public void setIslike(boolean islike) {
		this.islike = islike;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
}
