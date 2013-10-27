package org.frameworkset.task;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * <p>Title: Execute.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:03:46
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public interface Execute extends Serializable{
	public void execute(Map parameters);
}
