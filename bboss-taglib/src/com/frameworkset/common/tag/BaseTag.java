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
 *  Author of Learning Java 						     *
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.common.tag;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseSPIManager;

import com.frameworkset.common.tag.contextmenu.ContextMenu;


/**
 * 一个从javax.servlet.jsp.tagext.TagSupport继承、实现TagOutput接口的抽象类。
 * 子类必须实现抽象方法writer(OutputStream),客户程序调用write方法将tag的内容输出到
 * outputstream.
 * 子类必须实现抽象方法generateContent(),提供各自产生输出内容的机制
 * @author biaoping.yin
 */
public abstract class BaseTag extends TagSupport implements TryCatchFinally
{
	public void doCatch(Throwable arg0) throws Throwable {
		
		throw arg0;
		
	}

	public void doFinally() {
		// TODO Auto-generated method stub
		
	}

	private final static Logger log = Logger.getLogger(BaseTag.class);
	protected transient HttpServletRequest request = null;
								//(HttpServletRequest) pageContext.getRequest();
	protected transient JspWriter out =  null;//pageContext.getOut();
	protected transient HttpSession session = null;
	protected transient HttpServletResponse response = null;
	protected boolean enablecontextmenu = false;
	
	public static boolean ENABLE_TAG_SECURITY = BaseSPIManager.getBooleanProperty("ENABLE_TAG_SECURITY",false);
	
	public boolean isEnablecontextmenu()
	{
		return enablecontextmenu;
	}
	
	public void setEnablecontextmenu(boolean enablecontextmenu) {
		this.enablecontextmenu = enablecontextmenu;
	}
	
	/**
	 * 初始化右键菜单信息
	 *
	 */
	public void initContextMenu(){
		
	}
	
	public ContextMenu getContextMenu()
	{
		return null;
	}
    
   /**
    * 初始化request,out对象
    *  JetspeedRunData   data = (JetspeedRunData)pageContext.getAttribute(JspService.RUNDATA, PageContext.REQUEST_SCOPE);

    */
  
    public void setPageContext(PageContext pageContext)
    {
    	super.setPageContext(pageContext);
    	if(pageContext != null)
    	{
//			HttpServletRequest temp = (HttpServletRequest) pageContext.getRequest(); 
//			request = new CommonRequest((RunData)temp.getAttribute(JspService.RUNDATA),temp);
    		request = (HttpServletRequest) pageContext.getRequest(); 
			out 	= pageContext.getOut();
			session = request.getSession(false);	
			response= (HttpServletResponse)pageContext.getResponse();		
    	}		
    }
    
    public HttpServletRequest getHttpServletRequest()
    {
		if(request != null)
		{
			return request;
		}
		else
		{
			return (HttpServletRequest) pageContext.getRequest();
		}
    }
    
    public HttpServletResponse getHttpServletResponse()
    {
    	if(this.response != null)
		{
			return this.response;
		}
		else
		{
			return (HttpServletResponse) pageContext.getResponse();
		}
    	
    }
    
    public JspWriter getJspWriter()
    {
    	if(this.out != null)
		{
			return this.out;
		}
		else
		{
			return (JspWriter) pageContext.getOut();
		}
    	
    }
    
    public HttpSession getSession()
    {
    	if(this.session != null)
    		return session;
    	return getHttpServletRequest().getSession(false);
    }
    
    public int doEndTag() throws JspException
	{
		this.request = null;
		this.response = null;
		this.session = null;
		this.out = null;
		enablecontextmenu = false;
		return super.doEndTag();
	}
    
    public void release()
    {
    	request = null; 
		out 	= null;
		session = null;	
		response= null;
    	super.release();
    }
    
    public BaseTag() 
    {
		   		 
    }
    
    public static void main(String[] args)
    {
        boolean _ENABLE_TAG_SECURITY = BaseSPIManager.getBooleanProperty("ENABLE_TAG_SECURITY",false);
        System.out.println(_ENABLE_TAG_SECURITY);
    }
    
    
    
    
    
    
//	public int doStartTag() //throws JspException
//	{
////		try
////		{
////			//out.print(this.generateContent());
////		}
////		catch(IOException e)
////		{
////			//throw new JspException(e.getMessage());
////		}
//		return this.EVAL_BODY_INCLUDE;
//	}
    
    /**
     * @return java.lang.String
     */
    public String generateContent()
	{
		return null;
	}
    
    /**
     * @param output
     */
    public void write(OutputStream output)
    {    	
    }
}
