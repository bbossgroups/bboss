package com.frameworkset.common.ecs;

public class Option extends BaseElement{
	private String value;
	private boolean selected;
	
	public Option() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<option");
		if(value != null)
			a.append(" value=\"").append(value).append("\"");
		if(selected )
			a.append(" selected ");
		a.append(">");
		if(tagText != null)
			a.append(tagText);
		a.append("</option>");
		
	}

	public String getValue() {
		return value;
	}

	public Option setValue(String value) {
		this.value = value;
		return this;
	}

	public boolean isSelected() {
		return selected;
	}

	public Option setSelected(boolean selected) {
		this.selected = selected;return this;
	}

}
