package com.frameworkset.common.tag.pager.db;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.BaseTag;

/**
 * 
 * <p>Title: StatementTag.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-13
 * @author biaoping.yin
 * @version 1.0
 */
public class StatementTag extends BaseTag  implements SQLParamsContext{
	private String sql ;
	private String pretoken;
	private String endtoken;
	private boolean hasbag = false;
	BatchUtilTag batchUtilTag ;
	private String action = null;
	public SQLExecutor getSQLExecutor() {
		
		return batchUtilTag.getSQLExecutor();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -200649245148550945L;

	@Override
	public int doEndTag() throws JspException {
		if(!this.isHasbag() && batchUtilTag.getType().equals(SQLExecutor.BATCH_PREPARED))
		{
			this.getSQLExecutor().addPreparedBatch();
		}
		hasbag = false;
		batchUtilTag = null;
		sql = null;
		pretoken = null;
		endtoken = null;
		return EVAL_PAGE;
	}


	@Override
	public int doStartTag() throws JspException {
		batchUtilTag = (BatchUtilTag)findAncestorWithClass(this, BatchUtilTag.class);
		if(batchUtilTag.getType().equals(SQLExecutor.BATCH_PREPARED))
		{
			this.getSQLExecutor().init(sql, pretoken, endtoken, action);
		}
		else{
			this.getSQLExecutor().addBatch(sql);
		}
		return EVAL_BODY_INCLUDE;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
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


	public boolean isHasbag() {
		return hasbag;
	}


	public void setHasbag(boolean hasbag) {
		this.hasbag = hasbag;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}
}
