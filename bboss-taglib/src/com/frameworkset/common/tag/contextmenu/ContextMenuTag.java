package com.frameworkset.common.tag.contextmenu;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.config.PageConfig;
import com.frameworkset.util.VelocityUtil;

/**
 * 
 * <p>Title: com.frameworkset.common.tag.contextmenu.ContextMenuTag.java</p>
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
public class ContextMenuTag extends BaseTag {
	
	private static final String menuScript = "contextmenu/popmenu.vm";
	private static final String parentmenuScript = "contextmenu/parentpopmenu.vm";
	private String parent_indent ;
	 /**
     * 是否jquery装载,true-是，false-不是
            默认值：false;
            为false时,标签库将自动为页面引入以下样式和脚本，否则不导入
            true时，上述样式和脚本将通过外部导入
     */
    protected boolean jquery = false; 
	
	
	
//	protected boolean istreemenu = false;
	//private boolean flag = true;
	
	
	/**
	 * contextmenu缓冲的key
	 */
	private String context = null;
	
	/**
	 * contextmenu对象缓冲的范围：
	 * request 缺省值
	 * session
	 * pageContext
	 */
	private String scope = "request";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int doEndTag()  throws JspException  {
		HttpServletRequest request = getHttpServletRequest();
//		HttpSession session = request.getSession(false) ;
		parent_indent = request.getParameter("node_parent_indent");

		if(!isEnablecontextmenu())
			return EVAL_PAGE;
		
		ContextMenu contextMenu  = getContextMenu();
		if(contextMenu == null)
		{
			return EVAL_PAGE;
		}

		
		
		StringBuffer buffer = new StringBuffer();
		StringBuffer scriptbuffer = new StringBuffer();
		String popscript = "";
		if(parent_indent == null || parent_indent.equals(""))
		{
//		    String enablecontextmenu_flag = (String)request.getAttribute(PageConfig.popscript_set_flag);
//	        if(enablecontextmenu_flag != null && enablecontextmenu_flag.equals("true"))//启用右键菜单，如果脚本还没有有输出，则输出并设置属性为true，否则不做处理
//	        {
//	            
//	        }
//	        else
//	        {
			if(!this.isJquery())
			{
				popscript = PageConfig.getPopScript(request, enablecontextmenu);
			}
	            
//                buffer.append(popscript);
                
                
//	        }
	        
			contextMenu.mergeContextMenus();
			Set contextmenus = contextMenu.getContextmenus();
			buildMenus(buffer,scriptbuffer,contextmenus);
		}
		else
		{
			Set contextmenus = contextMenu.getNodeContextmenus();
			buildMenus(buffer,scriptbuffer,contextmenus);
		}

		try {
			Template template = null;
			if(parent_indent == null || parent_indent.equals(""))
			{
				template = VelocityUtil.getTemplate(menuScript);
				VelocityContext context = new VelocityContext();
				context.put("contextpath",request.getContextPath());
				context.put("popscript",popscript);
				context.put("initscript",scriptbuffer.toString());
				context.put("menus",buffer.toString());
				template.merge(context,out);
			}
			else
			{
				template = VelocityUtil.getTemplate(parentmenuScript);
				VelocityContext context = new VelocityContext();
				context.put("contextpath",request.getContextPath());
				
				context.put("initscript",scriptbuffer.toString());
				context.put("menus",buffer.toString());
				template.merge(context,out);
			}
						
			jquery = false;
			this.parent_indent = null;
			
		} catch (ResourceNotFoundException e) {
			this.parent_indent = null;
			
			e.printStackTrace();
		} catch (ParseErrorException e) {
			this.parent_indent = null;
			e.printStackTrace();
		} catch (MethodInvocationException e) {
			this.parent_indent = null;
			e.printStackTrace();
		} catch (Exception e) {
			this.parent_indent = null;
			e.printStackTrace();
		}
		
//		System.out.println("************************************************");
		//reset(contextMenu);
		return super.doEndTag();
	}
	
	private void buildMenus(StringBuffer buffer,StringBuffer scriptbuffer,Set contextmenus)
	{	
		
		
		
		for(Iterator it = contextmenus.iterator(); it.hasNext(); )
		{
			
			Menu menu = (Menu)it.next();
			buildMenu(buffer,scriptbuffer,menu,true);			
	    	
		}
		
//		if( parent_indent != null)
//		{
//				scriptbuffer.append(" $.parser.parse();");
//		}
		

	}
	
	
	
//	/**
//	 * 构建右键菜单
//	 * @param menuid
//	 * @param buffer
//	 * @param menu
//	 */
//	private void buildMenu(String menuid,StringBuffer buffer,Menu menu)
//	{
//		
//		buffer.append(" var menu_"+menuid+" = new Menu();\n ");
//		buffer.append("menu_"+menuid+".BuildMenu(\""+menu.getIdentity()+"\");\n");
//		menu.setJsObjectId("menu_"+menuid);
//		List contextmenuitems = menu.getContextMenuItems();
//		//根据不同的类型构建菜单 peng.yang
//		for(int i = 0; i < contextmenuitems.size(); i ++){
//			Menu.ContextMenuItem contextMenuItem = (Menu.ContextMenuItem)contextmenuitems.get(i);
//			if(contextMenuItem.isSeperate()){
//				buffer.append(menu.getJsObjectId()+".AddSeparator();\n");
//			}else if (contextMenuItem == Menu.MENU_OPEN) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"打开\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}else if (contextMenuItem == Menu.MENU_EXPAND) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"展开/折叠\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}
//			else{
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
//					
//					buildMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\""+contextMenuItem.getName()+"\",")
//						.append("\""+contextMenuItem.getLink()+"\",")
//						.append("\""+contextMenuItem.getIcon()+"\",")
//						.append("\""+contextMenuItem.getTarget()+"\",");
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
//					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append(",").append(contextMenuItem.isDisabled()).append("));\n");
////					buildMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				else
//				{
//					buffer.append("null,").append(contextMenuItem.isDisabled()).append("").append("));\n");
//				}
////				buffer.append("));\n");
//			}
//		}
//
//
//    
//	}
//	
//	/**
//	 * 构建父框的右键菜单
//	 * @param menuid
//	 * @param buffer
//	 * @param menu
//	 */
//	private void buildParentMenu(String menuid,StringBuffer buffer,Menu menu)
//	{
//		
//		buffer.append(" var menu_"+menuid+" = new Menu();\n ");
//		buffer.append("menu_"+menuid+".BuildMenu(\""+menu.getIdentity()+"\");\n");
//		//buffer.append("menuobjects.push('").append(menu.getIdentity()).append("');\n");
//		menu.setJsObjectId("menu_"+menuid);
//		List contextmenuitems = menu.getContextMenuItems();
//		//根据不同的类型构建菜单 peng.yang
//		for(int i = 0; i < contextmenuitems.size(); i ++){
//			Menu.ContextMenuItem contextMenuItem = (Menu.ContextMenuItem)contextmenuitems.get(i);
//			if(contextMenuItem.isSeperate()){
//				buffer.append(menu.getJsObjectId()+".AddSeparator();\n");
//			}else if (contextMenuItem == Menu.MENU_OPEN) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"打开\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}else if (contextMenuItem == Menu.MENU_EXPAND) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"展开/折叠\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}
//			else{
//				
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
////					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append("));\n");
//					buildParentMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\""+contextMenuItem.getName()+"\",")
//						.append("\""+contextMenuItem.getLink()+"\",")
//						.append("\""+contextMenuItem.getIcon()+"\",")
//						.append("\""+contextMenuItem.getTarget()+"\",");
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
//					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append(",").append(contextMenuItem.isDisabled()).append("));\n");
////					buildParentMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				else
//				{
//					buffer.append("null").append(",").append(contextMenuItem.isDisabled()).append("));\n");
//				}
////				buffer.append("));\n");
//			}
//		}
//
//
//    
//	}
	
	
	/**
	 * 构建右键菜单
	 * @param menuid
	 * @param buffer
	 * @param menu
	 */
	private void buildMenu(StringBuffer buffer,StringBuffer scriptbuffer,Menu menu,boolean isroot)
	{
		/**
		 * <div>
							<span>Sub</span>
							<div style="width:80px;">
								<div onclick="javascript:alert('sub21')">sub21</div>
								<div>sub22</div>
								<div>sub23</div>
							</div>
						</div>
		 */

		
		if(isroot)
		{
			String menuid = "_menu_" + menu.getIdentity();
			scriptbuffer.append("addMenu(\"").append(menuid).append("\",\"").append(menu.getIdentity()).append("\");");
		
			buffer.append("<div id=\"_parent_").append(menuid).append("\" style=\"display:none;\" >");
			
			buffer.append("<div id=\"").append(menuid).append("\" style=\"width:120px;\">");
//			buffer.append("<div id=\"").append(menuid).append("\" class=\"easyui-menu\" style=\"width:120px;\">");
		}
		else
		{
			buffer.append("<div style=\"width:150px;\">");
		}
		List contextmenuitems = menu.getContextMenuItems();
		//根据不同的类型构建菜单 peng.yang
		for(int i = 0; i < contextmenuitems.size(); i ++){
			Menu.ContextMenuItem contextMenuItem = (Menu.ContextMenuItem)contextmenuitems.get(i);
			if(contextMenuItem.isSeperate()){
				buffer.append("<div class=\"menu-sep\"></div>");
			}else if (contextMenuItem == Menu.MENU_OPEN) {
				
				if(!contextMenuItem.isDisabled())
				{
					buffer.append("<div onclick=\"javascript:expandNode('"+menu.getIdentity()+"');\">").append("打开").append("</div>");
				}
				else
				{
//					buffer.append("<div class=\"menu-shadow\" onclick=\"javascript:expandNode('"+menu.getIdentity()+"');\">").append("打开").append("</div>");
				}
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"打开\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
				
			}else if (contextMenuItem == Menu.MENU_EXPAND) {
				if(!contextMenuItem.isDisabled())
				{
					buffer.append("<div icon=\"").append(Menu.icon_add).append("\" onclick=\"javascript:expandNode('"+menu.getIdentity()+"');\">").append("展开/折叠").append("</div>");
				}
				else
				{
//					buffer.append("<div icon=\"").append(Menu.icon_add).append("\" class=\"menu-shadow\" onclick=\"javascript:expandNode('"+menu.getIdentity()+"');\">").append("展开/折叠").append("</div>");
				}
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"展开/折叠\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
			}
			else{
				buffer.append("<div " );
				if(!contextMenuItem.isDisabled())
				{
					if(contextMenuItem.getLink() != null)
					{
						if(contextMenuItem.getLink().startsWith("javascript:"))
						{
							buffer.append("onclick=\"").append(contextMenuItem.getLink()).append("\" ");
						}
						else
						{
							String target = contextMenuItem.getTarget();
							if(target != null || target.equals(""))
							{
								
								
								target = "_self";
								
							}
							buffer.append("onclick=\"javascript:openUrl('").append(contextMenuItem.getLink()).append("','").append(target).append("');\" ");
						}
					}
				}
				else
				{
					if(contextMenuItem.getDisableMsg() == null)
					{
						buffer.append("onclick=\"javascript:$.messager.alert('提示','操作无效，请确认拥有[").append(contextMenuItem.getName()).append("]的权限，请联系管理员.','warning');\" ");
					}
					else
					{
						buffer.append("onclick=\"javascript:$.messager.alert('提示','").append(contextMenuItem.getDisableMsg()).append("','warning');\" ");
					}
				}
				String icon = contextMenuItem.getIcon();
				if(icon != null && !icon.equals(""))
				{
					if(icon.startsWith("icon-"))
						buffer.append(" icon=\"").append(icon).append("\" ");
					else
					{
//						buffer.append(" icon=\"").append(Menu.icon_ok).append("\" ");
						buffer.append(" style=\"background:url('").append(icon).append("') no-repeat; \"");
					}
					
				}
//				if(!contextMenuItem.isDisabled())
				{
					String clazz = contextMenuItem.getClazz() ;
					
					if(clazz != null && !clazz.equals(""))
					{
						buffer.append(" class=\"").append(clazz).append("\" ");
					}
				}
//				else
//				{
//					buffer.append(" class=\"menu-shadow\" ");
//				}
				buffer.append(">");
				if(contextMenuItem.getSubMenu()!=null)
				{
//					String submenuid = "menu_"+menuid + i ;
//					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
					buffer.append("<span>").append(contextMenuItem.getName()).append("</span>");
					
					buildMenu(buffer,scriptbuffer,contextMenuItem.getSubMenu(),false);
					
					
				}
				else
				{
					
					
					buffer.append(contextMenuItem.getName());
				}
				buffer.append("</div>");
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\""+contextMenuItem.getName()+"\",")
//						.append("\""+contextMenuItem.getLink()+"\",")
//						.append("\""+contextMenuItem.getIcon()+"\",")
//						.append("\""+contextMenuItem.getTarget()+"\",");
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
//					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append(",").append(contextMenuItem.isDisabled()).append("));\n");
////					buildMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				else
//				{
//					buffer.append("null,").append(contextMenuItem.isDisabled()).append("").append("));\n");
//				}
//				buffer.append("));\n");
			}
		}
		buffer.append("</div>");
		if(isroot)
			buffer.append("</div>");
		


//		return menuid;
	}
	
//	/**
//	 * 构建父框的右键菜单
//	 * @param menuid
//	 * @param buffer
//	 * @param menu
//	 */
//	private void buildParentMenu(String menuid,StringBuffer buffer,Menu menu)
//	{
//		
//		buffer.append(" var menu_"+menuid+" = new Menu();\n ");
//		buffer.append("menu_"+menuid+".BuildMenu(\""+menu.getIdentity()+"\");\n");
//		//buffer.append("menuobjects.push('").append(menu.getIdentity()).append("');\n");
//		menu.setJsObjectId("menu_"+menuid);
//		List contextmenuitems = menu.getContextMenuItems();
//		//根据不同的类型构建菜单 peng.yang
//		for(int i = 0; i < contextmenuitems.size(); i ++){
//			Menu.ContextMenuItem contextMenuItem = (Menu.ContextMenuItem)contextmenuitems.get(i);
//			if(contextMenuItem.isSeperate()){
//				buffer.append(menu.getJsObjectId()+".AddSeparator();\n");
//			}else if (contextMenuItem == Menu.MENU_OPEN) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"打开\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}else if (contextMenuItem == Menu.MENU_EXPAND) {
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\"展开/折叠\",")
//				.append("\"javascript:expandNode('"+menu.getIdentity()+"');\",")
//				.append("\""+contextMenuItem.getIcon()+"\",")
//				.append("null,null,").append(contextMenuItem.isDisabled()).append("));\n");
//			}
//			else{
//				
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
////					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append("));\n");
//					buildParentMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				buffer.append(menu.getJsObjectId()+".Add(new MenuItem(\""+contextMenuItem.getName()+"\",")
//						.append("\""+contextMenuItem.getLink()+"\",")
//						.append("\""+contextMenuItem.getIcon()+"\",")
//						.append("\""+contextMenuItem.getTarget()+"\",");
//				if(contextMenuItem.getSubMenu()!=null)
//				{
////					String submenuid = "menu_"+menuid + i ;
////					contextMenuItem.getSubMenu().setJsObjectId(submenuid);
//					buffer.append(contextMenuItem.getSubMenu().getJsObjectId()).append(",").append(contextMenuItem.isDisabled()).append("));\n");
////					buildParentMenu(menuid + i,buffer,contextMenuItem.getSubMenu());
//					
//				}
//				else
//				{
//					buffer.append("null").append(",").append(contextMenuItem.isDisabled()).append("));\n");
//				}
////				buffer.append("));\n");
//			}
//		}
//
//
//    
//	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}
	
	public ContextMenu getContextMenu()
	{
		HttpServletRequest request = getHttpServletRequest();
		HttpSession session = request.getSession(false) ;
		if(this.context == null || this.context.trim().equals(""))
			return null;
		if(scope == null || scope.equals("request"))
		{
			ContextMenu menu = (ContextMenu)request.getAttribute(context);
			return menu;
		}
		else if(session != null && scope.equals("session"))
		{
			ContextMenu menu = (ContextMenu)session.getAttribute(context);
			return menu;
		}
		else if(scope.equals("pageContext"))
		{
			ContextMenu menu = (ContextMenu)pageContext.getAttribute(context);
			return menu;
		}
		else
			return null;
		
		
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	private void reset(ContextMenu menu) {
		// TODO Auto-generated method stub
		menu.getContextmenus().clear();		
//		menuScript = "contextmenu/popmenu.vm";
		//flag = true;
		context = null;
		jquery = false;
		scope = "request";
	}



	  public boolean isJquery()
	    {
	        return jquery;
	    }

	    public void setJquery(boolean jquery)
	    {
	        this.jquery = jquery;
	    }	
	

}
