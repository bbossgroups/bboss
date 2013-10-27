package com.frameworkset.common.tag.pager.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

/**
 * <p>Title: TextParamTag</p>
 *
 * <p>
 * Description: 上下翻页时，保存页面参数
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TextParamTag extends TextSupportTag {
    /**参数名称*/
    private String name;
    /**参数值*/
    private String value;
    /**参数属性名称*/
    private String attribute;
    /**request参数名称*/
    private String parameter;

    public static void main(String[] args) {
        TextParamTag textparamtag = new TextParamTag();
    }

    public String getAttribute() {
        return attribute;
    }

    public String getName() {
        return name;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int doEndTag() throws JspException
    {
    	HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
        if(value != null)
            pagerTag.addParam(name,value);
        else if(name != null)
        {
            value = request.getParameter(name);
            if(value != null)
                pagerTag.addParam(name,value);
        }
        if(value == null)
        {
            if(this.parameter != null)
                value = request.getParameter(parameter);
            if(value != null)
                pagerTag.addParam(name,value);
        }
        if(value == null)
        {
            if(this.attribute != null)
                value = (String)request.getAttribute(attribute);
            if(value != null)
                pagerTag.addParam(name,value);
        }
        this.clear_();
        return super.doEndTag();
    }
    
    public void clear_()
    {
    	this.attribute = null;
    	this.name = null;
    	this.parameter = null;
    	this.value = null;
    }
}
