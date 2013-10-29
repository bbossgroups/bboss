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

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.BaseTag;

import javax.servlet.jsp.JspException;

/**
 * <p>
 * Title: SQLParamsContextTag.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-3-11 下午06:01:08
 * @author biaoping.yin
 * @version 1.0
 */

public class SQLParamsContextTag extends BaseTag implements SQLParamsContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected SQLExecutor sqlExecutor;
	protected String sqlparamskey = "sql.params.key";
	protected String pretoken;
	protected String endtoken;

	public SQLExecutor getSQLExecutor() {
		return sqlExecutor;
	}

	public int doEndTag() throws JspException {
		this.request.setAttribute(sqlparamskey, getSQLExecutor());
		sqlparamskey = "sql.params.key";
		pretoken = null;
		endtoken = null;
		return super.doEndTag();
	}

	public int doStartTag() throws JspException {
		sqlExecutor = new SQLExecutor();
		sqlExecutor.setPretoken(pretoken);
		sqlExecutor.setEndtoken(endtoken);
		return EVAL_BODY_INCLUDE;
	}

	public String getSqlparamskey() {
		return sqlparamskey;
	}

	public void setSqlparamskey(String sqlparamskey) {
		this.sqlparamskey = sqlparamskey;
	}

	public String getPretoken() {
		return pretoken;
	}

	public void setPretoken(String pretoken) {
		this.pretoken = pretoken;
	}

	public String getEndtoken() {
		return endtoken;
	}

	public void setEndtoken(String endtoken) {
		this.endtoken = endtoken;
	}
}
