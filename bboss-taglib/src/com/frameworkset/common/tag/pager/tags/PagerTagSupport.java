/*
 *  Pager Tag Library
 *
 *  Copyright (C) 2002  James Klicman <james@jsptags.com>
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.frameworkset.common.tag.pager.tags;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 
 * To change for your class or interface
 * 
 * @author biaoping.yin
 * @version 1.0
 * 2005-2-3
 */
public abstract class PagerTagSupport extends BaseTag {
     

//	protected PagerTag pagerTag = null;
//	protected DetailTag detailTag = null;
	protected FieldHelper fieldHelper;
	protected PagerContext pagerContext;
	
	protected int length(Object _actualValue)
	{
		return ValueObjectUtil.length(_actualValue);
	}
	/**
	 * 判断是否导出页面数据到文件中
	 * @return boolean
	 */
	protected boolean isExportMeta()
	{
	    //如果页面标签是嵌套在分页标签中时，则根据分页标签的判断来决定是否导出页面数据
	    //如果页面标签是嵌套在详细标签中时，则根据详细标签的判断来决定是否导出页面数据
	    //否则不导出页面数据
//	    if(pagerTag != null)
//	        return pagerTag.isExportMeta();
//	    else if(detailTag != null)
//	        return detailTag.isExportMeta();
//	    else
	        return false;
	}

	protected final void restoreAttribute(String name, Object oldValue) {
		if (oldValue != null)
			pageContext.setAttribute(name, oldValue);
		else 
			pageContext.removeAttribute(name,PageContext.PAGE_SCOPE);
	}
	
	protected PagerContext findPageContext()
	{
		return null;
	}

//	private final PagerTag findRequestPagerTag(String pagerId) {
//		Object obj = request.getAttribute(pagerId);
//		if (obj instanceof PagerTag)
//			return (PagerTag) obj;
//		return null;
//	}

	public int doStartTag() throws JspException {
		
//		if (id != null) {
//			pagerTag = findRequestPagerTag(id);
//			
////			if (pagerTag == null)
////				throw new JspTagException("pager tag with id of \"" + id +
////											"\" not found.");
//		} else {
//			pagerTag = (PagerTag) findAncestorWithClass(this, PagerTag.class);
//			if (pagerTag == null) {
//				pagerTag = findRequestPagerTag(PagerTag.DEFAULT_ID);
////				if (pagerTag == null)
////					throw new JspTagException("not nested within a pager tag" +
////								" and no pager tag found at request scope.");
//			}
//		}
//		
//		if(pagerTag == null)
//		    detailTag = (DetailTag) findAncestorWithClass(this, DetailTag.class);
//		if(this.pagerTag == null)
//		    fieldHelper = detailTag;
//		else
//		    fieldHelper = pagerTag;
//		
//		
//		return EVAL_BODY_INCLUDE;
		pagerContext = null;
		/*
		 * 查找并初始化标签的pagerContext对象，以下情况需要从外围环境中，查找pagerContext
		 * 1.标签的数据依赖于外围的标签提供，首先根据标签
		 * 本身得id值在request中查找，如果没有找到，如果id为null时
		 * 则需要 
		 */
		if (id != null) 
		{
//			pagerTag = findRequestPagerTag(id);
			this.pagerContext = this.findRequestPagerContext(id);
			if (pagerContext == null) {
				pagerContext = findRequestPagerContext(PagerTag.DEFAULT_ID);
			}

		} 
		else 
		{
			
			PagerTag pagerTag = (PagerTag) findAncestorWithClass(this, PagerTag.class);
			if(this instanceof DetailTag)
			{
				
			}
			else if(this instanceof PagerDataSet)
			{
				if(pagerTag != null && ((PagerDataSet)this).usedwithpagerTag())
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag == null )
					{
						pagerContext = pagerTag.pagerContext;
					}
				}
				else
				{
					
				}
			}
			else if(this instanceof PagerRowCount)
			{
				if(pagerTag != null)
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag == null)
						pagerContext = pagerTag.pagerContext;
					else
						pagerContext = listTag.pagerContext;
				}
				else
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag != null)
						pagerContext = listTag.pagerContext;
					
				}
			}
			else if(this instanceof IndexTag)
			{
				if(pagerTag != null)
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag == null)
						pagerContext = pagerTag.pagerContext;
					else
						pagerContext = listTag.pagerContext;
				}
				else
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag != null)
						pagerContext = listTag.pagerContext;
				}
			}
			else if(this instanceof CellTag)
			{
				//无需初时化pagerContext
			}
			else if(this instanceof ParamTag || this instanceof ParamsTag || this instanceof BeanParamsTag)
			{
				if(pagerTag != null)
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag == null || listTag.isList)
						pagerContext = pagerTag.pagerContext;
					else
						pagerContext = listTag.pagerContext;
				}
			}
			else if(this instanceof TitleTag)
			{

				if(pagerTag != null)
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag == null || listTag.isList)
						pagerContext = pagerTag.pagerContext;
					else
						pagerContext = listTag.pagerContext;
				}
			}
			else if(this instanceof RowIDTag)
			{
				if(pagerTag != null)
				{
//					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
//					if(listTag == null)
						pagerContext = pagerTag.pagerContext;
//					else
//						pagerContext = listTag.pagerContext;
				}
				else
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag != null)
						pagerContext = listTag.pagerContext;
				}
			}
			else if(this instanceof NotifyTag)
			{
				if(pagerTag != null)
				{
//					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
//					if(listTag == null)
						pagerContext = pagerTag.pagerContext;
//					else
//						pagerContext = listTag.pagerContext;
				}
				else
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag != null)
						pagerContext = listTag.pagerContext;
				}
			}
			if(this instanceof QueryStringTag)
			{
				if(pagerTag != null)
				{
//					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
//					if(listTag == null)
						pagerContext = pagerTag.pagerContext;
//					else
//						pagerContext = listTag.pagerContext;
				}
				else
				{
					PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
					if(listTag != null)
						pagerContext = listTag.pagerContext;
				}
			}
				
				
//			/**
//			 * 
//			 */
//			if (pagerTag == null ) {
//				
//				PagerDataSet listTag = (PagerDataSet) findAncestorWithClass(this, PagerDataSet.class);
//				if(listTag != null)
//				{
//					pagerContext = listTag.pagerContext;
//				}
//				else
//				{
//					DetailTag detailTag = (DetailTag) findAncestorWithClass(this, DetailTag.class);
//					if(detailTag != null)
//						pagerContext = detailTag.pagerContext;
//					else if(this instanceof CellTag)
//					{
//						
//						
//						
//					}
//				}
//				if(pagerContext == null)
//				{
//					pagerContext = findRequestPagerContext(PagerTag.DEFAULT_ID);
//				}
//			}
//			else
//			{
//				pagerContext = pagerTag.pagerContext;
//			}
		}
		
		if(pagerContext == null)
		{
			//进行额外处理
		}
		fieldHelper = pagerContext;
//		if(this.pagerTag == null)
//		    fieldHelper = detailTag;
//		else
//		    fieldHelper = pagerTag;
		return EVAL_BODY_INCLUDE;
	}

	private PagerContext findRequestPagerContext(String pagerContextID) {
		HttpServletRequest request = this.getHttpServletRequest();
//		HttpServletResponse response = this.getHttpServletResponse();
		Object obj = request.getAttribute(pagerContextID);
		if (obj instanceof PagerContext)
			return (PagerContext) obj;
		return null;
	}

	public int doEndTag() throws JspException {

		return super.doEndTag();
//		return EVAL_PAGE;
	}
	
	public static void main(String[] args)
	{
		String ss = null;
		boolean f = ss instanceof String;
		System.out.println(f);
	}
	
	
	public void release() {
		pagerContext = null;
		super.release();
	}
	
	protected FieldHelper getFieldHelper()
	{    
	    return fieldHelper;
	}

	@Override
	public void doFinally() {
		try {
			HttpServletRequest request = this.getHttpServletRequest();
//		HttpServletResponse response = this.getHttpServletResponse();
			if (request == null || !(request instanceof CMSServletRequest) )
			{
				pagerContext = null;
				fieldHelper = null;
			}
		} catch (Exception e) {
			
		}
		super.doFinally();
	}
}

/* vim:set ts=4 sw=4: */
