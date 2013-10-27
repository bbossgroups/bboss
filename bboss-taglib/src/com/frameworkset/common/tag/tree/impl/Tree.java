/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/



package com.frameworkset.common.tag.tree.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.common.tag.contextmenu.ContextMenuImpl;
import com.frameworkset.common.tag.tree.itf.ICollapseListener;
import com.frameworkset.common.tag.tree.itf.IExpandListener;
import com.frameworkset.common.tag.tree.itf.ISelectListener;
import com.frameworkset.common.tag.tree.itf.ITree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.common.tag.tree.itf.IUnselectListener;

/**
 * 定义树结构的抽象类
 * @author biaoping.yin
 * created on 2005-3-25
 * version 1.0
 */
public abstract class Tree extends ContextMenuImpl implements ITree,java.io.Serializable {
	/**
	 * level:定义默认展开层级
	 */
	protected int level = -1;
    protected boolean   singleSelectionMode = false;
    protected Set       expanded = new TreeSet();
    protected Set       selected = new TreeSet();
    protected ITreeNode root     = null;

    protected List      expandListeners   = new ArrayList();
    protected List      collapseListeners = new ArrayList();
    protected List      selectListeners   = new ArrayList();
    protected List      unselectListeners = new ArrayList();
	protected List      nodeListeners = new ArrayList();
	/**标识当前展开得节点*/
	protected ITreeNode curExpanded;
	
	protected boolean dynamic = true;
	
	
	protected boolean needObservable = false;
	//	added this attribute by biaoping.yin on 2005-02-04
	/**更新结点得信息*/
	protected boolean refreshNode = true;
	
	protected boolean uprecursive = false;
	
	protected boolean recursive = false;
	
	public static final String mode_static = "static";
    public static final String mode_static_dynamic = "static-dynamic";
    public static final String mode_dynamic = "dynamic";
	
	
	
	
	/**
	 * 指定是否对树节点进行排序,缺省为true
	 */
    private boolean sortable = false;
    
	
	public void setNeedObservable(boolean needObservable)
	{
		this.needObservable = needObservable;
	}	
	
	/**
	 * 判断是否需要注册到观察器中
	 * @return boolean
	 */
	protected boolean isNeedObservable()
	{
		return this.needObservable;
	}

    public ITreeNode getRoot() {
        return this.root;
        
    }

    public void setRoot(ITreeNode node) {
    	
        this.root = node;
        this.root.setTree(this);
    }

    public ITreeNode findNode(String treeNodeId) {
        return findNode(getRoot(), treeNodeId);
    }

    protected ITreeNode findNode(ITreeNode treeNode, String treeNodeId){
        if(treeNode.getId().equals(treeNodeId)){
            return treeNode;
        }

        Iterator children = treeNode.getChildren().iterator();
        while(children.hasNext()){
            ITreeNode child = (ITreeNode) children.next();
            ITreeNode match = findNode(child, treeNodeId);
            if( match != null){
                return match;
            }
        }
        return null;
    }

    public Set findNodes(Set treeNodeIds) {
        Set treeNodes = new HashSet();
        findNodes(getRoot(), treeNodeIds, treeNodes);
        return treeNodes;
    }

    protected void findNodes(ITreeNode treeNode, Set treeNodeIds, Set treeNodes){
        if(treeNodeIds.contains(treeNode.getId())){
            treeNodes.add(treeNode);
        }

        Iterator children = treeNode.getChildren().iterator();
        while(children.hasNext()){
            findNodes((ITreeNode) children.next(), treeNodeIds, treeNodes);
        }
    }

    public boolean isExpanded(String treeNodeId){
        return this.expanded.contains(treeNodeId);
    }
	
//	/**
//	 * 展开节点treeNodeId，curLevel为当前层级
//	 */
//	
//    public void expand(String treeNodeId,int curLevel) {
//        this.expanded.add(treeNodeId);
//		ITreeNode expandedNode = findNode(treeNodeId);
//		/**
//		 * 如果需要实时刷新所有节点的信息，则调用notifyObservers方法通知所有的其他的节点
//		 * 及时刷新信息
//		 */
//		System.out.println("expand treenodeID 2:" + treeNodeId);
//		if (needObservable)
//		{
//			notifyObservers(expandedNode);
//		}		
//        if(this.expandListeners.size() > 0){
//            
//            
//            /**
//			if(expandedNode == null)
//			{
//				System.out.println("expandedNode:null");
//				expanded.remove(treeNodeId);
//				return;
//			}
//			*/
//			
//            Iterator iterator = this.expandListeners.iterator();
//            while(iterator.hasNext()){
//                ((IExpandListener) iterator.next()).nodeExpanded(expandedNode, this,curLevel,needObservable);
//            }            
//        }
//    }
    
    /**
     * 
     *  Description:
     * @param expandedNode
     * @param curLevel
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(com.frameworkset.common.tag.tree.itf.ITreeNode, int)
     */
	public void expand(ITreeNode expandedNode,int curLevel) {
		this.expanded.add(expandedNode.getId());
		//当展开事件是由于手动即点击页面的展开图标触发时，广播该事件
		if (needObservable && curLevel > level)
		{
			notifyObservers(expandedNode);
		}
		if(this.expandListeners.size() > 0){		
		Iterator iterator = this.expandListeners.iterator();
		while(iterator.hasNext()){
				((IExpandListener) iterator.next()).nodeExpanded(expandedNode, this,curLevel,needObservable);
			}
		}		
	}
	
	
    /**
     *  静态模式时使用
     *  Description:
     * @param expandedNode
     * @param curLevel
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(com.frameworkset.common.tag.tree.itf.ITreeNode, int)
     */
	public void impactExpand(ITreeNode expandedNode,int curLevel) {
//		this.expanded.add(expandedNode.getId());
		//当展开事件是由于手动即点击页面的展开图标触发时，广播该事件
		if (needObservable && curLevel > level)
		{
			notifyObservers(expandedNode);
		}
		if(this.expandListeners.size() > 0){		
		Iterator iterator = this.expandListeners.iterator();
		while(iterator.hasNext()){
				((IExpandListener) iterator.next()).impactExpandNode(expandedNode, this,curLevel,needObservable);
			}
		}		
	}
    
    /**
     * 页面上点击展开图标时，调用本方法展开点击的节点
     *  Description:
     * @param treeNodeId
     * @see com.frameworkset.common.tag.tree.itf.ITree#expand(java.lang.String)
     */
	public void expand(String treeNodeId,
					   String mode,
					   String scope,
					   HttpServletRequest request) {
		//modified by biaoping.yin on 2005-02-05
//		this.expanded.add(treeNodeId);
//		ITreeNode expandedNode = findNode(treeNodeId);	
//		if (needObservable)
//		{
//			notifyObservers(expandedNode);
//		}	
//		if(this.expandListeners.size() > 0){
//			Iterator iterator = this.expandListeners.iterator();
//			while(iterator.hasNext()){
//				((IExpandListener) iterator.next()).nodeExpanded(expandedNode, this,needObservable);
//			}
//		}
//		System.out.println("expand treenodeID 0:" + treeNodeId);
//		if(mode == null || mode.equals(""))
//		{
//			ITreeNode expandedNode = findNode(treeNodeId);
//			this.curExpanded = expandedNode;
//			expand(expandedNode,this.getUnknownLevel());
//		}
		if(mode.equals(Tree.mode_static_dynamic))
		{
//			 linkUrl += "__nodename=" + name;
//		      linkUrl += "__nodetype=" + nodetype;
//		      linkUrl += "__nodepath=" + nodepath;
			String treeName = request.getParameter("__nodename");
			String type = request.getParameter("__nodetype");
			String path = request.getParameter("__nodepath");
			String isfirstChild = request.getParameter("__nodefirst");
			String islastChild = request.getParameter("__nodelast");
			
			ITreeNode expandedNode =
	            new TreeNode(
	            	treeNodeId,
	                treeName,
	                type,
	                true,
	                null,
	                null,
	                null,
	                null, path, null);
			if(isfirstChild != null && !isfirstChild.equals("true"))
			{
				ITreeNode leftNode = new TreeNode();
				expandedNode.setLeftNode(leftNode);
			}
			
			if(islastChild != null && !islastChild.equals("true"))
			{
				ITreeNode lastNode = new TreeNode();
				expandedNode.setRightNode(lastNode);
			}
//			else
//				node =
//					new TreeNode(
//						treeid,
//						treeName,
//						type,
//						showHref,
//						this,
//						memo,
//						radioValue,
//						checkboxValue);
	        /**
	         * mark hasSon;
	         */
			expandedNode.setHasChildren(true);
			expandedNode.setTree(this);
	        this.curExpanded = expandedNode;
			expand(expandedNode,this.getUnknownLevel());
		}
		else
		{
			ITreeNode expandedNode = findNode(treeNodeId);
			this.curExpanded = expandedNode;
			expand(expandedNode,this.getUnknownLevel());
		}
	}
	
	/**
	 * 
	 *  Description:点击节点的折叠图标时调用本方法
	 * @param treeNodeId
	 * @see com.frameworkset.common.tag.tree.itf.ITree#collapse(java.lang.String)
	 */
    public void collapse(String treeNodeId) {
        this.expanded.remove(treeNodeId);
//        System.out.println("collapse treenodeID:" + treeNodeId);
//		System.out.println("treenodeID 4050 is expanded:" + this.isExpanded("4050"));
//		System.out.println("tree:" + this);
		
		ITreeNode collapsedNode = findNode(treeNodeId);		
		
		if (needObservable)
		{
			notifyObservers(collapsedNode);
		}
        if(this.collapseListeners.size() > 0){           
            
            Iterator iterator = this.collapseListeners.iterator();
            while(iterator.hasNext()){
                ((ICollapseListener) iterator.next()).nodeCollapsed(collapsedNode, this,needObservable);
            }
        }	
		//System.out.println("treenodeID 4050 is expanded:" + this.isExpanded("4050"));
    }
    
//	public void collapse(ITreeNode collapsedNode) {
//		this.expanded.remove(collapsedNode.getId());
//		if (needObservable)
//		{
//			notifyObservers(collapsedNode);
//		}
//		if(this.collapseListeners.size() > 0){
//			//ITreeNode collapsedNode = findNode(treeNodeId);
//			Iterator iterator = this.collapseListeners.iterator();
//			while(iterator.hasNext()){
//				((ICollapseListener) iterator.next()).nodeCollapsed(collapsedNode, this,needObservable);
//			}
//		}
//
//	}
    public Set getExpandedNodes() {
        return findNodes(this.expanded);
    }

    public void addExpandListener(IExpandListener expandListener) {
        this.expandListeners.add(expandListener);
    }

    public void removeExpandListener(IExpandListener expandListener) {
        this.expandListeners.remove(expandListener);
    }

    public void addCollapseListener(ICollapseListener collapseListener) {
        this.collapseListeners.add(collapseListener);
    }

    public void removeCollapseListener(ICollapseListener collapseListener) {
        this.collapseListeners.remove(collapseListener);
    }

    public boolean isSelected(String treeNodeId) {
        return this.selected.contains(treeNodeId);
    }

    public void select(String treeNodeId) {
        if(isSingleSelectionMode()){
            unSelectAll();
        }
		ITreeNode selectedNode = findNode(treeNodeId);
		
        this.selected.add(treeNodeId);
		if (needObservable)
		{
			notifyObservers(selectedNode);
		}

        if(this.selectListeners.size() > 0){
            
            
            Iterator iterator = this.selectListeners.iterator();
            while(iterator.hasNext()){
                ((ISelectListener) iterator.next()).nodeSelected(selectedNode, this,needObservable);
            }
        }
    }

    public void unSelect(String treeNodeId) {
        this.selected.remove(treeNodeId);
		ITreeNode unselectedNode = findNode(treeNodeId);
		if (needObservable)
		{
			notifyObservers(unselectedNode);
		}
        if(this.unselectListeners.size() > 0){			
            Iterator iterator = this.unselectListeners.iterator();
            while(iterator.hasNext()){
                ((IUnselectListener) iterator.next()).nodeUnselected(unselectedNode, this,needObservable);
            }
        }
    }
    
	public void unSelect(ITreeNode unselectedNode) {
		this.selected.remove(unselectedNode.getId());
		if (needObservable)
		{
			notifyObservers(unselectedNode);
		}
		if(this.unselectListeners.size() > 0){
			//ITreeNode unselectedNode = findNode(treeNodeId);
			Iterator iterator = this.unselectListeners.iterator();
			while(iterator.hasNext()){
				((IUnselectListener) iterator.next()).nodeUnselected(unselectedNode, this,needObservable);
			}
		}
		}


    public void unSelectAll() {
        Iterator iterator =  this.selected.iterator();
        while(iterator.hasNext()){
            unSelect((String) iterator.next());
        }
    }

    public Set getSelectedNodes() {
        return findNodes(this.selected);
    }

    public void setSingleSelectionMode(boolean mode) {
        this.singleSelectionMode = mode;
    }

    public boolean isSingleSelectionMode(){
        return this.singleSelectionMode;
    }
    
   

    public void addSelectListener(ISelectListener selectListener) {
        this.selectListeners.add(selectListener);
    }

    public void removeSelectListener(ISelectListener selectListener) {
        this.selectListeners.remove(selectListener);
    }

    public void addUnselectListener(IUnselectListener unselectListener) {
        this.unselectListeners.add(unselectListener);
    }

    public void removeUnselectListener(IUnselectListener unselectListener) {
        this.unselectListeners.remove(unselectListener);
    }

    public Iterator iterator(boolean includeRootNode) {
        return new TreeIterator(this, includeRootNode);
    }
    
    public Iterator iterator(String parentIndent) {
        return new TreeIterator(this, parentIndent);
    }
    
    /**
     * added by yinbiaoping on 2004/03/23
     * 重载方法java.util.Observable#notifyObservers(Object o)
     */
	public void notifyObservers(Object o)
	{
		setChanged();
		super.notifyObservers(o);		
	}    
	/**
	 * Description:
	 * @return
	 * boolean
	 */
	public boolean isRefreshNode() {
		return refreshNode;
	}

	/**
	 * Description:
	 * @param b
	 * void
	 */
	public void setRefreshNode(boolean b) {
		refreshNode = b;
	}
	
	/**
	 * 不知道当前层级时，调用本方法获取当前层级
	 * 没有指定节点所处的当前层级，为了避免与其他已有层级发生冲突，指定当前层级比默认层级大1，
	 * 这样setSon方法调用addNode方法不至于因为当前所处的层级
	 * 比默认展开层级小而展开其下级的情况出现
	 * Description:
	 * @return
	 * int
	 */
	protected int getUnknownLevel()
	{
		return level + 1;
	}
	
	public void setSortable(boolean b)
	{
		this.sortable = b;
		
	}


	public boolean isSortable()
	{
		return sortable;
	}

	/**
	 * 获取当前展开的节点信息，
	 * 主要在动静态方式生成树时当太地返回当前的树节点的html信息
	 */
	public ITreeNode getCurExpanded()
	{
//		return curExpanded == null ? this.root : curExpanded;
        return curExpanded ;
	}

	public boolean isDynamic()
	{
//		if(this.newmode)
			return this.mode.equals("dynamic");
//		return dynamic;
	}
	
	public boolean isStatic()
	{
//		if(this.newmode)
			return this.mode.equals("static");
//		else
//		{
//			return false;
//		}
	}
	
	public boolean isStaticDynamic()
	{
//		if(this.newmode)
			return this.mode.equals("static-dynamic");
//		else
//		{
//			return !this.dynamic;
//		}
	}

	public void setDynamic(boolean dynamic)
	{
		this.dynamic = dynamic;
	}
//	private boolean newmode = false;
	private String mode;
	private boolean partuprecursive = false;
	public void setMode(String mode)
	{
		
		this.mode = mode;
//		if(mode != null && !mode.trim().equals(""))
//			this.newmode = true;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	public boolean isUprecursive() {
		return uprecursive;
	}

	public void setUprecursive(boolean uprecursive) {
		this.uprecursive = uprecursive;
	}
	
	
	public void setPartuprecursive(boolean partuprecursive)
	{
		this.partuprecursive  = partuprecursive;
	}
	
	public boolean isPartuprecursive()
	{
		return partuprecursive;
	}

}
