package org.frameworkset.task;

import java.io.Serializable;

/**
 * 
 * <p>Title: ScheduleServiceException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:29
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class ScheduleServiceException extends Exception implements Serializable {
	
	public ScheduleServiceException()
	{
		super();
	}
	
	public ScheduleServiceException(String msg)
	{
		super(msg);
	}

}
