/*
 * Created on 2004-4-30
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.TR;

import com.frameworkset.common.tag.BaseTag;
/**
 * @author biaoping.yin
 * 显示表头的tag
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HeaderTag extends BaseTag
{
	String fields = null;
	String widths = null;	
	private String align = null;
	private String bgcolor = null;
	private String onClick = null;
	private String onDblClick = null;
	private String onMouseDown = null;
	private String onMouseUp = null;
	private String onMouseOver = null;
	
	private String onMouseMove = null;
	
	private String onMouseOut = null;
	
	private String onKeyPress = null;
	
	private String onKeyDown= null;
	
	private String onKeyUp= null;
	
	private String valign= null;
	private String extend = null;
	TR tr = null;
	PagerDataSet listTag = null;
	
	
	
	/**
	 * <tr align="center" class="th" > 
                  <td height="19" align="left" valign="bottom" bgcolor="#8da6c4"><img src="../../../images/t4.gif" width="17" height="17"></td>

                  <td bgcolor="#8da6c4">物资编码</td>
                  <td bgcolor="#8da6c4">物资名称</td>
                  <td bgcolor="#8da6c4">规格</td>
                  <td bgcolor="#8da6c4">型号</td>
                  <td bgcolor="#8da6c4">计量单位</td>
                </tr>

	 */
	
	public int doStartTag() throws JspException
	{	
		listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
		if(listTag == null || listTag.getRowid() == 0)
			/**
			当header标签出现在list标签中时，或者在pager标签中
			进行以下处理。如果当存在pager标签，并且将header也放在list标签中，这种情况是非法的
			但是程序没有做相应的判断和处理需要程序员自己处理。
			*/
		{
			tr = new TR();
			if(getAlign() != null)
				tr.setAlign(this.getAlign());
			if(bgcolor != null)
				tr.setBgColor(bgcolor);
			if(onClick != null)
				tr.setOnClick(onClick);			
			if(onDblClick != null)
				tr.setOnDblClick(this.onDblClick);
			if(onKeyDown != null)
				tr.setOnKeyDown(this.onKeyDown);
			if(onKeyPress != null)
				tr.setOnKeyPress(this.onKeyPress);
			if(onKeyUp != null)
				tr.setOnKeyUp(this.onKeyUp);
			if(onMouseDown != null)
				tr.setOnMouseDown(this.onMouseDown);
			if(onMouseMove != null)
				tr.setOnMouseMove(this.onMouseMove);
			if(onMouseOut != null)
				tr.setOnMouseOut(this.onMouseOut);
			if(onMouseOver != null)
				tr.setOnMouseOver(this.onMouseOver);
			if(onMouseUp != null)
				tr.setOnMouseUp(this.onMouseUp);
			if(valign != null)
				tr.setVAlign(this.valign);
			if(this.extend != null)
			{
				tr.setExtend(extend);
			}
			try
			{
				this.getJspWriter().print(tr.createStartTag());
			}
			catch(Exception e)
			{
				throw new JspException(e.getMessage());
			}
			return EVAL_BODY_INCLUDE;
		}
		else
		{
			return SKIP_BODY;
		}
		
		
		
//		tr.setClass("th");
		
		
		
	}
	public void clearR()
	{
		 fields = null;
		 widths = null;	
		 align = null;
		 bgcolor = null;
		 onClick = null;
		 onDblClick = null;
		 onMouseDown = null;
		 onMouseUp = null;
		 onMouseOver = null;
		
		 onMouseMove = null;
		
		 onMouseOut = null;
		
		 onKeyPress = null;
		
		 onKeyDown= null;
		
		 onKeyUp= null;
		
		 valign= null;
		 tr = null;
		 listTag = null;
	}
	
	public int doEndTag() throws JspException
	{
		if(listTag == null || listTag.getRowid() == 0)
		{
			try
			{
				this.getJspWriter().print(tr.createEndTag());
			}
			catch(Exception e)
			{
				clearR();
				throw new JspException(e.getMessage());
			}
		}
		clearR();
		
		return super.doEndTag();
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub
		
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getBgcolor() {
		return bgcolor;
	}

	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}

	public String getOnClick() {
		return onClick;
	}

	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	public String getOnDblClick() {
		return onDblClick;
	}

	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	public String getOnMouseDown() {
		return onMouseDown;
	}

	public void setOnMouseDown(String onMouseDown) {
		this.onMouseDown = onMouseDown;
	}

	public String getOnMouseUp() {
		return onMouseUp;
	}

	public void setOnMouseUp(String onMouseUp) {
		this.onMouseUp = onMouseUp;
	}

	public String getOnMouseOver() {
		return onMouseOver;
	}

	public void setOnMouseOver(String onMouseOver) {
		this.onMouseOver = onMouseOver;
	}

	public String getOnMouseMove() {
		return onMouseMove;
	}

	public void setOnMouseMove(String onMouseMove) {
		this.onMouseMove = onMouseMove;
	}

	public String getOnMouseOut() {
		return onMouseOut;
	}

	public void setOnMouseOut(String onMouseOut) {
		this.onMouseOut = onMouseOut;
	}

	public String getOnKeyPress() {
		return onKeyPress;
	}

	public void setOnKeyPress(String onKeyPress) {
		this.onKeyPress = onKeyPress;
	}

	public String getOnKeyDown() {
		return onKeyDown;
	}

	public void setOnKeyDown(String onKeyDown) {
		this.onKeyDown = onKeyDown;
	}

	public String getOnKeyUp() {
		return onKeyUp;
	}

	public void setOnKeyUp(String onKeyUp) {
		this.onKeyUp = onKeyUp;
	}

	public String getValign() {
		return valign;
	}

	public void setValign(String valign) {
		this.valign = valign;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}

	
	
	

}
