package com.frameworkset.common.ecs;

public class Span  extends BaseElement{
	private String clazz;
	public Span() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<span");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
//		if(style != null)
//			a.append(" style=\"").append(style).append("\"");
//		if(clazz != null)
//			a.append(" clazz=\"").append(clazz).append("\"");
//		if(onMouseOver != null)
//			a.append(" onMouseOver=\"").append(onMouseOver).append("\"");
//		if(target != null)
//			a.append(" target=\"").append(target).append("\"");
//		if(title != null)
//			a.append(" title=\"").append(title).append("\"");
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText) ;
		a.append("</span>");
		
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

}
