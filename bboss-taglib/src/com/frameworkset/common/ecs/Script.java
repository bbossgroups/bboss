package com.frameworkset.common.ecs;

public class Script extends BaseElement{
	private String src;
	 
	private String type;
	private String language;
	
	public Script() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<script");
		if(src != null)
			a.append(" src=\"").append(src).append("\"");
		if(language != null)
			a.append(" language=\"").append(language).append("\"");
		else
			a.append(" language=\"JavaScript\"");
		if(type != null)
			a.append(" type=\"").append(type).append("\"");		 
	     a.append("></script>");
		
	}

	public String getType() {
		return type;
	}

	public Script setType(String type) {
		this.type = type;
		return this;
	}

	public String getSrc() {
		return src;
	}

	public Script setSrc(String src) {
		this.src = src;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public Script setLanguage(String language) {
		this.language = language;
		return this;
	}

}
