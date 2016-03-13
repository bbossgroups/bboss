package com.frameworkset.common.ecs;

public class Meta  extends BaseElement{
	private String name;
	private String content;
	public Meta() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<meta");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(content != null)
			a.append(" content=\"").append(content).append("\"");
		
		a.append("/>");
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
