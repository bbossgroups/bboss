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
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.common.tag.tree.impl;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.BaseTag;

/**
 * 设置单选按钮标签
 * @author biaoping.yin
 * created on 2005-3-25
 * version 1.0
 */
 
 
public class RadioTag extends BaseTag {

	String name;
	String defaultValue;
	String extention;
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
	
	public int doEndTag() throws JspException
	{
		
		name = null;
		
		extention = null;
		this.defaultValue = null;
	    
		return super.doEndTag();		
	}

	public int doStartTag() {
		TreeTag tree = (TreeTag) findAncestorWithClass(this, TreeTag.class);
		tree.setRadio(getName());
		tree.setRadioDefaultValue(getDefaultValue());
		tree.setRadioExtention(getExtention());
		return SKIP_BODY;
	}

	/**
	 * @return 单选框的默认选中值
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setDefaultValue(String string) {
		defaultValue = string;
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

}
