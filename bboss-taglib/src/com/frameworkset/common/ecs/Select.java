package com.frameworkset.common.ecs;

import java.util.ArrayList;

public class Select extends BaseElement{
	 
	private String name;
	private String ID;
	private String onChange;
	private boolean disabled; 
	private boolean multiple;
	private Object size;
	public Select() {
		subelements = new ArrayList<BaseElement>();
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

	 

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<select");
	 
		if(disabled )
			a.append(" disabled ");
		if(multiple )
			a.append(" multiple ");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(onChange != null)
			a.append(" onChange=\"").append(onChange).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		if(size != null)
			a.append(" size=\"").append(size).append("\"");
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		buildElements( a);
		a.append("</select>");
	}



	public Object getSize() {
		return size;
	}



	public void setSize(Object size) {
		this.size = size;
	}



	public String getOnChange() {
		return onChange;
	}



	public void setOnChange(String onChange) {
		this.onChange = onChange;
	}
	
	 

}
