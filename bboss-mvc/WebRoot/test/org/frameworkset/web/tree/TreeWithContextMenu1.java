package org.frameworkset.web.tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import test.tree.TreeNode;
import test.tree.TreeUtil;

import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.contextmenu.Menu.ContextMenuItem;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class TreeWithContextMenu1  extends COMTree {
	
	
	@Override
	public void setPageContext(PageContext pageContext) {
		
		super.setPageContext(pageContext);
		TreeUtil.buildTreeDatas();
	}

	@Override
	public boolean hasSon(ITreeNode parent) {
		String uid = parent.getId();
		String treetype = request.getParameter("treetype");
		TreeNode node = TreeUtil.getTreeNode(uid.substring(treetype.length()));
		if(node.hasSon())
			return true;
		return false;
	}

	@Override
	public boolean setSon(ITreeNode parent, int curLevel) {
		String uid = parent.getId();
		String treetype = request.getParameter("treetype");
		TreeNode node = TreeUtil.getTreeNode(uid.substring(treetype.length()));
		List<TreeNode> nodes = node.getSons();
		Map params = null;
		/**
		 * 在添加每个节点时将节点的所有参数放置到一个Map对象中，但是以下的几个名称是树中保留的参数名称，不能作为其他参数：
			node_recursive:指定特定树节点的复选框是否具有递归选择的功能，值类型Boolean
			node_uprecursive:指定特定树节点的复选框是否具有递归选择上级复选框的功能，值类型Boolean
			node_partuprecursive：boolean值，指定特定节点的复选框是否具有部分递归
			node_checkboxname：单独指定节点前的的复选框的名称
			nodeLink:指定每个树节点的链接地址，可以是javascript函数，也可以是超链接
			node_linktarget：指定每个节点链接地址弹出窗口名称
			node_radioname：定义每个节点单选框名称
			node_checkboxchecked :boolean值 标识复选框是否选中
			node_checkboxdisabled：boolean值 标识节点前的复选框是否被禁用
			node_radiochecked：boolean值 标识节点前的单选按钮是否被选中
			node_radiodisabled：boolean值 标识节点前的单选按钮是否被禁用
		 */
//		Map<paramname,paramvalue> params = new HashMap();
		for(int i = 0; i < nodes.size(); i ++)
		{
			
			TreeNode son = nodes.get(i);
			params = new HashMap();
			if(son.getName().equals(treetype+"root-1-2"))
				params.put("node_checkboxchecked", new Boolean(true));
			ITreeNode t = addNode(parent,
					treetype+son.getUid(), //treeid 
					treetype+son.getUid(), //tree node name
	                null,// node type
	                true, //show href,true 时树节点将带超链接，false时不带超链接
	                curLevel, //current level
	                (String) null,//备注
	                (String) son.getUid(), //radio value,单选框按钮
	                (String) son.getUid(), //复选框的值
	                params  //为节点连接指定url的参数<paramname,paramvalue>
	                );
			Menu menu = new Menu();
			menu.addContextMenuItem(Menu.MENU_OPEN);
			
			menu.addContextMenuItem(Menu.MENU_EXPAND);
			menu.addContextMenuItem("添加","javascript:edit('添加')",Menu.icon_edit);
			//Menu.ContextMenuItem sitemenuitem0 = new Menu.ContextMenuItem();
			//sitemenuitem0.setName("编辑编辑编辑编辑");
			//sitemenuitem0.setLink("javascript:edit('编辑')");
			//sitemenuitem0.setIcon(Menu.icon_edit);
			//menu.addContextMenuItem(sitemenuitem0);
			menu.addSeperate();
			menu.addContextMenuItem("编辑编辑编辑编辑","javascript:edit('编辑')",Menu.icon_add);
			
			Menu.ContextMenuItem sitemenuitem2 = menu.addContextMenuItem("sitemenuitem2","javascript:edit('sitemenuitem2')",Menu.icon_ok);
			sitemenuitem2.addSubContextMenuItem("子menusubmenuitem_","javascript:edit('子menusubmenuitem_')",Menu.icon_ok);	
			sitemenuitem2.addSubContextMenuItem("子cut","javascript:edit('子cut')",Menu.icon_cut);				
			sitemenuitem2.addSubContextMenuItem("子icon_back","javascript:edit('子icon_back')",Menu.icon_back);
			sitemenuitem2.addSubContextMenuItem("子icon_cancel","javascript:edit('子icon_cancel')",Menu.icon_cancel);
			sitemenuitem2.addSubContextMenuItem("子icon_help","javascript:edit('子icon_help')",Menu.icon_help);
			sitemenuitem2.addSubContextMenuItem("子icon_no","javascript:edit('子icon_no')",Menu.icon_no);
			sitemenuitem2.addSubContextMenuItem("子icon_print","javascript:edit('子icon_print')",Menu.icon_print);
			sitemenuitem2.addSubContextMenuItem("子icon_redo","javascript:edit('子icon_redo')",Menu.icon_redo);
			sitemenuitem2.addSubContextMenuItem("子icon_reload","javascript:edit('icon_reload')",Menu.icon_reload);
			sitemenuitem2.addSubContextMenuItem("icon_remove","javascript:edit('icon_remove')",Menu.icon_remove);
			sitemenuitem2.addSubContextMenuItem("icon_save","javascript:edit('icon_save')",Menu.icon_save);
			sitemenuitem2.addSubContextMenuItem("icon_search","javascript:edit('icon_search')",Menu.icon_search);
			sitemenuitem2.addSubContextMenuItem("icon_undo","javascript:edit('icon_undo')",Menu.icon_undo);
			ContextMenuItem third = sitemenuitem2.addSubContextMenuItem("第二层","javascript:edit('icon_undo')",Menu.icon_undo);
			third.addSubContextMenuItem("三层", "javascript:edit('icon_undo')",Menu.icon_undo);
//			//构建一个子菜单
//			Menu submenu = new Menu();
//			submenu.setIdentity("submenu_" + son.getUid());//保证每个子菜单的id的唯一性，每个节点的子菜单都要唯一
//			Menu.ContextMenuItem submenuitem1 = new Menu.ContextMenuItem();
//			submenuitem1.setName("子菜单1");
//			submenuitem1.setLink("javascript:edit('子菜单1')");
//			submenuitem1.setIcon(request.getContextPath() + "/tree/tree_images/edit.gif");
//			submenu.addContextMenuItem(submenuitem1);
//			
//			Menu.ContextMenuItem submenuitem2 = new Menu.ContextMenuItem();
//			submenuitem2.setName("子菜单2");
//			submenuitem2.setLink("javascript:edit('子菜单2')");
//			submenuitem2.setIcon(request.getContextPath() + "/tree/tree_images/edit.gif");
//			submenu.addContextMenuItem(submenuitem2);
//			
//			Menu.ContextMenuItem cascade = new Menu.ContextMenuItem();
//		
//			cascade.setName("多级菜单演示");
//			cascade.setSubMenu(submenu);
//			
//			
//			
//			menu.addContextMenuItem(cascade);
			
//			//判断是否有模板管理的权限
//			
//			super.addContextMenuOfType(menu);
			
			
			super.addContextMenuOfNode(t,menu);
		}
		return true;
	}
}
