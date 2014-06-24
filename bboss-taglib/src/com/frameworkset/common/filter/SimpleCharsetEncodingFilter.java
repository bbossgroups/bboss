package com.frameworkset.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.util.StringUtil;
/**
 * 不带session管理功能的字符过滤器
 * @author yinbp
 *
 */
public class SimpleCharsetEncodingFilter  implements Filter{
	private FilterConfig config = null;
    private String RequestEncoding = null;
    private String ResponseEncoding = null;
    private String mode = "0";
    private boolean checkiemodeldialog;
    private static String[] wallfilterrules;
    private String[] wallwhilelist;
    private boolean refererDefender = false;
    private String[] refererwallwhilelist;
    private static String[] wallfilterrules_default = new String[]{"<script","%3Cscript","script","<img","%3Cimg","alert(","alert%28","eval(","eval%28","style=","style%3D",
    	"javascript","update ","drop ","delete ","insert ","create ","select ","truncate "};
    
    
    public void init(FilterConfig arg0) throws ServletException {
    	
        this.config = arg0;
        this.RequestEncoding = config.getInitParameter("RequestEncoding");
        this.ResponseEncoding = config.getInitParameter("ResponseEncoding");
        String refererDefender_ =  config.getInitParameter("refererDefender");
        refererDefender = StringUtil.getBoolean(refererDefender_, false);
        String wallfilterrules_ = config.getInitParameter("wallfilterrules");
        String wallwhilelist_ = config.getInitParameter("wallwhilelist");
        
        String defaultwall = config.getInitParameter("defaultwall");
        if(wallwhilelist_ != null )
        {
        	wallwhilelist = wallwhilelist_.split(",");
        }
        if(wallfilterrules_ != null )
        {
        	wallfilterrules = wallfilterrules_.split(",");
        }
        else if(defaultwall != null && defaultwall.equals("true"))
        {
        	wallfilterrules = wallfilterrules_default;
        }
        String _checkiemodeldialog = config.getInitParameter("checkiemodeldialog");
        if(_checkiemodeldialog != null && _checkiemodeldialog.equals("true"))
        	this.checkiemodeldialog = true;
        mode = config.getInitParameter("mode");
        if(mode == null)
            mode = "0";
    }
    private boolean iswhilerefer(String referer)
    {
    	if(this.refererwallwhilelist == null || this.refererwallwhilelist.length == 0)
    		return true;
    	for(String whilereferername:this.refererwallwhilelist)
    	{
    		if(whilereferername.startsWith(referer))
    			return true;
    	}
    	return false;
    }
    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc)
        throws IOException, ServletException {

        if(this.config == null){
            return;
        }

        HttpServletRequest request = (HttpServletRequest)req;
        

        //是否允许过滤器，
        String filterEnabled = request.getParameter("filterEnabled");

        HttpServletResponse response = (HttpServletResponse)res;
//        response.setHeader("Cache-Control", "no-cache"); 
//        response.setHeader("Pragma", "no-cache"); 
//        response.setDateHeader("Expires", -1);  
//        response.setDateHeader("max-age", 0);
        /**
         *  向所有会话cookie 添加“HttpOnly”属性,  解决方案，过滤器中
         */
//        response.setHeader( "Set-Cookie", "name=value; HttpOnly");
        //response.setHeader( "Set-Cookie", "name=value;HttpOnly"); 
        if(refererDefender)
        {
	        /**
	         * 跨站点请求伪造。修复任务： 拒绝恶意请求。解决方案，过滤器中
	         * 
	         */
	        String referer = request.getHeader("Referer");   //REFRESH
//	        if(!iswhilerefer(referer))
	       
	        	
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
	        			String context = request.getContextPath();
	        			if(!context.equals("/"))
	        			{
		        			String uri = request.getRequestURI();
		        			uri = uri.substring(request.getContextPath().length());
		        			request.getRequestDispatcher(uri).forward(request, response);
	        			}
	        			else
	        			{
	        				request.getRequestDispatcher(context).forward(request, response);
	        			}
	        			return;
	        		}
	        	}
	        	else
	        	{
	        		if(referer.indexOf(basePath)<0 && referer.indexOf(basePath80)<0)
	        		{
	        			String context = request.getContextPath();
	        			if(!context.equals("/"))
	        			{
		        			String uri = request.getRequestURI();
		        			uri = uri.substring(request.getContextPath().length());
		        			request.getRequestDispatcher(uri).forward(request, response);
	        			}
	        			else
	        			{
	        				request.getRequestDispatcher(context).forward(request, response);
	        			}
	        			return;
	        		}
	        	}
	        	
	        }  
        }
        







//        response.set

        if(filterEnabled != null && !filterEnabled.trim().equalsIgnoreCase("true"))
        {
            fc.doFilter(request, response);
//        	  super.doFilter(request, response, fc);
            return;
        }
//        System.out.println("old request:" + request.getClass());
        //模式0：对请求参数编码，对响应编码
        //      服务器对url不进行编码
        if(mode.equals("0"))
        {

            CharacterEncodingHttpServletRequestWrapper mrequestw = new
                CharacterEncodingHttpServletRequestWrapper(request, RequestEncoding,checkiemodeldialog,wallfilterrules,wallwhilelist);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
//            super.doFilter(mrequestw, wresponsew, fc);
        }
        //模式1：对请求参数编码，对响应不编码
        //      服务器对url进行编码
        else if(mode.equals("1"))
        {
            request.setCharacterEncoding(RequestEncoding);
            fc.doFilter(request,response);
//            super.doFilter(request, response, fc);
        }
        //其他模式
        else
        {
            CharacterEncodingHttpServletRequestWrapper mrequestw = new
                CharacterEncodingHttpServletRequestWrapper(request, this.RequestEncoding,checkiemodeldialog,wallfilterrules,wallwhilelist);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
//            super.doFilter(mrequestw, wresponsew, fc);
        }
    }

    public void destroy() {
    }

}
