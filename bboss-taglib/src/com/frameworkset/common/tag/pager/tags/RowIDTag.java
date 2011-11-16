/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

/**
 * extends BaseTag to extends PagerTagSupport
 * @author biaoping.yin
 * 2004-6-28
 */
public class RowIDTag extends PagerTagSupport
{
	protected String index;
	protected String increament = null;
	protected boolean offset = true;
	protected int index()
	{
		return index == null ? 0 : Integer.parseInt(index);
	}

	protected PagerDataSet searchDataSet(Tag obj, Class clazz)
	{
		
		PagerDataSet dataSet = null;
		if (this.getIndex() == null)
		{
			dataSet = (PagerDataSet) findAncestorWithClass(obj, clazz);
		}
		else
		{
			HttpServletRequest request = this.getHttpServletRequest();
			int idx = index();
			java.util.Stack stack =
				(java.util.Stack) request.getAttribute(PagerDataSet.PAGERDATASET_STACK);
			dataSet = (PagerDataSet) stack.elementAt(idx);
		}
		return dataSet;
	}

	public int doStartTag() throws JspException
	{
		super.doStartTag();
		PagerDataSet parent =  searchDataSet(this, PagerDataSet.class);
//		PagerTag pagerTag = (PagerTag) findAncestorWithClass(this, PagerTag.class);
		try
		{
			this.getJspWriter().print(parent.getOuterRowid(this.isOffset(),this.increament()));
//			if(!isOffset())
//				out.print(parent.getRowid() + increament());
//			else
//				out.print(pagerContext.getOffset() + parent.getRowid() + increament());
		} 
		catch (Exception e)
		{
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output)
	{
	}
	/**
	 * @return String
	 */
	public String getIncreament()
	{
		return increament;
	}

	/**
	 * @return int
	 */
	public int increament()
	{
		return increament == null ? 0 : Integer.parseInt(increament);
	}

	/**
	 * @param i
	 */
	public void setIncreament(String i)
	{
		increament = i;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public String getIndex()
	{
		return index;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setIndex(String string)
	{
		index = string;
	}

	public boolean isOffset() {
		return offset;
	}

	public void setOffset(boolean offset) {
		this.offset = offset;
	}
	
	public int doEndTag()throws JspException 
	{
		
		this.increament = null;
		this.index = null;
		this.offset = true;
		
		return super.doEndTag();
	}

}
