package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Field {
	private String fieldName;
	private String mfieldName;
	private String type;
	private String defaultValue;
	private List<Annotation> annos;
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
	public void addAnnotation(Annotation annotation) {
		if(annos == null)
			annos = new ArrayList<Annotation>();
		annos.add(annotation);
		
	}

}
