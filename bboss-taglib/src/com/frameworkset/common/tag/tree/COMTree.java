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
 *                                                                           *
 *****************************************************************************/

/**
 * @author:biaoping.yin
 */

package com.frameworkset.common.tag.tree;

import java.util.Map;
import java.util.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.security.AccessControlInf;
import org.frameworkset.security.SecurityUtil;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.tree.impl.Tree;
import com.frameworkset.common.tag.tree.impl.TreeNode;
import com.frameworkset.common.tag.tree.itf.ICollapseListener;
import com.frameworkset.common.tag.tree.itf.IExpandListenerAdapter;
import com.frameworkset.common.tag.tree.itf.ISelectListener;
import com.frameworkset.common.tag.tree.itf.ITree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;



public abstract class COMTree extends Tree {
    private static final Logger log = Logger.getLogger(COMTree.class);
    /**
     * 扩展条件，通过该条件来过滤树的节点,获取符合条件的节点
     */
    protected String extCondition = "";
    
   

    /**
     * jsp页面上下文对象，该对象提供request,response,session的访问方法
     */
    protected transient PageContext pageContext;

    protected transient AccessControlInf accessControl = null;

    protected transient HttpServletRequest request = null;
    //(HttpServletRequest) pageContext.getRequest();
    protected transient JspWriter out = null; //pageContext.getOut();
    protected transient HttpSession session = null;
    protected transient HttpServletResponse response = null;
	
    
    
    public abstract boolean hasSon(ITreeNode father);
    
   
    /**
     * 本方法没有指定father节点所处的当前层级
     * 为了避免与其他已有层级发生冲突，指定当前层级比默认层级大1，
     * 这样setSon方法调用addNode方法不至于因为当前所处的层级
     * 比默认展开层级小而展开其下级的情况出现
     *
     *  Description:
     * @param father
     * @return boolean

     */
    public boolean setSon(ITreeNode father) {
        //modified by biaoping.yin
        //return setSon(father, 0);
        return setSon(father, getUnknownLevel());
    }

    /**
     * 往树中添加父节点的所有直接儿子
     */
    public abstract boolean setSon(ITreeNode father, int curLevel);

//	/**
//	 * 从数据源获取节点信息
//	 */
//	public abstract ITreeNode getTreeNode(String id);

    /**
     * 获取节点的最新信息，缺省的实现直接返回当前节点，用户可以重载该方法
     * @param oldNode ITreeNode
     * @return ITreeNode
     */
    public ITreeNode getTreeNode(ITreeNode oldNode) {
        return oldNode;
    }

    /**
     * 设置页面上下文对象
     */
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
        if (pageContext != null) {
            request = (HttpServletRequest) pageContext.getRequest();
            out = pageContext.getOut();
            session = request.getSession(false);
            response = (HttpServletResponse) pageContext.getResponse();
            /**
             * 2009.07.02 注释，如果放入系统平台需要打开
             */
            if(BaseTag.ENABLE_TAG_SECURITY)
            {   
//                accessControl = AccessControl.getAccessControl();
//                if(accessControl == null)
//                {
//                	accessControl = AccessControl.getInstance();
//                	accessControl.checkAccess(request,response,out,false);
//                }
            	accessControl = SecurityUtil.getAccessControl(request, response, out);
            }

        }

    }


    /**
     * 获取一个结点
     * @param treeid
     * @param treeName
     * @param type
     * @param needObservable 是否注册到观察器中
     * @deprecated BY BIAOPING.YIN REPLACED BY
     * getTreeNode(
     String treeid,
     String treeName,
     String type)
     *
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean needObservable) {

        return getTreeNode(
            treeid,
            treeName,
            type,
            hasSon,
            needObservable,
            null);
    }

    /**
     *
     * @param treeid
     * @param treeName
     * @param type
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type
        ) {

        return getTreeNode(
            treeid,
            treeName,
            type,
            null);
    }

    /**
     * 获取一个结点
     * @param treeid
     * @param treeName
     * @param type
     * @param needObservable 是否注册到观察器中
     * @deprecated BY BIAOPING.YIN REPLACED BY
     * getTreeNode(
     String treeid,
     String treeName,
     String type,
     String memo)
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean needObservable,
        String memo) {
        return getTreeNode(
            treeid,
            treeName,
            type,
            hasSon,
            true,
            needObservable,
            memo);
    }


    /**
     * 给定节点信息，构建树节点
     * Description:
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param needObservable
     * @deprecated by biaoping.yin on 2005-02-04 REPLACED BY
     * getTreeNode(
     String treeid,
     String treeName,
     String type,
     boolean showHref,
     String memo)
     * @return
     * ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        boolean needObservable,
        String memo) {
        return getTreeNode(
            treeid,
            treeName,
            type,
            hasSon,
            showHref,
            needObservable,
            memo,
            null,
            null, (String)null); //last parameter is path
    }

    /**
     *
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param needObservable
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @deprecated the replace method is getTreeNode(
     String treeid,
     String treeName,
     String type,
     boolean hasSon,
     boolean showHref,
     boolean needObservable,
     String memo,
     String radioValue,
     String checkboxValue)
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        boolean needObservable,
        String memo,
        String radioValue,
        String checkboxValue, String path) {

        ITreeNode node;
        Observable notifier = null;
        if (needObservable) {
            notifier = this;
        }

        //if (!needObservable)
        node =
            new TreeNode(
                treeid,
                treeName,
                type,
                showHref,
                notifier,
                memo,
                radioValue,
                checkboxValue, path);
//		else
//			node =
//				new TreeNode(
//					treeid,
//					treeName,
//					type,
//					showHref,
//					this,
//					memo,
//					radioValue,
//					checkboxValue);
        /**
         * mark hasSon;
         */
        node.setHasChildren(hasSon);
        return node;
    }

    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        boolean needObservable,
        String memo,
        String radioValue,
        String checkboxValue, String path, Map params) {

        ITreeNode node;
        Observable notifier = null;
        if (needObservable) {
            notifier = this;
        }

        //if (!needObservable)
        node =
            new TreeNode(
                treeid,
                treeName,
                type,
                showHref,
                notifier,
                memo,
                radioValue,
                checkboxValue, path, params);
//		else
//			node =
//				new TreeNode(
//					treeid,
//					treeName,
//					type,
//					showHref,
//					this,
//					memo,
//					radioValue,
//					checkboxValue);
        /**
         * mark hasSon;
         */
        node.setHasChildren(hasSon);
        return node;
    }


    /**
     * 给定节点信息，构建树节点
     * Description:
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param needObservable
     * @deprecated by biaoping.yin on 2005-02-04 ,replaced by
     * 		getTreeNode(
     String treeid,
     String treeName,
     String type,
     boolean showHref
     )
     * @return
     * ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        boolean needObservable) {

        return getTreeNode(
            treeid,
            treeName,
            type,
            hasSon,
            showHref,
            needObservable,
            null);
    }


    /**
     *
     * @param treeid
     * @param treeName
     * @param type
     * @param showHref
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        ITreeNode node;
        Observable notifier = null;
        if (needObservable) {
            notifier = this;
        }

        //if (!needObservable)

        node =
            new TreeNode(
                treeid,
                treeName,
                type,
                showHref,
                notifier,
                memo,
                radioValue,
                checkboxValue, path);
//		else
//			node =
//				new TreeNode(
//					treeid,
//					treeName,
//					type,
//					showHref,
//					this,
//					memo,
//					radioValue,
//					checkboxValue);
        /**
         * mark hasSon;
         */

        node.setHasChildren(hasSon(node));
        return node;
    }

    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        String memo,
        String radioValue,
        String checkboxValue, String path, Map params) {
        ITreeNode node;
        Observable notifier = null;
        if (needObservable) {
            notifier = this;
        }

        //if (!needObservable)

        node =
            new TreeNode(
                treeid,
                treeName,
                type,
                showHref,
                notifier,
                memo,
                radioValue,
                checkboxValue, path, params);
//		else
//			node =
//				new TreeNode(
//					treeid,
//					treeName,
//					type,
//					showHref,
//					this,
//					memo,
//					radioValue,
//					checkboxValue);
        /**
         * mark hasSon;
         */

        node.setHasChildren(hasSon(node));
        return node;
    }


    /**
     *
     * @param treeid String
     * @param treeName String
     * @param type String
     * @param memo String
     * @param radioValue String
     * @param checkboxValue String
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        String memo,
        String radioValue,
        String checkboxValue) {
        return getTreeNode(
            treeid,
            treeName,
            type,
            memo,
            radioValue,
            checkboxValue, null);

    }


    /**
     * 获取树节点信息
     * @param treeid
     * @param treeName
     * @param type
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        String memo,
        String radioValue,
        String checkboxValue, String path) {

        ITreeNode node;
        Observable notifier = null;
        if (needObservable) {
            notifier = this;
        }
        //if (!needObservable)
        node =
            new TreeNode(
                treeid,
                treeName,
                type,
                true,
                notifier,
                memo,
                radioValue,
                checkboxValue, path);
//		else
//			node =
//				new TreeNode(
//					treeid,
//					treeName,
//					type,
//					true,
//					notifier,
//					memo,
//					radioValue,
//					checkboxValue);
        /**
         * mark hasSon;
         */

        node.setHasChildren(hasSon(node));
        return node;
    }

    /**
     *
     * @param treeid 节点标识
     * @param treeName 节点名称
     * @param type 节点类型

     * @param showHref 是否显示超链接

     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean showHref, String path) {

        return getTreeNode(
            treeid,
            treeName,
            type,
            showHref,
            null, path);
    }

    /**
     *
     * @param treeid
     * @param treeName
     * @param type
     * @param showHref
     * @param memo
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        String memo, String path) {
        return getTreeNode(
            treeid,
            treeName,
            type,
            showHref,
            memo,
            null,
            null, path);
    }

    /**
     *
     * @param treeid
     * @param treeName
     * @param type
     * @param memo
     * @return ITreeNode
     */
    protected ITreeNode getTreeNode(
        String treeid,
        String treeName,
        String type,
        String memo) {
        return getTreeNode(
            treeid,
            treeName,
            type,
            true,
            memo);
    }

    /**
     *
     *  Description:
     * @param root
     * @param rootName
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addRootNode(java.lang.String, java.lang.String)
     * @deprecated 调试未通过，请勿使用
     */
    public void addRootNode(String root, String rootName, String path) {
        addRootNode(root, rootName, (String)null, path);
    }

    /**
     *
     *  Description:
     * @param root
     * @param rootName
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addRootNode(java.lang.String, java.lang.String)
     * @deprecated 调试未通过，请勿使用
     */
    public void addRootNode(String root, String rootName, String memo,
                            String path) {
        addRootNode(root, rootName, getUnknownLevel(), true, memo, path);
    }

    /**
     * 添加根节点
     *  Description:
     * @param root
     * @param rootName
     * @param curLevel

     */
    public void addRootNode(
        String root,
        String rootName,
        int curLevel,
        boolean showRootHref, String path) {
        addRootNode(root, rootName, curLevel, showRootHref, null, path);
    }

    /**
     * 添加根节点
     *  Description:
     * @param root
     * @param rootName
     * @param curLevel
     */
    public void addRootNode(
        String root,
        String rootName,
        int curLevel,
        boolean showRootHref,
        String memo, String path) {
        addRootNode(root, rootName, curLevel, showRootHref, memo, null, null,
                    path);
    }

    /**
     * 添加根节点
     *  Description:
     * @param root
     * @param rootName
     * @param curLevel

     */
    public void addRootNode(
        String root,
        String rootName,
        int curLevel,
        boolean showRootHref,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        ITreeNode rootNode;
        rootNode =
            getTreeNode(
                root,
                rootName,
                Const.NODE_ROOT + "",
                //false,
                showRootHref,
                //isNeedObservable(),
                memo,
                radioValue,
                checkboxValue, path);
        //boolean hasSon = hasSon(rootNode);
        //rootNode.setHasChildren(hasSon);

        setRoot(rootNode);
        /**
         * 当前的层级小于默认层级，并且有儿子则展开
         */
        //removed by biaoping.yin on 2005-02-05
//		boolean hasSon = rootNode.hasChildren();
//		if (hasSon && (curLevel < level || this.isExpanded(rootNode.getId())))
//			expand(rootNode, curLevel + 1);
//		if (!hasSon && isExpanded(rootNode.getId()))
//			collapse(rootNode);
        //added by biaoping.yin on 2005-02-05
        boolean hasSon = rootNode.hasChildren();
        if (hasSon && curLevel < level) {
            expand(rootNode, curLevel + 1);
        }

    }

    /**
     * 向父节点father中添加给定信息的子节点
     *  Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param curLevel
     * @deprecated by biaoping.yin replaced by
     * addNode(
      ITreeNode father,
      String treeid,
      String treeName,
      String type,
      int curLevel)
     * @return ITreeNode
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addNode(com.frameworkset.common.tag.tree.itf.ITreeNode, java.lang.String, java.lang.String, java.lang.String, boolean, int)
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        int curLevel, String path) {

        return addNode(father, treeid, treeName, type, hasSon, curLevel, null,
                       path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param curLevel
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel, String path) {

        return addNode(father, treeid, treeName, type, curLevel, null, path);
    }

    /**
     * 向父节点father中添加给定信息的子节点
     *  Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param curLevel
     * @deprecated by biaoping.yin replaced by
     * addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     int curLevel,
     String memo)
     * @return ITreeNode
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addNode(com.frameworkset.common.tag.tree.itf.ITreeNode, java.lang.String, java.lang.String, java.lang.String, boolean, int)
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        int curLevel,
        String memo, String path) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            hasSon,
            true,
            curLevel,
            memo, path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param curLevel
     * @param memo
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        String memo, String path) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            curLevel,
            true,
            memo, path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param curLevel
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        int curLevel, String path) {

        return addNode(
            father,
            treeid,
            treeName,
            type,
            hasSon,
            showHref,
            curLevel,
            null, path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param curLevel
     * @param memo
     * @deprecated by biaoping.yin replaced by
     * addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     int curLevel,
     boolean showHref,
     String memo)
     *
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        int curLevel,
        String memo, String path) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            hasSon,
            showHref,
            curLevel,
            memo,
            null,
            null, path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param curLevel
     * @param showHref
     * @param memo
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        boolean showHref,
        String memo, String path) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            showHref,
            curLevel,
            memo,
            null,
            null, path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @param curLevel
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @deprecated by biaoping.yin
     * replaced by
     * addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     boolean showHref,
     int curLevel,
     String memo,
     String radioValue,
     String checkboxValue) {
     *
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                hasSon,
                showHref,
                isNeedObservable(),
                memo,
                radioValue,
                checkboxValue, path);
        sNode.setParentPath(father.getPath());
        addNode(father, sNode, curLevel);
        return sNode;
    }

    /**
     *
     * @param father ITreeNode 父节点
     * @param treeid String 节点id
     * @param treeName String 节点名称
     * @param type String 节点类型
     * @param hasSon boolean 节点是否有儿子
     * @param showHref boolean 节点是否带链接
     * @param curLevel int 节点当前级别
     * @param memo String 节点备注字段
     * @param radioValue String 单选按钮的值
     * @param checkboxValue String 复选按钮值
     * @param path String 节点路径
     * @param params Map 节点参数集，将需要传递给链接的所有参数可以全部放到Map中
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path, Map params) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                hasSon,
                showHref,
                isNeedObservable(),
                memo,
                radioValue,
                checkboxValue, path, params);
        sNode.setParentPath(father.getPath());
        addNode(father, sNode, curLevel);
        return sNode;
    }


    /**
     *
     * @param father ITreeNode
     * @param treeid String
     * @param treeName String
     * @param type String
     * @param showHref boolean
     * @param curLevel int
     * @param memo String
     * @param radioValue String
     * @param checkboxValue String
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            showHref,
            curLevel,
            memo,
            radioValue,
            checkboxValue, (String)null);

    }

    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue,Map params) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            showHref,
            curLevel,
            memo,
            radioValue,
            checkboxValue, (String)null,params);

    }


    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param showHref
     * @param curLevel
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                showHref,
                memo,
                radioValue,
                checkboxValue, path);
        addNode(father, sNode, curLevel);
        return sNode;
    }

    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean showHref,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path, Map params) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                showHref,
                memo,
                radioValue,
                checkboxValue, path, params);
        addNode(father, sNode, curLevel);
        return sNode;
    }


    /**
     * 添加树节点信息
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param curLevel
     * @param memo
     * @param radioValue
     * @param checkboxValue
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                true,
                memo,
                radioValue,
                checkboxValue, path);
        addNode(father, sNode, curLevel);
        return sNode;
    }

    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue, String path,Map params) {
        ITreeNode sNode =
            this.getTreeNode(
                treeid,
                treeName,
                type,
                true,
                memo,
                radioValue,
                checkboxValue, path,params);
        addNode(father, sNode, curLevel);
        return sNode;
    }


    /**
     * 添加树节点信息
     * @param father ITreeNode
     * @param treeid String
     * @param treeName String
     * @param type String
     * @param curLevel int
     * @param memo String
     * @param radioValue String
     * @param checkboxValue String
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            curLevel,
            memo,
            radioValue,
            checkboxValue, (String)null);

    }

    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        int curLevel,
        String memo,
        String radioValue,
        String checkboxValue,Map params) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            curLevel,
            memo,
            radioValue,
            checkboxValue, null,params);

    }


    /**
     *
     * Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @deprecated by biaoping.yin
     * replaced by
     * public ITreeNode addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     boolean showHref,
     String type)
     * @return
     * ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref, String path) {

        return addNode(father, treeid, treeName, type, hasSon, showHref, null,
                       path);
    }

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param showHref
     * @param type
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        boolean showHref,
        String type, String path) {

        return addNode(father, treeid, treeName, type, showHref, null, path);
    }

    /**
     *
     * Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @param showHref
     * @deprecated by biaoping.yin
     * replaced by
     * @return
     *  addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     String memo,
     boolean showHref)
     * ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        boolean showHref,
        String memo, String path) {
//
//		return addNode(
//			father,
//			treeid,
//			treeName,
//			type,
//			hasSon,
//			showHref,
//			0,
//			memo);
        return addNode(
            father,
            treeid,
            treeName,
            type,
            hasSon,
            showHref,
            getUnknownLevel(),
            memo, path);
    }

    /**
     * 替换方法addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     boolean hasSon,
     boolean showHref,
     String memo)
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param memo
     * @param showHref
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        String memo,
        boolean showHref, String path) {

        return addNode(father, treeid, treeName, type, getUnknownLevel(),
                       showHref, memo, path);
    }

    /**
     *
     *  Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @deprecated by biaoping.yin
     * replaced by
     * addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type)
     * @return ITreeNode
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addNode(com.frameworkset.common.tag.tree.itf.ITreeNode, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon, String path) {
        return addNode(father, treeid, treeName, type, hasSon, null, path);
    }
    

    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type, String path
        ) {
        return addNode(father, treeid, treeName, type, null, path);
    }

    /**
     *
     *  Description:
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param hasSon
     * @deprecated by biaoping.yin
     * replaced by
     * addNode(
     ITreeNode father,
     String treeid,
     String treeName,
     String type,
     String memo)
     * @return ITreeNode
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#addNode(com.frameworkset.common.tag.tree.itf.ITreeNode, java.lang.String, java.lang.String, java.lang.String, boolean)
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        boolean hasSon,
        String memo, String path) {
        return this.addNode(father, treeid, treeName, type, hasSon, true, memo,
                            path);
    }


    /**
     *
     * @param father
     * @param treeid
     * @param treeName
     * @param type
     * @param memo
     * @return ITreeNode
     */
    public ITreeNode addNode(
        ITreeNode father,
        String treeid,
        String treeName,
        String type,
        String memo, String path) {
        return addNode(
            father,
            treeid,
            treeName,
            type,
            memo,
            true, path);
        //return this.addNode(father, treeid, treeName, type, hasSon, true, memo);
    }

    /**
     * add node to a tree,the father node is fNode and the son node is sNode,
     * the variable curLevel is the current layer the sNode line in the tree
     * @param fNode
     * @param sNode
     * @param curLevel
     * @return ITreeNode
     */
    private ITreeNode addNode(ITreeNode fNode, ITreeNode sNode, int curLevel) {
        fNode.addChild(sNode);
        fNode.addSonid(sNode.getId());
//        if(sNode.getTree().isRecursive())
//        {
//        	if(fNode.getParams() != null)
//        	{
//        		Boolean t = (Boolean)fNode.getParams().get("node_recursive");
//        		if(t == null || t.booleanValue())
//        		{
//        			fNode.addSonid(sNode.getId());
//        		}
//        		
//        	}
//        	else
//        	{
//        		fNode.addSonid(sNode.getId());
//        	}
//        	
//        }
        
//        if(sNode.getTree().isUprecursive())
//        {
//        	if(fNode.getParams() != null)
//        	{
//        		Boolean t = (Boolean)fNode.getParams().get("node_uprecursive");
//        		if(t == null || t.booleanValue())
//        		{
//        			fNode.getFatherids();
//        		}
//        		
//        	}
//        	else
//        	{
//        		fNode.getFatherids();
//        	}
//        	
//        }
        sNode.setTree(this);
        sNode.setParentPath(fNode.getPath());
        sNode.setParent(fNode);
        //modified by biaoping.yin on 2005-02-05
//		if (sNode.hasChildren()
//			&& (curLevel < level || isExpanded(sNode.getId())))
//			expand(sNode, curLevel + 1);

        // 如果节点有孩子并且当前层级小于默认层级，则展开节点sNode
        boolean flag = false;
        
        if (sNode.hasChildren() && curLevel < level) {
            expand(sNode, curLevel + 1);
            flag = true;
        }
        //如果节点有孩子，并且节点是展开的，并且节点的孩子没有获取，则重新展开节点
        //if (sNode.hasChildren() && refreshNode && this.isExpanded(sNode.getId()))
        if (!flag && sNode.hasChildren() && sNode.childrenSize() == 0 &&
            this.isExpanded(sNode.getId()) ) {
            expand(sNode, curLevel + 1);
            flag = true;
            
        }
        
        if(!flag && sNode.hasChildren() && this.isStatic() )
        {
        	this.impactExpand(sNode,curLevel);
        }
        
        
        //removed by biaoping.yin on 2005-02-05
//		if (!sNode.hasChildren() && isExpanded(sNode.getId()))
//			collapse(sNode);
        return sNode;
    }

    /**
     * add node to a tree,the father node is fNode and the son node is sNode
     * @param fNode
     * @param sNode
     * @return ITreeNode
     */
    private ITreeNode addNode(ITreeNode fNode, ITreeNode sNode) {
        //retrun addNode(fNode,sNode,0);
        return addNode(fNode, sNode, getUnknownLevel());
    }

    /**
     * 装载根结点rootid的目录树，level为默认展开层位
     * @param rootid
     * @param level
     */
    public void loadTree(
        String rootid,
        String rootName,
        int level,
        boolean showRootHref, String path) {
        loadTree(rootid, rootName, level, showRootHref, null, path);
    }

    /**
     * 装载根结点rootid的目录树，level为默认展开层位
     * @param rootid
     * @param level
     */
    public void loadTree(
        String rootid,
        String rootName,
        int level,
        boolean showRootHref,
        String memo, String path) {
        loadTree(
            rootid,
            rootName,
            level,
            showRootHref,
            memo, null, null, path);
    }

    /**
     * 装载根结点rootid的目录树，level为默认展开层位
     * @param rootid
     * @param level
     */
    public void loadTree(
        String rootid,
        String rootName,
        int level,
        boolean showRootHref,
        String memo,
        String radioValue,
        String checkboxValue, String path) {
        this.level = level;
        addExpandListener();
        addCollapseListener();
        addSelectListener();
        addRootNode(rootid, rootName, 0, showRootHref, memo, radioValue,
                    checkboxValue, path);
    }

    /**
     * load tree that the node id is rootid and the node name is rootName
     * @param rootid
     * @param rootName
     * @deprecated 不建议使用，调试没通过，请勿使用
     */
    public void loadTree(String rootid, String rootName, String path) {
        loadTree(rootid, rootName, null, path);
    }

    /**
     * load tree that the node id is rootid and the node name is rootName
     * @param rootid
     * @param rootName
     * @deprecated 不建议使用，调试没通过，请勿使用
     */
    public void loadTree(String rootid, String rootName, String memo,
                         String path) {

        //loadTree(rootid, rootName, 0, true, memo);
        loadTree(rootid, rootName, getUnknownLevel(), true, memo, path);
    }

    /**
     * 添加展开监听器
     */

    public void addExpandListener() {
        addExpandListener(new IExpandListenerAdapter() {
            public void nodeExpanded(
                ITreeNode node,
                ITree tree,
                int curLevel,
                boolean needNotifier) {
                if (node == null) {
                    //selected.remove(node.getId());
                    //expanded.remove(node.getId());
                    return;
                }
//				if (needNotifier)
//					tree.notifyObservers(node);


                //removed by biaoping.yin on2005-02-04
                //node.removeAllChildren();
                boolean hasSon = node.hasChildren();
                //如果展开时需要刷新节点
                //并且没有及时监控每个节点的状态
                //并且不是默认展开级别情况
                //需要执行以下程序
                if (refreshNode && !needNotifier && (curLevel > level)) {
                    node.removeAllChildren();
                    //删除儿子时应该清楚所有节点的状态吗，分两种情况处理
                    //由有儿子转到没儿子
                    hasSon = hasSon(node);
                    node.setHasChildren(hasSon);
                }

                //当有儿子并且儿子没有加载
                //或者有儿子，并且要求刷新节点，不管儿子有没有加载，并且不是默认展开层级时
//				if ((hasSon && node.childrenSize() == 0 && (curLevel > level))
//					|| (hasSon && refreshNode && (curLevel > level))
//					|| curLevel < level
//					|| (node.hasChildren() && node.childrenSize() == 0 && this.isExpanded(sNode.getId())))
                //有儿子并且不是初始展开而且要刷新节点node时重新设置node的儿子
                if (hasSon && refreshNode && (curLevel > level)) {
                    tree.setSon(node, curLevel);
                }
                //有儿子并且当前展开的层级比默认展开的层级小时设置节点node的儿子
                else if (curLevel < level) {
                    tree.setSon(node, curLevel);
                }
                //节点有儿子，状态是展开的，但是儿子没有获取则设置节点的儿子
                else if (node.childrenSize() == 0 && tree.isExpanded(node.getId())) {
                    tree.setSon(node, curLevel);
                }
                //节点有儿子，但是儿子没有获取
                else if (node.childrenSize() == 0 && hasSon) {
                    tree.setSon(node, curLevel);
                }
                if (!hasSon) {
                    expanded.remove(node.getId());
                }
                //modified by biaoping.yin on 2005-02-04
                //node.setHasChildren(hasSon);
            }
//			public void nodeExpanded(
//				ITreeNode node,
//				ITree tree,
//				boolean needNotifier) {
//
//				if (node == null) {
//					//selected.remove(node.getId());
//					//expanded.remove(node.getId());
//					return;
//				}
////				if (needNotifier)
////					tree.notifyObservers(node);
//
//
//				node.removeAllChildren();
//				//boolean hasSon = hasSon(node);
//				boolean hasSon = node.hasChildren();
//				if(refreshNode && !needNotifier)
//				{
//					hasSon = hasSon(node);
//					node.setHasChildren(hasSon);
//				}
//				if ((hasSon && node.childrenSize() == 0)
//						|| (hasSon && refreshNode))
//					tree.setSon(node);
//				else {
//					expanded.remove(node.getId());
//				}
//				//node.setHasChildren(hasSon);
//			}
            
            /**
             * 隐含地展开每个树节点，静态一次加载树中所有的树节点，并且将
             */
            public void impactExpandNode(ITreeNode node, ITree tree,int curLevel,boolean needNotifier)
        	{
            	if (node == null) {
                    //selected.remove(node.getId());
                    //expanded.remove(node.getId());
                    return;
                }
//				if (needNotifier)
//					tree.notifyObservers(node);


                //removed by biaoping.yin on2005-02-04
                //node.removeAllChildren();
                boolean hasSon = node.hasChildren();
               

                //当有儿子并且儿子没有加载
                //或者有儿子，并且要求刷新节点，不管儿子有没有加载，并且不是默认展开层级时
//				if ((hasSon && node.childrenSize() == 0 && (curLevel > level))
//					|| (hasSon && refreshNode && (curLevel > level))
//					|| curLevel < level
//					|| (node.hasChildren() && node.childrenSize() == 0 && this.isExpanded(sNode.getId())))
                //有儿子并且不是初始展开而且要刷新节点node时重新设置node的儿子
                if (hasSon ) {
                    setSon(node,curLevel);
                }
        	}
        });
    }

    /**
     * 添加折叠监听器
     */
    public void addCollapseListener() {
        addCollapseListener(new ICollapseListener() {
            public void nodeCollapsed(
                ITreeNode node,
                ITree tree,
                boolean needNotifier) {
                if (node == null) {
                    //selected.remove(node.getId());
                    return;
                }
//				if (needNotifier)
//					tree.notifyObservers(node);


                //modified by biaoping.yin on 2005-02-04
                //boolean hasSon = hasSon(node);
                //if (!hasSon && node.hasChildren()) {
                //	node.removeAllChildren();
                //}

                //node.setHasChildren(hasSon);
                boolean hasSon = node.hasChildren();

                if (refreshNode && !needNotifier) {
                    hasSon = hasSon(node);
                    node.setHasChildren(hasSon);
                }
                if (!hasSon) {
                    node.removeAllChildren();
                }
//				if(refreshNode && hasSon)
//				{
//					tree.setSon(node);
//				}



            }
        });
    }

    /**
     * 添加选择监听器
     */
    public void addSelectListener() {
        addSelectListener(new ISelectListener() {
            public void nodeSelected(
                ITreeNode node,
                ITree tree,
                boolean needNotifier) {
                if (node == null) {
                    //selected.remove(node.getId());
                    //expanded.remove(node.getId());
                    return;
                }
//				if (needNotifier)
//					tree.notifyObservers(node);


                //modified by biaoping.yin on 2005-02-04
//				boolean hasSon = hasSon(node);
//				if (!hasSon && node.hasChildren()) {
//					node.removeAllChildren();
//					if (tree.isExpanded(node.getId()))
//						expanded.remove(node.getId());
//				}
//				node.setHasChildren(hasSon);

                boolean hasSon = node.hasChildren();
                if (refreshNode && !needNotifier) {
                    hasSon = hasSon(node);
                    node.setHasChildren(hasSon);
                }

                if (!hasSon) {
                    node.removeAllChildren();
                    if (tree.isExpanded(node.getId())) {
                        expanded.remove(node.getId());
                    }
                }
                //node.setHasChildren(hasSon);
            }
        });
    }

    /**
     * 更新node节点
     */
    public ITreeNode updateNode(ITree tree, ITreeNode node) {
        return getTreeNode(node);
    }

    /**
     * @return String 扩展条件参数
     */
    public String getExtCondition() {
        return extCondition;
    }

    /**
     * @param string
     */
    public void setExtCondition(String string) {
        extCondition = string;
    }


	

}
