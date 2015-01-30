package org.frameworkset.gencode.entity;

import java.util.ArrayList;
import java.util.List;

public class Method {
	private String returntype;
	private List<Annotation> returnannos;
	private String methodname;
	private List<MethodParam> params;
	private List<String> exceptions;
	private String body;
	public String getReturntype() {
		return returntype;
	}
	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public List<MethodParam> getParams() {
		return params;
	}
	public void setParams(List<MethodParam> params) {
		this.params = params;
	}
	public List<String> getExceptions() {
		return exceptions;
	}
	public void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	} 
	public void addAnnotation(Annotation annotation) {
		if(returnannos == null)
			returnannos = new ArrayList<Annotation>();
		returnannos.add(annotation);
		
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public List<Annotation> getReturnannos() {
		return returnannos;
	}
	public void setReturnannos(List<Annotation> returnannos) {
		this.returnannos = returnannos;
	}
}
