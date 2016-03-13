package com.frameworkset.common.ecs;

import java.util.ArrayList;

public class Table extends BaseElement{
	private Object border;
	private String bgColor;
	private String name;
	private String ID;

	 
	public Table() {
		// TODO Auto-generated constructor stub
		subelements = new ArrayList<BaseElement>();
	}
	 
	 
	@Override
	public void toString(StringBuilder a) {
		a.append("<table");
		 
	 
		 
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		 
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		if(bgColor != null)
			a.append(" bgcolor=\"").append(bgColor).append("\"");
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		buildElements( a);
		a.append("</table>");
		
	}
	
	public Object getBorder() {
		return border;
	}
	public void setBorder(Object border) {
		this.border = border;
	}
	public String getBgColor() {
		return bgColor;
	}
	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
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

}
