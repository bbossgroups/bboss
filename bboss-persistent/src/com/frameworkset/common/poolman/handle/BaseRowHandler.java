package com.frameworkset.common.poolman.handle;

import com.frameworkset.common.poolman.Record;



/**
 * 
 * 
 * <p>Title: BaseRowHandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 24, 2008 2:40:47 PM
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseRowHandler<T> extends RowHandler<T> {

	
	public T handleField_(Record record) throws Exception
	{
		throw new UnsupportedOperationException("T handleField_(Record record)");
	}
	
}
