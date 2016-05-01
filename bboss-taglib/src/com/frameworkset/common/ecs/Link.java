package com.frameworkset.common.ecs;

public class Link extends BaseElement{
	private String href;
	private String rel;
	private String type;
	public Link() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<link");
		if(href != null)
			a.append(" href=\"").append(href).append("\"");
		if(rel != null)
			a.append(" rel=\"").append(rel).append("\"");
		if(type != null)
			a.append(" type=\"").append(type).append("\"");		 
	     a.append("/>");
		
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
