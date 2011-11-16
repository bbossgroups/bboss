package com.frameworkset.common.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class CharacterEncodingHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private String newecoding = null;

    public CharacterEncodingHttpServletResponseWrapper(HttpServletResponse response,String encoding) {
        super(response);
        this.newecoding = encoding;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletResponse#setContentType(java.lang.String)
     */
    public void setContentType(String value) {
//        super.setContentType("text/html;charset="+ecoding);
        super.setContentType(value);
    }

}
