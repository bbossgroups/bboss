package com.frameworkset.common.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.util.StringUtil;

public class ReferHelper {
	private String[] refererwallwhilelist;
	 private boolean refererDefender = false;
	public ReferHelper() {
		
	}

	 private boolean iswhilerefer(String referer)
	    {
	    	if(this.refererwallwhilelist == null || this.refererwallwhilelist.length == 0)
	    		return true;
	    	for(String whilereferername:this.refererwallwhilelist)
	    	{
	    		if(referer.startsWith(whilereferername))
	    			return true;
	    	}
	    	return false;
	    }
	 
	 public boolean dorefer(HttpServletRequest request, HttpServletResponse response) throws IOException
	 {
		 if(refererDefender)
	        {
		        /**
		         * 跨站点请求伪造。修复任务： 拒绝恶意请求。解决方案，过滤器中
		         * 
		         */
		        String referer = request.getHeader("Referer");   //REFRESH
//		        if(!iswhilerefer(referer))
		       
		        	
		        if(referer!=null){
		        	String basePath = null;
		        	String basePath80 = null;
		        	if(!request.getContextPath().equals("/"))
		        	{
		        		if(request.getServerPort() != 80)
		        		{
			        		basePath = request.getScheme() + "://" 
			        			+ request.getServerName() + ":" + request.getServerPort() 
			        			+ request.getContextPath() + "/";
		        		}
		        		else
		        		{
		        			basePath = request.getScheme() + "://" 
				        			+ request.getServerName() + ":" + request.getServerPort() 
				        			+ request.getContextPath() + "/";
		        			basePath80 = request.getScheme() + "://" 
				        			+ request.getServerName() + 
				        			request.getContextPath() + "/";
		        		}
		        	}
		        	else
		        	{
		        		if(request.getServerPort() != 80)
		        		{
			        		basePath = request.getScheme() + "://" 
			        	
			    	        			+ request.getServerName() + ":" + request.getServerPort()
			        	
			    	        			+ request.getContextPath();
		        		}
		        		else
		        		{
		        			basePath = request.getScheme() + "://" 
		        		        	
			    	        			+ request.getServerName() + ":" + request.getServerPort()
			        	
			    	        			+ request.getContextPath();
		        			basePath80 = request.getScheme() + "://" 
				        			+ request.getServerName() + 
				        			request.getContextPath() ;
		        		}
		        	}
		        	if(basePath80 == null)
		        	{
		        		if(referer.indexOf(basePath)<0)
		        		{
		        			if(this.iswhilerefer(referer))
		        			{
		//	        			String context = request.getContextPath();
		//	        			if(!context.equals("/"))
		//	        			{
		//		        			String uri = request.getRequestURI();
		//		        			uri = uri.substring(request.getContextPath().length());
		//		        			request.getRequestDispatcher(uri).forward(request, response);
		//	        			}
		//	        			else
		//	        			{
		//	        				request.getRequestDispatcher(context).forward(request, response);
		//	        			}
//		        				return;
		        				return false;
		        			}
		        			else
		        			{
		        				sendRedirect403(request,
		        						response) ;
		        				return true;
		        			}
		        		}
		        	}
		        	else
		        	{
		        		if(referer.indexOf(basePath)<0 && referer.indexOf(basePath80)<0)
		        		{
//		        			String context = request.getContextPath();
//		        			if(!context.equals("/"))
//		        			{
//			        			String uri = request.getRequestURI();
//			        			uri = uri.substring(request.getContextPath().length());
//			        			request.getRequestDispatcher(uri).forward(request, response);
//		        			}
//		        			else
//		        			{
//		        				request.getRequestDispatcher(context).forward(request, response);
//		        			}
//		        			return;
		        			if(this.iswhilerefer(referer))
		        			{
		        				return false;
		        			}
		        			else
		        			{
		        				sendRedirect403(request,
		        						response) ;
		        				return true;
		        			}
		        		}
		        	}
		        	
		        }  
	        }
		 return false;
	 }
	 
	  public void sendRedirect403(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
			if(!response.isCommitted())
			{
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			}
		}

	public String[] getRefererwallwhilelist() {
		return refererwallwhilelist;
	}

	public void setRefererwallwhilelist(String[] refererwallwhilelist) {
		this.refererwallwhilelist = refererwallwhilelist;
		if(StringUtil.isNotEmpty(this.refererwallwhilelist))
		{
			for(int i = 0;i < this.refererwallwhilelist.length; i ++)
			{
				this.refererwallwhilelist[i] = this.refererwallwhilelist[i].trim();
			}
		}
	}

	public boolean isRefererDefender() {
		return refererDefender;
	}

	public void setRefererDefender(boolean refererDefender) {
		this.refererDefender = refererDefender;
	}
	    
}
