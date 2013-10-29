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

package com.frameworkset.common.tag.tree.itf;

import com.frameworkset.common.tag.contextmenu.ContextMenu;


/**
 *
 * @author biaoping.yin
 * created on 2005-3-13
 * version 1.0
 */

public interface ActiveTree  extends ContextMenu{
	/**
	 * 功能说明：判断结点nodeId是否有儿子
	 * @param node
	 * @return boolean
	 */
	public boolean hasSon(ITreeNode node);

//	/**
//	 * 功能说明：将结点nodeId的直接儿子添加到目录树
//	 * @param nodeId
//	 * @return boolean false表示设置不成功，true表示设置成功
//	 */
//	public boolean setSon(ITreeNode node);

	/**
	 *
	 * @param node
	 * @param curLevel
	 * @return boolean false表示设置不成功，true表示设置成功
	 */
	public boolean setSon(ITreeNode node,int curLevel);

	/**
	 * 往目录树中添加一个结点
	 * @param father
	 * @param treeid
	 * @param type
	 * @param hasSon
	 * @param curLevel
	 * @return ITreeNode
	 */
	public ITreeNode addNode(
							 ITreeNode father,
							 String treeid,
							 String name,
							 String type,
							 boolean hasSon,
							 int curLevel,String path);
	/**
		 * 往目录树中添加一个结点
		 * @param father
		 * @param treeid
		 * @param name
		 * @param type
		 * @param hasSon
		 * @return ITreeNode
		 */
		public ITreeNode addNode(
								 ITreeNode father,
								 String treeid,
								 String name,
								 String type,
								 boolean hasSon,String path
								 );
	/**
	 * 功能说明：添加根结点
	 * @param rootid 根结点id
	 * @param rootname 根结点名称
	 * @param curLevel 默认打开层级
	 * @param showRootHref 是否显示链接
	 * @return ITreeNode
	 */
	public void addRootNode(String rootid,String rootname ,int curLevel,boolean showRootHref,String path);

	/**
	 * add root node to tree where the nodeid is rootid, the node name is rootname
	 * @param rootid
	 * @param rootname
	 */
	public void addRootNode(String rootid,String rootname, String path);

	public void notifyObservers(Object o);

//	/**
//	 * 丛数据源中获取treeid为id的最新结点
//	 * @param id
//	 * @return ITreeNode
//	 */
//	public ITreeNode getTreeNode(String id);

  /**
   * 获取节点的最新信息
   * @param oldNode ITreeNode
   * @return ITreeNode
   */

    public ITreeNode getTreeNode(ITreeNode oldNode);
	/**
	 * 更新树节点信息
	 * @param tree
	 * @param node
	 * @return ITreeNode
	 */
	public ITreeNode updateNode(ITree tree,ITreeNode node);




}
