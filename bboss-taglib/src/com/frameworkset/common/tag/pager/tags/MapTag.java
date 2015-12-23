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

import javax.servlet.jsp.JspException;



/**
 * <p>MapTag.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * <pg:list requestKey="billDataNameList">
	                    	<pg:map requestKey="billDataList" key="" keycell="true" keyColName="">
		                    	<pg:list> 
			                    	<ul>
			                        	<li class="w50"><label>用户：</label>蔡睿</li>
			                        	<li class="w50"><label>部门：</label>文秘督办部</li>
			                        	<li><label>项目：</label>项目1</li>
			                        	<li><label>预算科目：</label>办公费-邮寄费</li>
			                        	<li><label>费用发生时间：</label>2012-01-31</li>
			                        	<li><label>金额：</label>1000.00</li>
			                        	<li><label>币种：</label>人民币</li>
			                        	<li><label>单据金额：</label>5905.05</li>
			                        	<li><label>备注：</label>测试</li>
			                        </ul>
		                        </pg:list>
	                        </pg:map>
                        </pg:list>
 * @Date 2011-7-27
 * @author biaoping.yin
 * @version 1.0
 */
public class MapTag extends PagerDataSet
{

	private boolean keycell = false;
	private String keycolName ;
	private String key;
	public boolean isKeycell() {
		return keycell;
	}
	public void setKeycell(boolean keycell) {
		this.keycell = keycell;
	}
	public String getKeycolName() {
		return keycolName;
	}
	public void setKeycolName(String keycolName) {
		this.keycolName = keycolName;
	}
	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		return super.doStartTag();
	}
	@Override
	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		int ret = super.doEndTag();
		this.keycell = false;
		this.keycolName = null;
		this.key = null;
		return ret;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		super.doFinally();
		this.keycell = false;
		this.keycolName = null;
		this.key = null;
	}
	
	

}
