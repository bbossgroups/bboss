/**
 * 功能说明：资源包管理框架新增javaBean基类
 * 
 * 修改说明：新增
 * 修改时间：2015-8.24
 * 修  改  人：姚建
 */
package org.frameworkset.soa.list;

import java.io.Serializable;

public class BaseModel implements Serializable{
	private static final long serialVersionUID = 20150825080612L;
	
	/**
	 * 操作类型
	 * 0：新增， 1：修改，2：删除（预留） 
	 */
	private  String operationType;

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
}
