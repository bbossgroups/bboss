package com.frameworkset.common.tag.contextmenu;

import java.util.Observer;

/**
 * 
 * <p>Title: com.frameworkset.common.tag.contextmenu.AttachElement.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author biaoping.yin
 * @version 1.0
 */
public interface AttachElement extends Observer  {
	
	public void setEnablecontextmenu(boolean enabalecontextmenu);
	
	public boolean isEnablecontextmenu();
	public String getId();

}
