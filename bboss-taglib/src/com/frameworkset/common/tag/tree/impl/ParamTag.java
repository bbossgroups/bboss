/*****************************************************************************
 *                                                                           *
 *  This file is part of the frameworkset distribution.                      *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping.yin. Portions created by biaoping.yin are Copyright     *
 *  (C) 2004.  All Rights Reserved.                                          *
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
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.tree.impl;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;

/**
 * 记录树的一些参数
 * @author biaoping.yin
 * created on 2005-7-8
 * version 1.0
 */
public class ParamTag extends BaseTag {
    String name;
    String scope;
    String value;
    boolean encode = true;
    private String defaultValue = null;
    public int doEndTag() throws JspException
    {
    	this.name = null;
    	this.scope = null;
    	this.value = null;
    	this.encode = true;
    	return super.doEndTag();
    }
    /**
     * Description:
     * @return defaultValue

     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Description:defaultValue
     * @return void

     */
    public void setDefaultValue(String string) {
        defaultValue = string;
    }


    public void setEncode(boolean encode)
    {
        this.encode = encode;
    }

    public boolean getEncode()
    {
        return this.encode;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the scope.
     */
    public String getScope() {
        return scope;
    }
    /**
     * @param scope The scope to set.
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
    private void addParam(TreeTag parent,String name,String value)
    {
        parent.addParam(name,value);
    }
    public int doStartTag()
    {
        TreeTag parent = (TreeTag) this.getParent();
        if(scope == null || scope.equals("request"))
        {
            if(value == null)
            {
	            String[] values = this.getHttpServletRequest().getParameterValues(name);
	            if(values != null)
                {
                    for(int i = 0; i < values.length; i ++)
                    {
                    	if(values[i] != null)
                    	{
	                        if(this.encode)
	                        {
	                            this.addParam(parent,name,java.net.URLEncoder.encode(values[i]));
	                        }
	                        else
	                            this.addParam(parent,name,values[i]);
                    	}
                    }
                }
                else if(this.defaultValue != null)
                {
                    if(this.encode)
                    {
                            this.addParam(parent,name,java.net.URLEncoder.encode(defaultValue));
                    }
                    else
                        this.addParam(parent,name,defaultValue);
                }
	        }
            else
            {
                if(encode)
                    addParam(parent,name,java.net.URLEncoder.encode(value));
                else
                    addParam(parent,name,value);
            }
        }
        else if(this.getSession() != null && scope.equals("session"))
        {
            String value = (String)getSession().getAttribute(name);
            if(value != null )
            {
                if(encode)
                    addParam(parent, name, java.net.URLEncoder.encode(value));
                else
                    addParam(parent, name, value);
            }
            else if(this.defaultValue != null)
            {
                if(this.encode)
                        this.addParam(parent,name,java.net.URLEncoder.encode(defaultValue));
                else
                    this.addParam(parent,name,defaultValue);
            }

        }
        else if(scope.equals("pageContext"))
        {
            String value = (String)pageContext.getAttribute(name);
            if(value != null )
            {
                if(encode)
                    addParam(parent, name, java.net.URLEncoder.encode(value));
                else
                    addParam(parent, name, value);
            }
            else if(this.defaultValue != null)
            {
                if(this.encode)
                        this.addParam(parent,name,java.net.URLEncoder.encode(defaultValue));
                else
                    this.addParam(parent,name,defaultValue);
            }

        }

        return SKIP_BODY;
    }
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
