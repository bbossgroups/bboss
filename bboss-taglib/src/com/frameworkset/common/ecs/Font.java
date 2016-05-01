package com.frameworkset.common.ecs;

public class Font extends BaseElement{
	private String color;
	public Font() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toString(StringBuilder a) {
		a.append("<font");
		if(color != null)
			a.append(" color=\"").append(color).append("\"");
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
//		if(this.extend != null)
//			a.append(" ").append(extend);
		a.append(">");
		if(tagText != null)
			a.append(tagText) ;
		a.append("</font>");
		
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
