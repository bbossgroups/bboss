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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;

/**
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class PagerRowCount extends PagerTagSupport
{
	
	public int doStartTag(){
		
		try
		{
			super.doStartTag();
			HttpServletRequest request = this.getHttpServletRequest();
			if(this.pagerContext == null)
			{
				String t_id = "pagerContext." ;
				
				if(request instanceof CMSServletRequest)
				{
					CMSServletRequest cmsRequest = (CMSServletRequest)request;
					t_id += cmsRequest.getContext().getID() ;
				}
				else if(this.id != null)
				{
					t_id += id;
				}
				else
					t_id += PagerContext.DEFAULT_ID;
				
				this.pagerContext = (PagerContext)request.getAttribute(t_id);
				
			}
			JspWriter out = this.getJspWriter();
			if(pagerContext != null && !pagerContext.ListMode())
				out.print(super.pagerContext.getItemCount());
			else
				out.print(super.pagerContext.getDataSize());
		}
		catch (JspException e1)
		{
			e1.printStackTrace();
		}			
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		
		return SKIP_BODY;
	}

	/**
	 *  Description:
	 * @return String
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		return null;
	}

	/**
	 *  Description:
	 * @param output
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output)
	{
		
	}

	

}
