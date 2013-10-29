/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.frameworkset.common.tag.pager.config;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.ecs.html.Link;
import org.apache.ecs.html.Script;

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.util.VelocityUtil;

/**
 * <p>Title: PageConfig.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-4-12 下午05:55:45
 * @author biaoping.yin
 * @version 1.0
 */
public class PageConfig extends BaseTag
{
    private static final String popscript = "contextmenu/popscript.vm";
    public static final String popscript_set_flag= "popscript_set_flag________"; 
    public static final String jqueryscript_set_flag= "jqueryscript_set_flag________";
    public static final String pagerscript_set_flag= "pagerscript_set_flag________";
    public static final String treescript_set_flag= "treescript_set_flag________";
    private boolean enablecontextmenu = true;
    private boolean enabletree = true;
    @Override
    public int doEndTag() throws JspException
    {
        this.enablecontextmenu = true;
        return super.doEndTag();
    }
    
    public static final String node_parent_indent = "node_parent_indent";
    public static final String easyui_css = "/include/themes/default/easyui.css";
    public static final String icon_css =     "/include/themes/icon.css";
    public static final String jquery_easyui_js =     "/include/jquery.easyui.min.js";
    public static final String jquery_js =     "/include/jquery-1.4.2.min.js";
    public static final String pager_js = "/include/pager.js";
    public static final String pager_css = "/include/pager.css";
  
    
    public static String getPopScript(HttpServletRequest request ,boolean enablecontextmenu) 
    {
//        if(!enablecontextmenu) //没有启用右键菜单，不输出右键菜单js脚本
//        {
//            return "";
//        }
//        StringBuffer output = new StringBuffer(150);
        String enablecontextmenu_flag = (String)request.getAttribute(popscript_set_flag);
        String parent_indent = request.getParameter(node_parent_indent);
       
        if(parent_indent == null || parent_indent.equals("")) 
        {
            
        }
        else //启用右键菜单，但是是树的延迟加载，不需要输出右键菜单js脚本
        {
            return "";
        }
        if(enablecontextmenu_flag != null && enablecontextmenu_flag.equals("true")) //启用右键菜单，已经输出右键菜单js脚本不需要加
        {
            return "";
        }
        else //启用右键菜单，第一次输出右键菜单js脚本，并设置标记enablecontextmenu_flag为true
        {
            try
            {
                /**
                 * <link rel="stylesheet" type="text/css" href="${contextpath}/include/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="${contextpath}/include/themes/icon.css">
	<script type="text/javascript" src="${contextpath}/include/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="${contextpath}/include/jquery.easyui.min.js"></script>
                 */
            	
            	
                
                java.io.StringWriter wi = new StringWriter();
                wi.write(PageConfig.getLink(request.getContextPath() + easyui_css, request));
                wi.write(PageConfig.getLink(request.getContextPath() + icon_css, request));
                wi.write(PageConfig.getScript(request.getContextPath() + jquery_js, request));
                wi.write(PageConfig.getScript(request.getContextPath() + jquery_easyui_js, request));
                if(enablecontextmenu)
            	{
	                Template template  = VelocityUtil.getTemplate(popscript);
	                
	                VelocityContext context = new VelocityContext();
	                context.put("contextpath",request.getContextPath());
	                template.merge(context,wi);
            	}
        //        System.out.println("wi.getBuffer()________________________________________________");
        //        System.out.println(wi.getBuffer());
//                output.append(wi.getBuffer());
                request.setAttribute(popscript_set_flag, "true");
               return wi.getBuffer().toString();
            }
            catch (ResourceNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ParseErrorException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (MethodInvocationException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            return "";//启用右键菜单，第一次输出右键菜单js脚本，加载时出现异常返回""
        }
        
//        if(enablecontextmenu)
//        {
//            Template template  = VelocityUtil.getTemplate(popscript);
//            
//            VelocityContext context = new VelocityContext();
//            context.put("contextpath",request.getContextPath());
//            
//            java.io.StringWriter wi = new StringWriter();
//            template.merge(context,wi);
//    //        System.out.println("wi.getBuffer()________________________________________________");
//    //        System.out.println(wi.getBuffer());
//            output.append(wi.getBuffer());
//            request.setAttribute(popscript_set_flag, "true");
//        }
//        return output.toString();
    }
    
    public static final String treeview_css = "/include/treeview.css";
    public static String getTreeConfig(HttpServletRequest request,boolean enabletree) 
    {
//        String configed = (String)request.getAttribute(treescript_set_flag);
//        if(configed == null || !configed.equals("true"))
//        {
//            StringBuffer output = new StringBuffer(150);
//            
//            output.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"").append(request.getContextPath()).append("/include/treeview.css\"/>");
//            request.setAttribute(treescript_set_flag,"true");
//            return output.toString();
//        }
//        return "";
    	if(!enabletree )
    		return "";
    	String treecss = request.getContextPath()+treeview_css;
    	return PageConfig.getLink(treecss, request);
    }
    
    public static String getJqueryConfig(HttpServletRequest request) 
    {
    	String src = request.getContextPath()+jquery_js;
    	return PageConfig.getScript(src, request);
//        String configed = (String)request.getAttribute(jqueryscript_set_flag);
//        
//        if(configed == null || !configed.equals("true"))
//        {
//            StringBuffer output = new StringBuffer(150);
//            
//            output.append("<script src=\"").append(request.getContextPath())
//            .append("/include/jquery-1.4.2.min.js\" type=\"text/javascript\"></script>");
//            
//            request.setAttribute(jqueryscript_set_flag,"true");
//            return output.toString();
//        }
//        return "";
    }
    
    public static String getPagerConfig(HttpServletRequest request)
    {
//        String configed = (String)request.getAttribute(pagerscript_set_flag);
//        if(configed == null || !configed.equals("true"))
//        {
//            StringBuffer output = new StringBuffer(150);
//            output.append("<script src=\"").append(request.getContextPath())
//            .append("/include/pager.js\" type=\"text/javascript\"></script>");
//            
//            request.setAttribute(pagerscript_set_flag,"true");
//            return output.toString();
//        }
//        return "";
    	String pagerjs = request.getContextPath()+pager_js;
    	return PageConfig.getScript(pagerjs, request);
    	
    }
    
    public static String getPagerCss(HttpServletRequest request)
    {
//        String configed = (String)request.getAttribute(pagerscript_set_flag);
//        if(configed == null || !configed.equals("true"))
//        {
//            StringBuffer output = new StringBuffer(150);
//            output.append("<script src=\"").append(request.getContextPath())
//            .append("/include/pager.js\" type=\"text/javascript\"></script>");
//            
//            request.setAttribute(pagerscript_set_flag,"true");
//            return output.toString();
//        }
//        return "";
    	String pagerjs = request.getContextPath()+pager_css;
    	return PageConfig.getLink(pagerjs, request);
    	
    }
    @Override
    public int doStartTag() throws JspException
    {
        
        int ret = super.doStartTag();
        
        try
        {
            out.print(getJqueryConfig(request));
            out.print(getPagerConfig(request));
            out.print(getPagerCss(request));            
            out.print(getTreeConfig(request,enabletree));
            out.print(getPopScript(request, enablecontextmenu));
        }
        catch (Exception e)
        {
            throw new JspException(e);
        }
        return ret;
    }
    public boolean isEnablecontextmenu()
    {
        return enablecontextmenu;
    }
    public void setEnablecontextmenu(boolean enablecontextmenu)
    {
        this.enablecontextmenu = enablecontextmenu;
    }
    public static final Object lock = new Object();

    public static void putScript(String src,HttpServletRequest request,JspWriter out)
    {
    	if(src == null || src.equals(""))
			return ;
		if(request.getAttribute(src) != null)
			return ;
		Script script = new Script();
		script.setSrc(src);
		script.setType("text/javascript");
		try {
			out.print(script.toString());
			request.setAttribute(src, lock);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public static String getScript(String src,HttpServletRequest request)
    {
    	if(src == null || src.equals(""))
			return "";
		if(request.getAttribute(src) != null)
			return "";
		Script script = new Script();
		script.setSrc(src);
		script.setType("text/javascript");
		
		request.setAttribute(src, lock);
		return script.toString();
			
		
		
    }
    
//    public static String getPagerCSS(String src,HttpServletRequest request)
//    {
//    	if(src == null || src.equals(""))
//			return "";
//		if(request.getAttribute(src) != null)
//			return "";
//		/**
//		 * <link rel="stylesheet"
//			href="${pageContext.request.contextPath}/css/classic/mainnav.css"
//			type="text/css">
//		 */
//		Link link = new Link();
//		link.setRel("stylesheet");
//		link.setHref(src);
//		link.setType("text/css");
//		
//		
//		request.setAttribute(src, lock);
//		return link.toString();
//			
//		
//		
//    }
    
    public static void putLink(String src,HttpServletRequest request,JspWriter out)
    {
    	if(src == null || src.equals(""))
			return ;
		if(request.getAttribute(src) != null)
			return ;
		Link script = new Link();
		script.setHref(src);
		
		script.setRel("stylesheet");
		script.setType("text/css");
		try {
			out.print(script.toString());
			request.setAttribute(src, lock);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public static String getLink(String src,HttpServletRequest request)
    {
    	if(src == null || src.equals(""))
			return "";
		if(request.getAttribute(src) != null)
			return "";
		Link script = new Link();
		script.setHref(src);
		
		script.setRel("stylesheet");
		script.setType("text/css");
		request.setAttribute(src, lock);
		return script.toString();
			
		
		
    }

	public boolean isEnabletree() {
		return enabletree;
	}

	public void setEnabletree(boolean enabletree) {
		this.enabletree = enabletree;
	}

}
