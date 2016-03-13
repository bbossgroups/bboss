package com.frameworkset.common.ecs;

public class IFrame extends BaseElement{
	private String src;
	private String width;
	private String height;
	private boolean frameBorder;
	private String scrolling;
	private String name;
	private String align;
	private String ID;
	private String longDesc;
	private String marginHeight;
	private String marginWidth;
	private Object tagPosition;
	private String title;
	
	
	public IFrame() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void toString(StringBuilder a) {
		a.append("<iframe");
		
		if(title != null)
			a.append(" title=\"").append(title).append("\"");
		if(longDesc != null)
			a.append(" longdesc=\"").append(longDesc).append("\"");
		if(tagPosition != null)
			a.append(" tabindex=\"").append(tagPosition).append("\"");
		if(marginHeight != null)
			a.append(" marginheight=\"").append(marginHeight).append("\"");
		if(marginWidth != null)
			a.append(" marginwidth=\"").append(marginWidth).append("\"");
		if(name != null)
			a.append(" name=\"").append(name).append("\"");
		if(ID != null)
			a.append(" id=\"").append(ID).append("\"");
		if(align != null)
			a.append(" align=\"").append(align).append("\"");
		if(src != null)
			a.append(" src=\"").append(src).append("\"");
		if(style != null)
			a.append(" style=\"").append(style).append("\"");
		if(clazz != null)
			a.append(" class=\"").append(clazz).append("\"");
		 
		if(width != null)
			a.append(" width=\"").append(width).append("\"");
		if(height != null)
			a.append(" height=\"").append(height).append("\"");
		if(frameBorder) 
			a.append(" frameborder=\"1\"");
		else
			a.append(" frameborder=\"0\"");
		if(scrolling != null) 
			a.append(" scrolling=\"").append(scrolling).append("\"");
		  
		if(this.extend != null)
			a.append(" ").append(extend);
		a.append("></iframe>");
		
		
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
 
	public String getScrolling() {
		return scrolling;
	}
	public void setScrolling(String scrolling) {
		this.scrolling = scrolling;
	}
	public boolean isFrameBorder() {
		return frameBorder;
	}
	public void setFrameBorder(boolean frameBorder) {
		this.frameBorder = frameBorder;
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
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}
	public String getMarginHeight() {
		return marginHeight;
	}
	public void setMarginHeight(String marginHeight) {
		this.marginHeight = marginHeight;
	}
	public String getMarginWidth() {
		return marginWidth;
	}
	public void setMarginWidth(String marginWidth) {
		this.marginWidth = marginWidth;
	}
	public Object getTagPosition() {
		return tagPosition;
	}
	public void setTagPosition(Object tagPosition) {
		this.tagPosition = tagPosition;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
