/*
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
package com.frameworkset.tag.logic;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.pager.tags.MatchTag;
import com.frameworkset.util.StringUtil;

/**
 * 
 * @author yinbp
 *
 */
public class CaseTag extends MatchTag {

	@Override
	protected boolean match() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int doStartTag() throws JspException {
	    init();
//	    dataSet = searchDataSet(this,PagerDataSet.class);
//	    t_formula = dataSet.getFormula(getExpression());
	    if(StringUtil.isEmpty(length))
	    	actualValue = evaluateActualValue();
	    else
	    	evalLengthInfo();
	    
//		actualValue = getOutStr();
//		setMeta();
		//如果期望值为表达式，则求解表达式的值来得到期望值	
		return EVAL_BODY_INCLUDE;
		
	}

}
