/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;

import javax.servlet.jsp.JspException;


/**
 * <p>ParamsTag.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-11-17
 * @author biaoping.yin
 * @version 1.0
 */
public class ParamsTag extends PagerTagSupport {

	private String name  = null;
	/**
	 * 编码次数，连续编码次数
	 */
	private int encodecount = 1;

    public int getEncodecount() {
		return encodecount;
	}

	public void setEncodecount(int encodecount) {
		this.encodecount = encodecount;
	}

	private boolean encode = false;
    public void setEncode(boolean encode)
  {
      this.encode = encode;
  }

  public boolean getEncode()
  {
      return this.encode;
  }


	

	public final void setName(String val) {
		name = val;
	}

	public final String getName() {
		return name;
	}

	
	
	public int doEndTag()throws JspException 
	{
		
		this.encode = false;
		this.name = null;
		encodecount = 1;
		
		return super.doEndTag();
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		if(pagerContext != null)
		{
			
			pagerContext.addParamsByRequest(name,this.encode,encodecount);
			
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
				
				pagerContext.addParamsByRequest(name,this.encode,encodecount);
				
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
	

	
}
