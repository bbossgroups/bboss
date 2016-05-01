package com.frameworkset.common.ecs;

public class LI extends BaseElement {

	public LI() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<li");
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText);
		a.append("</li>");

	}

}
