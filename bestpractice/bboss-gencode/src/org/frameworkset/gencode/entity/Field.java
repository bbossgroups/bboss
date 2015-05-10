package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private boolean pk;
	private String fieldName;
	private String mfieldName;
	private String fieldCNName;
	private String fieldAsciiCNName;
	/**
	 * url
	 * creditcard
	 * email
	 * file
	 * idcard
	 * textarea
	 * htmleditor
	 * word
	 * excel
	 * ppt
	 * fuction
	 */
	private String extendType;
	private String type;
	private String columntype;
	private String defaultValue;
	private boolean staticed;
	private boolean finaled;
	private String columnname;
	private boolean sortField;
	private boolean desc = true;
	private boolean defaultSortField ;
	private List<Annotation> annos;
	private int maxlength = 0;
	private int minlength = 0;
	private boolean required;
	private String replace;
	private boolean typecheck;
	private boolean daterange;
	private String dateformat;
	private String numformat;
	private boolean editable;
	
	
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
	public String getFieldAsciiCNName() {
		return fieldAsciiCNName;
	}
	public void setFieldAsciiCNName(String fieldAsciiCNName) {
		this.fieldAsciiCNName = fieldAsciiCNName;
	}
	public String getColumntype() {
		return columntype;
	}
	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}
	public boolean isPk() {
		return pk;
	}
	public void setPk(boolean ispk) {
		this.pk = ispk;
	}
	public int getMinlength() {
		return minlength;
	}
	public void setMinlength(int minlength) {
		this.minlength = minlength;
	}
	public boolean isTypecheck() {
		return typecheck;
	}
	public void setTypecheck(boolean typecheck) {
		this.typecheck = typecheck;
	}
	public boolean isDaterange() {
		return daterange;
	}
	public void setDaterange(boolean daterange) {
		this.daterange = daterange;
	}
	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
	public String getNumformat() {
		return numformat;
	}
	public void setNumformat(String numformat) {
		this.numformat = numformat;
	}
	public boolean isEditable() {
		return editable;
	}
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	public String getExtendType() {
		return extendType;
	}
	public void setExtendType(String extendType) {
		this.extendType = extendType;
	}

}
