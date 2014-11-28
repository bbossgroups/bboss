/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.common.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

/**
 * 一个从javax.servlet.jsp.tagext.BodyTagSupport继承、实现TagOutput接口的抽象类。
 * 子类必须实现抽象方法writer(OutputStream),客户程序调用write方法将tag的内容输出到
 * outputstream.
 * 子类必须实现抽象方法generateContent(),提供各自产生输出内容的机制
 * @author biaoping.yin
 */
public abstract class BaseBodyTag extends BodyTagSupport  implements TryCatchFinally
{
	protected transient HttpServletRequest request = null;
	protected transient JspWriter out =  null;
	protected transient HttpSession session = null;
	protected transient HttpServletResponse response = null;

    public BaseBodyTag()
    {

    }

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
    
    public void release()
    {
    	request = null; 
		out 	= null;
		session = null;	
		response= null;
    	super.release();
    }

	public int doStartTag() throws JspException
	{
//		try
//		{
//			//pageContext.getOut().print(this.generateContent());
//		}
//		catch(IOException e)
//		{
//			e.printStackTrace();
//			throw new JspException(e.getMessage());
//		}
		return EVAL_BODY_BUFFERED;
	}
	
	public int doEndTag() throws JspException
	{
		this.request = null;
		this.response = null;
		this.session = null;
		this.out = null;
		return super.doEndTag();
	}
//    /**
//     * @return java.lang.String
//     */
//    public String generateContent(){return null;}

	public void doCatch(Throwable arg0) throws Throwable {
		throw arg0;
		
	}

	public void doFinally() {
		// TODO Auto-generated method stub
		
	}

    /**
     * @param output
     */
    //public  void write(OutputStream output){}
}
