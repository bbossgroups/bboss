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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.frameworkset.util.I18NUtil;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.TreeFactory;

/**
 * 设置获取树的节点数据信息，为构造树节点构造树节点
 * @author biaoping.yin
 * created on 2005-3-25
 * version 1.0
 */

public class TreeData extends BaseTag {

	private boolean enablecontextmenu = false;
	/**
		 * 树根节点checkbox 的值
		 */
	

	private String checkboxValue;

	/**
	 * 定义树数据缓冲作用范围：
	 * request
	 * session
	 * pageContext
	 * 缺省为session
	 *
	 */
	private String scope = "session";

	/**
	 * 树根节点radio按钮值
	 */
	private String radioValue;
	private final static Logger log = Logger.getLogger(TreeData.class);
	/**
	 * 树根节点id
	 */
	private String rootid = "-1";
	/**
	 * 树根节点名称
	 */
	private String rootName = "根节点";
	
	private String rootNameCode;
	/**
	 * 树默认展开层级
	 */
	private String expandLevel = "1";

	/**
	 * 单选模式，true:单选，false:多选
	 */
	private String singleSelection = "false";
	/**
	 * 是否实时监控每个节点的信息，包括节点是否存在、节点的名称、有没有儿子等信息
	 * true：实时
	 * false:每次只跟踪当前节点的信息
	 */
	private boolean needObserver = false;

	/**
	 * 本属性控制是否及时刷新触发某个事件的焦点节点信息
	 */
	protected boolean refreshNode = true;

	private String treetype = "1";

	/**
	 * 扩展条件，通过该条件来过滤树的节点
	 */
	private String extCondition = "";

	private String showRootHref = "true";

	/**
	 * 定义根节点备注信息
	 */
	private String memo = "";
	
	/**
	 * 指定是否对树节点进行排序,缺省为true
	 */
	private boolean sortable = false;

    /**
     * 根路径对应的文件路径
     */
    private String path = null;
    
    public int doEndTag() throws JspException
	{
//		this.checkboxValue = null;
//		this.enablecontextmenu = false;
//		this.expandLevel = "1";
//		this.extCondition = "";
//		this.id = null;
//		this.memo = "";
//		this.needObserver = false;
//		this.path = null;
//		this.radioValue = null;
//		this.refreshNode = true;
//		this.rootid = "-1";
		this.rootName = "根节点";
//		this.scope = "session";  
//		this.showRootHref = "true" ;
//		this.singleSelection = "false";
//		this.sortable = false;
//		this.treetype = "1";
		
			    
		return super.doEndTag();		
	}

	/**
	 * 根节点复选框的值
	 * @return String
	 */
	public String getCheckboxValue() {
		return checkboxValue;
	}

	/**
	 * @return String
	 */
	public String getRadioValue() {
		return radioValue;
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

	public TreeData() {

	}

	private String getExtCondition() {
		String extCondition = this.getHttpServletRequest().getParameter("extCondition");
		if (extCondition == null)
			extCondition = (String) this.getHttpServletRequest().getAttribute("extCondition");
		return extCondition;
	}
	
	public boolean needloadroot(TreeTag parent)
	{
		String expandid = request.getParameter(parent.getExpandParam());
		return expandid == null || this.getScope().equals("session");
	}

	public int doStartTag() {
		TreeTag parent = (TreeTag) this.getParent();
		//设置缓冲数据有效范围
		parent.setScope(getScope());
		parent.setEnablecontextmenu(this.isEnablecontextmenu());
		String key = parent.getTree();
        
		extCondition = getExtCondition();
		String request_scope = this.getHttpServletRequest().getParameter("request_scope");

		COMTree comTree  = null;
		HttpSession session = this.getSession();
		HttpServletRequest request = this.getHttpServletRequest();
		//从session中获取com tree
		if(session != null &&getScope().equals("session"))
			comTree = (COMTree) session.getAttribute(key);
		//从pageContext中获取com tree
		else if(getScope().equals("pageContext"))
			comTree = (COMTree) pageContext.getAttribute(key);
		//从request_session中获取com tree实例

//		else if(session != null &&request_scope != null && request_scope.equals("request"))
		else if(request_scope != null && request_scope.equals("request"))
		{
//			comTree = (COMTree) session.getAttribute(key);
			comTree = (COMTree) request.getAttribute(key);
		}
		String parent_indent = request.getParameter("node_parent_indent");
		if(comTree != null)
        {
            comTree.setPageContext(pageContext);
            comTree.setNeedObservable(needObserver());
            comTree.setRefreshNode(isRefreshNode());
            comTree.setExtCondition(extCondition);
            comTree.setEnablecontextmenu(this.isEnablecontextmenu());
            comTree.setSortable(this.isSortable());
            
            /**
             * 如果第一次构建动静结合的右键菜单需要构建所有的类型右键菜单，
             * 否则需要清除之前构建的所有节点类型的右键菜单
             */
	        if( parent_indent == null)
	        	comTree.buildContextMenusWraper();
	        else
	        	comTree.clearNodeContextmenus();
	        
            
        }
		//如果名称为key的树已经在session中存在，则判断条件是否相同，如果不相同，需要重新初始化树
        
		if (comTree != null
			&& extCondition != null
			&& !extCondition.trim().equals(comTree.getExtCondition())) {
			

			int level = 1;
			try
			{
				level = Integer.parseInt(getExpandLevel().trim());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("getExpandLevel():" + getExpandLevel());
			}
			//如果不允许折叠操作，则将树的默认展开层级增加100
			if(!parent.isCollapse())
			    level += 100;
			if(parent.getMode() == null)
			{
				comTree.setDynamic(parent.isDynamic());
				
			}
			else
			{
				comTree.setMode(parent.getMode());
			}
			
			comTree.setRecursive(parent.isRecursive());
			comTree.setUprecursive(parent.isUprecursive());
			comTree.setPartuprecursive(parent.isPartuprecursive());
			
			comTree.loadTree(
				getRootid(),
				getI18NRootName(),
				level,
				showRootHref(),
				getMemo(),getRadioValue(),getCheckboxValue(),path);
			comTree.setSingleSelectionMode(singleSelection());
		}
		//如果名称为key的树在session中不存在，初始化相应类型的树，并存放到session中，名称为key
		if (comTree == null) {
			String type = getTreetype();
			comTree = (COMTree) TreeFactory.getTreeData(type);
			if (comTree == null) {
				log.info(
					"type " + type + " not found in treedata.properties!!");
				return SKIP_BODY;
			}
			comTree.setPageContext(pageContext);
			comTree.setNeedObservable(needObserver());
			comTree.setRefreshNode(isRefreshNode());
			comTree.setSortable(this.isSortable());
			if (extCondition != null)
				comTree.setExtCondition(extCondition);
			int level = Integer.parseInt(getExpandLevel());
//			如果不允许折叠操作，则将树的默认展开层级增加100
			if(!parent.isCollapse())
			    level += 100;
			
			comTree.setEnablecontextmenu(this.isEnablecontextmenu());
			comTree.buildContextMenusWraper();
			if(parent.getMode() == null)
			{
				comTree.setDynamic(parent.isDynamic());
				
			}
			else
			{
				comTree.setMode(parent.getMode());
			}
			comTree.setRecursive(parent.isRecursive());
			comTree.setUprecursive(parent.isUprecursive());
			comTree.setPartuprecursive(parent.isPartuprecursive());
			if(needloadroot(parent))
			{
				comTree.loadTree(
					getRootid(),
					getI18NRootName(),
					level,
					showRootHref(),
					getMemo(),getRadioValue(),getCheckboxValue(),path);
			}
			else
			{
				comTree.addExpandListener();
				comTree.addCollapseListener();
				comTree.addSelectListener();
				comTree.level = level;
			}
			comTree.setSingleSelectionMode(singleSelection());
			
			//缓冲已初始化的com tree到session中
			if(session != null && getScope().equals("session"))
				session.setAttribute(key, comTree);
			//缓冲已初始化的com tree到request_session 中
//			else if(session != null && getScope().equals("request"))
//				session.setAttribute(key, comTree);
			else if( getScope().equals("request"))
				request.setAttribute(key, comTree);
			else if(getScope().equals("pageContext"))
				pageContext.setAttribute(key, comTree);
		}
		
		return SKIP_BODY;
	}
	
	
	

	/**
	 * Access method for the treetype property.
	 *
	 * @return   the current value of the treetype property
	 */
	public String getTreetype() {
		return treetype == null ? "1" : treetype;
	}

	/**
	 * Sets the value of the treetype property.
	 *
	 * @param aTreetype the new value of the treetype property
	 */
	public void setTreetype(String aTreetype) {
		treetype = aTreetype;
	}


	/**
	 * @return String
	 */
	public String getExpandLevel() {
		return expandLevel == null ? "1" : expandLevel;
	}

	/**
	 * @return boolean
	 */
	public boolean needObserver() {
		return needObserver ;
	}

	/**
	 * @return String
	 */
	public String getRootid() {
		return rootid == null ? "-1" : rootid;
	}

	/**
	 * @return String
	 */
	public String getRootName() {
		return rootName;
	}
	
	/**
	 * @return String
	 */
	public String getI18NRootName() {
		if(this.getRootNameCode() == null)
		{
			return this.getRootName();
		}
		else
		{	
			return I18NUtil.getI18nMessage(this.getRootNameCode(), this.rootName, request);
		}
	}

	/**
	 * @return boolean
	 */
	public boolean singleSelection() {
		return singleSelection == null
			? true
			: new Boolean(singleSelection).booleanValue();
	}

	/**
	 * @param string
	 */
	public void setExpandLevel(String string) {
		expandLevel = string;
	}

	/**
	 * @param string
	 */
	public void setNeedObserver(boolean string) {
		needObserver = string;
	}

	/**
	 * @param string
	 */
	public void setRootid(String string) {
		rootid = string;
	}

	/**
	 * @param string
	 */
	public void setRootName(String string) {
		rootName = string;
	}

	/**
	 * @param string
	 */
	public void setSingleSelection(String string) {
		singleSelection = string;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public boolean showRootHref() {
		return showRootHref == null
			? true
			: !showRootHref.equalsIgnoreCase("false");
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setShowRootHref(String string) {
		showRootHref = string;
	}

	/**
	 * @return String
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param string
	 */
	public void setMemo(String string) {
		memo = string;
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
	 * Description:
	 * @return
	 * String
	 */
	public String getScope() {
		return scope;
	}

    public String getPath() {
        return path;
    }

    /**
	 * Description:
	 * @param string
	 * void
	 */
	public void setScope(String string) {
		scope = string;
	}

    public void setPath(String path) {
        this.path = path;
    }

	public boolean isEnablecontextmenu() {
		return enablecontextmenu;
	}

	public void setEnablecontextmenu(boolean enablecontextmenu) {
		this.enablecontextmenu = enablecontextmenu;
	}

	public boolean isSortable()
	{
		return sortable;
	}

	public void setSortable(boolean sortable)
	{
		this.sortable = sortable;
	}

	public String getRootNameCode() {
		return rootNameCode;
	}

	public void setRootNameCode(String rootNameCode) {
		this.rootNameCode = rootNameCode;
	}

}
