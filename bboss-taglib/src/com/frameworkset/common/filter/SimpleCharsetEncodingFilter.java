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

import org.frameworkset.util.ReferHelper;

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

    private ReferHelper referHelper;
   
  
    
    public void init(FilterConfig arg0) throws ServletException {
    	
        this.config = arg0;
        this.RequestEncoding = config.getInitParameter("RequestEncoding");
        this.ResponseEncoding = config.getInitParameter("ResponseEncoding");
        String refererDefender_ =  config.getInitParameter("refererDefender");
        boolean refererDefender = StringUtil.getBoolean(refererDefender_, false);
        referHelper = new ReferHelper();
        referHelper.setRefererDefender(refererDefender);
        String wallfilterrules_ = config.getInitParameter("wallfilterrules");
        String wallwhilelist_ = config.getInitParameter("wallwhilelist");
        String refererwallwhilelist_ = config.getInitParameter("refererwallwhilelist");
        String defaultwall = config.getInitParameter("defaultwall");
        if(StringUtil.isNotEmpty(wallwhilelist_ ))
        {
        	String[] wallwhilelist = wallwhilelist_.split(",");
        	referHelper.setWallwhilelist(wallwhilelist);
        }
        if(StringUtil.isNotEmpty(wallfilterrules_))
        {
        	String[] wallfilterrules = wallfilterrules_.split(",");
        	referHelper.setWallfilterrules(wallfilterrules);
        }
        else if(defaultwall != null && defaultwall.equals("true"))
        {
        	String[] wallfilterrules = ReferHelper.wallfilterrules_default;
        	referHelper.setWallfilterrules(wallfilterrules);
        }
        
        
        
        if(StringUtil.isNotEmpty(refererwallwhilelist_))
        {
        	String[] refererwallwhilelist = refererwallwhilelist_.split(",");
        	referHelper.setRefererwallwhilelist(refererwallwhilelist);
        }
        String _checkiemodeldialog = config.getInitParameter("checkiemodeldialog");
        if(_checkiemodeldialog != null && _checkiemodeldialog.equals("true"))
        	this.checkiemodeldialog = true;
        mode = config.getInitParameter("mode");
        if(mode == null)
            mode = "0";
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
        if(referHelper.dorefer(request, response))
        {
        	return;
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
                CharacterEncodingHttpServletRequestWrapper(request, RequestEncoding,checkiemodeldialog,referHelper);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
//            super.doFilter(mrequestw, wresponsew, fc);
        }
        //模式1：对请求参数编码，对响应不编码
        //      服务器对url进行编码
        else if(mode.equals("1"))
        {
        	 CharacterEncodingHttpServletRequestWrapper mrequestw = new
                     CharacterEncodingHttpServletRequestWrapper(request, RequestEncoding,checkiemodeldialog,referHelper);
            fc.doFilter(request,response);
//            super.doFilter(request, response, fc);
        }
        //其他模式
        else
        {
            CharacterEncodingHttpServletRequestWrapper mrequestw = new
                CharacterEncodingHttpServletRequestWrapper(request, this.RequestEncoding,checkiemodeldialog,referHelper);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
//            super.doFilter(mrequestw, wresponsew, fc);
        }
    }

    public void destroy() {
    }

}
