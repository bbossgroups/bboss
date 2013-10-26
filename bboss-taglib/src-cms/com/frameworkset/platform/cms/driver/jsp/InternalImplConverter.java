package com.frameworkset.platform.cms.driver.jsp;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;

import com.frameworkset.common.tag.CommonRequest;


/**
 * Static class that provides utility methods to convert a generic
 * PortletRequest or PortletResponse object into an Internal respresentation
 * of the same object.
 * @author <a href="mailto:zheng@apache.org">ZHENG Zhong</a>
 * @author <a href="mailto:ddewolf@apache.org">David H. DeWolf</a>
 * @author <a href="mailto:yin-bp@163.com">biaoping.yin</a>
 */
public class InternalImplConverter implements java.io.Serializable {
	
	/**
	 * Private constructor that prevents external instantiation.
	 */
	private InternalImplConverter() {
		// Do nothing.
	}
	
	
	// Public Static Utility Methods -------------------------------------------

    /**
     * The scary part about this is that there is not yet a 
     * PortletRequestWrapper defined by the spec.  Because of this, there's a
     * chance someone might implement their own wrapper and we won't be able to
     * get the real internal one!
     * @param request the portlet request to be converted.
     * @return the internal request.
     */
    public static CMSServletRequest getInternalRequest(
    		ServletRequest request) {
        while (!(request instanceof CMSServletRequest)) {
        	if(request instanceof CommonRequest)
        	{
        		request = ((CommonRequest) request).getInternalrequest();
        	}
        	else
        	{
        		if(request instanceof HttpServletRequestWrapper)
        			
        		{
        			request = ((HttpServletRequestWrapper) request).getRequest();
        		}
        		else
        		{
        			return null;
        		}
//	            if (request == null) {
//	                throw new IllegalStateException( 
//	                		"The internal cms request cannot be found.");
//	            }
        	}
        }
        return (CMSServletRequest) request;
    }

    /**
     * The scary part about this is that there is not yet a
     * PortletRequestWrapper defined by the spec.  Because of this, there's a
     * chance someone might implement their own wrapper and we won't be able to
     * get the real internal one!
     * @param response the portlet response to be converted.
     * @return the internal response.
     */
    public static CMSServletResponse getInternalResponse(
    		ServletResponse response) {
        while (!(response instanceof CMSServletResponse)) {
        	if(response instanceof HttpServletResponseWrapper)
        	{
        		response = ((HttpServletResponseWrapper) response).getResponse();
        	}
        	else 
        	{
        		return null;
        	}
//            if (response == null) {
//                throw new IllegalStateException(
//                		"The internal cms response cannot be found.");
//            }
        }
        return (CMSServletResponse) response;
    }
    
}
