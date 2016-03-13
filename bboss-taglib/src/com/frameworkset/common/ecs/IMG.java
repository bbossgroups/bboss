package com.frameworkset.common.ecs;

public class IMG extends BaseElement{
	private String src;
	private String onmousewheel;
	private Object width;
	private Object height;
	private int border;
	private String onClick;
	private String alt;
//	private String extend;
	private String title;
	private String onDblClick;
	private String align;
	private String onMouseOver;
	private String onMouseOut;
	private String name;
	private String ID;
	public IMG() {
		// TODO Auto-generated constructor stub
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	@Override
	public void toString(StringBuilder a) {
		a.append("<img");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		if(src != null)
			a.append(" src=\"").append(src).append("\"");
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(title != null)
			a.append(" title=\"").append(title).append("\"");
		
		
		if(onMouseOver != null)
			a.append(" onMouseOver=\"").append(onMouseOver).append("\"");
		if(onMouseOut != null)
			a.append(" onMouseOut=\"").append(onMouseOut).append("\"");
		
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		if(onmousewheel != null)
			a.append(" onmousewheel=\"").append(onmousewheel).append("\"");
		if(alt != null)
			a.append(" alt=\"").append(alt).append("\"");
		if(width != null)
			a.append(" width=\"").append(width).append("\"");
		if(align != null)
			a.append(" align=\"").append(align).append("\"");
		if(height != null)
			a.append(" height=\"").append(height).append("\"");
		if(onClick != null)
			a.append(" onClick=\"").append(onClick).append("\"");
		if(onDblClick != null)
			a.append(" onDblClick=\"").append(onDblClick).append("\"");
		a.append(" border=\"").append(border).append("\"");
		 
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append(">");
		
		
	}
	public String getOnmousewheel() {
		return onmousewheel;
	}
	public void setOnmousewheel(String onmousewheel) {
		this.onmousewheel = onmousewheel;
	}
	 
	public void setBorder(int border) {
		this.border = border;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getOnClick() {
		return onClick;
	}
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}
	public int getBorder() {
		return border;
	}
	 
	public Object getWidth() {
		return width;
	}
	public void setWidth(Object width) {
		this.width = width;
	}
	public Object getHeight() {
		return height;
	}
	public void setHeight(Object height) {
		this.height = height;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOnDblClick() {
		return onDblClick;
	}
	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getOnMouseOver() {
		return onMouseOver;
	}
	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}
	public String getOnMouseOut() {
		return onMouseOut;
	}
	public void setOnMouseOut(String onMouseOut) {
		this.onMouseOut = onMouseOut;
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
	 
	
	 
}
