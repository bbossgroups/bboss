package org.frameworkset.task;

import java.io.Serializable;

/**
 * 
 * <p>Title: Service.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:41
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public interface Service extends Serializable {
	public void startService();
	public void restartService();

}
