package org.frameworkset.gencode.web.entity;

import org.frameworkset.util.annotations.RequestParam;


public class FieldInfo {
	private String fieldName;
	private String mfieldName;
	private String fieldCNName;
	private String fieldAsciiCNName;
	private String type;
	private String columntype;
	private String defaultValue;	 
	private String columnname;
	private int stype = 0;
	private int desc = 0;
	@RequestParam(name="${rowid}_typecheck")
	private int typecheck = 0;
	@RequestParam(name="${rowid}_qtype")
	private int qtype = 0;
	@RequestParam(name="${rowid}_daterange")
	private int daterange = 0;
	private String dateformat; 
	private int maxlength = 0;
	@RequestParam(name="${rowid}_required")
	private int required;
	@RequestParam(name="${rowid}_hidden")
	private int hidden;
	private String replace;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getMfieldName() {
		return mfieldName;
	}
	public void setMfieldName(String mfieldName) {
		this.mfieldName = mfieldName;
	}
	public String getFieldCNName() {
		return fieldCNName;
	}
	public void setFieldCNName(String fieldCNName) {
		this.fieldCNName = fieldCNName;
	}
	public String getFieldAsciiCNName() {
		return fieldAsciiCNName;
	}
	public void setFieldAsciiCNName(String fieldAsciiCNName) {
		this.fieldAsciiCNName = fieldAsciiCNName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColumntype() {
		return columntype;
	}
	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	 
	public int getDesc() {
		return desc;
	}
	public void setDesc(int desc) {
		this.desc = desc;
	}
	public int getTypecheck() {
		return typecheck;
	}
	public void setTypecheck(int typecheck) {
		this.typecheck = typecheck;
	}
	public int getQtype() {
		return qtype;
	}
	public void setQtype(int qtype) {
		this.qtype = qtype;
	}
	public int getDaterange() {
		return daterange;
	}
	public void setDaterange(int daterange) {
		this.daterange = daterange;
	}
	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	public int getMaxlength() {
		return maxlength;
	}
	public void setMaxlength(int maxlength) {
		this.maxlength = maxlength;
	}
	 
	public String getReplace() {
		return replace;
	}
	public void setReplace(String replace) {
		this.replace = replace;
	}
	public int getStype() {
		return stype;
	}
	public void setStype(int stype) {
		this.stype = stype;
	}
	public int getRequired() {
		return required;
	}
	public void setRequired(int required) {
		this.required = required;
	}
	public int getHidden() {
		return hidden;
	}
	public void setHidden(int hidden) {
		this.hidden = hidden;
	}
}
