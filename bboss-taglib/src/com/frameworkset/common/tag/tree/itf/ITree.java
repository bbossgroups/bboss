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


package com.frameworkset.common.tag.tree.itf;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * <p>Title: com.frameworkset.common.tag.tree.itf.ITree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2004-9-15
 * @author biaoping.yin
 * @version 1.0
 */
public interface ITree extends ActiveTree {

    /**
     * Returns the root node of this tree.
     * @return The root node.
     */
    public ITreeNode getRoot();

    /**
     * Sets the root node of this tree.
     * @param node The node to set as root node for this tree.
     */
    public void      setRoot(ITreeNode node);

    /**
     * Returns the <code>ITreeNode</code> instance coresponding to the given treeNodeId. If
     * no node matches the given node id, null is returned.
     * Note: If more than one node has this node id, only the first one found is returned.
     * This shouldn't normally happen, since node id's are supposed to be unique within
     * the tree. But the tree doesn't enforce this uniqueness (as of now).
     * @param treeNodeId The id of the node to find.
     * @return The node with the id matching the treeNodeId parameter, or null if no node
     * in the tree matches the node.
     */
    public ITreeNode findNode  (String treeNodeId);


    /**
     * Returns the nodes matching the node id's in the passed set.
     * @param treeNodeIds A <code>Set</code> of node id's (<code>String</code> instances)
     * @return A <code>Set</code> of <code>ITreeNode</code> instances.
     */
    public Set findNodes(Set treeNodeIds);

    /**
     * Returns true if the node with the given node id is expanded.
     * @param treeNodeId The id of the node to check if is expanded.
     * @return True if the node is expanded, false if not.
     */
    public boolean   isExpanded(String treeNodeId);

    /**
     * Marks the node with the given node id as expanded, and notifies all expand listeners.
     * @param treeNodeId The id of the node to mark as expanded.
     */
    public void      expand    (String treeNodeId,String mode,String scope,HttpServletRequest request);
	//public void      expand    (ITreeNode node);
	public void      expand    (ITreeNode node,int curLevel);
	

    /**
     * Removes the expand mark of the node with the given node id, and notifies all collapse
     * listeners.
     * @param treeNodeId The id of the node to remove the expand mark from.
     */
    public void      collapse  (String treeNodeId);
    
	//public void      collapse  (ITreeNode node);
	

    /**
     * Returns the expanded nodes in this tree.
     * @return A <code>Set</code> of <code>ITreeNode</code> instances.
     */
    public Set       getExpandedNodes();

    /**
     * Adds an expand listener to this tree. All expand listeners are notified
     * whenever a node is expanded in this tree.
     * @param expandListener The <code>IExpandListener</code> instance to add.
     */
    public void      addExpandListener      (IExpandListener expandListener);

    /**
     * Removes an expand listener from this tree.
     * @param expandListener The expand listener to remove.
     */
    public void      removeExpandListener   (IExpandListener expandListener);

    /**
     * Adds a collapseListener to this three. All collapse listeners are notified
     * whenever a node is collapsed in this tree.
     * @param collapseListener The collapse listener to add.
     */
    public void      addCollapseListener    (ICollapseListener collapseListener);

    /**
     * Removes a collapse listener from this tree.
     * @param collapseListener The collapse listener to remove.
     */
    public void      removeCollapseListener (ICollapseListener collapseListener);

    /**
     * Returns true if the node with the given node id is selected.
     * @param treeNodeId The id of the node to check if is selected.
     * @return True if the node is selected, false if not.
     */
    public boolean   isSelected(String treeNodeId);

    /**
     * Marks the node with the given node id as selected, and notifies all select listeners.
     * A selected node can
     * be used to render that node's text in bold (or whatever you choose).
     * The nodeMatch tag can match on selected/unselected nodes.
     * @param treeNodeId The id of the node to mark as selected.
     */
    public void      select    (String treeNodeId);

    /**
     * Removes the selected mark of the node with this node id, and notifies all
     * unselect listeners.
     * @param treeNodeId The id of the node to remove the selected mark from.
     */
    public void      unSelect  (String treeNodeId);
    
	public void      unSelect  (ITreeNode node);

    /**
     * Unselects all nodes in this tree, and notifies all unselect listeners.
     * This method is called by the Tree (ITree implementation) whenever
     * a node is selected in single select mode, before marking the new node
     * as selected.
     */
    public void      unSelectAll();

    /**
     * Returns the selected nodes in this tree.
     * @return  A <code>Set</code> of <code>ITreeNode</code> instances.
     */
    public Set       getSelectedNodes();

    /**
     * Adds a select listener to this tree. All select listeners are notified whenever
     * a node is selected in this tree.
     * @param selectListener The select listener to add.
     */
    public void      addSelectListener      (ISelectListener selectListener);

    /**
     * Removes a select listener from this tree.
     * @param selectListener The select listener to remove.
     */
    public void      removeSelectListener   (ISelectListener selectListener);

    /**
     * Adds an unselect listener to this tree. All unselect listeners are notified whenever
     * a node is unselected in this tree.
     * @param unselectListener The unselect listener to add.
     */
    public void      addUnselectListener    (IUnselectListener unselectListener);

    /**
     * Removes an unselect listener form this tree.
     * @param unselectListener The unselect listener to remove.
     */
    public void      removeUnselectListener (IUnselectListener unselectListener);

    /**
     * If you call setSingleSelectionMode(true) the tree will unselect all
     * nodes before selecting a node. This way only one node can be
     * selected at any time.
     * @param mode  True to set the tree in single selection mode, false to set
     *              the tree in multiple selection mode.
     */
    public void      setSingleSelectionMode(boolean mode);

    /**
     * Returns true if this tree is in single selection mode. The default is
     * that the tree is not in single selection mode.
     * @return True if this tree is in single selection mode, false if not.
     */
    public boolean   isSingleSelectionMode();

    /**
     * Returns an iterator of the nodes in the tree. The nodes are wrapped in
     * ITreeIteratorElements instances, that contain extra info about the nodes
     * iterated, for instance if it is expanded etc. The ITreeNode doesn't have
     * this info itself.
     *
     * <br/><br/>
     * The tree nodes are iterated in the same sequence they would be displayed in, meaning
     * depth first mode.
     *
     * <br/><br/>
     * This method is normally only used by the TreeTag class, to iterate the nodes in
     * the tree.
     *
     * @param includeRootNode True if you want to include the root node in this
     *          iterator. False if you want to exclude the root node.
     * @return An iterator containing ITreeIteratorElements wrapping the nodes in the tree.
     */
    public Iterator  iterator(boolean includeRootNode);
    
    public void buildContextMenusWraper();
    
    public Set getContextmenus();
    
    public void setSortable(boolean b);
	


	public boolean isSortable();
	
	public ITreeNode getCurExpanded();
	
	/**
	 * 判断当前的树是否是动态树
	 * @return
	 */
	public boolean isDynamic();
	
	public boolean isStatic();
	
	public boolean isStaticDynamic();
	
	public void setDynamic(boolean dynamic);
	
	public void setMode(String mode);

	public Iterator iterator(String parent_indent);
	
	/**
	 * 为复选框定义的一组函数
	 */
	
	/**
	 * 
	 * @return
	 */
	public boolean isUprecursive() ;

	public void setUprecursive(boolean uprecursive) ;
	
	public boolean isRecursive();
	
	public void setRecursive(boolean recursive);

	public void setPartuprecursive(boolean partuprecursive);
	public boolean isPartuprecursive();
	
		
	
  }
