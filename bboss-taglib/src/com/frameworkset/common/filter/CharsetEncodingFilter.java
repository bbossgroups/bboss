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

/**
 *
 * 配置示例：
 * <filter>
    	<filter-name>CharsetEncoding</filter-name>
    	<filter-class>com.frameworkset.platform.oa.meeting.util.CharsetEncodingFilter</filter-class>
    	<init-param>
      		<param-name>RequestEncoding</param-name>
      		<param-value>iso-8859-1</param-value>
    	</init-param>
    	<init-param>
      		<param-name>ResponseEncoding</param-name>
      		<param-value>GBK</param-value>
    	</init-param>
  	</filter>

  	<filter-mapping>
  		<filter-name>CharsetEncoding</filter-name>
  		<url-pattern>/*</url-pattern>
  	</filter-mapping>
 * @author biaoping.yin
 * created on 2005-6-14
 * version 1.0
 */

public class CharsetEncodingFilter implements Filter {
    private FilterConfig config = null;
    private String RequestEncoding = null;
    private String ResponseEncoding = null;
    private String mode = "0";
    private boolean checkiemodeldialog;
    
    public void init(FilterConfig arg0) throws ServletException {
        this.config = arg0;
        this.RequestEncoding = config.getInitParameter("RequestEncoding");
        this.ResponseEncoding = config.getInitParameter("ResponseEncoding");
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
        response.setHeader("Cache-Control", "no-cache"); 
        response.setHeader("Pragma", "no-cache"); 
        response.setDateHeader("Expires", -1);  
        response.setDateHeader("max-age", 0); 
//        response.set

        if(filterEnabled != null && !filterEnabled.trim().equalsIgnoreCase("true"))
        {
            fc.doFilter(request, response);
            return;
        }
//        System.out.println("old request:" + request.getClass());
        //模式0：对请求参数编码，对响应编码
        //      服务器对url不进行编码
        if(mode.equals("0"))
        {

            CharacterEncodingHttpServletRequestWrapper mrequestw = new
                CharacterEncodingHttpServletRequestWrapper(request, RequestEncoding,checkiemodeldialog);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
        }
        //模式1：对请求参数编码，对响应不编码
        //      服务器对url进行编码
        else if(mode.equals("1"))
        {
            request.setCharacterEncoding(RequestEncoding);
            fc.doFilter(request,response);
        }
        //其他模式
        else
        {
            CharacterEncodingHttpServletRequestWrapper mrequestw = new
                CharacterEncodingHttpServletRequestWrapper(request, this.RequestEncoding,checkiemodeldialog);
            CharacterEncodingHttpServletResponseWrapper wresponsew = new
                CharacterEncodingHttpServletResponseWrapper(response, ResponseEncoding);
            fc.doFilter(mrequestw, wresponsew);
        }
    }

    public void destroy() {
    }

}
