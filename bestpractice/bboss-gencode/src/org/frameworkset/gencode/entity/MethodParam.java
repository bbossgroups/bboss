package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class MethodParam {
	private List<Annotation> annos;
	private String name;
	private String type;
	public List<Annotation> getAnnos() {
		return annos;
	}
	public void setAnnos(List<Annotation> annos) {
		this.annos = annos;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MethodParam addAnnotation(Annotation annotation) {
		if(annos == null)
			annos = new ArrayList<Annotation>();
		annos.add(annotation);
		return this;
	}
}
