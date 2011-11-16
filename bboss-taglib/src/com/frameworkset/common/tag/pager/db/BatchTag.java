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
package com.frameworkset.common.tag.pager.db;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.BaseTag;

/**
 * 
 * <p>Title: BatchTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-13
 * @author biaoping.yin
 * @version 1.0
 */
public class BatchTag extends BaseTag  implements SQLParamsContext{
	private StatementTag statementTag ;
	public SQLExecutor getSQLExecutor() {
		
		return statementTag.getSQLExecutor();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -717053305505663705L;

	@Override
	public int doEndTag() throws JspException {
		getSQLExecutor().addPreparedBatch();
		statementTag = null;
		return EVAL_PAGE;
	}

	

	@Override
	public int doStartTag() throws JspException {
		statementTag = (StatementTag)findAncestorWithClass(this, StatementTag.class);
		statementTag.setHasbag(true);
		return EVAL_BODY_INCLUDE;
	}

}
