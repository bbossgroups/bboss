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

// import javax.servlet.jsp.JspException;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.common.tag.tree.itf.ITree;
import com.frameworkset.common.tag.tree.itf.ITreeIteratorElement;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.VelocityUtil;

/**
 * 该类辅助TreeTag生成各种树的html代码， 可以通过ecs/velocity/StringBuilder几种方式来生成
 * 
 * @作者 biaoping.yin
 * @日期 2004-3-19 16:28:13
 * @版本 v1.0
 */

public class NodeHelper  implements Serializable
{
	private static final Logger log = Logger.getLogger(NodeHelper.class);
 
	/**
	 * 包含显示的节点信息
	 */
	private ITreeIteratorElement element = null;
	
//	private 

	/**
	 * 待显示的节点action
	 */
	private String action = null;

	/**
	 * 该节点是否为展开节点
	 */
	boolean expanded = false;

	/**
	 * 节点是否有字节点
	 */
	boolean hasChildren = true;

	/**
	 * 是否是最后孩子节点
	 */
	boolean isLastChild = false;

	/**
	 * 是否是第一个孩子节点
	 */
	boolean isFirstChild = false;

	/**
	 * 节点是否选中
	 */
	boolean selected = false;

	/**
	 * 控制树的折叠功能： false：不折叠 true：折叠 缺省值：true
	 */
	private boolean isCollapse = true;

	/**
	 * 节点href扩展串
	 */
	String extendString = "";

	/**
	 * 节点类型
	 */
	String type = "";

	/**
	 * 页面请求对象
	 */
	HttpServletRequest request;

	/**
	 * 动静结合时需要记录当前节点的ident，以便生成节点的儿子节点的indent 儿子节点的indent ＝ 父的indent ＋ 儿子的indent
	 */
	StringBuilder indent;

	boolean dynamic = true;
	
	

	/**
	 * 页面请求范围 session，request，pageContext，缺省值为session
	 */
	String scope = "session";
	
	

	/**
	 * 指定节点的修饰图片的目录
	 */
	String imageFolder = "/images/";

	/**
	 * checkbox名称
	 */
	String checkBox = null;

	/**
	 * radio名称
	 */
	String radio = null;

	/**
	 * checkbox默认选中值，以‘,’分隔的字符串
	 */
	private String[] checkBoxDefaultValue = null;

	/**
	 * checkbox 的值
	 */

	private String checkboxValue;

	/**
	 * radio的默认选中值
	 */
	private String radioDefaultValue = null;

	/**
	 * checkbox扩展串
	 */
	private String checkBoxExtention;

	/**
	 * radio按钮扩展串
	 */
	private String radioExtention;

	/**
	 * radio按钮值
	 */
	private String radioValue;

	/**
	 * 节点href目标
	 */
	String target = "";

	/**
	 * 节点标识
	 */
	String nodeId;

	/**
	 * 页面参数
	 */
	String params;

	/**
	 * 双事件控制变量，点击节点时是否展开节点的下一级目录，前提条件是节点要有下一级目录 false：为不展开，true为展开，缺省为false
	 */
	private boolean doubleEvent = false;

	private String nodeClickLink = null;

	private String nodeEventLink = null;
	private String sonids;

	public NodeHelper(ITreeIteratorElement element, HttpServletRequest request,String sonids)
	{
		this.element = element;
		this.expanded = element.isExpanded();
		this.hasChildren = element.getNode().hasChildren();
		this.radioValue = element.getNode().getRadioValue();
		this.checkboxValue = element.getNode().getCheckboxValue();
		this.isLastChild = element.isLastChild();
		this.isFirstChild = element.isFirstChild();
		this.selected = element.isSelected();
		this.type = element.getNode().getType();
		this.request = request;
		this.nodeId = element.getNode().getId();
		this.nodeClickLink = null;
		this.nodeEventLink = null;
		this.sonids = sonids; 
	}

	public void setCollapse(boolean isCollapse)
	{
		this.isCollapse = isCollapse;
	}

	public void setImageFolder(String imageFolder)
	{
		this.imageFolder = imageFolder;
	}

	public void setCheckBox(String checkBox)
	{
		this.checkBox = checkBox;
	}

	public void setRadio(String radio)
	{
		this.radio = radio;
	}

	/**
	 * 获取事件名称
	 * 
	 * @return
	 */
	private String getEvent()
	{
		if (!expanded && hasChildren && !isLastChild)
			return "expand";
		if (expanded && hasChildren && !isLastChild)
			return "collapse";
		if (!expanded && hasChildren && isLastChild)
			return "expand";
		if (expanded && hasChildren && isLastChild)
			return "collapse";
		return null;
	}

	private String getEventLabel()
	{
        if(itree.isDynamic())
        {
    		if (!expanded && hasChildren && !isLastChild)
    			return "展开";
               
    		if (expanded && hasChildren && !isLastChild)
    			return "折叠";
               
    		if (!expanded && hasChildren && isLastChild)
    			return "展开";
                
    		if (expanded && hasChildren && isLastChild)
    			return "折叠";
        }
        else
        {
            if (!expanded && hasChildren && !isLastChild)
    
                return "展开/折叠";
            if (expanded && hasChildren && !isLastChild)
    
                return "展开/折叠";
            if (!expanded && hasChildren && isLastChild)
   
                return "展开/折叠";
            if (expanded && hasChildren && isLastChild)
    
                return "展开/折叠";
        }
		return null;
	}

	public void getImageContent(StringBuilder buffer)
	{
		// StringBuilder buffer = new StringBuilder();

		// System.out.println(element.getName() + ":expanded=" + expanded +
		// ",hasChildren=" + hasChildren + ",isLastChild=" + isLastChild);
		if (!expanded && hasChildren && !isLastChild)
			// buffer.append(
			// getImageContent(
			// "expand",
			// getCollapsedMidNodeImage(),
			// getClosedFolderImage()));

			getImageContent(buffer, "expand", getCollapsedMidNodeImage(),
					getClosedFolderImage());
		/**
		 * 1,2 completed selected
		 */
		if (!expanded && hasChildren && isLastChild)

			getImageContent(buffer, "expand", getCollapsedLastNodeImage(),
					getClosedFolderImage());

		if (expanded && hasChildren && !isLastChild)

			getImageContent(buffer, "collapse", getExpandedMidNodeImage(),
					getOpenFolderImage());

		if (expanded && hasChildren && isLastChild)

			getImageContent(buffer, "collapse", getExpandedLastNodeImage(),
					getOpenFolderImage());

		// <td><a href="classtree.jsp?expand=<tree:nodeId
		// node="example.node"/>"><img src="images/collapsedLastNode.gif"
		// border="0"></a><img src="images/closedFolder.gif"></td>

		// <td><a href="classtree.jsp?collapse=<tree:nodeId
		// node="example.node"/>"><img src="images/expandedLastNode.gif"
		// border="0"></a><img src="images/openFolder.gif"></td>
		if (!expanded && !hasChildren && !isLastChild)

			getImageContent(buffer, "null", getNoChildrenMidNodeImage(),
					getNonFolderImage());
		// <td><img src="images/noChildrenMidNode.gif"><img
		// src="images/nonFolder.gif"></td>
		if ((!expanded && !hasChildren && isLastChild)
				|| (expanded && !hasChildren && isLastChild))

			getImageContent(buffer, "null", getNoChildrenLastNodeImage(),
					getNonFolderImage());
		// <td><img src="images/noChildrenLastNode.gif"><img
		// src="images/nonFolder.gif"></td>

		// return buffer.toString();
	}

	private String getPreImageForstatic(boolean expanded)
	{
		if (!expanded && !isLastChild)
			return getCollapsedMidNodeImage();
		/**
		 * 1,2 completed selected
		 */
		if (!expanded && isLastChild)

			return getCollapsedLastNodeImage();

		if (expanded && !isLastChild)

			return getExpandedMidNodeImage();

		if (expanded && isLastChild)
			return getExpandedLastNodeImage();
		return "";

	}
	
	private String getFolderImageForstatic(boolean expanded)
	{
		if (!expanded)
			return getClosedFolderImage();
		else
			return getOpenFolderImage();

	}

	public void getNodeContent(StringBuilder buffer)
	{
		// return getNodeContent(buffer,"selected", selected);
		getNodeContent(buffer, "selected", selected);
	}

	private String getCustomParams(Map attributes)
	{
		StringBuilder buffer = new StringBuilder();
		boolean flag = false;
		if (attributes != null)
		{

			Iterator keys = attributes.keySet().iterator();
			while (keys.hasNext())
			{
				String key = (String) keys.next();
				 /**
                 * 判断key是否是系统中缺省保留的参数
                 */
				if(this.isInnerVariable(key))
				{
					continue;
				}
				if (!flag)
				{
					String value = (String) attributes.get(key);
					buffer.append(key).append("=").append(value);
					flag = true;
				}
				else
				{
					String value = (String) attributes.get(key);
					buffer.append("&").append(key).append("=").append(value);
				}

			}
		}
		return buffer.toString();
	}
	
	
	private boolean isInnerVariable(String key)
	{
		boolean isInnerVariable = false;
		/**
         * 判断key是否是系统中缺省保留的参数
         */
		if (key.equals("nodeLink"))//节点链接参数名称
			isInnerVariable = true;
		else if (key.equals("node_recursive"))//复选框是否递归选择参数名称为boolean类型
			isInnerVariable = true;
        
		else if (key.equals("node_linktarget"))//节点链接地址key
        {
			isInnerVariable = true;
        }
        
		else if (key.equals("node_checkboxname"))//节点复选框名称key
        {
            
			isInnerVariable = true;
        }
        
		else if (key.equals("node_radioname"))//节点单选框名称key
        {
            
			isInnerVariable = true;
        }
		else if(key.equals("node_uprecursive"))//节点复选框是否有递归选择上级的功能
        {
			isInnerVariable = true;
        }
		else if(key.equals("node_partuprecursive"))//节点复选框是否有递归选择上级的功能
        {
			isInnerVariable = true;
        }
		else if(key.equals("node_checkboxchecked")) //标识复选框是否被选中
			isInnerVariable = true;
		else if(key.equals("node_checkboxdisabled")) //标识复选框是否被禁用
			isInnerVariable = true;
		else if(key.equals("node_radiochecked")) //标识单选按钮是否被选冲
			isInnerVariable = true;
		else if(key.equals("node_radiodisabled")) //标识单选按钮是否被禁用
			isInnerVariable = true;	
		return isInnerVariable;
	}
	

	/**
	 * 获取节点点击链接
	 * 
	 * @return
	 */
	private String getNodeClickLink(String event)
	{
		if (nodeClickLink != null)
			return this.nodeClickLink;
		nodeClickLink = "";
		String nodeLink = null;
		Map attributes = element.getNode().getParams();
		
		// 如果需要设置href，则获取链接地址，否则
		if (this.element.getNode().getShowHref())
		{
			if (attributes != null)
				nodeLink = (String) attributes.get("nodeLink");
			if (nodeLink == null)
				nodeLink = this.getAction();
		}
		if(!StringUtil.isJavascript(nodeLink))
		{
			String selectedNode = request.getParameter("selectedNode");
			StringBuilder buffer = new StringBuilder();
			
			boolean flag = nodeLink != null && nodeLink.trim().length() > 0;
			String nodeEvent = getEvent();
			String eventLink = getEventLink(nodeEvent).toString();
			if (flag)
			{
				buffer.append("doClickTreeNode('").append(
						StringUtil.getRealPath(request, nodeLink)).append(
						nodeLink.indexOf("?") == -1 ? "?" : "&").append(event)
						.append("=").append(element.getId());
				if (element.getNode().getType() != null)
					buffer.append("&classType=")
							.append(element.getNode().getType());
	
				if (element.getNode().getMemo() != null)
					buffer.append("&nodeMemo=").append(element.getNode().getMemo());
				if (element.getNode().getPath() != null)
	
					buffer.append("&nodePath=").append(
							StringUtil.encode(element.getNode().getPath(), null));
				String target = getTarget();
				if (attributes != null)
				{
					String temp = (String) attributes.get("node_linktarget");
					if(temp != null && !temp.equals(""))
						target = temp;
					Iterator keys = attributes.keySet().iterator();
					while (keys.hasNext())
					{
						String key = (String) keys.next();
	                    /**
	                     * 判断key是否是系统中缺省保留的参数
	                     */
						if(this.isInnerVariable(key))
						{
							continue;
						}
									          
						String value = (String) attributes.get(key);
						buffer.append("&").append(key).append("=").append(value);
	
					}
				}
	
				buffer.append("','" + element.getNode().getId() + "',").append("'")
						.append(target).append("','").append(selectedNode);
	
				if (!eventLink.equals("") && doubleEvent)
				{
					if(itree.isDynamic())
						buffer.append("','").append(eventLink).append("')\" ");
					else
						buffer.append("','").append(eventLink).append("','").append(this.element.getId()).append("')\" ");
						
					
				}
				else
					buffer.append("')");
	
			}
			else if (!eventLink.equals(""))
			{
				if(itree.isDynamic()) //纯动态的树模式
				{
					buffer.append("doClickImageIcon('").append(
								eventLink).append("')");
				}
				else //动静结合的模式
				{

					buffer.append("doClickImageIcon('").append(
							eventLink).append("','")
							.append(this.element.getId())
							.append("','")
							.append(StringUtil.encode(this.element.getName()))
							.append("','")
							.append(this.element.getNode().getType())	

							.append("','")
							.append(this.element.getNode().getPath())
							.append("','")
							.append(this.element.isFirstChild())
							.append("','")
							.append(this.element.isLastChild())
							.append("')");
				}
	
			}
			return nodeClickLink = buffer.toString();
		}
		else
		{
			return nodeClickLink = nodeLink;
		}
		
	}

	/**
	 * 生成节点的html文本，节点是否带链接以及带什么样的链接有以下几种情况：
	 * 不需要设置点击链接，但是节点包含字节点，那么需要设置展开该节点下一级的链接 需要设置点击链接，但节点不包含子节点而不需要设置展开节点的下一级链接
	 * 需要设置点击链接，节点包含字节点，也需要设置展开该节点下一级的链接
	 * 
	 * @param buffer
	 * @param event
	 * @param selected
	 */
	private void getNodeContent(StringBuilder buffer, String event,
			boolean selected)
	{

		// 定义从点击图标处传递过来的参数,然后判断默认选中的结点

		String selectedNode = request.getParameter("selectedNode");
		// StringBuilder buffer = new StringBuilder();
		if (nowrap)
			buffer.append("<td nowrap title=\"").append(element.getNode().getName()).append("\">");
		else
			buffer.append("<td title=\"").append(element.getNode().getName()).append("\">");

		buffer.append(getCheckBox());
		buffer.append(getRadio());
		String nodeClickEvent = this.getNodeClickLink(event);
		
		if (!nodeClickEvent.equals(""))
		{
			if (this.enablecontextmenu)
			{
				String id = "";
				if (this.element.getNode().isEnablecontextmenu()) // 如果节点本身制定了右键菜单，则设置本身的右键菜单项
					id = "_node_" + this.element.getNode().getId();
				else
					// 否则如果根据直接使用节点类型对应的右键菜单
					id = "_type_" + this.element.getNode().getType();
				String params = this.getCustomParams(this.element.getNode()
						.getParams());
				String expandLabel = this.getEventLabel();
				buffer
						.append("<a ")
						.append(
								expandLabel == null ? "" : "expandLabel=\""
										+ expandLabel + "\"")
						.append(" openNode=\"")
						.append(this.element.getNode().getId())
						.append("\"")
						.append(
								expandLabel == null ? " "
										: " expandNode=\"icon_"
												+ this.element.getNode()
														.getId() + "\"")
						.append(" id=\"")
						.append(id)
						.append("\" params=\"")
						.append(params)
						.append(
//								"\" oncotextmenu='InitializedDocEvent();' name=\"")
						"\"  name=\"")
						.append(this.element.getNode().getId()).append(
								"\" onclick=\"").append(nodeClickEvent).append(
								"\" style=\"cursor:hand;\"");
			}
			else
			{

				buffer.append("<a name=\"").append(
						this.element.getNode().getId()).append("\" onclick=\"")
						.append(nodeClickEvent).append(
								"\" style=\"cursor:hand;\"");
			}

			if (selectedNode != null
					&& selectedNode.equals(this.element.getNode().getId()))
				buffer.append(" class=\"selectedTextAnchor\"");
			buffer.append(">");
		}
		else
		{
			if (this.enablecontextmenu)
			{
				String id = "";
				if (this.element.getNode().isEnablecontextmenu()) // 如果节点本身制定了右键菜单，则设置本身的右键菜单项
					id = "_node_" + this.element.getNode().getId();
				else
					// 否则如果根据直接使用节点类型对应的右键菜单
					id = "_type_" + this.element.getNode().getType();
				String params = this.getCustomParams(this.element.getNode()
						.getParams());
				String expandLabel = this.getEventLabel();
				buffer
						.append("<a ")
						.append(
								expandLabel == null ? "" : "expandLabel=\""
										+ expandLabel + "\"")
						.append(" openNode=\"")
						.append(this.element.getNode().getId())
						.append("\"")
						.append(
								expandLabel == null ? " "
										: " expandNode=\"icon_"
												+ this.element.getNode()
														.getId() + "\"")
						.append(" id=\"")
						.append(id)
						.append("\" params=\"")
						.append(params)
						.append(
//								"\" oncotextmenu='InitializedDocEvent();' name=\"")
						"\" name=\"")
						.append(this.element.getNode().getId()).append(
								"\">");
			}
			else
			{
				//do nothing
			}
		}

		// if (selected)
		// buffer.append("<b>");

		String t_temp = element.getName();
		t_temp = StringUtil.replaceAll(t_temp,"'","\\'");
		buffer.append(t_temp);

		// if (selected)
		// buffer.append("</b>");

		// buffer.append("</span>");

		if (!nodeClickEvent.equals(""))
		{
			buffer.append("</a>");
			getCatchScript(buffer, request, element.getId());
		}
		else
		{
			if (this.enablecontextmenu)
				buffer.append("</a>");
			else
			{
				//do nothing.
			}
		}
			
		buffer.append("</td>");
	}

	private String localAction;

	/**
	 * 控制树节点是否换行
	 */
	private boolean nowrap = true;

	private boolean enablecontextmenu;

	/**
	 * 向下递归所有节点
	 */
    private boolean recursive = false;
    
    /**
     * 向上递归到所有父节点
     */
    private boolean uprecursive = false;
    
    /**
     * 向上递归到所有需要递归父节点
     */
    private boolean partrecursive = false;
    
    

    private String checkboxOnchange;

	private ITree itree;

	/**
	 * 获取本地连接，并且将页面参数追加到连接后面
	 * 
	 * @return String
	 */
	public String getLocalAction()
	{
		String temp = localAction;
		if (getParams() == null || getParams().trim().length() == 0)
			return temp;
		int index = temp.indexOf('?');
		if (index == -1)
		{
			temp += "?" + this.getParams();

		}
		else
			temp += "&" + this.getParams();
		return temp;
	}

	public String getLocalAction(int index, String attach)
	{
		return new StringBuilder(localAction).insert(index, attach).toString();
	}

	/**
	 * 获取事件链接
	 * 
	 * @param event
	 * @return
	 */
	private String getEventLink(String event)
	{
		if (this.nodeEventLink != null)
			return nodeEventLink;
		if (event == null)
			return nodeEventLink = "";
		String location = getLocalAction();
		int index = location.indexOf('?');

		// 如果允许树进行折叠，那么设置图标的链接，否则不设置
		StringBuilder temp_b = new StringBuilder();

	

		if (index == -1)
			temp_b.append(location)

			.append("?");
		else
			temp_b.append(location).append("&");
		// buffer.append("anchor=")
		// .append(anchor)
		// .append("&");
		temp_b.append(event).append("=").append(element.getId());
		temp_b.append("&request_scope=").append(getScope());
		// .append("')");
		return nodeEventLink = temp_b.toString();
	}
     private String getParent_indent()
    {
        String parent_indent = request.getParameter("node_parent_indent");
        return parent_indent;
    }
     
     private String getType()
     {
         if(type == null)
             return "";
         else
         {
             if(type.equals("1"))
                 return "root";
             else
                 return type;
         }
             
     }
     
    private boolean isFirsted()
    {
        return this.element.isExpanded() || this.itree.isStatic();
    }
	private void getImageContent(StringBuilder buffer, String event,
			String nodeImage, String typeImage)
	{
		String nodeClickEvent = this.getNodeClickLink("selected");
		// StringBuilder buffer = new StringBuilder();
		if (!event.equals("null") && isCollapse())
		{

			buffer.append("<td nowrap>");

			StringBuilder temp_b = new StringBuilder();// getEventLink(event);
			if(itree.isDynamic() || !this.hasChildren)
			{
				temp_b.append("<a id=\"icon_").append(element.getId()).append(
								"\" onclick=\"doClickImageIcon('").append(
								getEventLink(event)).append(
								"')\" style=\"cursor:hand;\">");
			}
			else  
			{
				
				temp_b.append("<a firsted=\"").append(isFirsted()).append("\" id=\"icon_").append(element.getId()).append(
				"\" onclick=\"doClickImageIcon('").append(
				getEventLink(event))
				.append("','").append(
				this.element.getId())
				.append("','")
				.append(StringUtil.encode(this.element.getName()))
				.append("','")
				.append(this.element.getNode().getType())	
//				.append("','")
//				.append(this.element.getNode().getShowHref())
//				.append("','")
//				.append(this.element.getNode().hasChildren())
				.append("','")
				.append(this.element.getNode().getPath())
				.append("','")
				.append(this.element.isFirstChild())
				.append("','")
				.append(this.element.isLastChild())
				.append("')\" style=\"cursor:hand;\" indent=\"")
				.append(this.indent).append("\"")
				.append(" collapsedimg=\"").append(this.getPreImageForstatic(false)).append("\"")
				.append(" expandedimg=\"").append(this.getPreImageForstatic(true)).append("\"")
				.append(" closedimg=\"").append(this.getFolderImageForstatic(false)).append("\"")
				.append(" openedimg=\"").append(this.getFolderImageForstatic(true)).append("\"")
				.append(">");
			}

			if (this.enablecontextmenu && !nodeClickEvent.equals(""))
			{
				String id = "";
				if (this.element.getNode().isEnablecontextmenu()) // 如果节点本身制定了右键菜单，则设置本身的右键菜单项
					id = "_node_" + this.element.getNode().getId();
				else
					// 否则如果根据直接使用节点类型对应的右键菜单
					id = "_type_" + this.element.getNode().getType();
				String params = this.getCustomParams(this.element.getNode()
						.getParams());
				String expandLabel = this.getEventLabel();
				buffer
						.append(temp_b.toString())
						.append("<img ")
						.append(
								expandLabel == null ? "" : "expandLabel=\""
										+ expandLabel + "\"")
						.append(" openNode=\"")
						.append(this.element.getNode().getId())
						.append("\"")
						.append(
								expandLabel == null ? " "
										: " expandNode=\"icon_"
												+ this.element.getNode()
														.getId() + "\"")
						.append(" id=\"")
						.append(id)
						.append("\" params=\"")
						.append(params)
						.append(
//								"\" oncotextmenu='InitializedDocEvent();' name=\"icon0_").append(this.element.getId())
						"\" name=\"icon0_").append(this.element.getId())
						.append("\" src=\"")
						.append(nodeImage).append("\" border=\"0\">");

			}
			else

				buffer.append(temp_b.toString()).append("<img name=\"icon0_").append(this.element.getId())
						.append("\" src=\"").append(
						nodeImage).append("\" border=\"0\">");

			buffer.append("</a>");
			// buffer.append(temp_b.toString())
			buffer.append("<a onclick=\"javascript:doclickevt(document.getElementById('icon_").append(
					element.getId()).append(
					"'));\" style=\"cursor:hand;\">");
			if (this.enablecontextmenu && !nodeClickEvent.equals(""))
			{
				String id = "";
				if (this.element.getNode().isEnablecontextmenu()) // 如果节点本身制定了右键菜单，则设置本身的右键菜单项
					id = "_node_" + this.element.getNode().getId();
				else
					// 否则如果根据直接使用节点类型对应的右键菜单
					id = "_type_" + this.element.getNode().getType();
				String params = this.getCustomParams(this.element.getNode()
						.getParams());
				String expandLabel = this.getEventLabel();
				buffer
						.append("<img ")
						.append(
								expandLabel == null ? "" : "expandLabel=\""
										+ expandLabel + "\"")
						.append(" openNode=\"")
						.append(this.element.getNode().getId())
						.append("\"")
						.append(
								expandLabel == null ? " "
										: " expandNode=\"icon_"
												+ this.element.getNode()
														.getId() + "\"")
						.append(" id=\"")
						.append(id)
						.append("\" params=\"")
						.append(params)
						.append(
//								"\" oncotextmenu=\"InitializedDocEvent();\" src=\"");
						"\"  src=\"");
			}
			else
				buffer.append("<img src=\"");
			buffer.append(typeImage).append("\" name=\"icon1_").append(this.element.getId())
						.append("\"></a></td>");

		}
		else
		{
			if (this.element.getNode().getShowHref() && this.enablecontextmenu
					&& !nodeClickEvent.equals(""))
			{
				String id = "";
				if (this.element.getNode().isEnablecontextmenu()) // 如果节点本身制定了右键菜单，则设置本身的右键菜单项
					id = "_node_" + this.element.getNode().getId();
				else
					// 否则如果根据直接使用节点类型对应的右键菜单
					id = "_type_" + this.element.getNode().getType();
				String params = this.getCustomParams(this.element.getNode()
						.getParams());

				buffer.append("<td nowrap>").append("<img src=\"").append(nodeImage)
						.append("\">");
				buffer
						.append("<img openNode=\"")
						.append(this.element.getNode().getId())
						.append("\" id=\"")
						.append(id)
						.append("\" params=\"")
						.append(params)
						.append(
//								"\" oncotextmenu=\"InitializedDocEvent();\" src=\"")
						"\"  src=\"")
						.append(typeImage).append("\"></td>");
			}
			else
				buffer.append("<td nowrap>").append("<img src=\"").append(nodeImage)
						.append("\">").append("<img src=\"").append(typeImage)
						.append("\"></td>");
		}
		// return buffer.toString();
	}

	/**
	 * 获取树头部分
	 */
	public void getUpper(StringBuilder buffer)
	{
		
		
			if (itree.isDynamic())
				buffer
						.append("<tr><td>")
						.append(
								"<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">")
						.append("<tr>");
			else if(itree.isStaticDynamic())
            {
				// 动静态结合的树节点
                String parent_indent = request.getParameter("node_parent_indent");
                if(element.isFirstChild()) //\\调整
                { 
                    if(parent_indent != null)
                        buffer.append("<div sonids=\"").append(sonids).append("\" style=\"display:none;\" id=\"div_parent_")
                        
                              .append(element.getNode().getParent().getId()).append("\">");
                    else
                    {
                       
                        buffer.append("<div id=\"div_parent_")
                        .append(element.getNode().getParent() != null ?element.getNode().getParent().getId() : "").append("\">");
                    }
    				
                }
                
                buffer
                .append("<div id=\"div_")
                .append(this.element.getId())
                .append("\">")

                .append(
                        "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">")
                .append("<tr>");
            }
			else if(itree.isStatic())
			{
//				
                if(element.isFirstChild()) //\\调整
                { 
                    
                    
                    {
                        if(element.getNode().getParent() == null )
                        {
	                        buffer.append("<div id=\"div_parent_\">");
                        }
                        else
                        {
                        	if(itree.isExpanded(element.getNode().getParent().getId()))
                        	{
                        		 buffer.append("<div id=\"div_parent_")
     	                        .append(element.getNode().getParent().getId() ).append("\">");
	                        	
                        		
                        	}
                        	else
                        	{
                        		buffer.append("<div style=\"display:none;\" id=\"div_parent_")
		                        .append(element.getNode().getParent().getId() ).append("\">");
                        	}
                        }
                    }
    				
                }
                
                buffer
                .append("<div id=\"div_")
                .append(this.element.getId())
                .append("\">")

                .append(
                        "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">")
                .append("<tr>");
			}
            
		
		
	}
    
    

	/**
	 * 获取树根
	 * 
	 * @return String
	 */
	public void getBoot(StringBuilder buffer)
	{
		// StringBuilder buffer = new StringBuilder();
		if (itree.isDynamic())
			buffer.append("</tr>").append("</table></td></tr>");
		else
        {
			buffer.append("</tr>").append("</table></div>");
            String parent_indent = request.getParameter("node_parent_indent");
            
            if(this.element.isLastChild())
            {
                //如果是静态点击展开节点，则直接添加</div>父层关闭标记
                if(parent_indent != null)
                {
                    buffer.append("</div>");
                }
                else
                    /**
                     * modifiye 
                     * 判断当前自动展开的节点是否有儿子:
                     *  没有儿子则直接添加</div>父层关闭标记，并且递归判断当前节点的所有祖先是否是最后的孩子如果是则补充</div>
                     *  有儿子则需要进行特殊处理，但是孩子没有加载则直接添加父的</div>标记
                     */
                {
                    if(!element.getNode().hasChildren())
                    {
                        
                        buffer.append("</div>");
                        setParentLastChildBoot(element.getNode().getParent(),buffer);
                        
                    }
                    else if(element.getNode().childrenSize() == 0) //
                    {
                    	buffer.append("</div>");
                    }
                }
            }
        }
		
	}
    
    private void setParentLastChildBoot(ITreeNode parent,StringBuilder buffer)
    {
        if(parent == null)
            return ;
        if(isLastChild(parent))
        {
            if(parent.isRoot() )
            {
                if( this.includeRootNode)
                {
                    buffer.append("</div>");
                    setParentLastChildBoot(parent.getParent(),buffer);
                }
            }
            else
            {
                buffer.append("</div>");
                setParentLastChildBoot(parent.getParent(),buffer);
            }
        }
    }
    
    private boolean isLastChild(ITreeNode node)
    {
        return node.getRightNode() == null;
    }

	/**
	 * 获取节点的缩排格式
	 */
	public void getIndent(StringBuilder indent)
	{
		Iterator indentationProfileIterator = element.getIndendationProfile()
				.iterator();
		// StringBuilder indent = new StringBuilder();
		indent.append("<td nowrap>");
		if (!itree.isDynamic() && this.element.getNode().hasChildren())
			this.indent = new StringBuilder();
		while (indentationProfileIterator.hasNext())
		{
			boolean isVerticalLineIndentationType = !((Boolean) indentationProfileIterator
					.next()).booleanValue();
			if (this.indent != null)
				this.indent.append(!isVerticalLineIndentationType ? "1" : "0");
			if (isVerticalLineIndentationType)
			{
				// :log imgsrc必须动态获取
				indent.append("<img src=\"").append(getVerticalLine()).append(
						"\">");
			}
			else
			{
				indent.append("<img src=\"").append(getBlankSpace()).append(
						"\">");
			}
		}
		indent.append("</td>");
		// return indent.toString();

		/**
		 * this.indentationProfileIterator =
		 * getElement().getIndendationProfile().iterator();
		 * 
		 * if(this.indentationProfileIterator.hasNext()){
		 * pageContext.getRequest().setAttribute(getIndentationType(),
		 * this.indentationProfileIterator.next()); return EVAL_BODY_INCLUDE; }
		 * return SKIP_BODY;
		 * 
		 */
	}

	private String getVerticalLine()
	{
		return this.getImageFolder() + "verticalLine.gif";
		// "<img src=\"images/verticalLine.gif\">";
	}

	private String getBlankSpace()
	{
		return this.getImageFolder() + "blankSpace.gif";
		// "<img src=\"images/blankSpace.gif\">";
	}

	private String getCollapsedMidNodeImage()
	{
		return this.getImageFolder() + "collapsedMidNode.gif";
	}

	private String getExpandedMidNodeImage()
	{
		return this.getImageFolder() + "expandedMidNode.gif";
	}

	/**
	 * 获取相应类型关闭目录前的图标
	 * 
	 * @return String
	 */
	private String getClosedFolderImage()
	{
		if (this.element.getNode().isRoot())
			return this.getImageFolder() + "close_root.gif";
		else
		{
			String type = this.element.getNode().getType();
			if (type == null || type.equals(""))
			{
				return this.getImageFolder() + "closedFolder.gif";
			}
			else
			{
				return this.getImageFolder() + type + "_closedFolder.gif";
			}
		}
	}

	private String getOpenFolderImage()
	{
		if (this.element.getNode().isRoot())
			return this.getImageFolder() + "open_root.gif";
		else
		{
			// return this.getImageFolder() + "openFolder.gif";
			String type = this.element.getNode().getType();
			if (type == null || type.equals(""))
			{
				return this.getImageFolder() + "openFolder.gif";
			}
			else
			{
				return this.getImageFolder() + type + "_openFolder.gif";
			}
		}

	}

	private String getCollapsedLastNodeImage()
	{
		return this.getImageFolder() + "collapsedLastNode.gif";
	}

	private String getExpandedLastNodeImage()
	{
		return this.getImageFolder() + "expandedLastNode.gif";
	}

	private String getNoChildrenMidNodeImage()
	{
		return this.getImageFolder() + "noChildrenMidNode.gif";
	}

	private String getNonFolderImage()
	{
		// return this.getImageFolder() + "nonFolder.gif";
		if (this.element.getNode().isRoot())
			return this.getImageFolder() + "close_root.gif";
		String type = this.element.getNode().getType();
		if (type == null || type.equals(""))
		{
			return this.getImageFolder() + "nonFolder.gif";
		}
		else
		{
			return this.getImageFolder() + type + "_nonFolder.gif";
		}
	}

	private String getNoChildrenLastNodeImage()
	{
		return this.getImageFolder() + "noChildrenLastNode.gif";
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getAction()
	{

		return StringUtil.getRealPath(request, action);
		// return this.action;
	}

	/**
	 * 获取复选框代码
	 * 
	 * @return String
	 */
	public String getCheckBox()
	{
		// 如果没有设置单选框的名称，或者单选框的值为null时，不生成相应的复选框
		String value = this.getCheckboxValue();
		// System.out.println("check box value:" + value);
		if (checkBox == null || checkBox.trim().equals("") || value == null)
			return "";
		String checked = "";

		String[] values = getCheckBoxDefaultValues();
		StringBuilder ret = new StringBuilder();
        String checkBox = this.checkBox;
        boolean recursive = itree.isRecursive();
        
        boolean uprecursive = itree.isUprecursive();
        
        boolean partuprecursive = itree.isPartuprecursive();
        //node_recursive,node_checkboxname
        Map params = this.element.getNode().getParams();
        boolean disabled = false;
        boolean bchecked = false;
        
        
        if(params != null)
        {
            Boolean btemp = (Boolean)params.get("node_recursive");
            if(btemp != null)
                recursive = btemp.booleanValue();
            String temp = (String)params.get("node_checkboxname");
            
            btemp = (Boolean)params.get("node_uprecursive");
            if(btemp != null)
                uprecursive = btemp.booleanValue();
            btemp = (Boolean)params.get("node_partuprecursive");
            if(btemp != null)
            	partuprecursive = btemp.booleanValue();
            
            btemp = (Boolean)params.get("node_checkboxdisabled");            
            if(btemp != null)
            	disabled = btemp.booleanValue();
            
            btemp = (Boolean)params.get("node_checkboxchecked");            
            if(btemp != null)
            	bchecked = btemp.booleanValue();
            

            if(temp != null)
                checkBox = temp;
            
                
        }
        
         
		ret.append("<input type='checkbox' name='").append(checkBox).append(
				"' value='");

		if (value == null)
			ret.append(nodeId);
		else
			ret.append(value);
        
		if(bchecked )
		{
			checked = "checked";
		}
		else if (values != null && StringUtil.containKey(values, value))
		{
			checked = "checked";
		}
		ret.append("' ").append(checked).append(" ").append(" id='checkbox_")
				.append(nodeId).append("'").append(" sonids='")
				.append(element.getNode().getSonids()).append("'").append(" ")
				.append(getCheckBoxExtention());
        if(this.checkboxOnchange != null)
            ret.append(" onClick='")
                .append(this.checkboxOnchange)
                .append("'"); 
        else
            ret.append(" onClick='treeNodeCheckboxChange(null,event)'");
        if(recursive)
        {
            ret.append(" recursive='true'");
        }
        
        else
        {
            ret.append(" recursive='false'");
        }
        
        if(uprecursive)
        {
            ret.append(" uprecursive='true'");
        }
        
        else
        {
            ret.append(" uprecursive='false'");
        }
        if(disabled)
        {
        	ret.append(" disabled");
        }
        ret.append(" partuprecursive='")
           .append(partuprecursive + "")
           .append("'");
        ret.append(">");
		return ret.toString();
	}

//	/**
//	 * 获取当前节点的所有子节点的id，组成一个串，以“##”作为分隔符
//	 * 
//	 * @return 返回生成的串
//	 */
//	public static String getSonIDs(ITreeNode node)
//	{
//		List list = node.getChildren();
//		StringBuilder ret = new StringBuilder();
//		if (list != null)
//		{
//			boolean first = true;
//			for (int i = 0; i < list.size(); i++)
//			{
//				if (first)
//				{
//					ret.append("").append(
//							((ITreeNode) list.get(i)).getId());
//					first = false;
//				}
//				else
//					ret.append("##").append("").append(
//							((ITreeNode) list.get(i)).getId());
//
//			}
//		}
//		return ret.toString();
//	}

	/**
	 * @return String
	 */
	public String getImageFolder()
	{
		return StringUtil.getRealPath(request, getPath(imageFolder));
	}

	private String getPath(String path)
	{
		if (path == null)
			return path;
		path = StringUtil.replaceAll(path, "\\\\", "/");
		if (path.endsWith("/"))
			return path;
		return path.concat("/");
	}

	/**
	 * 获取单选框的代码
	 * 
	 * @return String
	 */
	public String getRadio()
	{
		String value = this.getRadioValue();
		// System.out.println("value:"+value);
		// System.out.println("radio:"+radio);
		// 如果没有设置单选框的名称，或者单选框的值为null时，不生成相应的单选框
		if (radio == null || radio.trim().equals("") || value == null)
			return "";
		String checked = "";
		Map params = this.element.getNode().getParams();
		boolean disabled = false;
		boolean bchecked = false;
		
		
		if(params != null)
		{
		    Boolean btemp = (Boolean)params.get("node_radiodisabled");            
		    if(btemp != null)
		    	disabled = btemp.booleanValue();
		    
		    btemp = (Boolean)params.get("node_radiochecked");            
		    if(btemp != null)
		    	bchecked = btemp.booleanValue();	
		    String node_radioname = (String)params.get("node_radioname"); 
		    if(node_radioname != null && !node_radioname.equals(""))
		    {
		    	radio = node_radioname;
		    }
		        
		}
	        
		if(bchecked)
		{
			checked = "checked";
		}
		else if (getRadioDefaultValue() != null
				&& getRadioDefaultValue().equals(value))
			checked = "checked";

		StringBuilder ret = new StringBuilder();
		ret.append("<input type='radio' name='").append(radio).append(
				"' value='");

		if (value == null)
			ret.append(nodeId);
		else
			ret.append(value);
		ret.append("' ");
		if(disabled)
		{
			ret.append(" disabled ");
		}
		ret.append(checked).append(" ").append(
				this.getRadioExtention()).append(">");
		return ret.toString();
		// return radio == null
		// ? ""
		// : new Input()
		// .setType("radio")
		// .setName(radio)
		// .setValue(nodeId)
		// .setChecked(this.getRadioDefaultValue().equals(nodeId))
		// .toString();
	}

	/**
	 * @return target
	 */
	public String getTarget()
	{
		return target;
	}

	/**
	 * @param string
	 */
	public void setTarget(String string)
	{
		target = string;
	}

	/**
	 * @param string
	 */
	public void setLocalAction(String string)
	{
		localAction = string;
	}

	public void setExtendString(String string)
	{
		extendString = string;
	}

	/**
	 * @return String
	 */
	public String getExtendString()
	{
		return extendString == null ? "" : extendString;
	}

	/**
	 * 复选框的缺省值，以"$$"分隔
	 * 
	 * @return String[]
	 */
	public String[] getCheckBoxDefaultValues()
	{
//		String value = getCheckBoxDefaultValue();
//		if (value == null)
//			return null;
//		// System.out.println("check box default values:" + value);
//		String[] ret = StringUtil.split(value, "\\$\\$");
//		// for(int i = 0; i < ret.length; i ++)
//		// System.out.println("ret[" + i + "]:" + ret[i]);

		return this.checkBoxDefaultValue;

	}

	/**
	 * @return String
	 */
	public String[] getCheckBoxDefaultValue()
	{
		return checkBoxDefaultValue;
	}

	/**
	 * @return String
	 */
	public String getRadioDefaultValue()
	{
		return radioDefaultValue;
	}

	/**
	 * @param string
	 */
	public void setCheckBoxDefaultValue(String[] string)
	{
		checkBoxDefaultValue = string;
	}

	/**
	 * @param string
	 */
	public void setRadioDefaultValue(String string)
	{
		radioDefaultValue = string;
	}

	/**
	 * 获取复选框的扩展代码
	 * 
	 * @return String[]
	 */
	public String getCheckBoxExtention()
	{
		return checkBoxExtention == null ? "" : checkBoxExtention;
	}

	/**
	 * @param string
	 */
	public void setCheckBoxExtention(String string)
	{
		checkBoxExtention = string;
	}

	/**
	 * @return String
	 */
	public String getRadioExtention()
	{
		return radioExtention == null ? "" : radioExtention;
	}

	/**
	 * @param string
	 */
	public void setRadioExtention(String string)
	{
		radioExtention = string;
	}

	/**
	 * @return String
	 */
	public String getCheckboxValue()
	{
		return checkboxValue;
	}

	/**
	 * @return String
	 */
	public String getRadioValue()
	{
		return radioValue;
	}

	/**
	 * @param string
	 */
	public void setCheckboxValue(String string)
	{
		checkboxValue = string;
	}

	/**
	 * @param string
	 */
	public void setRadioValue(String string)
	{
		radioValue = string;
	}

	/**
	 * 锁定到当前页面的焦点节点 Description:
	 * 
	 * @return String
	 */
	public static void getCatchScript(StringBuilder ret,
			HttpServletRequest request, String curNodeId)
	{
		String anchor = request.getParameter("collapse");
		if (anchor == null)
		{
			// return "";
			anchor = request.getParameter("expand");
		}
		if (anchor == null)
			return;

		// StringBuilder ret = new StringBuilder();
		// ret.append("<tr><td><a id='anchor_id'
		// href='#").append(anchor).append("></a>");
		// ret.append("<tr><td><A id=\"anchor_id\"
		// HREF=\"#icon_").append(anchor).append("\"></A>");
		// ret.append("<script language='javascript'><!--\r\n")
		// .append("anchor_id.click();")
		// .append("\r\n//--></script></td></tr>");
		if (curNodeId.equals(anchor))
		{
			ret.append("<A id=\"anchor_id\" HREF=\"#icon_").append(anchor)
					.append("\"></A>");
			ret.append("<script language='javascript'><!--\r\n").append(
					"anchor_id.click();").append("\r\n//--></script>");
		}
		// return ret.toString();
	}
	
	public static void getSelectedScript(StringBuilder buffer,ITree tree,String treeid)
	{
		Template tpl = VelocityUtil.getTemplate("tree.vm");
		VelocityContext context = new VelocityContext();
		context.put("isStaticDynamic",new Boolean(tree.isStaticDynamic()));
		context.put("isStatic",new Boolean(tree.isStatic()));
		context.put("tree", treeid);
		context.put("rootid", tree.getRoot().getId());
		
		
		try {
			StringWriter out = new StringWriter();
			
			tpl.merge(context,out);
			out.flush();
			String temp = out.toString();
			buffer.append(temp);
			out.close();
			
			
		} catch (ResourceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}




	public static void getInitScript(StringBuilder ret, String selectedNode,String contextpath)
	{
		// StringBuilder ret = new StringBuilder();
		ret.append("<script language=\"javascript\">");
		ret.append("     var selectNode;");
		if (selectedNode != null)
		{
			ret.append("selectNode = \"").append(selectedNode).append("\";");
		}
		ret.append("</script >");
		
		
	}
	


	public static void main(String args[])
	{
		// String[] temp = StringUtil.split("aaa$$www","\\$\\$");
		// System.out.println(temp.length);
		// System.out.println(temp[0]);
		// System.out.println(temp[1]);
		System.out.println("堃");
		System.out.println(System.getProperties());

	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	/**
	 * Description:
	 * 
	 * @return String
	 */
	public String getScope()
	{
		return scope;
	}

	/**
	 * @return Returns the isCollapse.
	 */
	public boolean isCollapse()
	{
		return isCollapse;
	}

	/**
	 * @return Returns the params.
	 */
	public String getParams()
	{
		return params;
	}

	public boolean isDoubleEvent()
	{
		return doubleEvent;
	}

	/**
	 * @param params
	 *            The params to set.
	 */
	public void setParams(String params)
	{
		this.params = params;
	}

	/**
	 * @param b
	 */
	public void setNowrap(boolean nowrap)
	{

		this.nowrap = nowrap;

	}

	public void setDoubleEvent(boolean doubleEvent)
	{
		this.doubleEvent = doubleEvent;
	}

	public void setEnablecontextmenu(boolean b)
	{
		enablecontextmenu = b;
	}

	public boolean isEnablecontextmenu()
	{
		return enablecontextmenu;
	}

//	public boolean isDynamic()
//	{
//		return dynamic;
//	}
	private boolean includeRootNode = true;
	public void setTree(ITree itree)
	{
		this.itree = itree;
	}
//	public void setDynamic(boolean dynamic)
//	{
//		this.dynamic = dynamic;
//	}
    
    public void setRecursive(boolean recursive)
    {
        this.recursive = recursive;
        
    }

    public void setCheckboxOnchange(String onchange)
    {
        this.checkboxOnchange = onchange;
        
    }

    public String getCheckboxOnchange()
    {
        return checkboxOnchange;
    }

    public boolean isRecursive()
    {
        return recursive;
    }

	public boolean isUprecursive() {
		return uprecursive;
	}

	public void setUprecursive(boolean uprecursive) {
		this.uprecursive = uprecursive;
	}

    public boolean isIncludeRootNode()
    {
        return includeRootNode;
    }

    public void setIncludeRootNode(boolean includeRootNode)
    {
        this.includeRootNode = includeRootNode;
    }
}
