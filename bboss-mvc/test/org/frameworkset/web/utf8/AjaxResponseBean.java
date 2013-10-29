package org.frameworkset.web.utf8;

import java.io.Serializable;

/**
 * 
 *<p>Title:AjaxResponseBean.java</p>
 *<p>Description: 规范封装后台操作的结果信息，便于转换成json格式</p>
 *<p>Copyright:Copyright (c) 2010</p>
 *<p>Company:湖南科创</p>
 *@author 刘剑峰
 *@version 1.0
 *2011-4-19
 */
public class AjaxResponseBean implements Serializable {
	//状态,success表示成功，error表示失败
	private String status;
	
	//信息
	private String data;
	
	private String e;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
}
