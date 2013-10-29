package com.frameworkset.common.tag.contextmenu;

import java.util.Collections;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * <p>Title: ContextMenuImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 长沙科创计算机系统集成有限公司</p>
 * @Date 2006-9-15
 * @author biaoping.yin
 * @version 1.0
 */
public class ContextMenuImpl extends Observable implements ContextMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean enablecontextmenu = false;
	protected Set node_contextmenus = Collections.synchronizedSet(new TreeSet()); 
	protected Set type_contextmenus = Collections.synchronizedSet(new TreeSet());
	protected Set contextmenus = Collections.synchronizedSet(new TreeSet());
	
    

	public Set getContextmenus() {
		
		return contextmenus;
	}


	/**
	 * 为树节点添加类型右键菜单功能
	 * 
	 * @param ITreeNode
	 */
	public void addContextMenuOfType(Menu menu) {
		menu.setIdentity("_type_" + menu.getIdentity());
		this.type_contextmenus.add(menu);

	}
	

	/**
	 * 添加一般元素的右键菜单功能
	 * 
	 * @param ITreeNode
	 */
	public void addContextMenu(Menu menu) {
		menu.setIdentity(menu.getIdentity());
		this.type_contextmenus.add(menu);

	}

	/**
	 * 为树节点添加节点类型右键菜单功能
	 * 
	 * @param ITreeNode
	 */
	public void addContextMenuOfNode(AttachElement node, Menu menu) {
		if (this.enablecontextmenu) {
			menu.setIdentity("_node_" + node.getId());
			node.setEnablecontextmenu(true);
			menu.setGenerateByNode(true);
			this.node_contextmenus.add(menu);
		}

	}

	/**
	 * 构建类型菜单
	 *
	 */
	public void buildContextMenusWraper() {
		if (this.enablecontextmenu && this.type_contextmenus.size() == 0) {
			//this.type_contextmenus.clear();
			buildContextMenus();
		}
	}
	
	/**
	 * 合并类型右键菜单和节点右键菜单
	 *
	 */
	public void mergeContextMenus() {
		this.clearContextmenus();
		this.contextmenus.addAll(type_contextmenus);
		this.contextmenus.addAll(node_contextmenus);
	}

	/**
	 * 构建树的右键菜单，不同类型的节点可设置不同的右键菜单项 该类由可以被子类重载
	 */
	protected void buildContextMenus() {

	}
	
	public void setEnablecontextmenu(boolean enablecontextmenu)
	{
		this.enablecontextmenu = enablecontextmenu; 
	}


	public Set getNodeContextmenus() {
		
		return this.node_contextmenus;
	}


	public Set getTypeContextmenus() {
		
		return this.type_contextmenus;
	}


	public void clearNodeContextmenus() {
		this.node_contextmenus.clear();
		
	}


	public void clearTypeContextmenus() {
		this.type_contextmenus.clear();
		
	}


	public void clearContextmenus() {
		this.contextmenus.clear();
	}

}
