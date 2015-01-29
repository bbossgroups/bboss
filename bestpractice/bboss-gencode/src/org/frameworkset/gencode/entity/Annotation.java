package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Annotation {
	private String name;
	private List<AnnoParam> params;
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
	public void addAnnotation(AnnoParam annoParam) {
		if(params == null)
			params = new ArrayList<AnnoParam>();
		params.add(annoParam);
		
	}
}
