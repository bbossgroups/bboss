package com.frameworkset.common.ecs;

import java.util.ArrayList;

public class TD extends BaseElement{
	private Integer colSpan;
	private Integer rowSpan;
	private String align;
	private boolean noWrap;
	private Object width;
	protected boolean tdtype = true;
	public TD() {
		subelements = new ArrayList<BaseElement>();
		
	}
	protected String getTD()
	{
		return this.tdtype?"td":"th";
	}
	 
	public void toString(StringBuilder a) {
		a.append("<").append(getTD());
		
		if(width != null)
			a.append(" width=\"").append(width).append("\"");
		if(colSpan != null)
			a.append(" colspan=\"").append(colSpan).append("\"");
		if(noWrap)
			a.append(" nowrap ");
		if(rowSpan != null)
			a.append(" rowspan=\"").append(rowSpan).append("\"");
		
		if(align != null)
			a.append(" align=\"").append(align).append("\"");
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
			super.buildElements(a);
		a.append("</").append(getTD()).append(">");
//		return a.toString();
	}

	public Integer getColSpan() {
		return colSpan;
	}

	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}

	public Integer getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public boolean isTdtype() {
		return tdtype;
	}

	public void setTdtype(boolean tdtype) {
		this.tdtype = tdtype;
	}
	 
	public Object getWidth() {
		return width;
	}
	public void setWidth(Object width) {
		this.width = width;
	}
	public boolean isNoWrap() {
		return noWrap;
	}
	public void setNoWrap(boolean noWrap) {
		this.noWrap = noWrap;
	}
}
