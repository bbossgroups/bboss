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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * 
 * className: 
 * @author biaoping.yin
 * version 1.0
 * createDate 2004-06-29
 */
public class QueryStringTag  extends PagerTagSupport
{
	private String encode ;	
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

	public int doStartTag() throws JspException
	{				 
		super.doStartTag();
		try
		{
			JspWriter out = this.getJspWriter();
			String queryString = pagerContext.getQueryString(pagerContext.getOffset(),pagerContext.getSortKey(),pagerContext.getDesc());		
			if(this.getEncode() != null && this.getEncode().trim().equals("true"))
			{			    
				out.print(URLEncoder.encode(queryString));
			}
			else
				out.print(queryString);
		}
		catch (IOException e)
		{			
			e.printStackTrace();
		}

		return  SKIP_BODY;
	}
	
	
	/**
	 * Method <br> getEncode<br>
	 * Return <br> String<br>
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * Method <br> setEncode<br>
	 * Return <br> void<br>
	 */
	public void setEncode(String string) {
		encode = string;
	}
	
	public int doEndTag()throws JspException 
	{
		
		this.encode = null;
		
		return super.doEndTag();
	}

}
