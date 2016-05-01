package com.frameworkset.common.ecs;

import java.util.ArrayList;

public class Form extends BaseElement{
	private String name;
	private String ID;
	private String method;
	private String target;
	private String action;
	public Form() {
		subelements = new ArrayList<BaseElement>();
	}
	@Override
	public void toString(StringBuilder a) {
		a.append("<form");
		 
		 
		if(target != null)
			a.append(" target=\"").append(target).append("\"");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		 
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		 
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		buildElements( a);
		a.append("</form>");
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
	

}
