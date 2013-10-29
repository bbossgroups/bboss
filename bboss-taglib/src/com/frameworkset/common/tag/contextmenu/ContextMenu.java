package com.frameworkset.common.tag.contextmenu;

import java.io.Serializable;
import java.util.Set;
/**
 * 
 * <p>Title: com.frameworkset.common.tag.contextmenu.ContextMenu.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author biaoping.yin
 * @version 1.0
 */
public interface ContextMenu extends Serializable{
	/**
	 * 获取在单独的节点上构造的右键菜单
	 * @return
	 */	
	public Set getNodeContextmenus();
	/**
	 * 获取类别的右键菜单列表
	 * @return
	 */
	public Set getTypeContextmenus();
	
	/**
	 * 清除节点右键菜单
	 *
	 */
	public void clearNodeContextmenus();
	/**
	 * 清除类别右键菜单
	 *
	 */
	public void clearTypeContextmenus();
	/**
	 * 清除所有的右键菜单
	 *
	 */
	public void clearContextmenus();
	/**
	 * 添加类别右键菜单
	 * @param menu
	 */
	public void addContextMenuOfType(Menu menu);
	/**
	 * 合并类型右键菜单和节点右键菜单
	 *
	 */
	public void mergeContextMenus();
	
	/**
	 * 获取所有的右键菜单
	 * @return
	 */
	public Set getContextmenus();
	
	/**
	 * 添加节点右键菜单
	 * @param node
	 * @param menu
	 */
	public void addContextMenuOfNode(AttachElement node,Menu menu);
	/**
	 * 添加一般的右键菜单功能
	 * @param menu
	 */
	public void addContextMenu(Menu menu);

}
