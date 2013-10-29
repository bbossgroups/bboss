/*
   The Jenkov JSP Tree Tag provides extra tasks for Apaches Ant build tool

   Copyright (C) 2003 Jenkov Development

   Jenkov JSP Tree Tag is free software; you can redistribute it and/or
   modify it under the terms of the GNU Lesser General Public
   License as published by the Free Software Foundation; either
   version 2.1 of the License, or (at your option) any later version.

   Jenkov JSP Tree Tag is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Lesser General Public License for more details.

   You should have received a copy of the GNU Lesser General Public
   License along with this library; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA


   Contact Details:
   ---------------------------------
   Company:     Jenkov Development - www.jenkov.com
   Project URL: http://www.jenkov.dk/projects/treetag/treetag.jsp
   Email:       info@jenkov.com
*/

/**
 * @version $revision$
 * @author Jakob Jenkov
 */
package com.frameworkset.common.tag.tree.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.frameworkset.common.tag.contextmenu.AttachElement;
import com.frameworkset.common.tag.tree.Const;
import com.frameworkset.common.tag.tree.itf.ITree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 树的节点信息对象
 * @author biaoping.yin 
 * @date 2006-12-19 8:53:36
 * @version v1.0
 * @company chinacreator.com
 */
public class TreeNode implements ITreeNode, java.io.Serializable ,AttachElement{

	/**
	 * comtree接口
	 */
	Tree tree;
	/**
	 * 备注，可以保存所有需要扩充的信息
	 * tag和action中可以通过request.getParameter("memo")获取该信息
	 */
	protected String memo;
	protected String name;
	protected String id; 
	protected String type;
	protected String target; 
	/**
	 * 节点link
	 */
	protected String nodeLink;
	protected List children = new ArrayList();
	protected boolean hasChildren = false;

    protected String path;
    protected String parentPath;
    protected Map params;
	/**
	 * checkbox 的值
	 */

	private String checkboxValue;

	/**
	 * radio按钮值
	 */
	private String radioValue;

	protected ITreeNode parent = null;

	protected Observable notifier;

    protected boolean sorted = false;
    
    protected boolean enablecontextmenu = false;
      
    
	/**
	 * 控制节点是否显示链接
	 * true 显示（默认）
	 * false 不显示
	 */
	protected boolean showHref = true;
	
	protected ITreeNode leftNode;
	protected ITreeNode rightNode;
	
	/**
	 * 儿子节点串，只有当复选框的属性recursive为true或者节点本身的recursive为true时需要
	 * 拼接儿子的ids
	 */
	protected StringBuffer sonids;
	/**
	 * 父节点ids串，只有复选框的uprecursive的值为true或者节点本身的uprecursive的值为true时
	 * 需要拼接父ids
	 */
	protected StringBuffer fatherids;
	
	public void addSonid(String sonid)
	{
		if(sonids == null)
		{
			sonids = new StringBuffer();
			sonids.append(sonid);
		}
		else
		{
			sonids.append("##").append(sonid);
		}
	}
	
	
	

	//    public TreeNode(String id,
	//    				String name,
	//					String link,
	//    				String target
	//    				){
	//        setId(id);
	//        setName(name);
	//        this.link = link;
	//        this.target = target;
	//    }

	//	public TreeNode(String id,
	//					String name,
	//					boolean showHref,
	//					Observable notifier){
	//			setId(id);
	//			setName(name);
	//			notifier.addObserver(this);
	//			this.setShowHref(showHref);
	//			this.notifier = notifier;
	//
	//		}

	/**
	 * 刷新节点本身信息
	 * derived from java.util.Observer
	 * added by yinbiaoping on 2003/03/23
	 * Object o is the Observer 如果o就是对象本身（this）时该怎么处理？？？？？？？？ #questtion1
	 * @see     java.util.Observable
	 * @see     java.util.Observer
	 */
	public void update(Observable observable, Object o) {

		ITree tree = (ITree) observable;
		ITreeNode temp = tree.updateNode(tree, this);

		if (temp == null && !type.equals(Const.NODE_ROOT + "")) {
			if (tree.isExpanded(getId()))
				tree.getExpandedNodes().remove(getId());
			if (tree.isSelected(getId()))
				tree.getSelectedNodes().remove(getId());
			getParent().removeChild(this);
		}

		if (temp != null) {
			if (!type.equals(Const.NODE_ROOT + "")) {
				this.id = temp.getId();
				this.name = temp.getName();
				this.type = temp.getType();

			}
			this.hasChildren = temp.hasChildren();

			if (!hasChildren) {
				removeAllChildren();
				tree.getExpandedNodes().remove(id);
			}

		}

	}

	/**
	 *
	 * @param id 节点id,全局唯一
	 * @param name 节点名称
	 * @param type 节点类型
	 */
	public TreeNode(String id, String name, String type) {
		this(id, name, type, (String) null);
	}

	/**
		 *
		 * @param id 节点id,全局唯一
		 * @param name 节点名称
		 * @param type 节点类型
		 * @param memo 节点备注
		 */
	public TreeNode(String id, String name, String type, String memo) {
		this(id, name, type, true, null, memo);
		//this.type = type == null?"":type;
	}

	/**
	 *
	 * @param id 节点id,全局唯一
	 * @param name 节点名称
	 * @param type 节点类型
	 * @param showHref 是否显示节点的超链接
	 */
	public TreeNode(String id, String name, String type, boolean showHref) {
		this(id, name, type, showHref, (String) null);
	}

	/**
		 *
		 * @param id 节点id,全局唯一
		 * @param name 节点名称
		 * @param type 节点类型
		 * @param showHref 是否显示节点的超链接
		 * @param memo 节点备注
		 */
	public TreeNode(
		String id,
		String name,
		String type,
		boolean showHref,
		String memo) {
		this(id, name, type, true, null, memo);
	}

	/**
		 * 构建器
		 * 初始化属性id,name,type,notifier
		 * @param id 节点id,全局唯一
		 * @param name 节点名称
		 * @param type 节点类型
		 * @param memo 节点备注
		 */
	public TreeNode(
		String id,
		String name,
		String type,
		Observable notifier,
		String memo) {
		this(id, name, type, true, notifier, memo);
	}
	/**
	 * 构建器
	 * 初始化属性id,name,type,notifier
	 * @param id 节点id,全局唯一
	 * @param name 节点名称
	 * @param type 节点类型
	 * @param notifier 观察者
	 */
	public TreeNode(String id, String name, String type, Observable notifier) {
		this(id, name, type, notifier, null);
	}

	/**
	 * 构建器
	 * 初始化属性id,name,type,showHref,notifier
	 * @param id 节点标识
	 * @param name 节点名称
	 * @param type 节点类型
	 * @param showHref 是否显示超链接
	 * @param notifier 观察者
	 */
	public TreeNode(
		String id,
		String name,
		String type,
		boolean showHref,
		Observable notifier) {
		this(id, name, type, showHref, notifier, null);
	}

	/**
	 * 构建器
	 * 初始化属性id,name,type,showHref,notifier
	 * @param id 节点标识
	 * @param name 节点名称
	 * @param type 节点类型
	 * @param showHref 是否显示节点超链接
	 * @param notifier 节点观察器
	 * @param memo
	 */
	public TreeNode(
		String id,
		String name,
		String type,
		boolean showHref,
		Observable notifier,
		String memo) {

		this(id,
			name,
			type,
			showHref,
			notifier,
			memo,null,null,null);//String path = null;
	}

	/**
	 * 构建器
	 * 初始化属性id,name,type,showHref,notifier
	 * @param id 节点标识
	 * @param name 节点名称
	 * @param type 节点类型
	 * @param showHref  是否显示节点超链接
	 * @param notifier 节点观察器
	 * @param memo
	 */
	public TreeNode(
		String id,
		String name,
		String type,
		boolean showHref,
		Observable notifier,
		String memo,
		String radioValue,
		String checkboxValue,
        String path
		) {
		setId(id);
		setName(name);
		setShowHref(showHref);
		if(notifier != null)
		{
			notifier.addObserver(this);
			this.notifier = notifier;
		}

		setType(type);
		setMemo(memo);
		setCheckboxValue(checkboxValue);
		setRadioValue(radioValue);

        setPath(path);
	}
	
	public TreeNode()
	{
		
	}

    public TreeNode(
        String id,
        String name,
        String type,
        boolean showHref,
        Observable notifier,
        String memo,
        String radioValue,
        String checkboxValue,
        String path,Map params
        ) {
        setId(id);
        setName(name);
        setShowHref(showHref);
        if(notifier != null)
        {
            notifier.addObserver(this);
            this.notifier = notifier;
        }

        setType(type);
        setMemo(memo);
        setCheckboxValue(checkboxValue);
        setRadioValue(radioValue);
        this.params = params;
        setPath(path);
    }


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type == null ? "" : type;
	}

	public void addChild(ITreeNode node) {
		if(children.size() != 0)
		{
			ITreeNode left = (ITreeNode)children.get(children.size() - 1);
			left.setRightNode(node);
			node.setLeftNode(left);
		}
		children.add(node);
		
        this.sorted = false;
	}

	public void removeChild(ITreeNode node) {
		this.children.remove(node);
	}

	public void removeAllChildren() {
		this.children.clear();
	}

	/**
	 * get children node of this node
	 */
	public List getChildren() {
		/**
		 * 在获取children时对所有的孩子进行排序
		 */
        if(!sorted && this.tree.isSortable())
        {
    		Collections.sort(children, new TreeNodeCompare());
    		sorted = true;
        }
		return this.children;
	}

	/**
	 * call this method when the layers is at the curLevel but the cur node has son
	 */
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**
	 * 判断是否有儿子
	 */
	public boolean hasChildren() {
		//modified by biaoping.yin on 2005-02-04
		//return children.size() > 0 || hasChildren;
		return hasChildren;
		//return children.size() > 0;

	}

	/**
	 * 	added by biaoping.yin on 2005-02-04
	 *  获取儿子数量
	 */
	public int childrenSize()
	{
		return this.children.size();
	}

	public boolean equals(Object o) {
		try {
			ITreeNode node = (ITreeNode) o;
			//if(!this.id.equals(node.getId()) || !this.name.equals(node.getName())){
			if (!this.id.equals(node.getId())) {
				return false;
			}
		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(id: ");
		buffer.append(getId());
		buffer.append(",    name: ");
		buffer.append(getName());
		buffer.append(",    type: ");
		buffer.append(getType());
		buffer.append(",    hasChildren: ");
		buffer.append(this.hasChildren);
		buffer.append(",    showHref: ");
		buffer.append(this.showHref);
		buffer.append(",    memo: ");
		buffer.append(getMemo());
		buffer.append(")");

		return buffer.toString();
	}

	public void setParent(ITreeNode parent) {
		this.parent = parent;
	}

	public ITreeNode getParent() {
		return this.parent;
	}

	/**
	 * Description:
	 * @return
	 * boolean
	 */
	public boolean getShowHref() {
		return showHref;
	}

	/**
	 * Description:
	 * @param showHref
	 *
	 */
	public void setShowHref(boolean showHref) {
		this.showHref = showHref;
	}

	/**
	 * 树结点排序比较器,节点类型排序
	 * @author biaoping.yin
	 * 2004-7-1
	 */
	static class TreeNodeCompare implements Comparator {
		private ITreeNode node1;
		private ITreeNode node2;
		/* 首先根据节点类型排序，然后根据节点的是否具有儿子排序
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			node1 = (ITreeNode) o1;
			node2 = (ITreeNode) o2;
            if(node2.getType() == null && node1.getType() == null)
            {

                if(node1.hasChildren() && !node2.hasChildren())
                {

                    return -1;
                }
                else if((node1.hasChildren() && node2.hasChildren())
                        || (!node1.hasChildren() && !node2.hasChildren()))
                {

                    return 0;
                }
                else
                {

                    return 1;
                }
            }
            else if(node1.getType() != null && node2.getType() == null)
            {

            }
            else if(node1.getType() == null && node2.getType() != null)
            {

            }


            int ret = node1.getType().compareTo(this.node2.getType());
            if(ret == 1)
            {

            }
            else if(ret == 0)
            {
                if(node1.hasChildren() && !node2.hasChildren())
                {

                    return -1;
                }
                else if((node1.hasChildren() && node2.hasChildren())
                        || (!node1.hasChildren() && !node2.hasChildren()))
                {

                    return 0;
                }
                else
                {

                    return 1;
                }

            }
            else if(ret == -1)
            {

            }

			return 0;
		}
	}
	/**
	 * remark属性获取方法
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * remark属性设置方法。
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return checkboxValue  复选框的值
	 */
	public String getCheckboxValue() {
		return checkboxValue;
	}

	/**
	 * @return radioValue 单选框的值
	 */
	public String getRadioValue() {
		return radioValue;
	}

    public String getPath() {
        return path;
    }

    public String getParentPath() {
        return parentPath;
    }

    public Map getParams() {
        return params;
    }

    /**
	 * @param string
	 */
	public void setCheckboxValue(String string) {
		checkboxValue = string;
	}

	/**
	 * @param string
	 */
	public void setRadioValue(String string) {
		radioValue = string;
	}

    public void setPath(String path) {
        this.path = path;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public boolean isRoot()
	{
		return getType() != null && getType().equals(Const.NODE_ROOT + "");
	}
    /**
     * @return Returns the nodeLink.
     */
    public String getNodeLink() {
        return nodeLink;
    }
    /**
     * @param nodeLink The nodeLink to set.
     */
    public void setNodeLink(String nodeLink) {
        this.nodeLink = nodeLink;
    }
//    
//    /**
//     * 添加右键菜单项
//     */
//	public void addContextMenuItem(String menuid, String icon, String name, String link) {
//		ContextMenuItem contextMenuItem = new ContextMenuItem();
//		contextMenuItem.setIcon(icon);
//		contextMenuItem.setMenuid(menuid);
//		contextMenuItem.setLink(link);
//		contextMenuItem.setName(name);
//		contextmenus.add(contextMenuItem);
//		
//	}

	public boolean isEnablecontextmenu() {
		return enablecontextmenu;
	}

	public void setEnablecontextmenu(boolean enablecontextmenu) {
		this.enablecontextmenu = enablecontextmenu;
	}

	public Tree getTree()
	{
		return tree;
	}

	public void setTree(Tree tree)
	{
		this.tree = tree;
	}

	public ITreeNode getLeftNode()
	{
		// TODO Auto-generated method stub
		return this.leftNode;
	}

	public ITreeNode getRightNode()
	{
		// TODO Auto-generated method stub
		return this.rightNode;
	}

	public void setLeftNode(ITreeNode leftNode)
	{
		this.leftNode = leftNode;
	}

	public void setRightNode(ITreeNode rightNode)
	{
		this.rightNode = rightNode;
	}

	public StringBuffer getSonids() {
		return sonids != null ? sonids:new StringBuffer(0);
	}
	
	/**
	 * 儿子节点会调用
	 * @return
	 */
	public StringBuffer getFatherids() {
		if(fatherids == null)
		{
			fatherids = new StringBuffer();
			if(this.parent != null)
			{
				fatherids.append(parent.getFatherids()).append("##").append(this.getId());
			}
		}
		
		return fatherids;
	}

	

//	public Menu getContextMenu() {
//		// TODO Auto-generated method stub
//		return contextMenu ;
//	}

//	public void setContextMenu(Menu contextMenu) {
//		// TODO Auto-generated method stub
//		contextMenu.setGenerateByNode(true);
//		this.contextMenu = contextMenu;
//	}
	
//	/**
//	 * 添加右键菜单项中的间隔线
//	 * @param icon
//	 */
//	public void addContextMenuItemSeperate(String icon) {
//		Seperate seperate = new Seperate();
//		seperate.setIcon(icon);
//		contextmenus.add(seperate);		
//	}

	
}
