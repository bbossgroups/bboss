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



import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.common.tag.pager.ObjectDataInfoImpl;
import com.frameworkset.util.StringUtil;

/**
 * 从给定的session或者request或者pageContext中获取给定名称的值对象
 * 用cellTag显示属性的值，
 * 用list tag显示类型为list,set的属性中的对象的属性值.
 *
 * 如果本标签作为一个分页/列表标签的嵌套标签使用时，
 * 可设置colName,property属性，不要设置其他几个属性
 * @author biaoping.ying
 * @version 1.0
 *  2004-9-10
 */
public class DetailTag extends PagerDataSet implements FieldHelper{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static final Logger log = Logger.getLogger(DetailTag.class);


	/**详细显示页的显示的字段信息,以逗号分隔*/
	protected String field;
	/**详细显示页的显示的字段显示名称,以逗号分隔*/
	protected String title;

	/**详细显示页的显示的字段显示宽度,以逗号分隔*/
	protected String width;

	protected boolean exportMeta;
	
	protected boolean wapflag = false;
	/**
	 * 指定内置变量beaninfo的变量名称
	 */
	protected String beaninfoName = "beaninfo";
	

	/**
	 * 重载父类的方法，分析详细显示页的显示的字段信息为字段数组
	 * @return 包含信息字段的数组
	 */
	public String[] getFields() {
		if(field == null)
			return null;
		return StringUtil.split(field,",");
	}

	public String[] getTitles()
	{
	    if(title == null)
	        return null;
	    return StringUtil.split(title,",");
	}

	 /* (non-Javadoc)
     * @see com.frameworkset.common.tag.pager.tags.FieldHelper#getWidths()
     */
    public String[] getWidths() {
        if(width == null)
	        return null;
	    return StringUtil.split(width,",");
    }

//    public void init() throws LoadDataException
//    {
//    	this.pagerContext = new PagerContext(this.request, this.response,
//				this.pageContext,this);
//
//		// /*
//		// * id的值为“pager“,备份先前的页面得上下文环境，然后再将当前得上下文设置到request或pageContext中
//		// */
//		// if(REQUEST.equals(scope))
//		// {
//		// this.oldPager = (PagerContext)request.getAttribute(id);
//		// request.setAttribute(id,pagerContext);
//		// }
//
//		// }
//		// else
//		// {
//		// log.debug("DoStartTag pager_info_" + id);
//		// pageContext.setAttribute("pager_info_" + id,this);
//		// }
//		
//		pagerContext.setIsList(this.isList);
//
//		pagerContext.setField(this.field);
////		pagerContext.setForm(this.form);
//		pagerContext.setId(this.getId());
////		pagerContext.setNavindex(this.navindex);
////		pagerContext.setPromotion(this.promotion);
////		pagerContext.setScope(this.scope);
//		pagerContext.setTitle(this.title);
////		pagerContext.setMaxIndexPages(this.maxIndexPages);
////		pagerContext.setMaxItems(this.maxItems);
////		pagerContext.setMaxPageItems(this.maxPageItems);
//		pagerContext.setWapflag(this.wapflag);
//		pagerContext.setWidth(this.width);
////		pagerContext.setIsOffset(this.isOffset);
//		pagerContext.setDbname(this.dbname);
//		pagerContext.setStatement(this.statement);
//		pagerContext.setColName(this.colName);
//		pagerContext.setProperty(this.property);
//		pagerContext.setRequestKey(this.requestKey);
//		pagerContext.setSessionKey(this.sessionKey);
//		pagerContext.setPageContextKey(this.pageContextKey);
////		pagerContext.setData(this.data);
////		pagerContext.setUrl(url);
//
////		pagerContext.setUri();
//		
//		// params = 0;
//		// offset = 0;
//		// itemCount = 0;
//
////		// 设置是否是升序还是降序
////		String desc_key = pagerContext.getKey("desc");
////
////		String t_desc = request.getParameter(desc_key);
////		boolean desc = false;
////		if (t_desc != null && t_desc.equals("false"))
////			desc = false;
////		else if (t_desc != null && t_desc.equals("true"))
////			desc = true;
////
////		pagerContext.setDesc(desc);
////		// 设置排序关键字，首先通过request.getParameter获取
////
////		String sortKey_key = pagerContext.getKey("sortKey");
////
////		String t_sortKey = request.getParameter(sortKey_key);
////		// 如果获取到的sortKey为空时，通过request.getAttribute获取
////		if (t_sortKey == null)
////			t_sortKey = (String) request.getAttribute(sortKey_key);
////		// 如果上面获取到的sortKey不为null时，设置sortKey
////		if (t_sortKey != null)
////			pagerContext.setSortKey(t_sortKey);
//
//		
////		pagerContext.setDataInfo();
//
//		pagerContext.init();
//    }
    
	public int doStartTag() throws JspException{
		try
		{
			push();
			setVariable();
			if(this.pagerContext == null)
			{
				try {
					init();
					
				} catch (LoadDataException e) {
					if(e.getCause() == null)
						log.debug(e.getMessage());
					else
						log.debug(e.getCause().getMessage());
//					return SKIP_BODY;
				}
				catch (Throwable e) {
					if(e.getCause() == null)
						log.debug(e.getMessage());
					else
						log.debug(e.getCause().getMessage());
//					return SKIP_BODY;
				}
			}
		    /**
		     * 设置对象的原模型
		     */
	//	    setMeta();
			
	//		this.flag = true;
			initField(pagerContext.getFields());
	
			
			/**
			 * 得到页面上要显示的值对象中字段
			 *
			 */
			try { 
				DataInfo dataInfo = pagerContext.getDataInfo();
				if (dataInfo == null)
				{
					loadClassDataNull();
				}
				else if(dataInfo instanceof ObjectDataInfoImpl)
				{
					if(dataInfo instanceof ObjectDataInfoImpl)
					{
						Object data = dataInfo.getObjectData();
	//					if(data instanceof Collection)
	//				        loadClassData((Collection)data);
	//				    else if(data instanceof Iterator)
	//				        loadClassData((Iterator)data);
					    if(data instanceof Map)
					    {
					    	this.loadClassData((Map)data,null);
					    }
					    else 
					    {
					    	loadClassData(data,data.getClass());
					    }
					}
	
				}
	//		    else if(dataInfo instanceof DefaultDataInfoImpl)
	//		    {
	//		    	loadClassData(dataInfo);
	//		    }
			    	
			    else if(dataInfo instanceof DataInfo)
			    	loadClassData(dataInfo, pagerContext.ListMode());
	//			/**
	//			 * 以下的代码对取到的数据（及当前页面数据）进行排序
	//			 */
	//			sortKey = pagerContext.getSortKey();
	
			} catch (LoadDataException e) {
				if(e.getCause() == null)
					log.info(e.getMessage());
				else
					log.info(e.getCause().getMessage());
	//			e.printStackTrace();
			}
			catch (Throwable e) {
				
				if(e.getCause() == null)
					log.info(e.getMessage());
				else
					log.info(e.getCause().getMessage());
	//			e.printStackTrace();
			}
	
	//		super.fields = getFields();
	//		String scope = null;
	//		if(getColName() == null)
	//		{
	//		    if(statement != null && !statement.equals(""))
	//		        scope = DB_SCOPE;
	//			else if(sessionKey != null)
	//				scope = SESSION_SCOPE;
	//			else if(pageContextKey != null)
	//				scope = PAGECONTEXT_SCOPE;
	//			else if(requestKey != null)
	//			    scope = REQUEST_SCOPE;//loadObject(requestKey);
	//
	//		}
	//		/**
	//		 * 如果本标签作为一个分页/列表标签的嵌套标签使用时，可设置colName,property属性，不要设置其他几个属性
	//		 */
	//		else
	//		{
	//			scope = COLUMN_SCOPE;
	//		}
	//		try {
	//			loadObject(scope);
	//		} catch (LoadDataException e) {
	//			// TODO Auto-generated catch block
	//
	//			log.info(e.getMessage());
	//			//e.printStackTrace();
	//			//return EVAL_BODY_INCLUDE;
	//		}
			if (size() == 0)
				this.rowid = -1;
			else
			    rowid = 0;
	
	
			return EVAL_BODY_INCLUDE;
		}
		catch(Exception e)
		{
			log.debug(e);
			return EVAL_BODY_INCLUDE;
//			throw new JspException(e);
		}
		//return SKIP_BODY;
	}


	public void setVariable()
	{
//	    if(id != null && !id.trim().equals(""))
//		{
//		    pageContext.setAttribute("beaninfo_" + id,this);
//
//		}
//		else
		{
		    pageContext.setAttribute(this.getBeaninfoName() ,this);

		}
	}

//	/**
//	 * 根据不同的范围获取数据对象
//	 * @param scope
//	 * @throws LoadDataException
//	 */
//	protected void loadObject(String scope)
//			throws LoadDataException
//	{
//		Object dataInfo = null;
//		if(scope.equals(DB_SCOPE))
//		    dataInfo = new DefaultDataInfoImpl();
//		else if(scope.equals(REQUEST_SCOPE))
//		{
//			dataInfo = request.getAttribute(requestKey);
//		}
//		else if(scope.equals(SESSION_SCOPE))
//		{
//			dataInfo = session.getAttribute(sessionKey);
//		}
//		else if(scope.equals(PAGECONTEXT_SCOPE))
//		{
//			dataInfo = pageContext.getAttribute(pageContextKey);
//		}
//		else if(scope.equals(COLUMN_SCOPE))
//		{
//			PagerDataSet dataSet =
//				searchDataSet(this, PagerDataSet.class);
//
//			//System.out.println("dataSet:" + dataSet);
//			//System.out.println("dataSet.getRowid():" + dataSet.getRowid());
//			/**
//			 * 根据调用getProperty()方法返回值判断当前列是否为bean
//			 * 如果为空,表示字段为collection
//			 *
//			 * 否则表示字段是一个javabean,getProperty()返回的值为该对象的一个属性，类型为collection
//			 */
//
//			if (getProperty() == null) {
//
//				dataInfo =
//					dataSet.getValue(
//						dataSet.getRowid(),
//						getColName());
//
//				//System.out.println("dataInfo.size:" + dataInfo.size());
//			} else {
//				dataInfo =
//					dataSet.getValue(
//						dataSet.getRowid(),
//						getColName(),
//						getProperty());
//			}
//
//		}
//		else
//			throw new LoadDataException("load from "+ scope +" error: this scope not support!");
////notified by biaoping.yin on 2005.10.9 8:29
////		if (dataInfo == null)
////			throw new LoadDataException("load "+ scope +" error:dataInfo == null || dataInfo.size() == 0");
//		try {
//			loadClassData(dataInfo);
//		} catch (LoadDataException e) {
//
//			log.info(e.getMessage());
//			e.printStackTrace();
//		}
//	}





//	private void clear()
//	{
//		if(getNeedClear().equals("true"))
//		{
//			if(requestKey != null)
//				request.removeAttribute(requestKey);
//			else if(sessionKey != null)
//				session.removeAttribute(sessionKey);
//			else if(pageContextKey != null)
//				pageContext.removeAttribute(pageContextKey);
//		}
//	}
	public int doEndTag() throws JspException
	{
//		super.doEndTag();
		try
		{
			super.clear();
			this.pagerContext = null;
			
			
		}
		catch(Exception e)
		{
			
		}
		
//		pretoken= null;
//        endtoken= null;
//		
//		if(flag)
//		{
			removeVariable();
			beaninfoName = "beaninfo";
			//恢复父beaninfo实例
			recoverParentBeanInfo();
//		}
//		this.request = null;
//		this.session = null;
//		this.response = null;
		enablecontextmenu = false;
//		this.out = null;
//		flag = false;
		sqlparamskey = "sql.params.key";
		
		return EVAL_PAGE;
	}
	
	
	
	/**
	 * 恢复父DetailTag变量
	 *
	 */
	 
	protected void recoverParentBeanInfo()
	{
	    if(stack.size() > 0)
	    {
	        DetailTag beaninfo = getParentBeanInfo();
	        if(beaninfo != null)
	            this.pageContext.setAttribute(beaninfo.getBeaninfoName(),beaninfo);
//	        this.pageContext.setAttribute("rowid",dataSet.getRowid() + "");
	    }
	}
	
	/**
	 * 获取父beaninfo对象实例
	 * @return DetailTag
	 */
	private DetailTag getParentBeanInfo()
	{
	    int size = stack.size();
	    int count  = size - 1;
	    while(count >= 0)
	    {
	        Object beaninfo = stack.elementAt(count);
	        if(beaninfo instanceof DetailTag)
	        {
	            return (DetailTag)beaninfo;
	        }
	        count --;
	    }
	    return null;
	}
	public void removeVariable()
	{
//	    if(id != null && !id.trim().equals(""))
//		{
//		    pageContext.removeAttribute("beaninfo_" + id);
//
//		}
//		else
		{
		    pageContext.removeAttribute(this.getBeaninfoName(),PageContext.PAGE_SCOPE);
		}
		super.removeVariable();
	}

    /**
     * @return Returns the field.
     */
    public String getField() {
        return field;
    }
    /**
     * @param field The field to set.
     */
    public void setField(String field) {
        this.field = field;
    }


    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the width.
     */
    public String getWidth() {
        return width;
    }
    /**
     * @param width The width to set.
     */
    public void setWidth(String width) {
        this.width = width;
    }

    public int getInt(String field)
    {
        return super.getInt(0,field.trim());
    }
    public String getString(String field)
    {
        return super.getString(0,field.trim());
    }

    public String getString(String field,String defaultValue)
    {
        String retValue = getString(0,field.trim());
        return retValue == null?defaultValue:retValue;
    }

    public int getInt(String field,int index)
    {
        return super.getInt(field.trim(),index);
    }
    public String getString(String field,int index)
    {
        return super.getString(field.trim(),index);
    }

    public String getString(String field,String defaultValue,int index)
    {
        String retValue = getString(field.trim(),defaultValue,index);
        return retValue ;
    }
    
    public int doAfterBody() {
//		if (this.rowid < this.size() - 1) {
//			rowid++;
//			if(id != null && !id.trim().equals(""))
//			{
//				pageContext.setAttribute("rowid_" + id,rowid + "");
//			}
//			else
//			{
//				pageContext.setAttribute("rowid",rowid + "");
//			}
//			return EVAL_BODY_AGAIN;
//		} else 
		{
			return SKIP_BODY;
		}
	}

    /**
     * @param exportMeta The exportMeta to set.
     */
    public void setExportMeta(boolean exportMeta) {
        this.exportMeta = exportMeta;
    }

    /**
	 * 重写父类中方法，判断是否导出数据到文件中
	 * @return boolean
	 */
    protected boolean isExportMeta()
    {
        //如果是嵌套在分页/列表中的详细信息直接使用父类的方法，否则根据属性的配置来
        //判断是否导出数据
        if(getColName() != null)
            return super.isExportMeta();
        else
            return this.exportMeta;
    }

	public boolean isWapflag() {
		return wapflag;
	}

	public void setWapflag(boolean wapflag) {
		this.wapflag = wapflag;
	}

	public String getBeaninfoName() {
		return beaninfoName;
	}

	public void setBeaninfoName(String beaninfoName) {
		this.beaninfoName = beaninfoName;
	}

	
}
