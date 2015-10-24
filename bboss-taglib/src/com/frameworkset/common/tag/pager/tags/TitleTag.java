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



import java.io.OutputStream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.ecs.html.A;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.frameworkset.util.I18NUtil;

import com.frameworkset.util.StringUtil;

//import com.frameworkset.common.tag.BaseTag;

/**
 * @author biaoping.yin
 * 显示表头标题的tag
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TitleTag  extends PagerTagSupport
{

	private String bgColor = "#8da6c4";
	private String title = null;
	/**标题国际化code*/
	private String titlecode = null;
	
	private boolean sort = false;
	private String desc ;
	private int colid = -1;


	/**标题样式名称*/
	private String className = null;

	/**标题对齐样式名称*/
	private String align = null;

	/**控制内容是否折行*/
	private boolean nowrap = false;

	/**控制标题跨行*/
	private int colspan = 0;
	/**
	 * 指定标题的宽度
	 */
	private String width = null;

	private int pageItems = 0;
	private String type = "th";
	private String extend = null;
	private int rowspan = 0;
	/**
	 * 指定标题对应的属性名称
	 */
	private String colName = null;
	
	public int doEndTag() throws JspException
	{
		
		return super.doEndTag();
	}
	public int doStartTag() throws JspException
	{
		super.doStartTag();
		if(pagerContext == null)
		{
			PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
			if(listTag != null)
			{
				this.pagerContext = listTag.getPagerContext();
				fieldHelper = pagerContext;
			}
		}

		try
		{
			JspWriter out = this.getJspWriter();
			if(pagerContext.isWapflag())
			{
				out.print(generateContent());
			}
			else
			{
				if(this.type != null && this.type.equals("td"))
				{
					out.print(generateTDContent());
				}
				else
				{
					out.print(generateTHContent());
				}
			}
//			if(pagerTag == null)
//			{
//	            if(!this.detailTag.isWapflag())
//	            {
//	                out.print(generateContent());
//	            }
//	            else
//	               out.print(generateWapContent());
//			}
//			else
//			{
//				if(!this.pagerTag.isWapflag())
//	            {
//	                out.print(generateContent());
//	            }
//	            else
//	               out.print(generateWapContent());
//			}
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			throw new JspException("Title tag must be set in header tag in list or pager tag:" +e.getMessage());
		}

		return SKIP_BODY;
	}
    public String generateWapContent() {
        StringBuilder ret = new StringBuilder();
        TD td = new TD();
        if(this.getTitle() == null)//如果没有设定标题
        {
            /**
             * 如果改字段需要排序，构建排序的href
             * 并且反转排序顺序
             *
             */
            if(isSortKey())
            {
                A a = new A();
                String href = null;

                String sortKey = "";
                if(this.getColName() != null)
                    sortKey = getColName();
                else if(getColid() >= 0)
                {
                    sortKey = getSortKey(getColid());
                }
                if(pagerContext != null)
                {
	                if(!pagerContext.ListMode())
	                {
	                    href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
	                    href = StringUtil.replace(href,"&","&amp;");
	                }
	
	                else
	                {
	                    //href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
	                    href = pagerContext.getUrl(sortKey,!desc());
	                    href = StringUtil.replace(href,"&","&amp;");
	                }
	
	
	
	                a.setHref(href);
	                //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
	                a.setTagText( getTitle(getColid()) );
	                td.addElement(a);
                }
            
            }
            else
                td.setTagText(getTitle(getColid()));
        }
        else //如果不需要排序直接输出标题
        {

            String title = getTitle();
            if(isSortKey())
            {
                A a = new A();
                String href = null;

                String sortKey = "";
                if(this.getColName() != null)
                    sortKey = getColName();
                else if(getColid() >= 0)
                {
                    sortKey = getSortKey(getColid());
                }

                if(pagerContext != null)
                {
                	if(!pagerContext.ListMode())
                
	                {
	                    href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
	                    href = StringUtil.replace(href,"&","&amp;");
	                }
	
	                else
	                {
	                    //href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
	                    href = pagerContext.getUrl(sortKey,!desc());
	                    href = StringUtil.replace(href,"&","&amp;");
	                }
	
	
	
	                a.setHref(href);
	                //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
	                a.setTagText( title );
	                td.addElement(a);
                }
            }
            else
                td.setTagText(title);
        }
        if(this.getClassName() != null)
            td.setClass(this.getClassName());
        ret.append(td.toString());
        return ret.toString();
    }
    
    private boolean desc()
    {
    	if(this.getDesc() == null || pagerContext.isDescfromrequest())
    	{
    		return pagerContext.getDesc();
    	}
    	else
    	{
    		return desc.equals("true");
    	}
    }
    
    
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateTHContent() {

		StringBuilder ret = new StringBuilder();
		TH td = new TH();
		String title_lable = this.titlecode == null?this.getTitle():convertTitlei18n();
		//td.setBgColor(getBgColor());
		String fwidth = getWidth(getColid());

		if(width != null && !width.equals(""))
			td.setWidth(width);
		else if(!fwidth.equals(""))
			td.setWidth(fwidth);
		if(this.align != null)
		    td.setAlign(align);
		td.setNoWrap(nowrap);
		if(this.getColspan() != 0)
		    td.setColSpan(getColspan());
		if(this.getRowspan() != 0)
		    td.setRowSpan(getRowspan());
		if(StringUtil.isNotEmpty(this.getExtend()))
		{
			td.setExtend(getExtend());
		}
		if(title_lable == null)//如果没有设定标题
		{
			/**
			 * 如果改字段需要排序，构建排序的href
			 * 并且反转排序顺序
			 *
			 */
			if(isSortKey())
			{
				A a = new A();
				String href = null;

				String sortKey = "";
				if(this.getColName() != null)
				    sortKey = getColName();
				else if(getColid() >= 0)
				{
				    sortKey = getSortKey(getColid());
				}
				if(pagerContext != null)
				{
					if( !pagerContext.ListMode())
					{
						href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
									pagerContext.getQueryString(pagerContext.getOffset(),sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}
	
					else
					{
						//href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
						href = pagerContext.getUrl(sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}


					if(pagerContext.getForm() == null)
                    {
                        if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
                        {
                            a.setHref(href);
                        }
                        else
                        {
                            a.setOnClick(IndexTag.getJqueryUrl(href,pagerContext.getContainerid(),pagerContext.getSelector()));
                        }
                        
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( getTitle(getColid()) );
                       
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
	   						 span.setClass("pg-sort-desc");
	   					 else
	   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
                    else
                    {
                        a.setHref(href);
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( getTitle(getColid()) );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 	span.setClass("pg-sort-desc");
	   					else
	   						span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
//					a.setHref(href);
//					//a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
//					a.setTagText( getTitle(getColid()) );
//					td.addElement(a);
				}
			}
			else
				td.setTagText(getTitle(getColid()));
		}
		else //如果不需要排序直接输出标题
		{

			String title = title_lable;
			if(isSortKey())
			{
				A a = new A();
				String href = null;

				String sortKey = "";
				if(this.getColName() != null)
				    sortKey = getColName();
				else if(getColid() >= 0)
				{
				    sortKey = getSortKey(getColid());
				}
				if(pagerContext != null)
				{
					if(!pagerContext.ListMode())
					{
						href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(pagerContext.getOffset(),sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}
	
					else
					{
						//href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
						href = pagerContext.getUrl(sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}


					if(pagerContext.getForm() == null)
                    {
                        if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
                        {
                            a.setHref(href);
                        }
                        else
                        {
                            a.setOnClick(IndexTag.getJqueryUrl(href,pagerContext.getContainerid(),pagerContext.getSelector()));
                        }
                        
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( title );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 span.setClass("pg-sort-desc");
   					 else
   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
                    else
                    {
                        a.setHref(href);
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( title );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 span.setClass("pg-sort-desc");
   					 else
   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
//					a.setHref(href);
//					//a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
//					a.setTagText( title );
//					td.addElement(a);
				}
			}
			else
				td.setTagText(title);
		}
		if(this.getClassName() != null)
		    td.setClass(this.getClassName());
		ret.append(td.toString());
		return ret.toString();
	}
	private String convertTitlei18n()
	{
//		MessageSource source = WebApplicationContextUtils.getWebApplicationContext();
//		
//		return source.getMessage(this.titlecode, RequestContextUtils.getRequestContextLocal(request));
		return I18NUtil.getI18nMessage(titlecode, this.title,request);
	}
	/**
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateTDContent() {

		StringBuilder ret = new StringBuilder();
		TD td = new TD();
		String title_lable = this.titlecode == null?this.getTitle():convertTitlei18n();
		
		//td.setBgColor(getBgColor());
		String fwidth = getWidth(getColid());

		if(width != null && !width.equals(""))
			td.setWidth(width);
		else if(!fwidth.equals(""))
			td.setWidth(fwidth);
		if(this.align != null)
		    td.setAlign(align);
		td.setNoWrap(nowrap);
		if(this.getColspan() != 0)
		    td.setColSpan(getColspan());
		if(this.getRowspan() != 0)
		    td.setRowSpan(getRowspan());
		if(StringUtil.isNotEmpty(this.getExtend()))
		{
			td.setExtend(getExtend());
		}
		if(title_lable == null)//如果没有设定标题
		{
			/**
			 * 如果改字段需要排序，构建排序的href
			 * 并且反转排序顺序
			 *
			 */
			if(isSortKey())
			{
				A a = new A();
				String href = null;

				String sortKey = "";
				if(this.getColName() != null)
				    sortKey = getColName();
				else if(getColid() >= 0)
				{
				    sortKey = getSortKey(getColid());
				}
				if(pagerContext != null)
				{
					if( !pagerContext.ListMode())
					{
						href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
									pagerContext.getQueryString(pagerContext.getOffset(),sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}
	
					else
					{
						//href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
						href = pagerContext.getUrl(sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}
					

					if(pagerContext.getForm() == null)
					{
					    if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
			            {
			                a.setHref(href);
			            }
			            else
			            {
			                a.setOnClick(IndexTag.getJqueryUrl(href,pagerContext.getContainerid(),pagerContext.getSelector()));
			            }
			            
    					//a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
    					a.setTagText( getTitle(getColid()) );
    					td.addElement(a);
    					 Span span = new Span();
    					 if(desc())
    						 span.setClass("pg-sort-desc");
    					 else
    						 span.setClass("pg-sort-asc");
                         span.setTagText("&nbsp;");
                         td.addElement(span);
					}
					else
					{
					    a.setHref(href);
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( getTitle(getColid()) );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 span.setClass("pg-sort-desc");
   					 else
   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
					}
				}
			}
			else
				td.setTagText(getTitle(getColid()));
		}
		else //如果不需要排序直接输出标题
		{

			String title = title_lable;
			if(isSortKey())
			{
				A a = new A();
				String href = null;

				String sortKey = "";
				if(this.getColName() != null)
				    sortKey = getColName();
				else if(getColid() >= 0)
				{
				    sortKey = getSortKey(getColid());
				}
				if(pagerContext != null)
				{
					if(!pagerContext.ListMode())
					{
						href = pagerContext.getOffsetUrl(pagerContext.getOffset(),sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(pagerContext.getOffset(),sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}
	
					else
					{
						//href = pagerContext.hasParams()?"&":"?" + "sortKey=" + getSortKey(getColId());
						href = pagerContext.getUrl(sortKey,!desc());
						if(pagerContext.getForm() != null)
							href = pagerContext.getCustomUrl(pagerContext.getForm(),
														 pagerContext.getQueryString(sortKey,!desc()),
														 pagerContext.getPromotion(),
														 href,pagerContext.getId());
					}


					if(pagerContext.getForm() == null)
                    {
                        if(pagerContext.getContainerid() == null || pagerContext.getContainerid().equals(""))
                        {
                            a.setHref(href);
                        }
                        else
                        {
                            a.setOnClick(IndexTag.getJqueryUrl(href,pagerContext.getContainerid(),pagerContext.getSelector()));
                        }
                        
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( title );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 span.setClass("pg-sort-desc");
   					 else
   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
                    else
                    {
                        a.setHref(href);
                        //a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
                        a.setTagText( title );
                        td.addElement(a);
                        Span span = new Span();
                        if(desc())
   						 span.setClass("pg-sort-desc");
   					 else
   						 span.setClass("pg-sort-asc");
                        span.setTagText("&nbsp;");
                        td.addElement(span);
                    }
//					a.setHref(href);
//					//a.setTagText( "<strong>" + getTitle(getColId()) + "</strong>");
//					a.setTagText( title );
//					td.addElement(a);
				}
			}
			else
				td.setTagText(title);
		}
		if(this.getClassName() != null)
		    td.setClass(this.getClassName());
		ret.append(td.toString());
		return ret.toString();
	}

	private String getTitle(int i)
	{
		String[] temp = fieldHelper.getTitles();
		return temp == null?"":temp[i] ;
	}
	private String getSortKey(int i)
	{
		String[] temp = fieldHelper.getFields();
		return temp == null?"":temp[i] ;
	}

	private String getWidth(int i)
	{

		String[] temp = fieldHelper.getWidths();
		if(temp == null)
			return "";
		if(i >= temp.length || i < 0)
			return "";
		return temp[i] ;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return int
	 */
	public long getOffset()
	{
		return pagerContext.getOffset();
	}

	/**
	 * @return int
	 */
	public int getPageItems() {
		return pageItems;
	}

	/**
	 * 判断是否是排序字段
	 * @return boolean
	 */
	public boolean isSortKey() {
		return sort;
	}



	/**
	 * @param i
	 */
	public void setPageItems(int i) {
		pageItems = i;
	}

	/**
	 * @param string
	 */
	public void setSort(boolean string) {
		sort = string;
	}


	/**
	 * @return int
	 */
	public int getColid() {
		return colid;
	}

	/**
	 * @param colid
	 */
	public void setColid(int colid) {
		this.colid = colid;
	}

	/**
	 * @return String
	 */
	public String getBgColor() {
		return bgColor == null?"#8da6c4":bgColor;
	}

	/**
	 * @param string
	 */
	public void setBgColor(String string) {
		bgColor = string;
	}

	/**
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setWidth(String string) {
		width = string;
	}

    /**
     * @return Returns the className.
     */
    public String getClassName() {
        return className;
    }
    /**
     * @param className The className to set.
     */
    public void setClassName(String className) {
        this.className = className;
    }
    /**
     * @return Returns the align.
     */
    public String getAlign() {
        return align;
    }
    /**
     * @param align The align to set.
     */
    public void setAlign(String align) {
        this.align = align;
    }
    /**
     * @return Returns the nowrap.
     */
    public boolean isNowrap() {
        return nowrap;
    }
    /**
     * @param nowrap The nowrap to set.
     */
    public void setNowrap(boolean nowrap) {
        this.nowrap = nowrap;
    }
    /**
     * @return Returns the colspan.
     */
    public int getColspan() {
        return colspan;
    }
    /**
     * @param colspan The colspan to set.
     */
    public void setColspan(int colspan) {
        this.colspan = colspan;
    }
    /**
     * @return Returns the colName.
     */
    public String getColName() {
        return colName;
    }
    /**
     * @param colName The colName to set.
     */
    public void setColName(String colName) {
        this.colName = colName;
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitlecode() {
		return titlecode;
	}
	public void setTitlecode(String titlecode) {
		this.titlecode = titlecode;
	}
	public String getExtend() {
		return extend;
	}
	public void setExtend(String extend) {
		this.extend = extend;
	}
	public int getRowspan() {
		return rowspan;
	}
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}
	@Override
	public void doFinally() {
		bgColor = "#8da6c4";
		title = null;
		this.titlecode = null;
		sort = false;
		colid = -1;
		className = null;
		align = null;
		nowrap = false;       
		colspan = 0;
		width = null;
		pageItems = 0;
		colName = null;
		extend = null;
		rowspan = 0;
		type = "th";
		this.pagerContext = null;
		this.fieldHelper = null;
		this.desc = null;
		super.doFinally();
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
