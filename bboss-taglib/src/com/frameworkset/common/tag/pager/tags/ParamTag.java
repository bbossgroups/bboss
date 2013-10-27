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

package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;

/**
 *
 * To change for your class or interface
 *
 * @author biaoping.yin
 * @version 1.0
 * 2005-2-3
 */
public final class ParamTag extends PagerTagSupport {

	private String name  = null;
	private String value = null;
	/**
	 * 编码次数，连续编码次数
	 */
	private int encodecount = 1;

	private String defaultValue = null;

    private boolean encode = false;
    public void setEncode(boolean encode)
  {
      this.encode = encode;
  }

  public boolean getEncode()
  {
      return this.encode;
  }


	/**
	 * request:parameter
	 * 		  :attribute
	 */

	private String type = null;

	public final void setName(String val) {
		name = val;
	}

	public final String getName() {
		return name;
	}

	public final void setValue(String val) {
		value = val;
	}

	public final String getValue() {
		return value;
	}
	
	public int doEndTag()throws JspException 
	{
		this.defaultValue = null;
		this.encode = false;
		this.name = null;
		this.type = null;
		this.value = null;
		encodecount = 1;
		return super.doEndTag();
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		if(pagerContext != null)
		{
			if(value != null ||
					   type == null ||
					   (!type.equals(pagerContext.ATTRIBUTE) && !type.equals(pagerContext.PARAMETER)))
			{

						pagerContext.addParam(name, value,defaultValue,encode,encodecount);
			}
			else
			{
				pagerContext.addParamByRequest(name,type,defaultValue,encode,encodecount);
			}

			return EVAL_BODY_INCLUDE;
		}
		else 
		/**
		当param标签出现在list标签中时，
		进行以下处理。如果当存在pager标签，并且将param也放在list标签中，这种情况是非法的
		但是程序没有做相应的判断。
		*/
		{
			PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
			if(listTag != null && listTag.getRowid() == 0)
			{
				pagerContext = listTag.getPagerContext();
				if(value != null ||
						   type == null ||
						   (!type.equals(pagerContext.ATTRIBUTE) && !type.equals(pagerContext.PARAMETER)))
				{

					pagerContext.addParam(name, value,defaultValue,encode,encodecount);
				}
				else
				{
					pagerContext.addParamByRequest(name,type,defaultValue,encode,encodecount);
				}
				return EVAL_BODY_INCLUDE; 
			}
			else
			{
				return SKIP_BODY;
			}
			
			
		}
		
	}

	public void release() {
		name = null;
		value = null;
		super.release();
	}

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
	/**
	 * Description:
	 * @return String

	 */
	public String getType() {
		return type;
	}

	/**
	 * Description:
	 * @return void

	 */
	public void setType(String string) {
		type = string;
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

	public int getEncodecount() {
		return encodecount;
	}

	public void setEncodecount(int encodecount) {
		this.encodecount = encodecount;
	}

}

/* vim:set ts=4 sw=4: */
