package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private String fieldName;
	private String mfieldName;
	private String fieldCNName;
	private String type;
	private String defaultValue;
	private boolean staticed;
	private boolean finaled;
	private String columnname;
	private boolean sortField;
	private boolean desc = true;
	private boolean defaultSortField ;
	private List<Annotation> annos;
	private int maxlength = 0;
	private boolean required;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public List<Annotation> getAnnos() {
		return annos;
	}
	public void setAnnos(List<Annotation> annos) {
		this.annos = annos;
	}
	public Field addAnnotation(Annotation annotation) {
		if(annos == null)
			annos = new ArrayList<Annotation>();
		annos.add(annotation);
		return this;
		
	}
	public boolean isStaticed() {
		return staticed;
	}
	public void setStaticed(boolean staticed) {
		this.staticed = staticed;
	}
	public boolean isFinaled() {
		return finaled;
	}
	public void setFinaled(boolean finaled) {
		this.finaled = finaled;
	}
	public String getColumnname() {
		return columnname;
	}
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}
	public boolean isSortField() {
		return sortField;
	}
	public void setSortField(boolean sortField) {
		this.sortField = sortField;
	}
	
	public String getFieldCNName() {
		return fieldCNName;
	}
	public void setFieldCNName(String fieldCNName) {
		this.fieldCNName = fieldCNName;
	}
	public boolean isDesc() {
		return desc;
	}
	public void setDesc(boolean desc) {
		this.desc = desc;
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
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isDefaultSortField() {
		return defaultSortField;
	}
	public void setDefaultSortField(boolean defaultSortField) {
		this.defaultSortField = defaultSortField;
	}

}
