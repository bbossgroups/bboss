package com.frameworkset.common.ecs;

import java.util.List;

public abstract class BaseElement {
	protected String extend;
	protected String clazz;
	protected String style;
	protected String tagText;
	protected List<BaseElement> subelements;
	 
	 
	public BaseElement addElement(BaseElement option)
	{
		this.subelements.add(option);
		return this;
	}
	protected void buildElements(StringBuilder a)
	{
		for(BaseElement e:subelements)
		{
			e.toString(a);
		}
	}
	public BaseElement() {
		// TODO Auto-generated constructor stub
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	 
	public BaseElement setClass(String clazz) {
		this.clazz = clazz;
		return this;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	@Override
	public String toString() {
		StringBuilder a = new StringBuilder();

		toString(a);
		return a.toString();
	}
	public abstract void toString(StringBuilder a);
	public String getTagText() {
		return tagText;
	}
	public BaseElement setTagText(String tagText) {
		this.tagText = tagText;
		return this;
	}

}
