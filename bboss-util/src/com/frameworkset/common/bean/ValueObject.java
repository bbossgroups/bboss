/*
 * Created on 2004-6-3
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.frameworkset.common.bean;

import java.io.Serializable;

/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class ValueObject implements Serializable
{	
	/**
	 * 获取值对象的关键字
	 * @return 返回值对象的关键字
	 */
	public abstract Object getKey();
	/**
	 * 设置值对象的关键字 
	 */	
	public abstract void setKey(Object key);
	
	public boolean equals(Object obj)
	{		
		if(obj == null)
			return false;
		ValueObject vo = (ValueObject)obj;
		if(this.getKey() == vo.getKey())
			return true;
		return false;
	}	
}
