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

 *                                                                           *
 *****************************************************************************/

package com.frameworkset.common.tag.tree.impl;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.util.StringUtil;

/**
 * @author biaoping.yin
 * 复选框设置标签 
 */
public class CheckBoxTag extends BaseTag {
	String name;
	String defaultValues;
	String extention ;
    boolean recursive = false;
    boolean uprecursive = false;
    boolean partuprecursive = false;
    
    String onchange;
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub
		
	}
	
	public int doStartTag()
	{
		TreeTag tree = (TreeTag)findAncestorWithClass(this,TreeTag.class);
		tree.setCheckBox(getName());
		String value = getDefaultValues();
		if (value != null)
		{
			
			// System.out.println("check box default values:" + value);
			String[] ret = StringUtil.split(value, "\\$\\$");
			// for(int i = 0; i < ret.length; i ++)
			// System.out.println("ret[" + i + "]:" + ret[i]);
	
			
			tree.setCheckBoxDefaultValue(ret);
		}
		tree.setCheckBoxExtention(getExtention());
        tree.setRecursive(this.getRecursive());
        tree.setCheckboxOnchange(this.getOnchange());
        tree.setUprecursive(this.isUprecursive());
        tree.setPartuprecursive(this.isPartuprecursive());
		return SKIP_BODY;		
	}
	
	public int doEndTag() throws JspException
	{
		
		name = null;
		defaultValues = null;
		extention = null;
		this.onchange = null;
		this.partuprecursive = false;
		
	    recursive = false;
	    
	    uprecursive = false;
	    
		return super.doEndTag();		
	}

	/**
	 * @return 复选框的默认选中值
	 */
	public String getDefaultValues() {
		return defaultValues;
	}

	/**
	 * @return 复选框的名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setDefaultValues(String string) {
		defaultValues = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @return String
	 */
	public String getExtention() {
		return extention;
	}

	/**
	 * @param string
	 */
	public void setExtention(String string) {
		extention = string;
	}

    public String getOnchange()
    {
        return onchange;
    }

    public void setOnchange(String onchange)
    {
        this.onchange = onchange;
    }

    public boolean getRecursive()
    {
        return recursive;
    }

    public void setRecursive(boolean recursive)
    {
        this.recursive = recursive;
    }

	public boolean isUprecursive() {
		return uprecursive;
	}

	public void setUprecursive(boolean uprecursive) {
		this.uprecursive = uprecursive;
	}

	public boolean isPartuprecursive() {
		return partuprecursive;
	}

	public void setPartuprecursive(boolean partuprecursive) {
		this.partuprecursive = partuprecursive;
	}

}
