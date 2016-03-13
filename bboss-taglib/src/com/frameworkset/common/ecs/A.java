package com.frameworkset.common.ecs;

public class A extends BaseElement{

	private String href;
	private String onClick;
	
	private String onMouseOver;
	private String target;
	private String title;
	private String name;
	private String ID;
	public A() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void toString(StringBuilder a) {
		a.append("<a");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		if(href != null)
			a.append(" href=\"").append(href).append("\"");
		if(onClick != null)
			a.append(" onClick=\"").append(onClick).append("\"");
		
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		if(onMouseOver != null)
			a.append(" onMouseOver=\"").append(onMouseOver).append("\"");
		if(target != null)
			a.append(" target=\"").append(target).append("\"");
		if(title != null)
			a.append(" title=\"").append(title).append("\"");
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText);
		 a.append("</a>");
//		return a.toString();
	}
 
	public String getHref() {
		return href;
	}
	public A setHref(String href) {
		this.href = href;
		return this;
	}
	 
	public String getOnMouseOver() {
		return onMouseOver;
	}
	public A setOnMouseOver(String mouseOverEvent) {
		this.onMouseOver = mouseOverEvent;
		return this;
	}
	public String getTarget() {
		return target;
	}
	public A setTarget(String target) {
		this.target = target;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public A setTitle(String title) {
		this.title = title;
		return this;
	}
	public String getOnClick() {
		return onClick;
	}
	public void setOnClick(String onClick) {
		this.onClick = onClick;
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
