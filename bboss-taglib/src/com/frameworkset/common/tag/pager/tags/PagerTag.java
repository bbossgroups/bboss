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
 *  biaoping.yin (yin-bp@163.com)
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.common.tag.pager.tags;

import java.io.OutputStream;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.support.RequestContext;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.BaseTag;
import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.common.tag.pager.model.MetaDatas;
import com.frameworkset.common.tag.pager.parser.PagerTagExport;
import com.frameworkset.common.tag.pager.parser.ParseException;
import com.frameworkset.common.tag.pager.parser.TagExportParser;

/**
 * @author biaoping.yin
 * @version 1.0 2005-2-3
 */
public class PagerTag extends BaseTag implements FieldHelper, PagerInfo {
	private final static Logger log = Logger.getLogger(PagerTag.class);
	protected boolean moreQuery = false;
//	protected String pretoken = "#\\[";
//    protected String endtoken = "\\]";
    /**
     * jquery内容容器
     */
    private String containerid ;
    
    /**
     * jquery内容选择器
     */
    private String selector;
	/**
	 * 标识是否是wap应用
	 */
	private boolean wapflag = false;

	// /**
	// * 保存页面上所有的IndexTag的标签 Movied to PagerContext
	// */
	// Stack indexs;
	// static final String PARAMETER = "parameter";
	// static final String ATTRIBUTE = "attribute";
	private DataInfo dataInfo = null;

	static final String DEFAULT_ID = "pager";

	/** 处理上下翻页触发form提交事件，本属性指定需要提交的form表单名称 */
	private String form = null;

	/** 当需要保存数据时是否提示保存，与form属性配合使用 */
	private boolean promotion = false;
	
	/**表单提交之前需要执行的事件*/
	private String commitevent = null;

	private String sortKey = null;

	/**
	 * 如果需要导出数据到xml，pdf,word,excel,csv时 设置本属性为true，否则为false
	 */
	private boolean exportMeta = false;

	/**
	 * 存放页面导出到文件时所有参数，当exportMeta为true时设置本变量
	 */
	protected MetaDatas metaDatas = null;

	/**
	 * 输出数据到文件时需要保存页面上的数据集合堆栈
	 */
	protected Stack dataSets;

	/**
	 * 显示的数据字段名称
	 */
	public static final int DEFAULT_MAX_ITEMS = Integer.MAX_VALUE,
			DEFAULT_MAX_PAGE_ITEMS = 10, DEFAULT_MAX_INDEX_PAGES = 10;

	private StringBuilder queryString = null;

	protected PagerContext pagerContext;

	static final String OFFSET_PARAM = ".offset";

	static final String
	// scope values
			PAGE = "page", REQUEST = "request",
			// index values
			CENTER = "center", FORWARD = "forward", HALF_FULL = "half-full";

	/*
	 * Tag Properties 定义跳转的url
	 */
	private String url = null;

	private String index = null;

	// /**
	// * items:总的记录条数 to pagercontext
	// */
	// private long items = 0;
	private int maxItems = DEFAULT_MAX_ITEMS;

	private int maxPageItems = DEFAULT_MAX_PAGE_ITEMS;

	private int maxIndexPages = DEFAULT_MAX_INDEX_PAGES;

	// private boolean isOffset = false;
	private boolean isOffset = true;

	private String export = null;

	private String scope = null;

	// /*
	// * Tag Variables
	// */
	// private StringBuilder uri = null;
	// private int params = 0;
	// /**
	// * offset：当前页面第一条记录id值,
	// * 例如offset=80表示当前页面第一条记录id值为80
	// */
	// private long offset = 0; to pagercontext
	// private long itemCount = 0; to pagercontext
	private String data = null;

	/**
	 * 数据库查询语句属性
	 */
	private String statement = "";

	/**
	 * 数据库连接池名称
	 */
	private String dbname = "";

	// /**
	// * pageNumber：当前是在第几页
	// */
	//
	// private long pageNumber = 0;

	// public int getParams()
	// {
	// return this.params;
	// }

	// /**
	// * pageNumberInteger：封装当前页页码的对象
	// */
	// private Long pageNumberInteger = null;

	/**
	 * idOffsetParam：定义从request中获取offset值得属性名称，例如：pager.offset
	 */
	private String idOffsetParam = DEFAULT_ID + OFFSET_PARAM;

	/**
	 * 分析需要导出参数的分析类
	 */
	private PagerTagExport pagerTagExport = null;

	private PagerContext oldPager = null;

	private Object oldOffset = null;

	private Object oldPageNumber = null;
	

	private boolean desc = true;
	
	private String pager_infoName = "pager_info";

	// /**
	// * Movied to PagerContext
	// * @return
	// */
	// public boolean getDesc()
	// {
	// return desc;
	// }

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	public PagerTag() {
		id = DEFAULT_ID;
	}

	/**
	 * 设置排序关键字
	 * 
	 * @param string
	 */
	public void setSortKey(String string) {
		// String t_key = request.getParameter("sortKey");
		// if(t_key != null && !t_key.trim().equals(""))
		// sortKey = t_key;
		// else
		sortKey = string;
	}

	// /**
	// * 获取排序关键字 Movied to PagerContext
	// * Description:
	// * @return
	// * String
	// */
	// public String getSortKey()
	// {
	// // String t_key = request.getParameter("sortKey");
	// // if(t_key == null )
	// // t_key = (String)request.getAttribute("sortKey");
	// // if(t_key != null && sortKey == null)
	// // sortKey = t_key;
	// return sortKey;
	// }

	public final void setId(String sid) {
		super.setId(sid);
		// OFFSET_PARAM = ".offset";
		idOffsetParam = sid + OFFSET_PARAM;
	}

	public final void setUrl(String value) {
		url = value;
	}

	public final String getUrl() {
		return url;
	}

	public final void setIndex(String val) throws JspException {
		if (!(val == null || CENTER.equals(val) || FORWARD.equals(val) || HALF_FULL
				.equals(val))) {
			throw new JspTagException(
					"value for attribute \"index\" "
							+ "must be either \"center\", \"forward\" or \"half-full\".");
		}
		index = val;
	}

	public final String getIndex() {
		return index;
	}

	// public final void setItems(long value) {
	//
	// items = value;
	// }
	//
	// public final long getItems() {
	// return items;
	// }

	public final void setMaxItems(int value) {
		maxItems = value;
	}

	public final int getMaxItems() {
		return maxItems;
	}

	public final void setMaxPageItems(int value) {
		maxPageItems = value;
	}

	// /**
	// * Movied to PagerContext
	// * @return
	// */
	// public final int getMaxPageItems() {
	// return maxPageItems;
	// }

	public final void setMaxIndexPages(int value) {
		maxIndexPages = value;
	}

//	public final int getMaxIndexPages() {
//		return maxIndexPages;
//	}

	public final void setIsOffset(boolean val) {
		isOffset = val;
	}

	public final boolean getIsOffset() {
		return isOffset;
	}

	public final void setExport(String value) throws JspException {
		if (export != value) {
			try {

				pagerTagExport = TagExportParser.parsePagerTagExport(value);
			} catch (ParseException ex) {
				throw new JspTagException(ex.getMessage());
			}
		}
		export = value;
	}

	public final String getExport() {
		return export;
	}

	public final void setScope(String val) throws JspException {
		if (!(val == null || PAGE.equals(val) || REQUEST.equals(val))) {
			throw new JspTagException("value for attribute \"scope\" "
					+ "must be either \"page\" or \"request\".");
		}
		scope = val;
	}

	
	protected String sqlparamskey = "sql.params.key";
	/**
	 * 标签开始时执行以下方法
	 */
	public int doStartTag() throws JspException {
		// log.debug("id:" + id);
		// if(id == null || id.trim().equals(""))
		// {
		// log.debug("DoStartTag id == null || id.trim().equals(\"\")");
		pageContext.setAttribute(pager_infoName, this);
		HttpServletRequest request = this.getHttpServletRequest();
		HttpServletResponse response = this.getHttpServletResponse();
		// 初始化页面上下文信息
		this.pagerContext = new PagerContext(request, response,
				this.pageContext,this);

		// /*
		// * id的值为“pager“,备份先前的页面得上下文环境，然后再将当前得上下文设置到request或pageContext中
		// */
		// if(REQUEST.equals(scope))
		// {
		// this.oldPager = (PagerContext)request.getAttribute(id);
		// request.setAttribute(id,pagerContext);
		// }

		// }
		// else
		// {
		// log.debug("DoStartTag pager_info_" + id);
		// pageContext.setAttribute("pager_info_" + id,this);
		// }
		pagerContext.setIsList(this.isList);
		pagerContext.setMoreQuery(moreQuery);
		pagerContext.setField(this.field);
		pagerContext.setForm(this.form);
		pagerContext.setId(this.getId());
		pagerContext.setNavindex(this.index);
		pagerContext.setPromotion(this.promotion);
		pagerContext.setScope(this.scope);
		pagerContext.setTitle(this.title);
		pagerContext.setMaxIndexPages(this.maxIndexPages);
		pagerContext.setMaxItems(this.maxItems);
		
//		Object object = request.getAttribute(data);
		String baseUri = request.getRequestURI();
		boolean isControllerPager = PagerContext.isPagerMehtod(request); 
		String cookieid = null;
		if(isControllerPager)
		{
//			baseUri = PagerContext.getPathwithinHandlerMapping(request);
			baseUri = PagerContext.getHandlerMappingRequestURI(request);
//			String mappingpath = PagerContext.getHandlerMappingPath(request); 
//			cookieid = this.pagerContext.getId() == null ?PagerDataSet.COOKIE_PREFIX + mappingpath :PagerDataSet.COOKIE_PREFIX + mappingpath + "|" +this.pagerContext.getId();
			cookieid = PagerContext.getControllerCookieID(request);
			pagerContext.setUrl(baseUri);
//			ListInfo mvcinfo = (ListInfo)object;
			int controllerPagerSize = PagerContext.getControllerPagerSize(request);
//			pagerContext.setMaxPageItems(mvcinfo.getMaxPageItems());
//			pagerContext.setCustomMaxPageItems(mvcinfo.getMaxPageItems());
			pagerContext.setMaxPageItems(controllerPagerSize);
			pagerContext.setCustomMaxPageItems(PagerContext.getCustomPagerSize(request));
			
			
		}
		else
		{
			pagerContext.setUrl(url);
			cookieid = this.pagerContext.getId() == null ?RequestContext.COOKIE_PREFIX + baseUri :RequestContext.COOKIE_PREFIX + baseUri + "|" +this.pagerContext.getId();
		
			int defaultSize = PagerDataSet.consumeCookie(cookieid,maxPageItems,request,pagerContext);
			pagerContext.setCustomMaxPageItems(maxPageItems);
			pagerContext.setMaxPageItems(defaultSize);
		}
		pagerContext.setCookieid(cookieid);
		pagerContext.setWapflag(this.wapflag);
		pagerContext.setWidth(this.width);
		pagerContext.setIsOffset(this.isOffset);
		pagerContext.setData(this.data);
		pagerContext.setDbname(this.dbname);
		pagerContext.setStatement(this.statement);
		SQLExecutor sqlExecutor = (SQLExecutor)request.getAttribute(sqlparamskey);
		pagerContext.setSQLExecutor(sqlExecutor);
//		Object object = request.getAttribute(data);
//		if(object instanceof ListInfo)
//		{
//			pagerContext.setUrl(PagerContext.getHandlerMappingPath(request));
//		}
//		else
//			pagerContext.setUrl(url);
		pagerContext.setCommitevent(this.commitevent);
		

		pagerContext.setUri();
		pagerContext.setContainerid(this.getContainerid());
		pagerContext.setSelector(this.getSelector());
		// params = 0;
		// offset = 0;
		// itemCount = 0;

		// 设置是否是升序还是降序
		String desc_key = pagerContext.getKey("desc");

		String t_desc = request.getParameter(desc_key);
		if(t_desc != null)
		{
			if (t_desc.equals("false"))
				desc = false;
			else if (t_desc.equals("true"))
				desc = true;
			pagerContext.setDescfromrequest(true);
		}

		pagerContext.setDesc(desc);
		// 设置排序关键字，首先通过request.getParameter获取

		String sortKey_key = pagerContext.getKey("sortKey");

		String t_sortKey = request.getParameter(sortKey_key);
		// 如果获取到的sortKey为空时，通过request.getAttribute获取
		if (t_sortKey == null)
			t_sortKey = (String) request.getAttribute(sortKey_key);
		// 如果上面获取到的sortKey不为null时，设置sortKey
		if (t_sortKey != null)
			pagerContext.setSortKey(t_sortKey);

		
		try {
			pagerContext.init();
		}  catch (LoadDataException e) {
			if(e.getCause() == null)
				log.info(e.getMessage());
			else
				log.info(e.getCause().getMessage());
//			return SKIP_BODY;
		}
		catch (Throwable e) {
			if(e.getCause() == null)
				log.info(e.getMessage());
			else
				log.info(e.getCause().getMessage());
			throw new JspException(e);
//			return SKIP_BODY;
		}

		// //addParam("sortKey",getSortKey());
		// //:log
		// if(!pagerContext.ListMode())//:log如果是分页模式，初始化上下文
		// {
		// pagerContext.initContext();
		// }
		//
		// else//:log如果是列表模式，直接设置dataInfo信息即可
		// pagerContext.setDataInfo(pagerContext.getData());

		// /**
		// * 如果需要导出数据到文件，调用本方法进行初始化
		// */
		// this.setMeta();

		return EVAL_BODY_INCLUDE;
	}

	public void setMeta() {
		if (this.isExportMeta())
			this.metaDatas = new MetaDatas();
	}

	// /**
	// * Movied to PagerContext
	// * @return
	// */
	// public String getUri()
	// {
	// return uri.toString();
	// }

	// /**
	// * Moved to PagerContext
	// */
	// private static void restoreAttribute(ServletRequest request, String name,
	// Object oldValue)
	// {
	// if (oldValue != null)
	// request.setAttribute(name, oldValue);
	// else
	// request.removeAttribute(name);
	// }
	//
	// /**
	// * Moved to PagerContext
	// * @param pageContext
	// * @param name
	// * @param oldValue
	// */
	// private static void restoreAttribute(PageContext pageContext, String
	// name,
	// Object oldValue)
	// {
	// if (oldValue != null)
	// pageContext.setAttribute(name, oldValue);
	// else
	// pageContext.removeAttribute(name);
	// }

	/**
	 * 获取数据获取类在request中的存放名称
	 * 
	 * @return String 数据获取类在request中的存放名称
	 */
	public String getData() {

		return data == null ? "dataInfo" : data;
	}
	@Override
	public void doFinally() {
		try {
			/**
			 * 恢复上下文环境旧得上下文环境，清除数据获取接口
			 */
			this.pagerContext.release();
			this.commitevent = null;
			this.form = null;
			this.promotion = false;
//		pretoken= null;
//        endtoken= null;
			sqlparamskey = "sql.params.key";
      
			//		
			// if (REQUEST.equals(scope)) {
			//			
			// PagerContext.restoreAttribute(request, id, oldPager);
			//			
			// request.removeAttribute(getData());
			// oldPager = null;
			//
			// // if (pagerTagExport != null) {
			// // String name;
			// // if ((name = pagerTagExport.getPageOffset()) != null) {
			// // PagerContext.restoreAttribute(request, name, oldOffset);
			// // oldOffset = null;
			// // }
			// //
			// // if ((name = pagerTagExport.getPageNumber()) != null) {
			// // PagerContext.restoreAttribute(request, name, oldPageNumber);
			// // oldPageNumber = null;
			// // }
			// // }
			//			
			// } else {
			// if (pagerTagExport != null) {
			// String name;
			// if ((name = pagerTagExport.getPageOffset()) != null) {
			// PagerContext.restoreAttribute(pageContext, name, oldOffset);
			// oldOffset = null;
			// }
			//
			// if ((name = pagerTagExport.getPageNumber()) != null) {
			// PagerContext.restoreAttribute(pageContext, name, oldPageNumber);
			// oldPageNumber = null;
			// }
			// }
			// }

			//
			// // limit size of re-usable StringBuilder
			// if (uri.capacity() > 1024)
			// uri = null;
			//
			// // indexs = null;
			// pageNumberInteger = null;

			/**
			 * return EVAL_PAGE:继续分析结束标签后的页面代码
			 */
			// try
			// {
			// pageContext.getOut().print("<table width=\"100%\">");
			// }
			// catch(Exception e)
			// {
			// throw new JspException(e.getMessage());
			// }
			// release();
     //begin clear some field by biaoping.yin on 2015.3.8
			scope = null;
			this.containerid = null;
			this.selector = null;
			 this.moreQuery = false;
			 this.statement = "";
			 this.dbname = "";
			 this.isList = false;
			 //end clear some field by biaoping.yin on 2015.3.8
			 
			 url = null;
				index = null;

				// items = 0;
				maxItems = DEFAULT_MAX_ITEMS;
				maxPageItems = DEFAULT_MAX_PAGE_ITEMS;
				maxIndexPages = DEFAULT_MAX_INDEX_PAGES;
				isOffset = true;
				export = null;
			
				// uri = null;
				queryString = null;
				// params = 0;
				// offset = 0;
				// itemCount = 0;
				// pageNumber = 0;
				// pageNumberInteger = null;

				idOffsetParam = DEFAULT_ID + OFFSET_PARAM;
				pagerTagExport = null;
				oldPager = null;
				oldOffset = null;
				oldPageNumber = null;
		} catch (Exception e) {
			
		}
			
		
			
		super.doFinally();
	}
	public int doEndTag() throws JspException {
		
		return EVAL_PAGE;
	}

	public void release() {
		url = null;
		index = null;

		// items = 0;
		maxItems = DEFAULT_MAX_ITEMS;
		maxPageItems = DEFAULT_MAX_PAGE_ITEMS;
		maxIndexPages = DEFAULT_MAX_INDEX_PAGES;
		isOffset = true;
		export = null;
		scope = null;
		//
		// uri = null;
		queryString = null;
		// params = 0;
		// offset = 0;
		// itemCount = 0;
		// pageNumber = 0;
		// pageNumberInteger = null;

		idOffsetParam = DEFAULT_ID + OFFSET_PARAM;
		pagerTagExport = null;
		oldPager = null;
		oldOffset = null;
		oldPageNumber = null;
		
		this.containerid = null;
		this.selector = null;
		 this.moreQuery = false;
		//begin clear some field by biaoping.yin on 2015.3.8
		 this.statement = "";
		 this.dbname = "";
		 //end clear some field by biaoping.yin on 2015.3.8
		super.release();
	}

	public String getIdOffsetParam() {
		return this.idOffsetParam;
	}

	/**
	 * @param string
	 */
	public void setWidth(String string) {
		width = string;
	}

	/**
	 * @return String[]
	 */
	public String[] getWidths() {
		return this.pagerContext.getWidths();
	}

	public static void main(String[] args) {

	}

	/**
	 * @return String[]
	 */
	public String[] getTitles() {
		return this.pagerContext.getTitles();
	}

	/**
	 * @param string
	 */
	public void setTitle(String string) {
		title = string;
	}

	String field = "";

	String width = null;

	String title = null;

	/**
	 * @return String[]
	 */
	public String[] getFields() {
		return this.pagerContext.getFields();
	}

	/**
	 * @param string
	 */
	public void setField(String string) {
		field = string;
	}

	boolean isList = false;

	// /** Movied to PagerContext
	// * @return boolean 判断是否是列表模式
	// */
	// boolean ListMode() {
	// // if(isList == null || isList.trim().length() == 0)
	// // return false;
	// return isList;
	// }

	/**
	 * @param string
	 */
	public void setIsList(boolean string) {
		isList = string;
	}

	// /**
	// * 获取数据获取类在request中的存放名称
	// * @return String 数据获取类在request中的存放名称
	// */
	// public String getData() {
	//
	// return data == null?"dataInfo":data;
	// }

	/**
	 * @param string
	 */
	public void setData(String string) {
		data = string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {

	}

	// /**
	// * 获取页面查询参数 Movied to PagerContext
	// * @return String
	// */
	// public String getQueryString()
	// {
	// return queryString == null?"":queryString.toString();
	// }
	// /**
	// * Description:获取页面查询的参数 Movied to PagerContext
	// * @return
	// * StringBuilder
	// */
	// public String getQueryString(long offset,String sortKey,boolean desc) {
	// String offsetString = "";
	// if(queryString != null)
	// {
	// int length = queryString.length();
	// queryString.append(params == 0 ? "" : "&")
	// .append(idOffsetParam)
	// .append('=')
	// .append(offset);
	//
	// if(sortKey != null)
	// queryString.append("&").append(getKey("sortKey")).append("=").append(sortKey);
	// queryString.append("&").append(getKey("desc")).append("=").append(desc);
	// offsetString = queryString.toString();
	// queryString.setLength(length);
	// }
	// return offsetString;
	// }

	// /**
	// * Description:获取页面查询的参数 Movied to PagerContext
	// * @return
	// * StringBuilder
	// */
	// public String getQueryString(String sortKey,boolean desc) {
	//
	// String offsetString = "";
	// if(queryString != null && sortKey != null)
	// {
	// int length = queryString.length();
	// queryString.append(params == 0 ? "" : "&")
	// .append(getKey("sortKey")).append("=").append(sortKey);
	// queryString.append("&").append(getKey("desc")).append("=").append(desc);
	// offsetString = queryString.toString();
	// queryString.setLength(length);
	// }
	// return offsetString;
	// }

	// /**
	// * Movied to PagerContext
	// * Description:
	// * @return
	// * String
	// */
	// public String getForm() {
	// return form;
	// }

	/**
	 * Description:
	 * 
	 * @param string
	 *            void
	 */
	public void setForm(String string) {
		form = string;
	}

	// /**
	// * Movied to PagerContext
	// * Description:
	// * @return
	// * String
	// */
	// public boolean getPromotion() {
	// return promotion;
	// }

	/**
	 * Description:
	 * 
	 * @param string
	 *            void
	 */
	public void setPromotion(boolean string) {
		promotion = string;
	}

	// /**
	// * Movied to PagerContext
	// * @return
	// */
	// public boolean hasParams()
	// {
	// return params != 0;
	// }
	/**
	 * @return Returns the dbname.
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param dbname
	 *            The dbname to set.
	 */
	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	/**
	 * @return Returns the statement.
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * @param statement
	 *            The statement to set.
	 */
	public void setStatement(String statement) {
		this.statement = statement;
	}

	/**
	 * 输出当前页面记录条数
	 * 
	 * @see com.frameworkset.common.tag.pager.tags.PagerInfo#getDataSize()
	 */
	public int getDataSize() {

		return this.pagerContext == null ? 0 : pagerContext.getDataSize();
	}

	/**
	 * @return Returns the exportMeta.
	 */
	public boolean isExportMeta() {
		return exportMeta;
	}

	/**
	 * @param exportMeta
	 *            The exportMeta to set.
	 */
	public void setExportMeta(boolean exportMeta) {
		this.exportMeta = exportMeta;
	}

	/**
	 * @return Returns the dataSets.
	 */
	public Stack getDataSets() {
		return dataSets;
	}

	/**
	 * @param dataSets
	 *            The dataSets to set.
	 */
	public void setDataSets(Stack dataSets) {
		this.dataSets = dataSets;
	}

	/**
	 * @return Returns the metaDatas.
	 */
	public MetaDatas getMetaDatas() {
		return metaDatas;
	}

	// /**
	// * Movied to PagerContext
	// * @return
	// */
	// public boolean isWapflag() {
	// return wapflag;
	// }

	/**
	 * @param metaDatas
	 *            The metaDatas to set.
	 */
	public void setMetaDatas(MetaDatas metaDatas) {
		this.metaDatas = metaDatas;
	}

	public void setWapflag(boolean wapflag) {
		this.wapflag = wapflag;
	}

	public String getPager_infoName() {
		return pager_infoName;
	}

	public void setPager_infoName(String pager_infoName) {
		this.pager_infoName = pager_infoName;
	}

	public String getCommitevent() {
		return commitevent;
	}

	public void setCommitevent(String commitevent) {
		this.commitevent = commitevent;
	}

	public String getSqlparamskey() {
		return sqlparamskey;
	}

	public void setSqlparamskey(String sqlparamskey) {
		this.sqlparamskey = sqlparamskey;
	}

    public String getContainerid()
    {
        return containerid;
    }

    public void setContainerid(String containerid)
    {
        this.containerid = containerid;
    }

    public String getSelector()
    {
        return selector;
    }

    public void setSelector(String selector)
    {
        this.selector = selector;
    }

	public boolean isMoreQuery() {
		return moreQuery;
	}

	public void setMoreQuery(boolean moreQuery) {
		this.moreQuery = moreQuery;
	}

 
}
