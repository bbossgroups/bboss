package com.frameworkset.common.ecs;

import java.util.ArrayList;

public class TR extends BaseElement{
	public TR() {
		subelements = new ArrayList<BaseElement>();
	}

	public void toString(StringBuilder a) {
		a.append("<tr");
		 
		 
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\""); 
		 
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText);
		else
		{
			buildElements(  a);
		}
		a.append("</tr>");
//		return a.toString();
	}
	 
}
