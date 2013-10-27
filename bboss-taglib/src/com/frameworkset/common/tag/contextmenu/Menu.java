package com.frameworkset.common.tag.contextmenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * <p>Title: com.frameworkset.common.tag.contextmenu.Menu.java</p>
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
public class Menu implements Serializable,Comparable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7601500738484270118L;
	protected List contextMenuItems = new ArrayList();	
	protected String identity;
//	//页面上生成的每个JS对象名字 peng.yang
//	protected String jsObjectId;
	public final static Menu.ContextMenuItem MENU_OPEN = new Menu.ContextMenuItem();
    public final static Menu.ContextMenuItem MENU_EXPAND = new Menu.ContextMenuItem();
    public final static Menu.ContextMenuItem MENU_COLLAPSE = new Menu.ContextMenuItem();
    public static final String icon_add = "icon-add";
	public static final String icon_edit = "icon-edit";
	public static final String icon_remove = "icon-remove";
	public static final String icon_save = "icon-save";
	public static final String icon_cut = "icon-cut";
	public static final String icon_ok = "icon-ok";
	public static final String icon_no = "icon-no";
	public static final String icon_cancel = "icon-cancel";
	public static final String icon_reload = "icon-reload";
	public static final String icon_search = "icon-search";
	
	public static final String icon_print = "icon-print";
	public static final String icon_help = "icon-help";
	public static final String icon_undo = "icon-undo";
	public static final String icon_redo = "icon-redo";
	public static final String icon_back = "icon-back";
	
	/**
	 * 判断右键菜单是根据节点本身产生还是根据节点类型产生
	 */
	protected boolean generateByNode = false;
	
	private String style="width:150px";
	
	public List getContextMenuItems() {
		return contextMenuItems;
	}
	
	/**
	 * 
	 * <p>Title: com.frameworkset.common.tag.contextmenu.Menu.ContextMenuItem.java</p>
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
	public static class ContextMenuItem implements Serializable
	{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 3928855452818954985L;
		public ContextMenuItem()
		{}
		private String icon ;
		private String name ;
		private String link ;
		private String target;
		private Menu subMenu;
		private String clazz ;
		private boolean seperate = false;
		
		private String disableMsg;
		/**
		 * 标识是否禁用该菜单项
		 */
		protected boolean disabled = false;
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
		public boolean isSeperate() {
			return seperate;
		}
		public void setSeperate(boolean seperate) {
			this.seperate = seperate;
		}
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
		private void initsubMenu()
		{
			if(this.subMenu == null)
				this.subMenu = new Menu();
		}
		public Menu getSubMenu() {
			return subMenu;
		}

		public void setSubMenu(Menu subMenu) {
			this.subMenu = subMenu;
		}
		
		public boolean hasSubMenu()
		{
			return this.subMenu != null;
		}
		public boolean isDisabled() {
			return disabled;
		}
		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
		public String getClazz() {
			return clazz;
		}
		public void setClazz(String clazz) {
			this.clazz = clazz;
		}
		
		/**
		 * 添加右键菜单项
		 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
		 */
		public ContextMenuItem addSubContextMenuItem(String name,String link,String target,String icon,String clazz,boolean disabled) {
			initsubMenu();
			ContextMenuItem contextmenu = new ContextMenuItem();
			contextmenu.setName(name);
			contextmenu.setLink(link);
			contextmenu.setIcon(icon);
			contextmenu.setTarget(target);
			contextmenu.setClazz(clazz);
			contextmenu.setDisabled(disabled);
			this.subMenu.addContextMenuItem(contextmenu);
			return contextmenu;
		}
		
		/**
		 * 添加右键菜单项
		 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
		 */
		public ContextMenuItem addSubContextMenuItem(String name,String link,String target,String icon,String clazz) {
			ContextMenuItem contextmenu = new ContextMenuItem();
			contextmenu.setName(name);
			contextmenu.setLink(link);
			contextmenu.setIcon(icon);
			contextmenu.setTarget(target);
			contextmenu.setClazz(clazz);
			initsubMenu();
			this.subMenu.addContextMenuItem(contextmenu);
			return contextmenu;
		}
		
		/**
		 * 添加右键菜单项
		 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
		 */
		public ContextMenuItem addSubContextMenuItem(String name,String link,String target,String icon) {
			ContextMenuItem contextmenu = new ContextMenuItem();
			contextmenu.setName(name);
			contextmenu.setLink(link);
			contextmenu.setIcon(icon);
			contextmenu.setTarget(target);
			
			initsubMenu();
			this.subMenu.addContextMenuItem(contextmenu);
			return contextmenu;
		}
		
		/**
		 * 添加右键菜单项
		 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
		 */
		public ContextMenuItem addSubContextMenuItem(String name,String link,String icon) {
			ContextMenuItem contextmenu = new ContextMenuItem();
			contextmenu.setName(name);
			contextmenu.setLink(link);
			contextmenu.setIcon(icon);
			
			
			initsubMenu();
			this.subMenu.addContextMenuItem(contextmenu);
			return contextmenu;
		}
		
		/**
		 * 添加右键菜单项
		 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
		 */
		public ContextMenuItem addSubContextMenuItem(String name,String link) {
			ContextMenuItem contextmenu = new ContextMenuItem();
			contextmenu.setName(name);
			contextmenu.setLink(link);
			
			initsubMenu();
			this.subMenu.addContextMenuItem(contextmenu);
			return contextmenu;
		}
		public String getDisableMsg() {
			return disableMsg;
		}
		public void setDisableMsg(String disableMsg) {
			this.disableMsg = disableMsg;
		}
	
		
		
		
		
	}
	
	public int compare(Object o1, Object o2) {
		if(o1.equals(o2))
			return 0;
		else
			return 1;
		
		
	}
	

	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public void addContextMenuItem(ContextMenuItem contextmenu) {
		this.contextMenuItems.add(contextmenu);
	}
	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public ContextMenuItem addContextMenuItem(String name,String link,String target,String icon,String clazz,boolean disabled) {
		ContextMenuItem contextmenu = new ContextMenuItem();
		contextmenu.setName(name);
		contextmenu.setLink(link);
		contextmenu.setIcon(icon);
		contextmenu.setTarget(target);
		contextmenu.setClazz(clazz);
		contextmenu.setDisabled(disabled);
		this.contextMenuItems.add(contextmenu);
		return contextmenu;
	}
	
	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public ContextMenuItem addContextMenuItem(String name,String link,String target,String icon,String clazz) {
		ContextMenuItem contextmenu = new ContextMenuItem();
		contextmenu.setName(name);
		contextmenu.setLink(link);
		contextmenu.setIcon(icon);
		contextmenu.setTarget(target);
		contextmenu.setClazz(clazz);
		
		this.contextMenuItems.add(contextmenu);
		return contextmenu;
	}
	
	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public ContextMenuItem addContextMenuItem(String name,String link,String target,String icon) {
		ContextMenuItem contextmenu = new ContextMenuItem();
		contextmenu.setName(name);
		contextmenu.setLink(link);
		contextmenu.setIcon(icon);
		contextmenu.setTarget(target);
		
		this.contextMenuItems.add(contextmenu);
		return contextmenu;
	}
	
	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public ContextMenuItem addContextMenuItem(String name,String link,String icon) {
		ContextMenuItem contextmenu = new ContextMenuItem();
		contextmenu.setName(name);
		contextmenu.setLink(link);
		contextmenu.setIcon(icon);
		
		
		this.contextMenuItems.add(contextmenu);
		return contextmenu;
	}
	
	/**
	 * 添加右键菜单项
	 * @see com.frameworkset.common.tag.tree.itf.ITreeNode#addContextMenuItem(com.frameworkset.common.tag.tree.impl.TreeNode.ContextMenuItem)
	 */
	public ContextMenuItem addContextMenuItem(String name,String link) {
		ContextMenuItem contextmenu = new ContextMenuItem();
		contextmenu.setName(name);
		contextmenu.setLink(link);
		
		this.contextMenuItems.add(contextmenu);
		return contextmenu;
	}
	
	/**
	 * 添加邮件菜单项的分隔线
	 */
	public void addSeperate() {
		this.contextMenuItems.add(seperate);		
	}
	private static final ContextMenuItem seperate = new ContextMenuItem();
	static{
		seperate.setSeperate(true);
	}
	

	public boolean isGenerateByNode() {
		return generateByNode;
	}

	public void setGenerateByNode(boolean generateByNode) {
		this.generateByNode = generateByNode;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public int hashCode()
	{
		return this.getIdentity().hashCode();
	}
	public boolean equal(Object otherMenu)
	{
		if(otherMenu instanceof Menu)
		{
			Menu temp = (Menu)otherMenu;
			return temp.getIdentity().equals(temp.getIdentity());
		}
		else
			return false;
	}


	public int compareTo(Object o) {
		return this.compare(this,o);
	}


	public String getStyle() {
		return style;
	}


	public void setStyle(String style) {
		this.style = style;
	}


//	public String getJsObjectId() {
//		return jsObjectId;
//	}
//
//
//	public void setJsObjectId(String jsObjectId) {
//		this.jsObjectId = jsObjectId;
//	}
}
