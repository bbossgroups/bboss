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

package com.frameworkset.common.tag.pager.tags;

import java.util.Locale;
import java.util.Stack;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.ecs.html.A;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Option;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.Select;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.util.I18NUtil;

import com.frameworkset.common.tag.TagUtil;
import com.frameworkset.common.tag.pager.config.PageConfig;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.util.StringUtil;

/**
 *
 * 生成导航按钮，例如：上一页，下一页，首页，尾页等等
 *
 * @author biaoping.yin
 * @version 1.0
 */
public final class IndexTag extends PagerTagSupport {
	
	/**
	 * 扩展属性，导航按钮是否使用图片
	 */
    private boolean useimage = false;
    private boolean usegoimage = false;
    public static final String[] _sizescope = new String[] {"5","10","20","30","40","50","60","70","80","90","100"};
    private String sizescope = null;
    
    private static boolean enable_page_size_set = ApplicationContext.getApplicationContext().getBooleanProperty("enable_page_size_set",true);
    private static boolean enable_page_total_size = ApplicationContext.getApplicationContext().getBooleanProperty("enable_page_total_size",true);
    /**
     * 上下翻页如果没有连接时，是否添加a标记
     * true（默认值） 添加，false不添加
     */
    private boolean aindex = false;
    /**
     * 数字页面索引前缀
     */
    private String numberpre ;
    /**
     * 数字页面索引后缀
     */
    private String numberend;
    
//    /**
//     * jquery内容容器
//     */
//    private String containerid ;
//    
//    /**
//     * jquery内容选择器
//     */
//    private String selector;
    
    public String getImagedir()
    {
        return imagedir;
    }
    

    public void setImagedir(String imagedir)
    {
//        if(imagedir != null)
//        {
//            this.imagedir = StringUtil.getRealPath(request, imagedir);            
//        }
        this.imagedir = imagedir;
        
    }

    public void setUseimage(boolean useimage)
    {
        this.useimage = useimage;
    }
    
    /**
     * 导航图片的扩展属性串
     */
    private String imageextend = " border=0 ";
    
    /**
     * 导航图片的扩展属性串
     */
    private String centerextend ;
    public String getCenterextend()
    {
        return centerextend;
    }


    public void setCenterextend(String centerextend)
    {        
        this.centerextend = centerextend;
    }


    /**
     * 
     * @return
     */
    public String getImageextend()
    {
        return imageextend;
    }


    public void setImageextend(String imageextend)
    {
        if(imageextend != null && !imageextend.equals(""))
            this.imageextend = imageextend;
    }
    
    

    /**
     * 扩展属性，导航按钮图片对应的目录
     */
    private String imagedir = "/include/images";
    
    public static final String first_image = "first.gif";
    public static final String next_image = "next.gif";
    public static final String pre_image = "pre.gif";
    public static final String last_image = "last.gif";
    public static final String go_image = "go.jpg";
    
    /**本属性控制分页标签中哪些导航按钮出现在页面上*/
	private String export = null;
    private String style = "V";
    
    
    
    /**
     * 自定义跳转函数切换标识
     * 为true，jsp页面中必须放置一个名称为
     * com.frameworkset.goform的form表单，不需要指定action，method为post
     */
    private boolean custom = false;

	public final void setExport(String value) throws JspException {
		this.export = value;
	}

    public void setStyle(String style) {
        this.style = style;
    }

    public final String getExport() {
		return export;
	}

    public String getStyle() {
        return style;
    }

    /**
	 * 本属性为7个byte字节，每个字节的值域为0和1，
	 * 分别控制对应位置上的按钮是否出现
	 * 0表示对应位置上的按钮出现，否则不出现
	 */
	protected byte[] switchcase = new byte[9];

	/**
	 * 中间页面标记个数，为-1时没有中间页面
	 */
	
    private int tagnumber = -1;

    public int getTagnumber()
    {
        return tagnumber;
    }


    public void setTagnumber(int tagnumber)
    {
        this.tagnumber = tagnumber;
    }

    /**
     * 中间页面样式名称
     */
    private String classname;

	public String getClassname()
    {
        return classname;
    }


    public void setClassname(String classname)
    {
        this.classname = classname;
    }


    protected void parser() {
		if(getExport() != null)
		{
			String temp = getExport();
			for (int i = 0; i <switchcase.length && i < temp.length(); i++)
				switchcase[i] = (byte)(temp.charAt(i) - 48);
		}
	}

	private boolean skip()
	{
		return (switchcase[0]
				& switchcase[1]
				& switchcase[2]
				& switchcase[3]
				& switchcase[4]
				& switchcase[5]
				& switchcase[6]& switchcase[7]& switchcase[8]) == 1;
	}
	public int doStartTag() throws JspException {
		super.doStartTag();
		if(this.pagerContext == null)
		{
			String t_id = "pagerContext." ;
			HttpServletRequest request = this.getHttpServletRequest();
			if(request instanceof CMSServletRequest)
			{
				CMSServletRequest cmsRequest = (CMSServletRequest)request;
				t_id += cmsRequest.getContext().getID() ;
			}
			else if(this.id != null && !id.trim().equals(""))
			{
				t_id += id;
			}
			else
				t_id += PagerContext.DEFAULT_ID;
			this.pagerContext = (PagerContext)request.getAttribute(t_id);
			
		}
        //indexhelper = new IndexHelper(pagerContext);
		if (pagerContext == null || pagerContext.ListMode())
		{
			return SKIP_BODY;
		}

		//分析控制页面上哪些按钮需要被显示的属性export
		parser();
		if(imagedir != null)
        {
            this.imagedir = StringUtil.getRealPath(request, imagedir);            
        }
		if(!skip())
			try {
			    String output = generateContent();
//			    System.out.println(output);
				this.getJspWriter().print(output);
			} catch (Exception e) {
				throw new JspException(e.getMessage());
			}
		return SKIP_BODY;


	}

//	public static void main(String[] args)
//	{
//		byte[] switchcase = new byte[7];
//		String test = "1000000";
//		System.out.println("test:"+test.charAt(0));
//		switchcase[0] = (byte)(test.charAt(0) - 48);
//		System.out.println(switchcase[0]);
//	}

	public int doEndTag() throws JspException {

		//		if (indexTagExport != null) {
		//			String name;
		//			if ((name = indexTagExport.getItemCount()) != null) {
		//				restoreAttribute(name, oldItemCount);
		//				oldItemCount = null;
		//			}
		//
		//			if ((name = indexTagExport.getPageCount()) != null) {
		//				restoreAttribute(name, oldPageCount);
		//				oldPageCount = null;
		//			}
		//		}
		//
		super.doEndTag();//是否存在问题，biaoping.yin打开于20081121日
		this.custom = false;
		export = null;
	    style = "V";
	    switchcase = new byte[9];
	    this.pageContext = null;
	    imagedir = "/include/images";
	    this.imageextend = " border=0 ";
	    this.useimage = false;	    
	    this.tagnumber = -1;
	    this.classname = null;
	    this.centerextend = null;
//	    pagerContext.getContainerid() = null;
//	    pagerContext.getSelector() = null;
	    this.sizescope = null;
	    this.usegoimage = false;
	    this.numberend = null;
	    this.numberpre = null;
	    this.aindex = false;
		return EVAL_PAGE;
	}

	public void release() {
		export = null;
		super.release();
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */

	/**
	 *
	 */
	public String generateContent() {
		StringBuffer ret = new StringBuffer();
        //针对web应用引入javascript脚本文件，wap应用无需引入
		boolean needScript = (pagerContext.indexs == null || pagerContext.indexs.isEmpty());
		if(needScript)
		{
            if(!pagerContext.isWapflag())
			    ret.append(this.getScript());
		}
		Locale locale = I18NUtil.getRequestContextLocal(request);
		String total_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.total",locale) ;
		String total_page_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.total.page",locale) ;
		String total_records_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.total.records",locale) ;
//		ret.append(
//			"<table width=\"100%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"1\">");
		//ret.append("<tr class='TableHead1'>");
		//ret.append("<td align='center' colspan=100>");
        if(!pagerContext.isWapflag())
        {
            if (needScript && pagerContext.getForm() != null) {
                String name = pagerContext.getId() + ".PAGE_QUERY_STRING";
                ret.append("<input type=\"hidden\" id=\"" + name + "\" name=\"" + name + "\"/>");
            }
        }
        if( switchcase[8] == 0)
        {
        	if(enable_page_total_size && !pagerContext.isMore())
        		ret.append(total_label)//共
        		.append("<span class='Total'>").append(!pagerContext.ListMode()?pagerContext.getItemCount():pagerContext.getDataSize()).append("</span>").append(total_records_label);//条记录  
        }

		//ret.append("<div align=\"right\">");
		if (switchcase[0] == 0) {
			long serial =
					!pagerContext.isMore()?Math.min(pagerContext.getPageNumber() + 1, pagerContext.getPageCount()):pagerContext.getPageNumber() + 1;
			if(!pagerContext.isMore())
			{
	            if(!pagerContext.isWapflag())
				    ret.append("<span class='current'>" + serial + "</span>/");
	            else
	                ret.append("<span class='current'>" + serial + "</span>/");
			}
			else
			{
				if(!pagerContext.isWapflag())
				    ret.append("<span class='current'>第" + serial + "页</span>");
	            else
	                ret.append("<span class='current'>第" + serial + "页</span>");
			}
		}
		if (switchcase[1] == 0 && !pagerContext.isMore()) {
            if(!pagerContext.isWapflag())
                ret.append(
                		pagerContext.getPageCount()).append(total_page_label);//" 页"
            else
                ret.append(
                		pagerContext.getPageCount()).append(total_page_label);//" 页"

		}
		

		if (switchcase[2] == 0) {
			if(!pagerContext.isWapflag() )
				ret.append("<span class='next'>"+this.getFirstContent()).append("</span>");           
            else
                ret.append(this.getWapFirstContent()).append("	");
		}

		if (switchcase[3] == 0) {
            if(!pagerContext.isWapflag())
    			ret.append("<span class='next'>"+this.getPrevContent()).append("</span>	");
            else
                ret.append(this.getWapPrevContent()).append("	");
		}
		if(this.tagnumber > 0)//待处理
        {
            StringBuffer center = new StringBuffer();
            
            this.getCenterContent((int)pagerContext.getPageNumber(), (int)pagerContext.getPageCount(), center);
            String temp = center.toString();
            ret.append(temp);
        }
		if (switchcase[4] == 0)
            if(!pagerContext.isWapflag())
			    ret.append("<span class='next'>"+this.getNextContent()).append("</span>");
            else
                ret.append(this.getWapNextContent()).append("	");
		if (switchcase[5] == 0)
		{
			if(!pagerContext.isMore())
			{
	            if(!pagerContext.isWapflag())
	    			ret.append("<span class='next'>"+this.getLastContent()).append("</span>");
	            else
	                ret.append(this.getWapLastContent()).append("	");
			}
		}
		if (switchcase[6] == 0)
			if(!pagerContext.isMore())
			{
	            if(!pagerContext.isWapflag())
	    			ret.append(this.getGoContent());
	            else
	               	ret.append(this.getWapGoContent());
			}
		
		
		if (switchcase.length > 7 )
		{
			if( switchcase[7] == 0)
				ret.append(this.getMaxPageItem());
						
		}
		else
		{
			if(enable_page_size_set)
				ret.append(this.getMaxPageItem());
		}
//            else
//               	ret.append(this.getWapGoContent());
		/* 中间页码 */
        
//		ret.append("</div>");
//		ret.append("</td>");
//		ret.append("</tr>");
//		ret.append("</table>");
		//标签本身进栈，以便分页标签进行跟踪
		push();
		return ret.toString();

	}
	
	
	public  String getMaxPageItem()
	{
		
		String defaultsize = pagerContext.getMaxPageItems() + "";
		Select select = new Select();
		select.setID("__max_page_size");
		select.setName("__max_page_size");
		if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
		{
			select.setOnChange("javascript:bboss.pager.__chagePageSize(event,'" + pagerContext.getCookieid() + "','__max_page_size','" +  pagerContext.getTruePageUrl()  + "')");
		}
		else
		{
			select.setOnChange("javascript:bboss.pager.__chagePageSize(event,'" + pagerContext.getCookieid() 
							+ "','__max_page_size','"
							+ pagerContext.getTruePageUrl() 
							+ "','"+ pagerContext.getSelector() 
							+ "','"+ pagerContext.getContainerid() + "')");
		}
		
//		Option  option = new Option();
//		option.setValue(defaultsize);
//		option.setTagText("记录设置");
//		select.addElement(option);
		Option  option = null;
		String[] ops = null;
		if(this.sizescope == null || this.sizescope.equals(""))
			ops = _sizescope;
		else
			ops = this.sizescope.split(",");
		ops = sortitems(ops,pagerContext.getCustomMaxPageItems());
		for(int i =0; i < ops.length; i ++)
		{
			option = new Option();
			option.setValue(ops[i]);
			option.setTagText(ops[i]);
			if(defaultsize.equals(ops[i]))
			{
				option.setSelected(true);
			}
			select.addElement(option);
		}
		Locale locale = I18NUtil.getRequestContextLocal(request);
		String everypage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.everypageshow",locale) ;
		String everypagerecords_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.everypageshow.records",locale) ;
		return new StringBuffer(" <span class='pages1'>").append(everypage_label)//每页显示
		.append(select.toString()).append(everypagerecords_label)//条
		.append("</span>").toString();
		
	}
	
	private String[] sortitems(String[] ops,int pagesize)
	{
		if(pagesize <= 0 )
			return ops;
		if(ops == null || ops.length <= 0)
		{
			return new String[]{pagesize + ""};
		}
		
		
		for(int i = 0 ; i < ops.length ; i ++)
		{
			String size = ops[i];
			
			try {
				int size_ = Integer.parseInt(size);
				if (pagesize == size_)
					return ops;
			} catch (Exception e) {
				continue;
			}
		}
		
		String[] ret = new String[ops.length + 1];
		ret[0] = pagesize + "";
		for(int i = 1 ;i< ret.length; i ++)
		{
			ret[i] = ops[i - 1];
		}
		return ret;
	}

//	/**
//	 * notified by biaoping.yin on 2005-02-16
//	 * 注销原因：已经移到pagerContext中
//	 * Description:生成自定义的翻页索引的链接地址
//	 * @param formName
//	 * @param params
//	 * @param promotion
//	 * @param forwardUrl
//	 * @return
//	 * String
//	 */
//	private String getCustomUrl(String formName,String params,boolean promotion,String forwardUrl)
//	{
//		String customFirstUrl = "javascript:pageSubmit('"
//									 + formName
//									 + "','"
//									 + params
//									 + "','"
//									 + forwardUrl
//									 + "',"
//									 + promotion + ")";
//		return customFirstUrl;
//
//	}
    private String getWapFirstContent()
    {
        String firstUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.FIRST_PAGE));
        if (firstUrl.equals(""))
            return "";
        String customFirstUrl = null;
        customFirstUrl = firstUrl;
        customFirstUrl = StringUtil.replace(customFirstUrl,"&","&amp;");
        if(this.getStyle().equals("V"))
            return "<br/>" + new A().setHref(customFirstUrl).setTagText("首页").toString();
        else
            return "  " + new A().setHref(customFirstUrl).setTagText("首页").toString();

    }
    private static String buildPath(String dir,String fileName)
    {
        if(dir == null || dir.equals(""))
            return fileName;
        if(dir.endsWith("/"))
        {
            return dir + fileName;
        }
        else
        {
            return dir + "/" + fileName;
        }            
    }
    
    public static String getJqueryUrl(String url,String containerid,String selector)
    {
        StringBuffer ret = new StringBuffer();
        if(selector != null)
            ret.append("javascript:bboss.pager.loadPageContent('").append(url).append("','").append(containerid).append("','").append(selector).append("');");
        else
            ret.append("javascript:bboss.pager.loadPageContent('").append(url).append("','").append(containerid).append("',null);");
        return ret.toString();
    }
    
	private String getFirstContent() {
		String firstUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.FIRST_PAGE));
		Locale locale = I18NUtil.getRequestContextLocal(request);
		String firstpage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.firstpage",locale) ;
		if (firstUrl.equals(""))
		{
		    if(!this.useimage)
		    {
		    	if(this.aindex)
		    	{
			        return new StringBuffer("<a href='#'>")
			    			.append(firstpage_label)//首 页
			    			.append("</a>").toString();
		    	}
		    	else
		    	{
		    		return firstpage_label;
		    	}
		    }
		    else
		    {
		        IMG image = new IMG();
		        
		        image.setSrc(buildPath(this.getImagedir(), first_image));
		        image.setAlt(firstpage_label);//首 页
		        if(this.imageextend != null)
		        {
		            
		            image.setExtend(this.imageextend);
		        }
		       
		        return image.toString();
		    }
		}
		String customFirstUrl = null;
		if(pagerContext.getForm() != null)
		{
			String params  = pagerContext.getQueryString(0,pagerContext.getSortKey(),pagerContext.getDesc());
			customFirstUrl = pagerContext.getCustomUrl(pagerContext.getForm(),
											   params,
											   pagerContext.getPromotion(),firstUrl,pagerContext.getId());
		}
		else
		{
			customFirstUrl = firstUrl;
		}

		if(!this.useimage)
		{
		    A a = new A();
		    a.setTagText(firstpage_label);//首 页;
		    if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
		    {
		        a.setHref(customFirstUrl);
		    }
		    else
		    {
		    	a.setHref("#");
		        a.setOnClick(this.getJqueryUrl(customFirstUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
		    }
		    
		    return a.toString();
		}
		else
		{
		    IMG image = new IMG();
            
            image.setSrc(buildPath(this.getImagedir(), first_image));
            image.setAlt(firstpage_label);//首 页
            if(this.imageextend != null)
            {                
                image.setExtend(this.imageextend);
            }       
            A a = new A();
            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
            {
                a.setHref(customFirstUrl);
            }
            else
            {
                a.setOnClick(this.getJqueryUrl(customFirstUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
            }
            a.addElement(image);
            return a.toString();
            
		}

		//共1页　 首页　上一页　下一页　尾页 　 跳转  页

		//return  ret.toString();
	}
	
//	private String getFirstContent() {
//        String firstUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.FIRST_PAGE));
//        
//        if (firstUrl.equals(""))
//        {
//            if(!this.useimage)
//                return "首 页";
//            else
//            {
//                IMG image = new IMG();
//                
//                image.setSrc(buildPath(this.getImagedir(), first_image));
//                image.setAlt("首 页");
//                if(this.imageextend != null)
//                {
//                    
//                    image.setExtend(this.imageextend);
//                }
//               
//                return image.toString();
//            }
//        }
//        String customFirstUrl = null;
//        if(pagerContext.getForm() != null)
//        {
//            String params   = pagerContext.getQueryString(0,pagerContext.getSortKey(),pagerContext.getDesc());
//            customFirstUrl = pagerContext.getCustomUrl(pagerContext.getForm(),
//                                               params,
//                                               pagerContext.getPromotion(),firstUrl,pagerContext.getId());
//        }
//        else
//        {
//            customFirstUrl = firstUrl;
//        }
//
//        if(!this.useimage)
//        {
//            return new A().setHref(customFirstUrl).setTagText("首 页").toString();
//        }
//        else
//        {
//            IMG image = new IMG();
//            
//            image.setSrc(buildPath(this.getImagedir(), first_image));
//            image.setAlt("首 页");
//            if(this.imageextend != null)
//            {                
//                image.setExtend(this.imageextend);
//            }            
//            return new A().setHref(customFirstUrl).addElement(image).toString();
//            
//        }
//
//        //共1页　 首页　上一页　下一页　尾页 　 跳转  页
//
//        //return  ret.toString();
//    }
	
	public static void main(String args[])
	{
//	    IMG image = new IMG();        
//        image.setSrc(buildPath("aaa", "bbb.gif"));
//        image.setAlt("首页");
//        image.setExtend(" border=0 ");
//        System.out.println(new A().setHref("www.sina.com.cn").addElement(image).toString());
		 System.out.println(UUID.randomUUID().toString());
		long start = System.currentTimeMillis();
        String gotopageid = UUID.randomUUID().toString();       
        long end  = System.currentTimeMillis();
        System.out.println(gotopageid + ":"+ (end - start));
//        UUIDGenerator generator = UUIDGenerator.getInstance();
//        gotopageid =  generator.generateRandomBasedUUID().toString();
         start = System.currentTimeMillis();  
//        gotopageid =  generator.generateRandomBasedUUID().toString();
         end  = System.currentTimeMillis();
        System.out.println(gotopageid + ":"+ (end - start));
        
	}

//	private String getNavigationUrl(int offset,String sortKey)
//	{
//		String forwardUrl = pagerContext.getOffsetUrl(offset,sortKey);
//		String customUrl = null;
//		if(pagerContext.getForm() != null)
//		{
//			customUrl = pagerContext.getCustomUrl(pagerContext.getForm(),
//											  pagerContext.getQueryString(offset,sortKey),
//											  pagerContext.getPromotion(),
//											  forwardUrl);
//
//		}
//		else
//			customUrl = forwardUrl;
//		return customUrl;
//	}
	/**
	 * 获取上一页的html代码
	 * @return String
	 */
    private String getWapPrevContent()
    {
        StringBuffer ret = new StringBuffer();
        if (!pagerContext.hasPrevPage())
            return "";
        long offset = pagerContext.getPrevOffset();
        String sortKey = pagerContext.getSortKey();
        String prevurl = pagerContext.getOffsetUrl(offset,sortKey,pagerContext.getDesc());
        String customPrevutl = null;
        customPrevutl = prevurl;
        //String prevurl = pagerContext.getPageUrl(getJumpPage(PagerConst.PRE_PAGE));
        customPrevutl = StringUtil.replace(customPrevutl,"&","&amp;");
        if(this.getStyle().equals("V"))
            return "<br/>" + new A().setHref(customPrevutl).setTagText("上一页");
        else
            return "    " + new A().setHref(customPrevutl).setTagText("上一页");

    }
    
    
	private String getPrevContent() {
		
		Locale locale = I18NUtil.getRequestContextLocal(request);
		String prepage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.prepage",locale) ;
		if (!pagerContext.hasPrevPage())
		{
//			return "　上一页";
			if(!this.useimage)
			{
				if(this.aindex)
				{
		            return new StringBuffer("<a href='#'>")
					.append(prepage_label)//上一页
					.append("</a>").toString();
				}
				else
				{
					return prepage_label//上一页
					;
				}
			}
	        else
	        {
	            IMG image = new IMG();
	            
	            image.setSrc(buildPath(this.getImagedir(), this.pre_image));
	            image.setAlt(prepage_label);//上一页
	            if(this.imageextend != null)
	            {
	                
	                image.setExtend(this.imageextend);
	            }
	           
	            return image.toString();
	        }
		}
		long offset = pagerContext.getPrevOffset();
		String sortKey = pagerContext.getSortKey();
		String prevurl = pagerContext.getOffsetUrl(offset,sortKey,pagerContext.getDesc());
		String customPrevutl = null;
		if(pagerContext.getForm() != null)
		{
			customPrevutl = pagerContext.getCustomUrl(pagerContext.getForm(),
											  pagerContext.getQueryString(offset,sortKey,pagerContext.getDesc()),
											  pagerContext.getPromotion(),
											  prevurl,pagerContext.getId());

		}
		else
			customPrevutl = prevurl;
		//String prevurl = pagerContext.getPageUrl(getJumpPage(PagerConst.PRE_PAGE));
		if(!this.useimage)
        {
		    A a = new A();
            a.setTagText(prepage_label);//上一页
            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
            {
                a.setHref(customPrevutl);
            }
            else
            {
            	a.setHref("#");
                a.setOnClick(this.getJqueryUrl(customPrevutl,pagerContext.getContainerid(),pagerContext.getSelector()));
            }
            
            return  a.toString();
//		    return "　" + new A().setHref(customPrevutl).setTagText("上一页");
        }
        else
        {
            IMG image = new IMG();
            
            image.setSrc(buildPath(this.getImagedir(), pre_image));
            image.setAlt(prepage_label);//上一页
            if(this.imageextend != null)
            {                
                image.setExtend(this.imageextend);
            }
            A a = new A();
            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
            {
                a.setHref(customPrevutl);
            }
            else
            {
                a.setOnClick(this.getJqueryUrl(customPrevutl,pagerContext.getContainerid(),pagerContext.getSelector()));
            }
            a.addElement(image);
            return "　" + a.toString();
//            return "　" + new A().setHref(customPrevutl).addElement(image).toString();
//          return new A().setHref(customFirstUrl).addElement(image).toString();
            
        }
		

	}
	
	
	private String getCenterPageUrl(long offset,String sortKey) {
        StringBuffer ret = new StringBuffer();
        
//        long offset = pagerContext.getPrevOffset();
//        String sortKey = pagerContext.getSortKey();
        String prevurl = pagerContext.getOffsetUrl(offset,sortKey,pagerContext.getDesc());
        String customPrevutl = null;
        if(pagerContext.getForm() != null)
        {
            customPrevutl = pagerContext.getCustomUrl(pagerContext.getForm(),
                                              pagerContext.getQueryString(offset,sortKey,pagerContext.getDesc()),
                                              pagerContext.getPromotion(),
                                              prevurl,pagerContext.getId());

        }
        else
            customPrevutl = prevurl;
        return customPrevutl;
        

    }

    /**
     * 获取wap得下一页地址
     * @return String
     */
    private String getWapNextContent()
    {
        if (pagerContext.hasNextPage()) {
            long offset = pagerContext.getNextOffset();
            String sortKey = pagerContext.getSortKey();
            String nextUrl = pagerContext.getOffsetUrl(offset,pagerContext.getSortKey(),pagerContext.getDesc());
            //String prevurl = pagerContext.getOffsetUrl(offset,sortKey);
            String customNextUrl = null;
            customNextUrl = nextUrl;
            customNextUrl = StringUtil.replace(customNextUrl,"&","&amp;");


            //String nextUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.NEXT_PAGE));
            if(this.getStyle().equals("V"))
                return "<br/>" + new A().setHref(customNextUrl).setTagText("下一页");
            else
                return "    " + new A().setHref(customNextUrl).setTagText("下一页");
        }
        return "";

    }

	/**
	 * 获取下一页的html代码

	 *
	 * To change the template for this generated type comment go to
	 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
	 */

	private String getNextContent() {
		Locale locale = I18NUtil.getRequestContextLocal(request);
		String nextpage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.nextpage",locale) ;
		if (pagerContext.hasNextPage()) {
			long offset = pagerContext.getNextOffset();
			String sortKey = pagerContext.getSortKey();
			String nextUrl = pagerContext.getOffsetUrl(offset,pagerContext.getSortKey(),pagerContext.getDesc());
			//String prevurl = pagerContext.getOffsetUrl(offset,sortKey);
			String customNextUrl = null;
			if(pagerContext.getForm() != null)
			{
				customNextUrl = pagerContext.getCustomUrl(pagerContext.getForm(),
												  pagerContext.getQueryString(offset,sortKey,pagerContext.getDesc()),
												  pagerContext.getPromotion(),
												  nextUrl,pagerContext.getId());

			}
			else
				customNextUrl = nextUrl;


			if(!this.useimage)
	        {
			    A a = new A();
	            a.setTagText(nextpage_label);//"下一页"
	            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
	            {
	                a.setHref(customNextUrl);
	            }
	            else
	            {
	            	a.setHref("#");
	                a.setOnClick(this.getJqueryUrl(customNextUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
	            }
			    return a.toString();
	        }
	        else
	        {
	            IMG image = new IMG();
	            
	            image.setSrc(buildPath(this.getImagedir(), this.next_image));
	            image.setAlt(nextpage_label);//"下一页"
	            if(this.imageextend != null)
	            {                
	                image.setExtend(this.imageextend);
	            }
	            A a = new A();
	            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
	            {
	                a.setHref(customNextUrl);
	            }
	            else
	            {
	                a.setOnClick(this.getJqueryUrl(customNextUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
	            }
	            a.addElement(image);
	            return a.toString();
//	            return new A().setHref(customNextUrl).addElement(image).toString();
//	            return new A().setHref(customFirstUrl).addElement(image).toString();
	            
	        }
			
		}
		if(!this.useimage)
		{
			if(this.aindex)
            {
				return new StringBuffer("<a href='#'>").append(nextpage_label)//"下一页"            
            		.append("</a>").toString();
            }
			else
			{
				return nextpage_label//"下一页"            
	            		;
			}
		}
        else
        {
            IMG image = new IMG();            
            image.setSrc(buildPath(this.getImagedir(), next_image));
            image.setAlt(nextpage_label);//"下一页"
            if(this.imageextend != null)
            {
                
                image.setExtend(this.imageextend);
            }
           
            return image.toString();
        }
//		return "　下一页";
	}

    /**
     * 获取wap尾页按钮
     * @return String
     */
    private String getWapLastContent() {
        String lastUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.LAST_PAGE));
        if (lastUrl.equals(""))
            return "";
        long offset = pagerContext.getLastOffset();
        String sortKey = pagerContext.getSortKey();

        String customLastUrl = null;
        customLastUrl = lastUrl;
        customLastUrl = StringUtil.replaceAll(customLastUrl,"&","&amp;");
        if(this.getStyle().equals("V"))
            return "<br/>" + new A().setHref(customLastUrl).setTagText("尾 页");
        else
            return "    " + new A().setHref(customLastUrl).setTagText("尾 页");

    }
    
    /**
     * 生成中间页面地址
     * @param tagnumber
     * @param currentPage
     * @param totalPage
     * @param output
     * @return
     */
    private void getCenterContent(int currentPage,int totalPage,StringBuffer output)
    {
/* 起始页 */
//        System.out.println("currentPage:"+currentPage);
//        System.out.println("totalPage:"+totalPage);
        if(tagnumber >0){       
            int start = 1;
            int end = 1;
            if(tagnumber<=2) tagnumber = 3;
//            if(tagnumber>3)
            {
            	if(!pagerContext.isMore())
            	{
	                if(currentPage>tagnumber ){
	                    start = (int)currentPage-(int)Math.floor(tagnumber/2);          
	                }else{
	                    if((currentPage%tagnumber)>=(int)Math.floor(tagnumber/2) || currentPage%tagnumber==0 )
	                        start = (int)currentPage-(int)Math.floor(tagnumber/2);
	                    
	                }
	                start = start<=0?1:start;           
	                /* 尾页 */
	                end = (int)this.tagnumber + start  ;
	                if(end > totalPage) {
	                    //页码是从1开始的
	                    end = (int)totalPage + 1;
	                    start = end - (int)this.tagnumber;
	                    if(start<=0) start = 1;
	                }
            	}
            	else
            	{
            		if(pagerContext.getDataResultSize() < pagerContext.getMaxPageItems())//最后一页
            		{
            			if(currentPage > 1)
            			{
	            			start = Math.max(0, currentPage - tagnumber) ;
	            			if(start == 0) start = 1;
	            			else start =start+1;
	            			end = start + tagnumber +1;
            			}
            			else
            			{
            				start = 1;
	            			end = 1;
            			}
            		}
            		else
            		{
	            		if(currentPage>tagnumber ){
		                    start = (int)currentPage-(int)Math.floor(tagnumber/2);          
		                }else{
		                    if((currentPage%tagnumber)>=(int)Math.floor(tagnumber/2) || currentPage%tagnumber==0 )
		                        start = (int)currentPage-(int)Math.floor(tagnumber/2);
		                    
		                }
		                start = start<=0?1:start;           
		                /* 尾页 */
		                end = (int)this.tagnumber + start  ;
//		                if(end > totalPage) {
//		                    //页码是从1开始的
//		                    end = (int)totalPage + 1;
//		                    start = end - (int)this.tagnumber;
//		                    if(start<=0) start = 1;
//		                }
            		}
            	}
            }
//            else{
//                if(tagnumber<=2) tagnumber = 3;
//                start = (int)currentPage-1;
//                if(start<=0) start = 1 ;
//                
//                if(currentPage == 1) end = (int)currentPage + (int)this.tagnumber ;
//                else end = (int)currentPage + (int)this.tagnumber - 1 ;
//                if(end>totalPage){                  
//                    end = (int)totalPage + 1;
//                    start = end - (int)tagnumber;
//                    if(start<=0) start = 1;
//                }
//            }
            if((!pagerContext.isMore() && totalPage>1) || pagerContext.isMore()){
	            for(int i=start;i<end;i++){	                
	                if((i - 1) ==currentPage){
	                	if(aindex)
	                	{
	                		if(this.numberpre == null)
	                			output.append("<span class='current_page'><a href='#'>").append(i).append("</a></span> ");
	                		else
	                			output.append("<span class='current_page'><a href='#'>").append(this.numberpre).append(i).append(this.numberend).append("</a></span> ");
	                	}
	                	else
	                	{
	                		if(this.numberpre == null)
	                			output.append("<span class='current_page'>").append(i).append("</span> ");
	                		else
	                			output.append("<span class='current_page'>").append(this.numberpre).append(i).append(this.numberend).append("</span> ");
	                	}
	                }else{
	//                        Context loop = new VelocityContext();       
	                    A a = new A();
	                    if(this.numberpre == null)
	                    {
	                    	a.setTagText(String.valueOf(i));
	                    }
	                    else
	                    {
	                    	a.setTagText(this.numberpre +i+this.numberend);
	                    }
	                    
	                    String url = getCenterPageUrl((i-1) * this.pagerContext.getMaxPageItems(),this.pagerContext.getSortKey());
	                    if(pagerContext.getForm() != null)
	                    {
	                        a.setHref(url);
	                    }
	                    else
	                    {
	                    	if(pagerContext.getContainerid() != null && !pagerContext.getContainerid().equals(""))
	                    	{
	                    		a.setHref("#");
	                    		a.setOnClick(getJqueryUrl(url,pagerContext.getContainerid(),pagerContext.getSelector()));
	                    	}
	                    	else
	                    	{
	                    		a.setHref(url);
	                    	}
	                        	
	                    }
	                    if(centerextend != null)
	                        a.setExtend(centerextend);
	                    if(this.classname != null && !this.classname.equals(""))
	                        a.setClass(classname);
	//                        if(this.style != null && !this.style.equals(""))
	//                            a.setStyle(style);
	//                        loop.put("count",i+"");
	//                        long off = (i-1) * this.pagerContext.getMaxPageItems();
	//                        loop.put("currentpath",);  
	//                        loop.put("classname",this.classname);   
	//                        loop.put("style",this.style);   
	//                        output.append(CMSTagUtil.loadTemplate("publish/newsTurnPage/content-loop.vm",loop));
	                    output.append(a);
	                }
	                
	            }   
            }
        }
    }
	/**
	 * 获取尾页按钮
	 * @return String
	 */
	private String getLastContent() {
		 String lastpage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.lastpage",I18NUtil.getRequestContextLocal(request)) ;
	    
		String lastUrl = pagerContext.getPageUrl(getJumpPage(PagerConst.LAST_PAGE));
		if (lastUrl.equals(""))
		{
//			return "　尾 页";
			if(!this.useimage)
			{
				if(this.aindex)
				{
	                return new StringBuffer().append(" <a href='#'>")
	                		.append(lastpage_label)//尾页
	                		.append("</a>").toString();
				}
				else
				{
					 return lastpage_label;
				}
			}
            else
            {
                IMG image = new IMG();
                
                image.setSrc(buildPath(this.getImagedir(), last_image));
                image.setAlt(lastpage_label);//尾页
                if(this.imageextend != null)
                {
                    
                    image.setExtend(this.imageextend);
                }
               
                return image.toString();
            }
		}
		long offset = pagerContext.getLastOffset();
		String sortKey = pagerContext.getSortKey();

		String customLastUrl = null;
		if(pagerContext.getForm() != null)
		{
			customLastUrl = pagerContext.getCustomUrl(pagerContext.getForm(),
											  pagerContext.getQueryString(offset,sortKey,pagerContext.getDesc()),
											  pagerContext.getPromotion(),
											  lastUrl,pagerContext.getId());

		}
		else
			customLastUrl = lastUrl;
		if(!this.useimage)
        {
		    
		    A a = new A();
            a.setTagText(lastpage_label);//尾页
            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
            {
                a.setHref(customLastUrl);
            }
            else
            {
            	a.setHref("#");
                a.setOnClick(this.getJqueryUrl(customLastUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
            }
            return a.toString();
//		    return "　" + new A().setHref(customLastUrl).setTagText("尾 页");
        }
        else
        {
            IMG image = new IMG();
            
            image.setSrc(buildPath(this.getImagedir(), this.last_image));
            image.setAlt(lastpage_label);//尾页
            if(this.imageextend != null)
            {                
                image.setExtend(this.imageextend);
            }
            A a = new A();
            if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
            {
                a.setHref(customLastUrl);
            }
            else
            {
                a.setOnClick(this.getJqueryUrl(customLastUrl,pagerContext.getContainerid(),pagerContext.getSelector()));
            }
            a.addElement(image);
            return a.toString();
//            return new A().setHref(customLastUrl).addElement(image).toString();
//            return new A().setHref(customFirstUrl).addElement(image).toString();
            
        }
		
	}
	/**
	 * <a id="bridge"></a>
	 * function goTo(url,maxPageItem,hasParam)
	 * {
	 *
	 * 	   var go = document.all.item("go").value;
	 *     var goPage = parserInt(go);
	 *     if(isNaN(goPage))
	 * 	   {
	 * 			alert("跳转页必须是数字");
	 * 			return;
	 * 	   }
	 * 	   var offset = (goPage - 1) * maxPageItems;
	 * 	   document.bridge.href = url + (hasParam?"&":"?" + "pager.offset=" + offset);
	 * 	   document.bridge.onClick();
	 * }
	 * @return String
	 */
	private String getScript() {
		StringBuffer script =  new StringBuffer();
		HttpServletRequest request = this.getHttpServletRequest();
		if(!this.isCustom())
		{
//			script.append(new Script()
//				.setSrc(StringUtil.getRealPath(request, "/include/pager.js"))
//				.setLanguage("javascript")
//				.toString());
			if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
			{
				script.append(PageConfig.getPagerConfig(request)); //jquery
				script.append(PageConfig.getPagerCss(request)); //jquery
			}
			else
			{
				request.setAttribute(PageConfig.jqueryscript_set_flag,"true");
			}
		    
            
		}
		else
		{
			script.append(new Script()
			.setSrc(StringUtil.getRealPath(request, "/include/pager_custom.js"))
			.setLanguage("javascript")
			.toString());
			script.append(PageConfig.getPagerCss(request)); //jquery
		}
		if(this.pagerContext.getCommitevent() != null)
		{
			if(this.pagerContext.getCommitevent().startsWith("'") || this.pagerContext.getCommitevent().startsWith("\""))
			{
				script .append(new Script().setTagText("bboss.pager.beforeload = "+ this.pagerContext.getCommitevent() + ";"));
			}
			else 			
			{
				script .append(new Script().setTagText("bboss.pager.beforeload = \"" + this.pagerContext.getCommitevent() + "\";"));
			}
		}
		return script.toString();
	}
	
//	public static void main(String[] args )
//	
//	{
//		
//		System.out.println(new Script().setTagText("commitevent = \"turnPageSumbmitSet()\";").toString());
//	}
    /**
     * 获取wap得跳转按钮
     * @return String
     */
    private String getWapGoContent()
    {
        StringBuffer ret = new StringBuffer(); //.append("跳转到");
        //ret.append("<a id=\"bridge\"></a>");
        //ret.append(this.getScript());
        long pages = pagerContext.getPageCount();
        HttpServletRequest request = this.getHttpServletRequest();
        //System.out.println("pagerContext.getPageCount():"+pagerContext.getPageCount());
        if (pages > 1) {
            String goTo = null;

            goTo = request.getContextPath() + "/include/pager.wmls#goTo('"
                    + pagerContext.getUri()
                    + "',"
                    + pages
                    + ","
                    + pagerContext.getMaxPageItems()
                    + ","
                    + String.valueOf(pagerContext.getParams() > 0)
                    + ",";
                    if(pagerContext.getSortKey() == null)
                        goTo += "null";
                    else
                        goTo += "'" + pagerContext.getSortKey() + "'";
                    goTo += "," + pagerContext.getDesc()
                    + ",'"
                    + pagerContext.getId()
                    + "')";
            // 调用validator.wmls中的外部函数validate()进行有效性检验
            String doEvent = "<do type=\"accept\" label=\"跳转到\">"
                        + "<go href=\"" + goTo + "\"/>"
                        + "</do>";


            //a.setStyle("cursor:hand");
            ret.append(doEvent);
            ret
                .append("<input name='gotopage' type='text' size='2'/>")
                .append("页");
        } else {
//            ret
//                .append("跳转到 ")
//                .append(
//                    new Input()
//                        .setName("gotopage")
//                        .setType("text")
//                        .setSize(2)
//                        .setDisabled(true)
//                        )
//                .append(" 页");
        }

        //<input name="txtName2" type="text" size="4" attrib="editor">
        //							<a href="#">页</a>
        if(this.getStyle().equals("V"))
            return "<br/>" + ret.toString();
        else
            return "   " + ret.toString();

    }

    
//    /**
//     * 获取跳转按钮
//     * @return String
//     */
//    private String getGoContent() {
//		StringBuffer ret = new StringBuffer(); //.append("跳转到");
//		//ret.append("<a id=\"bridge\"></a>");
//		//ret.append(this.getScript());
//		long pages = pagerContext.getPageCount();
//		//System.out.println("pagerContext.getPageCount():"+pagerContext.getPageCount());
//		if (pages > 1) {
//			
//			
//			String goTo = null;
//			StringBuffer gogo = new StringBuffer();
//			if(pagerContext.getForm() == null)
//			{
//			    if(pagerContext.getSortKey() != null)
//			    {
//    				goTo = "goTo('"
//    						+ pagerContext.getUri()
//    						+ "',"
//    						+ pages
//    						+ ","
//    						+ pagerContext.getMaxPageItems()
//    						+ ","
//    						+ pagerContext.hasParams()
//    						+ ",'"
//    						+ pagerContext.getSortKey()
//    						+ "',"
//    						+ pagerContext.getDesc()
//    						+ ",'"
//    						+ pagerContext.getId()
//    						+ "')";
//			    }
//			    else
//			    {
//			        goTo = "goTo('"
//                        + pagerContext.getUri()
//                        + "',"
//                        + pages
//                        + ","
//                        + pagerContext.getMaxPageItems()
//                        + ","
//                        + pagerContext.hasParams()
//                        + ",null,"
//                        + pagerContext.getDesc()
//                        + ",'"
//                        + pagerContext.getId()
//                        + "')";
//			    }
//			}
//			else
//			{
//			    if(pagerContext.getSortKey() != null)
//			    {
//			       
//			        
//    				goTo = "goTo('"
//    						+ pagerContext.getUri()
//    						+ "',"
//    						+ pages
//    						+ ","
//    						+ pagerContext.getMaxPageItems()
//    						+ ","
//    						+ pagerContext.hasParams()
//    						+ ",'"
//    						+ pagerContext.getSortKey()
//    						+ "',"
//    						+ pagerContext.getDesc()
//    						+ ",'"
//    						+ pagerContext.getId()
//    						+ "','"
//    						+ pagerContext.getForm()
//    						+ "','"
//    						+ pagerContext.getQueryString()
//    						+ "',"
//    						+ pagerContext.getPromotion()
//    						+ ")";
//			    }
//			    else
//			    {
//			        goTo = "goTo('"
//                        + pagerContext.getUri()
//                        + "',"
//                        + pages
//                        + ","
//                        + pagerContext.getMaxPageItems()
//                        + ","
//                        + pagerContext.hasParams()
//                        + ",null,"
//                        + pagerContext.getDesc()
//                        + ",'"
//                        + pagerContext.getId()
//                        + "','"
//                        + pagerContext.getForm()
//                        + "','"
//                        + pagerContext.getQueryString()
//                        + "',"
//                        + pagerContext.getPromotion()
//                        + ")";
//			    }
//			}
//
//					/**
//					 * goTo(url,
//              pages,
//              maxPageItem,
//              hasParam,
//              sortKey,
//              formName,
//              params,
//              promotion)
//					 */
//			
//			
//			A a = new A();
//			
//			if(!this.useimage)
//			{
//			    a.setOnClick(goTo);
//			
//			    a.setTagText("跳转到 ");
//			}
//			else
//			{
//			    IMG image = new IMG();                
//                image.setSrc(buildPath(this.getImagedir(), this.go_image));
//                image.setAlt("跳转到");
//                if(this.imageextend != null)
//                {
//                    
//                    image.setExtend(this.imageextend);
//                }
//                image.setOnClick(goTo);
//               
//                a.addElement(image);
//			}
//			a.setName("pager.jumpto");
//			a.setID("pager.jumpto");
//			a.setHref("#");
//			///a.setStyle("cursor:hand");
//			ret.append(a.toString());
//			Input go = new Input()
//			
//			.setName("gotopage")
//			.setType("text")
//			.setSize(2);
//			
//			go.setOnKeyDown("keydowngo('pager.jumpto')");
//			ret
//				.append(
//					go.toString())
//				.append(" 页");
//		} else {
//		    if(!this.useimage)
//            {
//		        Input go = new Input()
//	            
//	            .setName("gotopage")
//	            .setType("text")
//	            .setSize(2)
//	            .setDisabled(true);
//	            
//	            ret
//	                .append("跳转到 ")
//	                .append(
//	                    go.toString()                       
//	                        )
//	                .append(" 页");
//            }
//            else
//            {
//                IMG image = new IMG();                
//                image.setSrc(buildPath(this.getImagedir(), this.go_image));
//                image.setAlt("跳转到");
//                if(this.imageextend != null)
//                {
//                    
//                    image.setExtend(this.imageextend);
//                }
//               
//                Input go = new Input()
//                
//                .setName("gotopage")
//                .setType("text")
//                .setSize(2)
//                .setDisabled(true);
//                
//                ret
//                    .append(image.toString())
//                    .append(
//                        go.toString()                       
//                            )
//                    .append(" 页");
//            }
//			
//		}
//
//		//<input name="txtName2" type="text" size="4" attrib="editor">
//		//							<a href="#">页</a>
////		if(pagerContext.indexs == null || pagerContext.indexs.isEmpty())
////		{
////			
////			ret.append("<form action='' name='com.frameworkset.goform' method='post'></form>" );
////		}
//		return ret.toString();
//	}
    
    private static final String gopageerror_msg = "请输入跳转页或者跳转页必须为整数";
    /**
     * 获取跳转按钮
     * @return String
     */
    private String getGoContent() {
        StringBuffer ret = new StringBuffer(); //.append("跳转到");
        //ret.append("<a id=\"bridge\"></a>");
        //ret.append(this.getScript());
        long pages = pagerContext.getPageCount();
        //System.out.println("pagerContext.getPageCount():"+pagerContext.getPageCount());
        String uuid = UUID.randomUUID().toString();
        String gotopageid = uuid+".go";
        Locale locale = I18NUtil.getRequestContextLocal(request);
        String gotopage_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.gotopage",locale) ;
        String page_label = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.page",locale) ;
        String gopageerror_msg = TagUtil.tagmessageSource.getMessage("bboss.tag.pager.gopageerror_msg",locale) ;
        if (pages > 1) {
            
           
            String goTo = null;
            StringBuffer gogo = new StringBuffer();
            if(pagerContext.getForm() == null)
            {
                if(pagerContext.getSortKey() != null)
                {
                    gogo.append("bboss.pager.goTo('").append(gotopageid).append("','").append(gopageerror_msg).append("',");
                    if(pagerContext.getContainerid() != null && !pagerContext.getContainerid().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getContainerid()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",");
                    if(pagerContext.getSelector() != null && !pagerContext.getSelector().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getSelector()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",'");
                    gogo.append(pagerContext.getUri())
                        .append("',")
                        .append(pages)
                        .append(",")
                        .append(pagerContext.getMaxPageItems())
                        .append(",")
                        .append(pagerContext.hasParams())
                        .append(",'")
                        .append(pagerContext.getSortKey())
                        .append("',")
                        .append(pagerContext.getDesc())
                        .append(",'")
                        .append(pagerContext.getId())
                        .append("')");
                }
                else
                {
                    gogo.append("bboss.pager.goTo('").append(gotopageid).append("','").append(gopageerror_msg).append("',");
                    if(pagerContext.getContainerid() != null && !pagerContext.getContainerid().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getContainerid()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",");
                    if(pagerContext.getSelector() != null && !pagerContext.getSelector().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getSelector()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",'");
                    gogo.append(pagerContext.getUri())
                    .append("',")
                    .append(pages)
                    .append(",")
                    .append(pagerContext.getMaxPageItems())
                    .append(",")
                    .append(pagerContext.hasParams())
                    .append(",null,")
                    .append(pagerContext.getDesc())
                    .append(",'")
                    .append(pagerContext.getId())
                    .append("')");
                }
            }
            else
            {
                if(pagerContext.getSortKey() != null)
                {
                    gogo.append("bboss.pager.goTo('").append(gotopageid).append("','").append(gopageerror_msg).append("',");
                    if(pagerContext.getContainerid() != null && !pagerContext.getContainerid().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getContainerid()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",");
                    if(pagerContext.getSelector() != null && !pagerContext.getSelector().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getSelector()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",'");
                    gogo.append(pagerContext.getUri())
                    .append("',")
                    .append(pages)
                    .append( ",")
                    .append( pagerContext.getMaxPageItems())
                    .append(",")
                    .append(pagerContext.hasParams())
                    .append(",'")
                    .append(pagerContext.getSortKey())
                    .append("',")
                    .append(pagerContext.getDesc())
                    .append(",'")
                    .append(pagerContext.getId())
                    .append("','")
                    .append(pagerContext.getForm())
                    .append("','")
                    .append(pagerContext.getQueryString())
                    .append("',")
                    .append(pagerContext.getPromotion())
                    .append(")");
                }
                else
                {
                    gogo.append("bboss.pager.goTo('").append(gotopageid).append("','").append(gopageerror_msg).append("',");
                    if(pagerContext.getContainerid() != null && !pagerContext.getContainerid().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getContainerid()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",");
                    if(pagerContext.getSelector() != null && !pagerContext.getSelector().equals(""))
                    {
                        gogo.append("'").append(pagerContext.getSelector()).append("'");                        
                    }
                    else
                    {
                        gogo.append("null");
                    }
                    gogo.append(",'");
                    gogo.append(pagerContext.getUri())
                    .append("',")
                    .append(pages)
                    .append( ",")
                    .append( pagerContext.getMaxPageItems())
                    .append(",")
                    .append(pagerContext.hasParams())
                    .append(",null,")
                    .append(pagerContext.getDesc())
                    .append(",'")
                    .append(pagerContext.getId())
                    .append("','")
                    .append(pagerContext.getForm())
                    .append("','")
                    .append(pagerContext.getQueryString())
                    .append("',")
                    .append(pagerContext.getPromotion())
                    .append(")");
                }
            }
            goTo = gogo.toString();

                    /**
                     * goTo(url,
              pages,
              maxPageItem,
              hasParam,
              sortKey,
              formName,
              params,
              promotion)
                     */
            
            String pagerjumpto = uuid+".jumpto";
            
            Input go = (Input) new Input()            
            .setName(gotopageid)            
            .setType("text")
            .setSize(2)
            .setClass("page_input");
            go.setID(gotopageid);            
            go.setOnKeyDown("bboss.pager.___keydowngo(event,'"+ pagerjumpto + "')");
            
           
                  
            
            	if(this.usegoimage){
            		ret.append(" ")
                    .append(gotopage_label)//跳转到
                    .append(go.toString())
                    .append(page_label); //页
            		IMG image = new IMG(); 
            		image.setID(pagerjumpto);
            		image.setName(pagerjumpto);
                    image.setSrc(buildPath(this.getImagedir(), this.go_image));
                    image.setAlt(gotopage_label);
                    if(this.imageextend != null)
                    {
                        
                        image.setExtend(this.imageextend);
                    }
                    image.setOnClick(goTo);                   
                    ret.append(image.toString());
            	}else{
            		
            		A a = new A();     
            		a.setID(pagerjumpto);
            		a.setName(pagerjumpto);
            		a.setOnClick(goTo);            
                    a.setTagText(gotopage_label);//跳转到
                    ret.append(a.toString()).append(go.toString()).append(page_label); //页
                    
            	}                
            
            
        } else {
            
            	
            	if(this.usegoimage){
            		Input go = ((Input) new Input()                
                    .setName(gotopageid)
                    .setType("text")
                    .setSize(2)
                    .setClass("page_input"))
                    .setDisabled(true);
            		ret.append(" ")
                    .append(gotopage_label)//跳转到
                    .append(go.toString())
                    .append(page_label); //页
            		IMG image = new IMG();                
                    image.setSrc(buildPath(this.getImagedir(), this.go_image));
                    image.setAlt(gotopage_label);//跳转到
                    if(this.imageextend != null)
                    {
                        
                        image.setExtend(this.imageextend);
                    }
//                    image.setOnClick(goTo);                   
                    ret.append(image.toString());
            	}else{
            		
            		Input go = ((Input) new Input()                
                    .setName(gotopageid)
                    .setType("text")
                    .setSize(2)
                    .setClass("page_input"))
                    .setDisabled(true);
                    
                    ret.append(" ")
                    .append(gotopage_label)//跳转到
                    .append(go.toString()                       
                                )
                        .append(page_label); //页
                    
            	}     
                
            
            
        }

        //<input name="txtName2" type="text" size="4" attrib="editor">
        //                          <a href="#">页</a>
//      if(pagerContext.indexs == null || pagerContext.indexs.isEmpty())
//      {
//          
//          ret.append("<form action='' name='com.frameworkset.goform' method='post'></form>" );
//      }
        return ret.toString();
    }

	private long getJumpPage(int type) {
		switch (type) {
			case PagerConst.FIRST_PAGE :
				return 0;
				//			case PagerConst.PRE_PAGE://保留关键字，目前没有使用
				//				return pagerContext.getPrevOffset();
				//			case PagerConst.INDEX_PAGE://保留关键字，目前没有使用
				//				return 2;
				//			case PagerConst.NEXT_PAGE:
				//				return pagerContext.getNextOffset();
			case PagerConst.LAST_PAGE :
				return pagerContext.getLastPageNumber();
				//			case PagerConst.GO_PAGE://保留关键字，目前没有使用
				//				return 2;
			default :
				return 2;
		}
	}

	/**
	 * 将Index标签实例压栈
	 * Description:
	 * @return
	 * Object
	 */
	public Object push()
	{
		if(pagerContext.indexs == null)
			pagerContext.indexs = new Stack();
		return pagerContext.indexs.push(this);
	}

	public boolean isCustom() {
		return custom;
	}

	public void setCustom(boolean custom) {
		this.custom = custom;
	}


	/**
	 * @return the sizescope
	 */
	public String getSizescope() {
		return sizescope;
	}


	/**
	 * @param sizescope the sizescope to set
	 */
	public void setSizescope(String sizescope) {
		this.sizescope = sizescope;
	}


	public boolean isUsegoimage() {
		return usegoimage;
	}


	public void setUsegoimage(boolean usegoimage) {
		this.usegoimage = usegoimage;
	}


	public boolean isAindex() {
		return aindex;
	}


	public void setAindex(boolean aindex) {
		this.aindex = aindex;
	}


	public String getNumberpre() {
		return numberpre;
	}


	public void setNumberpre(String numberpre) {
		this.numberpre = numberpre;
	}


	public String getNumberend() {
		return numberend;
	}


	public void setNumberend(String numberend) {
		this.numberend = numberend;
	}


//    public String getContainerid()
//    {
//        return containerid;
//    }
//
//
//    public void setContainerid(String containerid)
//    {
//        pagerContext.getContainerid() = containerid;
//    }
//
//
//    public String getSelector()
//    {
//        return selector;
//    }
//
//
//    public void setSelector(String selector)
//    {
//        pagerContext.getSelector() = selector;
//    }
}

/* vim:set ts=4 sw=4: */
