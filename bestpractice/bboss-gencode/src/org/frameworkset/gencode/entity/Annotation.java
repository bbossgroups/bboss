package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Annotation {
	private String name;
	private List<AnnoParam> params;
	
	public Annotation(String name) {
		this.name = name;
	}
	
	public Annotation() {		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<AnnoParam> getParams() {
		return params;
	}
	public void setParams(List<AnnoParam> params) {
		this.params = params;
	}
	public Annotation addAnnotationParam(AnnoParam annoParam) {
		if(params == null)
			params = new ArrayList<AnnoParam>();
		params.add(annoParam);
		return this;
		
	}
	
	public Annotation addAnnotationParam(String name,String value) {
		if(params == null)
			params = new ArrayList<AnnoParam>();
		AnnoParam annoParam = new AnnoParam();
		annoParam.setName(name);
		annoParam.setValue(value);
		params.add(annoParam);
		return this;
		
	}
	
	public Annotation addAnnotationParam(String name,String value,int vtype) {
		if(params == null)
			params = new ArrayList<AnnoParam>();
		AnnoParam annoParam = new AnnoParam();
		annoParam.setName(name);
		annoParam.setValue(value);
		annoParam.setVtype(vtype);
		params.add(annoParam);
		return this;
		
	}
}
