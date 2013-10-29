/*
 * Created on 2004-6-9
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.frameworkset.common.bean;

import java.io.Serializable;

/**
 * @author biaoping.yin
 * 操作标志常数类:
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public final class StatusConst implements Serializable
{
	/**
		 * 删除状态标志
		 */
		public final static int DELETE = 0;
		/**
		 * 更新状态标识
		 */
		public final static int UPDATE = 1;
		/**
		 * 添加状态标识
		 */
		public final static int ADD = 2;
		/**
		 * 缓冲状态标识
		 */
		public final static int CACHE = 3;
}
