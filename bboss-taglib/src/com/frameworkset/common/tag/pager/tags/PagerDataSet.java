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
 *  
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.common.tag.pager.tags;


import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.frameworkset.util.DataFormatUtil;
import org.frameworkset.web.servlet.support.RequestContext;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.ClassData;
import com.frameworkset.common.tag.pager.ClassDataList;
import com.frameworkset.common.tag.pager.DataInfo;
import com.frameworkset.common.tag.pager.DefaultDataInfoImpl;
import com.frameworkset.common.tag.pager.ObjectDataInfoImpl;
import com.frameworkset.common.tag.pager.model.DataModel;
import com.frameworkset.common.tag.pager.model.Formula;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.util.StringUtil;

/**
 * 调用DataInfo接口获取分页/列表/详细数据， 并对封装这些数据到特定的数据结构中，为显示数据作准备
 * 
 * 
 * 判断是否是嵌套列表，如果是嵌套列表则可设置如下属性： colName,property,sortKey,desc
 * 
 * @author biaoping.yin
 */
public class PagerDataSet extends PagerTagSupport {
//    protected String pretoken = "#\\[";
//    protected String endtoken = "\\]";
//	protected boolean flag = false;
	protected boolean moreQuery = false;
	/**
	 * 如果通过title标签设置排序字段，通过该属性来
	 * 控制是否自动对当前页数据排序，还是在数据加载器中手工构造sql语句对全部数据排序
	 * true-自动排序，缺省值
	 * false-在数据加载器中手工构造sql语句对全部数据排序,如果数据加载器没有将title指定的排序字段作为sql的排序字段，那么
	 *       指定的排序字段将不起作用
	 */
	protected boolean autosort = false;
	/**
	 * 针对sessionKey、requestKey、pagecontextKey进行classdataList数据对象缓存，
	 * 避免重复使用时重复生成数据对象
	 */
	protected boolean softparser = true;
	protected Object actual;
	
	/**
	 * position属性用来指定集合中数据对象的位置，对应位置上的对象作为
	 * list标签的数据源，如果是-1则忽略position属性
	 * position只针对list标签起作用，对map标签不起作用
	 */
	protected int position = -1;
	/**
	 *  start属性用来指定集合循环起点位置，默认为0,小于0，就从0开始，大于集合长度将不做任何输出
	 */
	protected int start;
	// protected Tag origineTag = null;

	/***************************************************************************
	 * 设置内容管理系统改造添加的系列属性开始 **
	 **************************************************************************/

	// /**
	// * 频道id
	// */
	// private String channelid="";
	/** 处理上下翻页触发form提交事件，本属性指定需要提交的form表单名称 */
	private String form = null;

	/*
	 * Tag Properties 定义跳转的url
	 */
	private String url = null;

	/**
	 * 导航索引类别，对应PagerContext中的index属性
	 */
	private String navindex = null;

	private int items;

	private int maxItems = DEFAULT_MAX_ITEMS;

	private int maxPageItems = DEFAULT_MAX_PAGE_ITEMS;

	private int maxIndexPages = DEFAULT_MAX_INDEX_PAGES;

	private static final int DEFAULT_MAX_ITEMS = Integer.MAX_VALUE,
			DEFAULT_MAX_PAGE_ITEMS = 10, DEFAULT_MAX_INDEX_PAGES = 10;

	private boolean isOffset = true;

	private String scope = null;

	private String data = null;

	/**
	 * 标识是否是wap应用
	 */
	private boolean wapflag = false;

	/** 当需要保存数据时是否提示保存，与form属性配合使用 */
	private boolean promotion = false;

	boolean isList = true;

	String field = "";

	String width = null;

	String title = null;

	/***************************************************************************
	 * 设置内容管理系统改造添加的系列属性结束 **
	 **************************************************************************/

	/**
	 * 定义全局变量
	 */
	protected DataModel dataModel = null;

	private int index = -1;

	private static final Logger log = Logger.getLogger(PagerDataSet.class);
	
	/**
	 * 外部定义的变量名称信息
	 */
	private String rowidName = "rowid";
	private String dataSetName = "dataSet";
	

	/**
	 * 定义数据集的堆栈
	 */
	protected Stack<PagerDataSet> stack = null;

	protected String sessionKey = null;

	protected String requestKey = null;

	protected String pageContextKey = null;

	protected boolean needClear = false;
	
	protected String sqlparamskey = "sql.params.key";

	/**
	 * 定义dataSet的获取范围
	 */
	protected static final String SESSION_SCOPE = "session";

	protected static final String REQUEST_SCOPE = "request";

	protected static final String PAGECONTEXT_SCOPE = "pageContext";

	protected static final String COLUMN_SCOPE = "column";

	protected static final String DB_SCOPE = "db";

	/**
	 * 内容管理补充常量，内容管理系统发布时需要用到的范围
	 */
	protected static final String CMS_SCOPE = "cms";

	/**
	 * 保存页面pageSet的堆栈
	 */
	protected static final String PAGERDATASET_STACK = "PAGERDATASET_STACK";

	/**
	 * 保存dataSet中出现的所有公式 以公式串为key值 以Formula对象作为值
	 * 标签库对公式的解析先编译
	 */
	private Map formulas = null;

	// static
	// {
	// try
	// {
	// System.setOut(new PrintStream(new FileOutputStream(new
	// java.io.File("d:/test.txt"))));
	// }
	// catch (FileNotFoundException e1)
	// {
	//
	// e1.printStackTrace();
	// }
	// }
	/**
	 * 字符串数组，存放页面中要显示属性字段名称
	 */
	protected java.lang.String[] fields;

	protected ClassDataList theClassDataList = new ClassDataList();
	/**
	 * 记录迭代的当前记录对象
	 */
	protected ClassData currentValueObject ;

	protected String sortKey = null;

	/***************************************************************************
	 * 当值对象属性colName对应的类型为Collection时，* 本标签将该collection作为数据源， *
	 * 循环嵌套输出collection中的对象属性 * property下级属性名称
	 **************************************************************************/
	/**
	 * 列名
	 */
	protected String colName;

	/**
	 * 下级属性名称
	 */
	protected String property;

	int rowid = 0;

	/**
	 * 定义列表类型，获取数据
	 */
	String type = "";

	/**
	 * 定义数据库查询语句
	 */
	protected String statement = null;

	/**
	 * 定义数据库连接池名称
	 */
	protected String dbname = null;

	protected String desc = null;

	/**
	 * 是否声明外部变量 true:声明，缺省值 false：不声明
	 */
	protected boolean declare = true;
	
    /**
     * jquery内容容器
     */
    private String containerid ;
    
    /**
     * jquery内容选择器
     */
    private String selector;
    /**
     * 指定当前记录对象el表达式变量名称
     */
    private String var;
    
    /**
     * 指定当前循环变量el表达式名称
     */
    private String loopvar;
    
    /**
     * 存放map当前记录key变量名称
     */
    private String mapkeyvar;
    /**
     * 存放总记录数变量名称
     */
    private String rowcountvar;
    /**
    * 存放分页当前页面offset变量名称
    */
    private String offsetvar;
    
    public String getOffsetvar() {
		return offsetvar;
	}

	public void setOffsetvar(String offsetvar) {
		this.offsetvar = offsetvar;
	}

	public String getRowcountvar() {
		return rowcountvar;
	}

	public void setRowcountvar(String rowcountvar) {
		this.rowcountvar = rowcountvar;
	}

	public String getPagesizevar() {
		return pagesizevar;
	}

	public void setPagesizevar(String pagesizevar) {
		this.pagesizevar = pagesizevar;
	}
	private String pagesizevar;
	public String getLoopvar() {
		return loopvar;
	}

	public void setLoopvar(String loopvar) {
		this.loopvar = loopvar;
	}

	public PagerDataSet() {

	}
//	 public HttpServletRequest getHttpServletRequest()
//    {
//    	return (HttpServletRequest) pageContext.getRequest();
//    }
//    
//    public HttpServletResponse getHttpServletResponse()
//    {
//    	return (HttpServletResponse) pageContext.getResponse();
//    }
//    
//    public JspWriter getJspWriter()
//    {
//    	
//    	return (JspWriter) pageContext.getOut();
//    }
//    
//    public HttpSession getSession()
//    {
//    	return getHttpServletRequest().getSession(false);
//    }
	
	/**
	 * 这个构造方法只在内容管理系统发布时调用
	 */
	public PagerDataSet(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext) {
//		self_var_flag = true;
		this.request = request;
		this.session = request.getSession(false);
		this.response = response;
		this.pageContext = pageContext;

	}


	

	public PagerContext getPagerContext()
	{
//		System.out.println("public PagerContext getPagerContext()");
		if(this.pagerContext == null)
		{
			
			if(stack.size() == 0)
				return null;
			PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
			return dataSet.getPagerContext();
		}
		else
		{
			return this.pagerContext;
		}
	}
	public long getRowcount() 
	{
		PagerContext pagerContext = getPagerContext();
		if(pagerContext != null)
		{
			if(pagerContext != null && !pagerContext.ListMode())
				return pagerContext.getItemCount();
			else
				return pagerContext.getDataSize();
		}
		else
		{
			return 0;
		}
	}
	
	public int getPageSize() 
	{
		PagerContext pagerContext = getPagerContext();
		if(pagerContext != null)
		{
			return pagerContext.getDataSize();
		}
		else
		{
			return 0;
		}
	}
	
	

	/**
	 * 初始化时必须要调用的方法，调用顺序1
	 * 
	 * @param fields
	 */
	public void initField(String[] fields) {

		this.fields = fields;
		for (int i = 0; this.fields != null && i < this.fields.length; i++) {
			this.fields[i] = this.fields[i].trim();
		}
	}

	/**
	 * @param dataInfo
	 * @param fields -
	 *            字符串数组，存放页面中要显示属性字段名称
	 * 
	 * public PagerDataSet(DataInfo dataInfo, String[] fields) {
	 * this.loadClassData(dataInfo.getDataList()); this.fields = fields; }
	 */

	/**
	 * Access method for the fields property.
	 * 
	 * @return the current value of the fields property
	 */
	public java.lang.String[] getFields() {
		return fields;
	}

	/**
	 * 获取dataSet的值对象的属性 rowid:行索引，确定值对像的位置 columnid：列索引，确定获取值对象的哪个属性
	 * 
	 * 江获取的属性封装成一个Object对象返回
	 * 
	 * @param rowid
	 * @param columnid
	 * @return Object
	 */
	public Object getValue(int rowid, int columnid) {
		return getValue(rowid, locateField(columnid));
	}

	/**
	 * 行号rowid所对应的属性对象的属性值
	 * 
	 * @param rowid
	 * @param columnid
	 * @param property
	 * @return Object
	 */
	public Object getValue(int rowid, int columnid, String property) {
		return getValue(rowid, locateField(columnid), property);

	}
	
	private boolean needPeak()
	{
		return this.pagerContext == null;
	}

	/**
	 * 获取行号为rowid的值对象中属性名称为colName的值
	 * 
	 * @param rowid -
	 *            值对象行号
	 * @param colName -
	 *            属性名称
	 * @return Object
	 */
	public Object getValue(int rowid, String colName) {
		if (rowid == -1)
			return null;
		try
		{
			if(rowid == this.rowid)//如果是当前记录，则直接返回当前记录对象的字段属性值，否则根据rowid检索对应的记录对象 2015.3.10 by biaoping.yin
			{
				return getValue(this.currentValueObject, colName);
				
			}
			else
			{
        		if(!this.needPeak())
        		{
        		     
        			return getValue(theClassDataList.get(rowid), colName);
        		}
        		else
        		{
        			if(stack.size() == 0)
        				return null;
        			PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
        			return dataSet.getValue(rowid,colName);
        		}
			}
		}
		catch(Exception e)
		{
		    throw new RuntimeException("获取属性[colName=" + colName + "]失败：" + theClassDataList + "=" + theClassDataList,e);
		}
	}
	
	/**
	 * 获取行号为rowid的值对象中属性名称为colName的值或者size
	 * 如果是求size，那么则返回：字符串类型对象长度，容器对象size
	 * 
	 * @param rowid -
	 *            值对象行号
	 * @param colName - 属性名称或者带size:前缀的变量名称，例如：name,size:name
	 *            属性名称
	 * @return Object
	 */
	public Object getValueOrSize(int rowid, String colName) {
		boolean issize = false;
		try
		{
			
			if(colName.startsWith("size:"))
			{
				issize = true;
				colName = colName.substring(5); 
			}
			Object value =  getValue( rowid,  colName) ;
			if(issize)
			{
				return length(value);
			}
			else
			{
				return value;
			}
		}
		catch(Exception e)
		{
		    throw new RuntimeException("获取属性[colName=" + colName + "]"+(issize?"长度":"")+"失败：" + theClassDataList + "=" + theClassDataList,e);
		}
	}
	
	
	/**
	 * 获取行号为rowid的值对象中属性名称为colName的值
	 * 
	 * @param rowid -
	 *            值对象行号
	 * @param colName -
	 *            属性名称
	 * @return Object
	 */
	public Object getValue(int rowid) {
		if (rowid == -1)
			return null;
		try
		{
			if(rowid == this.rowid)
			{
				return getValue(this.currentValueObject);
			}
			else
			{
        		if(!this.needPeak())
        		{
        		     
        			return getValue(theClassDataList.get(rowid));
        		}
        		else
        		{
        			if(stack.size() == 0)
        				return null;
        			PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
        			return dataSet.getValue(rowid);
        		}
			}
		}
		catch(Exception e)
		{
		    throw new RuntimeException("获取属性[colName=" + colName + "]失败：" ,e);
		}
	}
	
	/**
	 * 获取行号为rowid的值对象中属性名称为colName的值
	 * 
	 * @param rowid -
	 *            值对象行号
	 * @param colName -
	 *            属性名称
	 * @return Object
	 */
	public ClassData getClassDataValue(int rowid) {
		if (rowid == -1)
			return null;
		try
		{
        		if(!this.needPeak())
        		{
        		     
        			return theClassDataList.get(rowid);
        		}
        		else
        		{
        			if(stack.size() == 0)
        				return null;
        			PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
        			return dataSet.getClassDataValue(rowid);
        		}
		}
		catch(Exception e)
		{
		    throw new RuntimeException("获取属性[colName=" + colName + "]失败：" ,e);
		}
	}
	
	/**
	 * 获取对应行原始对象数据类型
	 * @param rowid
	 * @return
	 */
	public Object getOrigineObject(int rowid)
	{
		if(rowid == this.rowid)
		{
			
			if(this.currentValueObject != null)
			{
				Object value = this.currentValueObject.getValueObject();
				if(value == null)
				{
					log.debug("-------------------->584 PagerDataSet this.currentValueObject.getValueObject() return null...");
				}
				return value;
			}			
			else
			{
				log.debug("-------------------->589 PagerDataSet this.currentValueObject is null...");
				return null;
			}
			
		}
		else
		{		
			if(!this.needPeak())
			{
				ClassData object = this.theClassDataList.get(rowid);
				Object value = object.getValueObject();
				if(value == null)
					log.debug("PagerDataSet !this.needPeak() getOrigineObject(rowid="+rowid+") from ClassData is null...");
				return value;
			}
			else
			{
				if(stack.size() == 0)
				{
					log.debug("PagerDataSet  needPeak getOrigineObject(rowid="+rowid+") from ClassData stack.size() == 0 return null.---------------");
					return null;
				}
				PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
				log.debug("PagerDataSet  needPeak getOrigineObject(rowid="+rowid+") from peek dataSet. ");
				return dataSet.getOrigineObject(rowid);
			}
		}
	}
	
	public Object getMapKey()
	{
		return getMapKey(rowid);
	}
	
	public Object getMapKey(int rowid)
	{
		if(rowid == this.rowid)
		{
			return this.currentValueObject.getMapkey();
		}
		else
		{
			if(!this.needPeak())
			{
				ClassData object = this.theClassDataList.get(rowid);
				return object.getMapkey();
			}
			else
			{
				if(stack.size() == 0)
					return null;
				PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
				return dataSet.getMapKey(rowid);
			}
		}
	}
	
	/**
	 * 获取当前行原始对象数据类型
	 * @param rowid
	 * @return
	 */
	public Object getOrigineObject()
	{
		return getOrigineObject(rowid);
	}
	
	

	public Object getValue(String colName) {
		if (rowid == -1)
			return null;
		return getValue(rowid, colName);
	}

	/**
	 * 获取值对象中所引用的其它值对象（变量名称为colName） 中属性（属性名称为subColName）值
	 * 
	 * @param rowid
	 * @param colName
	 * @param subColName
	 * @return Object
	 */

	public Object getValue(int rowid, String colName, String subColName) {
		if(!this.needPeak())
		{
			Object referObj = getValue(rowid, colName);
			if (referObj == null)
				return null;
			ClassData referData = new ClassData(referObj);
			return getValue(referData, subColName);
		}
		else
		{
			if(stack.size() == 0)
				return null;
			PagerDataSet dataSet = (PagerDataSet)this.stack.peek();
			return dataSet.getValue( rowid,  colName,  subColName);
		}
	}

	/**
	 * 从data中获取属性值
	 * 
	 * @param data
	 * @param colName
	 * @return Object
	 */
	public Object getValue(ClassData data, String colName) {
		Object value = data.getValue(colName);
		
		return value;
	}
	
	/**
	 * 从data中获取属性值
	 * 
	 * @param data
	 * @param colName
	 * @return Object
	 */
	public Object getValue(ClassData data) {
		Object value = data.getValueObject();
		
		return value;
	}

	// public List getValue(int rowid, String colName,String subColName)
	// {
	// return (List)theClassDataList.get(rowid).getValue(colName);
	// }

	/**
	 * @param rowid
	 * @param columnid
	 * @return java.lang.String
	 */
	public String getString(int rowid, int columnid) {
		Object value = getValue(rowid, columnid);
		
		return convertLobToString(value);
		// return String.valueOf(getValue(rowid, columnid));
	}

	public String getStringWithDefault(int rowid, int columnid, Object defaultValue) {
		Object value = getValue(rowid, columnid);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
		// return String.valueOf(getValue(rowid, columnid));
	}

	public String getString(int rowid, int columnid, String property) {
		Object value = getValue(rowid, columnid, property);
		
		return convertLobToString(value);
		// return String.valueOf(getValue(rowid, columnid));
	}
	
	public Object getObject(int rowid, int columnid, String property) {
		Object value = getValue(rowid, columnid, property);
		
		return convertLobToObject(value);
		// return String.valueOf(getValue(rowid, columnid));
	}

	public String getStringWithDefault(int rowid, int columnid, String property,
			Object defaultValue) {
		Object value = getValue(rowid, columnid, property);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
		// return String.valueOf(getValue(rowid, columnid));
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getString(int rowid, String colName) {
		Object value = getValue(rowid, colName);
		return convertLobToString(value);
	}
	
	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public Object getObject(int rowid, String colName) {
		Object value = getValue(rowid, colName);
		return convertLobToObject(value);
	}
	
	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getString(int rowid) {
		Object value = getValue(rowid);
		return convertLobToString(value);
	}
	
	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public Object getObject(int rowid) {
		Object value = getValue(rowid);
		return convertLobToObject(value);
	}
	
	
	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public Object getObject() {
		return getObject(this.rowid);
	}
	
	/**
	 * 获取属性对应的map对象
	 * @param colName
	 * @return java.lang.String
	 */
	public Map getMap(String colName) {
		Object value = getValue(rowid, colName);
		try
		{
			return value == null ? null : (Map)value;
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getString(String colName) {
		Object value = getValue(rowid, colName);
		
		return convertLobToString(value);
	}

	/**
	 * 根据索引获取对应的父值
	 * 
	 * @param rowid
	 * @param colName
	 * @param index
	 * @return
	 */
	public String getString(String colName, int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return null;
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		Object value = dataSet.getValue(dataSet.getRowid(), colName);
		
		return convertLobToString(value);
	}



	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getStringWithDefault(int rowid, String colName, Object defaultValue) {
		Object value = getValue(rowid, colName);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getStringWithDefault(String colName, Object defaultValue) {
		Object value = getValue(rowid, colName);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public String getStringWithDefault(String colName, Object defaultValue, int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return defaultValue.toString();
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		Object value = dataSet.getValue(dataSet.getRowid(), colName);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
	}

	public String getString(int rowid, String colName, String property) {
		Object value = getValue(rowid, colName, property);
		return convertLobToString(value);
	}
	
	public Object getObject(int rowid, String colName, String property) {
		Object value = getValue(rowid, colName, property);
		return convertLobToObject(value);
	}
	
	public String getString( String colName, String property) {
		Object value = getValue(rowid, colName, property);
		return convertLobToString(value);
	}
	
	/**
	 * 根据列名对应的对象的属性获取属性值
	 * @param colName
	 * @param property
	 * @param defaultValue
	 * @return
	 */
	public String getStringWithDefault( String colName, String property,Object defaultValue) {
		Object value = getValue(rowid, colName, property);
		if(value == null)
			return defaultValue.toString();
		return convertLobToString(value);
	}
	

	/**
	 * 将lob字段输出到字符串
	 * @param value
	 * @return
	 */
	private String convertLobToString(Object value)
	{
		if(value == null)
			return null;
		if(!(value instanceof Clob) && !(value instanceof Blob))
			return value == null ? null : value.toString();
		else if(value instanceof Clob)
		{
			try
			{
				 Clob temp = (Clob)value;
	             int leg = (int)temp.length();
	             if(leg > 0)
	             	return temp.getSubString(1,leg);
	             else
	             	return "";
			}
			catch(Exception e)
			{
				
			}
		}
		else if(value instanceof Blob)
		{
			try
			{
				 Blob blob = (Blob)value;
	             int length = (int)blob.length();
	             if(length > 0)
	             {
		                byte ret[] = new byte[(int)blob.length()];
		                ret = blob.getBytes(1,length);
		                return new String(ret);
	             }
	             else
	             {
	             	return "";
	             }
			}
			catch(Exception e)
			{
				
			}
		}
		return value.toString();
	}
	
	/**
	 * 将lob字段输出到字符串
	 * @param value
	 * @return
	 */
	private Object convertLobToObject(Object value)
	{
		if(value == null)
			return null;
		if(!(value instanceof Clob) && !(value instanceof Blob))
			return value ;
		else if(value instanceof Clob)
		{
			try
			{
				 Clob temp = (Clob)value;
	             int leg = (int)temp.length();
	             if(leg > 0)
	             	return temp.getSubString(1,leg);
	             else
	             	return "";
			}
			catch(Exception e)
			{
				
			}
		}
		else if(value instanceof Blob)
		{
			try
			{
				 Blob blob = (Blob)value;
	             int length = (int)blob.length();
	             if(length > 0)
	             {
		                byte ret[] = new byte[(int)blob.length()];
		                ret = blob.getBytes(1,length);
		                return new String(ret);
	             }
	             else
	             {
	             	return "";
	             }
			}
			catch(Exception e)
			{
				
			}
		}
		return value;
	}
	public String getString(String colName, String property, int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return null;
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		Object value = dataSet.getValue(dataSet.getRowid(), colName, property);
		
		return convertLobToString(value);
	}

	public String getStringWithDefault(int rowid, String colName, String property,
			Object defaultValue) {
		Object value = getValue(rowid, colName, property);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
	}

	public String getStringWithDefault(String colName, String property,
			Object defaultValue, int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return defaultValue.toString();
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		Object value = dataSet.getValue(dataSet.getRowid(), colName, property);
		value = convertLobToString(value);
		return value == null ? defaultValue.toString() : value.toString();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return int
	 */
	public int getInt(int rowid, int columnid) {
		return ((Integer) getValue(rowid, columnid)).intValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public int getInt(int rowid, String colName) {
		return ((Integer) getValue(rowid, colName)).intValue();
	}

	/**
	 * 根据数据集索引index和字段名称,获取整形字段的值
	 * 
	 * @param colName
	 * @param index
	 * @return
	 */

	public int getInt(String colName, int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return -1;
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		Object value = dataSet.getValue(dataSet.getRowid(), colName, property);
		if (value == null)
			return -1;
		return ((Integer) value).intValue();
	}

	/**
	 * 根据字段名称,获取整形字段的值
	 * 
	 * @param rowid
	 * @param colName
	 * @return java.lang.String
	 */
	public int getInt(String colName) {
		return ((Integer) getValue(rowid, colName)).intValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return Date
	 */
	public Date getDate(int rowid, int columnid) {
		return (Date) getValue(rowid, columnid);
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return Date
	 */
	public Date getDate(int columnid) {
		return (Date) getValue(rowid, columnid);
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return Date
	 */
	public String getFormatDate(int rowid, int columnid, String format,String locale,boolean userRequestLocale,String timeZone) {
		Object obj = getValue(rowid, columnid);
		return formatDate(request,obj,format,  locale,  userRequestLocale,  timeZone);

	}
	public static String formatDate(HttpServletRequest request,Object data,String dateformat,String locale,boolean userRequestLocale,String timeZone)
	{
		if (data == null)
			return null;
//		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		SimpleDateFormat dateFormat = null;
		if(locale == null)
		{
			if(!userRequestLocale)
				dateFormat = DataFormatUtil.getSimpleDateFormat(request,dateformat,(String)null,timeZone);
			else
				dateFormat = DataFormatUtil.getSimpleDateFormat(request,dateformat,request.getLocale(),timeZone);
		}
		else
		{
			dateFormat = DataFormatUtil.getSimpleDateFormat(request,dateformat,locale,timeZone);
		}
		
		try {
		    if(data instanceof Date)
		    {
    			Date date = (Date) data;
    			return dateFormat.format(date);
		    }
		    else if(data instanceof Long)
		    {
		        long va = ((Long)data).longValue();
		        if(va <= 0)
		            return "";
		        Date date = new Date(va);
                return dateFormat.format(date);
		    }
		    else 
            {
                return data.toString();
            }
            
		} catch (Exception e) {
			// e.printStackTrace();
			return data.toString();
		}
	}
	
	public static String formatDate(HttpServletRequest request,Object data,String dateformat)
	{
		if (data == null)
			return null;
//		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		 
		SimpleDateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(request,dateformat);
		try {
		    if(data instanceof Date)
		    {
    			Date date = (Date) data;
    			return dateFormat.format(date);
		    }
		    else if(data instanceof Long)
		    {
		        long va = ((Long)data).longValue();
		        if(va <= 0)
		            return "";
		        Date date = new Date(va);
                return dateFormat.format(date);
		    }
		    else 
            {
                return data.toString();
            }
            
		} catch (Exception e) {
			// e.printStackTrace();
			return data.toString();
		}
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return Date
	 */
	public Date getDate(int rowid, String colName) {
		return (Date) getValue(rowid, colName);
	}

	/**
	 * 
	 * @param colName
	 * @return Date
	 */
	public Date getDate(String colName) {
		return (Date) getValue(rowid, colName);
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return Date
	 */
	public String getFormatDate(int rowid, String colName, String format,String locale,boolean userRequestLocale,String timeZone) {
		Object obj = getValue(rowid, colName);
		return formatDate(request,obj,format,  locale,  userRequestLocale,  timeZone);
	}
	
	
	/**
	 * @param rowid
	 * @param colName
	 * @return Date
	 */
	public String getFormatDate(int rowid,  String format,String locale,boolean userRequestLocale,String timeZone) {
		Object obj = getValue(rowid);
		return formatDate(request,obj,format,  locale,  userRequestLocale,  timeZone);
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return Date
	 */
	public Date getDate(int rowid, String colName, String property) {
		return (Date) getValue(rowid, colName, property);
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return Date
	 */
	public String getFormatDate(int rowid, String colName, String property,
			String format,String locale,boolean userRequestLocale,String timeZone) {
		Object obj = getValue(rowid, colName, property);
		return formatDate(request,obj,format,  locale,  userRequestLocale,  timeZone);
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @param property
	 * @return Date
	 */
	public Date getDate(int rowid, int columnid, String property) {
		return (Date) getValue(rowid, columnid, property);
	}

	/**
	 * 获取格式化的日期字符串
	 * 
	 * @param rowid
	 * @param columnid
	 * @param property
	 * @param format
	 * @return String
	 */

	public String getFormatDate(int rowid, int columnid, String property,
			String format,String locale,boolean userRequestLocale,String timeZone) {
		Object obj = getValue(rowid, columnid, property);
		return formatDate(request,obj,format,  locale,  userRequestLocale,  timeZone);

	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return char
	 */
	public char getChar(int rowid, int columnid) {
		return ((Character) getValue(rowid, columnid)).charValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return char
	 */
	public char getChar(int rowid, String colName) {
		return ((Character) getValue(rowid, colName)).charValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return long
	 */
	public long getLong(int rowid, int columnid) {
		return ((Long) getValue(rowid, columnid)).longValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return long
	 */
	public long getLong(int rowid, String colName) {
		return ((Long) getValue(rowid, colName)).longValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return short
	 */
	public short getShort(int rowid, int columnid) {
		return ((Short) getValue(rowid, columnid)).shortValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return short
	 */
	public short getShort(int rowid, String colName) {
		return ((Short) getValue(rowid, colName)).shortValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return double
	 */
	public String getFormatData(int rowid, int columnid, String format) {
		
		Object data = getValue(rowid, columnid);
		return formatData(request,data,format);
	}
	
	public static String formatData(HttpServletRequest request,Object data,String dataformat)
	{
		if (data == null)
			return null;
		
		NumberFormat numerFormat = DataFormatUtil.getDecimalFormat(request,dataformat);
//		NumberFormat numerFormat = new DecimalFormat(dataformat);
		
		// double value = dd.doubleValue();

		return numerFormat.format(data);
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public String getFormatData(int rowid, String colName, String format) {
		Object data = getValue(rowid, colName);

		return formatData(request,data,format);
	}
	
	
	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public String getFormatData(int rowid, String format) {
		Object data = getValue(rowid);
		return formatData(request,data,format);
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return double
	 */
	public String getFormatData(int rowid, int columnid, String property,
			String format) {
		Object data = getValue(rowid, columnid, property);
		return formatData(request,data,format);
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public String getFormatData(int rowid, String colName, String property,
			String format) {
		Object data = getValue(rowid, colName, property);
		return formatData(request,data,format);
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return double
	 */
	public double getDouble(int rowid, int columnid) {
		return ((Double) getValue(rowid, columnid)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return double
	 */
	public double getDouble(int columnid) {
		return ((Double) getValue(rowid, columnid)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public double getDouble(int rowid, String colName) {
		return ((Double) getValue(rowid, colName)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public double getDouble(String colName) {
		return ((Double) getValue(rowid, colName)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return double
	 */
	public double getDouble(int rowid, int columnid, String property) {
		return ((Double) getValue(rowid, columnid, property)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return double
	 */
	public double getDouble(int rowid, String colName, String property) {
		return ((Double) getValue(rowid, colName, property)).doubleValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return float
	 */
	public float getFloat(int rowid, int columnid) {
		return ((Float) getValue(rowid, columnid)).floatValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return float
	 */
	public float getFloat(int rowid, String colName) {
		return ((Float) getValue(rowid, colName)).floatValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return boolean
	 */
	public boolean getBoolean(int rowid, String colName) {
		return ((Boolean) getValue(rowid, colName)).booleanValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return boolean
	 */
	public boolean getBoolean(String colName) {
		return ((Boolean) getValue(rowid, colName)).booleanValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return boolean
	 */
	public boolean getBoolean(int rowid, int columnid) {
		return ((Boolean) getValue(rowid, columnid)).booleanValue();
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return boolean
	 */
	public boolean getBoolean(int columnid) {
		return ((Boolean) getValue(rowid, columnid)).booleanValue();
	}

	/**
	 * 调用dataInfo接口方法，获取数据 处理dataSet中的所有对象
	 * 
	 * @param dataInfo
	 * @param isList
	 * @throws LoadDataException
	 */
	protected void loadClassData(DataInfo dataInfo, boolean isList)
			throws LoadDataException {
		// List list;
		if (dataInfo == null)
			throw new LoadDataException(
					"load list Data error in loadClassData(DataInfo dataInfo, boolean isList):数据对象为空");
		// log.info();
		// Class voClass = dataInfo.getVOClass();
		/**
		 * 通过dataInfo接口获取数据分为两种情况，一种是采用DefaultDataInfoImpl实现，其他的为DataInfoImpl实现
		 * 在此处要做相应的判断，分别处理
		 */
		if (dataInfo instanceof DefaultDataInfoImpl) {
			if (isList)
				loadClassData(dataInfo.getListItemsFromDB(), dataInfo
						.getVOClass());
			else
				loadClassData(dataInfo.getPageItemsFromDB(), dataInfo
						.getVOClass());
		} else {
			Object datas = null;
			if (isList)
			{
				datas = dataInfo.getListItems();
				
			}
			// list = dataInfo.getListItems();
			else {
				datas = dataInfo.getPageItems();				
				// list = dataInfo.getPageItems();
			}
			if(datas instanceof List)
			{
				loadClassData(((List)datas).iterator(), dataInfo
						.getVOClass());
			}
			else
			{
				loadClassData((Object[])datas, dataInfo
						.getVOClass());
			}
		}
		// :log
	}

	/**
	 * 装载hashtable数组中的数据，通过属性获取hashtable中的数组时将属性全部置换为大写
	 * 
	 * @param datas
	 * @param voClazz
	 */
	protected void loadClassData(Map[] datas, Class voClazz) {
		// modified by biaoping.yin on 2005.03.28
		if (datas == null)
			return;
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
		for (int i = 0; i < datas.length; i++) {
			theClassDataList.add(new ClassData(datas[i]));
		}

	}

	/**
	 * 装载Map数组中的数据， 通过属性获取map中的数组时将根据toUpercase参数来控制是否将属性全部置换为大写
	 * 
	 * @param datas
	 * @param toUpercase
	 * @param voClazz
	 */
	protected void loadClassData(Map[] datas, Class voClazz,
			boolean toUpercase) {
		// modified by biaoping.yin on 2005.03.28
		if (datas == null)
			return;
		
//		if (theClassDataList == null)
//			theClassDataList = new ClassDataList();
		if(this.softparsered())
			return;
		if(position < 0)
		{
			for (int i = 0; i < datas.length; i++) {
				theClassDataList.add(new ClassData(datas[i], toUpercase));
			}
		}
		else
		{
		
			if(this.position < datas.length)
			{
				theClassDataList.add(new ClassData(datas[position]));
			}
			else
			{
				throw new ArrayIndexOutOfBoundsException("Map数组长度为:"+datas.length+",指定的数据位置为:"+position);
			}
		}

	}

	/**
	 * 装载map中的数据，并且通过属性获取时不会对属性进行大写置换
	 * 
	 * @param datas
	 * @param voClazz
	 */
	protected void loadClassData(Map data, Class voClazz) {
		// modified by biaoping.yin on 2007.02.4
		if (data == null)
			return;
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
		theClassDataList.add(new ClassData(data, false));
		// for(int i = 0; i < datas.length; i ++)
		// {
		// theClassDataList.add(new ClassData(datas[i]));
		// }

	}
	
	/**
	 * 装载map中的数据，用于map标签迭代展示map中的数据
	 * key标签用来在迭代过程中或者对应的key值
	 * cell标签用来展示value对象的属性值或者value对象本身	 * 
	 * @param data
	 */
	protected void loadMapClassData(Map data,MapTag maptag) {
		
		if (data == null)
			return;
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
		if(data.size() <=0)
			return;
		
		if(!StringUtil.isEmpty(maptag.getKey()))
		{	
			theClassDataList.add(new ClassData(data.get(maptag.getKey()),maptag.getKey(), false));
		}
		else if(!StringUtil.isEmpty(maptag.getKeycolName()))
		{
			PagerDataSet dataSet = (PagerDataSet)stack.elementAt(stack.size() - 2);
			Object key = dataSet.getValue(maptag.getKeycolName());
			theClassDataList.add(new ClassData(data.get(key),key, false));
		}
		else if(maptag.isKeycell())
		{
			PagerDataSet dataSet = (PagerDataSet)stack.elementAt(stack.size() - 2);
			Object key = dataSet.getValue(dataSet.getRowid());
			theClassDataList.add(new ClassData(data.get(key),key, false));
		}
		else
		{
			if(this.softparsered())
				return;
			Iterator its = data.entrySet().iterator();
			while(its.hasNext())
			{
				Map.Entry ent = (Map.Entry)its.next();
				theClassDataList.add(new ClassData(ent.getValue(),ent.getKey(), false));
			}
		}
		// for(int i = 0; i < datas.length; i ++)
		// {
		// theClassDataList.add(new ClassData(datas[i]));
		// }

	}

	/**
	 * 装载对象数组数组中的数据
	 * 
	 * @param datas
	 * @param voClazz
	 */
	protected void loadClassData(Object[] datas) {
		// modified by biaoping.yin on 2005.03.28
		if (datas == null)
			return;
//		if (theClassDataList == null)
//			theClassDataList = new ClassDataList();
		if(this.softparsered())
			return;
//		Class valueClass = null;
		if(position < 0)
		{
			for (int i = 0; i < datas.length; i++) {
	//			if (valueClass == null)
	//				valueClass = datas[i].getClass();
				theClassDataList.add(new ClassData(datas[i]));
			}
		}
		else
		{
			if(this.position < datas.length)
			{
				theClassDataList.add(new ClassData(datas[position]));
			}
			else
			{
				throw new ArrayIndexOutOfBoundsException("对象数组长度为:"+datas.length+",指定的数据位置为:"+position);
			}
		}

	}
	
	/**
	 * 装载对象数组数组中的数据
	 * 
	 * @param datas
	 * @param voClazz
	 */
	protected void loadClassData(Object[] datas,Class clazz) {
		loadClassData(datas);
//		// modified by biaoping.yin on 2005.03.28
//		if (datas == null)
//			return;
//		if (theClassDataList == null)
//			theClassDataList = new ClassDataList();
////		Class valueClass = null;
//		for (int i = 0; i < datas.length; i++) {
////			if (valueClass == null)
////				valueClass = datas[i].getClass();
//			theClassDataList.add(new ClassData(datas[i]));
//		}

	}

	/**
	 * 
	 * @param dataInfo
	 * @param voClazz
	 */
	protected void loadClassData(Iterator dataInfo, Class voClazz)
			throws LoadDataException {
		if (dataInfo == null)
			throw new LoadDataException(
					"load list Data error loadClassData(Iterator dataInfo, Class voClazz):数据对象为空");
		// log.info();
		if(this.softparsered())
			return;
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
		if(position < 0)
		{
			while (dataInfo.hasNext()) {
				theClassDataList.add(new ClassData(dataInfo.next()));
			}
		}
		else
		{
			int i = 0;
			while (dataInfo.hasNext()) {
				if(i == position)
				{
					theClassDataList.add(new ClassData(dataInfo.next()));
				}
				else
				{
					dataInfo.next();
				}
					
				i ++;
				
			}
			if(this.position >= i)
			{
				throw new ArrayIndexOutOfBoundsException("集合大小为:"+i+",指定的数据位置为:"+position);
			}
		}

	}
	public static final String softparser_cache_pre = "com.frameworkset.common.tag.pager.tags.softparser.";

	protected String buildColnameKey(String colName)
	{
		
		StringBuffer buf = new StringBuffer();
		if(this.index >= 0)
		{
			buf.append(index).append(".").append( this.getRowidByIndex(index) ).append(".").append(colName);
		}
		else
		{				
			int idx = this.stack.size() - 2;
			if(idx >= 0)
				buf.append(idx).append(".").append( this.getRowidByIndex(idx)  ).append( "." ).append( colName);
			else
				buf.append( colName);
		}
		return buf.toString();
	}
	protected boolean softparsered()
	{
		if (theClassDataList == null)
		{
//			theClassDataList = new ClassDataList();
			if(this.softparser)
			{
				if(!StringUtil.isEmpty(this.requestKey)) //对于直接指定的请求属性进行缓冲处理
				{
					String cachekey = softparser_cache_pre +requestKey;
					if(this.position >= 0)
						cachekey = cachekey + "|"+position; 
					theClassDataList = (ClassDataList)request.getAttribute(cachekey);
					if(theClassDataList != null)
						return true;
					theClassDataList = new ClassDataList();
					request.setAttribute(cachekey,theClassDataList);
					
				}
				else if(!StringUtil.isEmpty(this.sessionKey))//对于直接指定的session属性进行缓冲处理
				{
					
					if(this.session != null)
					{
						String cachekey = softparser_cache_pre +sessionKey;
						if(this.position >= 0)
							cachekey = cachekey + "|"+position; 
						theClassDataList = (ClassDataList)session.getAttribute(cachekey);
						if(theClassDataList != null)
							return true;
						theClassDataList = new ClassDataList();
						session.setAttribute(cachekey,theClassDataList);
					}
				}
				else if(!StringUtil.isEmpty(this.pageContextKey))//对于直接指定的pageContext属性进行缓冲处理
				{
					
					String cachekey = softparser_cache_pre +pageContextKey;
					if(this.position >= 0)
						cachekey = cachekey + "|"+position; 
					theClassDataList = (ClassDataList)pageContext.getAttribute(cachekey,PageContext.APPLICATION_SCOPE);
					if(theClassDataList != null)
						return true;
					theClassDataList = new ClassDataList();
					pageContext.setAttribute(cachekey,theClassDataList,PageContext.APPLICATION_SCOPE);
					
				}
				
//				else if(this.colName != null )//如果对应的集合来自于外层或者本层对象属性对应的map或者list或者数组
//				{
//					String key = buildColnameKey(colName);
//				
//					String cachekey = softparser_cache_pre +key;
//					if(this.position >= 0)
//						cachekey = cachekey + "|"+position; 
//					theClassDataList = (ClassDataList)request.getAttribute(cachekey);
//					if(theClassDataList != null)
//						return true;
//					theClassDataList = new ClassDataList();
//					request.setAttribute(cachekey,theClassDataList);
//				}
				else
				{
					theClassDataList = new ClassDataList();
				}
			}
			else
			{
				theClassDataList = new ClassDataList();
			}
			
		}
		return false;
		
	}
	/**
	 * 装载集合中的数据
	 * 
	 * @param dataInfo
	 */
	protected void loadClassData(Collection dataInfo) throws LoadDataException {
		if (dataInfo == null)
			throw new LoadDataException(
					"load list Data error loadClassData(Collection dataInfo):数据对象为空");
		
		
			
			
//			if (theClassDataList == null)
//			{
////				theClassDataList = new ClassDataList();
//				if(this.softparser)
//				{
//					if(!StringUtil.isEmpty(this.requestKey))
//					{
//						String cachekey = softparser_cache_pre +"requestKey";
//						theClassDataList = (ClassDataList)request.getAttribute(cachekey);
//						if(theClassDataList != null)
//							return;
//						theClassDataList = new ClassDataList();
//						request.setAttribute(cachekey,theClassDataList);
//						
//					}
//					else if(!StringUtil.isEmpty(this.sessionKey))
//					{
//						
//						if(this.session != null)
//						{
//							String cachekey = softparser_cache_pre +"requestKey";
//							theClassDataList = (ClassDataList)session.getAttribute(cachekey);
//							if(theClassDataList != null)
//								return;
//							theClassDataList = new ClassDataList();
//							session.setAttribute(cachekey,theClassDataList);
//						}
//					}
//					else if(!StringUtil.isEmpty(this.pageContextKey))
//					{
//						
//						String cachekey = softparser_cache_pre +"requestKey";
//						theClassDataList = (ClassDataList)pageContext.getAttribute(cachekey);
//						if(theClassDataList != null)
//							return;
//						theClassDataList = new ClassDataList();
//						pageContext.setAttribute(cachekey,theClassDataList);
//						
//					}
//					else
//					{
//						theClassDataList = new ClassDataList();
//					}
//				}
//				else
//				{
//					theClassDataList = new ClassDataList();
//				}
//				
//			}
			if(this.softparsered())
				return;
			
			if(position < 0)
			{
				Iterator it = dataInfo.iterator();
				while (it.hasNext()) {
					theClassDataList.add(new ClassData(it.next()));
				}
			}
			else
			{
				int i = 0;
				if(this.position < dataInfo.size())
				{
					Iterator it = dataInfo.iterator();
					while (it.hasNext()) {
						if(i == position)
							theClassDataList.add(new ClassData(it.next()));
						else
						{
							it.next();
						}
						i ++;
						
					}
				}
				else
				{
					throw new ArrayIndexOutOfBoundsException("集合大小为:"+i+",指定的数据位置为:"+position);
				}
			}
				/**
				 * 以下的代码对取到的数据进行排序
				 */
				// sortKey = getSortKey();
				// sortBy(getSortKey(),desc());
			
		
	}

	/**
	 * 
	 * @param dataInfo
	 * @param voClazz
	 */
	protected void loadClassData(Object dataInfo, Class voClazz)
			throws LoadDataException {
		if (dataInfo == null)
			throw new LoadDataException(
					"load list Data error in loadClassData(Object dataInfo, Class voClazz):数据对象为空");
		// log.info();
//		Field[] fields = voClazz == null ? null : voClazz.getFields();
//		Method[] methods = voClazz == null ? null : voClazz.getMethods();
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
//		if(dataInfo instanceof Map)
//		{
//			theClassDataList.add(new ClassData((Map)dataInfo,false));
//		}
//		else
//		{
//			theClassDataList.add(new ClassData(dataInfo));
//		}
		theClassDataList.add(new ClassData(dataInfo));
			

	}

	/**
	 * 装载迭代器中的数据对象
	 * 
	 * @param dataInfo
	 * 
	 */
	protected void loadClassData(Iterator dataInfo) throws LoadDataException {
//		if (dataInfo == null)
//			throw new LoadDataException(
//					"load list Data error in loadClassData(Object dataInfo, Class voClazz):数据对象为空");
//		// log.info();
//
////		if (theClassDataList == null)
////			theClassDataList = new ClassDataList();
//		if(this.softparsered())
//			return;
//		while (dataInfo.hasNext()) {
//			Object data = dataInfo.next();
//			theClassDataList.add(new ClassData(data));
//		}
		loadClassData(dataInfo, (Class) null);

	}

	/**
	 * 装载空数据，详细显示页面上可用
	 */
	protected void loadClassDataNull() {
		if (theClassDataList == null)
			theClassDataList = new ClassDataList();
		theClassDataList.add(new ClassData(null));
	}


	/**
	 * 输入参数index 定位fields数组中下标为index的数据项并返回该项
	 * 
	 * 异常捕获： 数组越界异常
	 * 
	 * @param index
	 * @return java.lang.String
	 */
	protected String locateField(int index) {
		if (index >= fields.length || index < 0)
			return null;
		return fields[index];
	}

	/**
	 * 对classDatas中的对象排序，方法体中直接调用classDatas.sortby(sortKey,desc)方法既?
	 * 
	 * desc参数决定排序的秩序： true :降序 false:升序
	 * 
	 * sortKey参数：排序字段
	 * 
	 * @param sortKey -
	 *            排序关键字
	 * @param desc
	 */
	public void sortBy(String sortKey, boolean desc) {
		if (theClassDataList != null)
			theClassDataList.sortBy(sortKey, desc);
		else
			// :log
			log.warn("没有要排序列表数据：" + PagerDataSet.class.getName() + ".sort("
					+ sortKey + "," + desc + ")");
	}

	/**
	 * 获取记录条数
	 * 
	 * @return int
	 */
	public int size() {
		if(!this.needPeak())
		{
			return theClassDataList == null ? 0 : theClassDataList.size();
		}
		else
		{
			if(this.stack.size() == 0)
			{
				return 0;
			}
			else
			{
				PagerDataSet dataSet = (PagerDataSet)stack.peek();
				return dataSet.size();
			}
		}
	}

	/**
	 * @param rowid
	 * @param columnid
	 * @return byte
	 */
	public byte getByte(int rowid, int columnid) {
		return ((Byte) getValue(rowid, columnid)).byteValue();
	}

	/**
	 * @param rowid
	 * @param colName
	 * @return byte
	 */
	public byte getByte(int rowid, String colName) {
		return ((Byte) getValue(rowid, colName)).byteValue();
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
	public void push() {
		HttpServletRequest request = this.getHttpServletRequest();
		stack = (Stack) request.getAttribute(PAGERDATASET_STACK);
		if (stack == null) {
			
			stack = new Stack();
			request.setAttribute(PAGERDATASET_STACK, stack);
		}
		stack.push(this);
		// //保存副本
		// if(isExportMeta())
		// savecopy();
	}
	

	// /**
	// * 在pagerContext中保存pageDataSet的副本，其他的页面功能使用数据集合
	// */
	// protected void savecopy()
	// {
	// this.pagerContext.dataSets.push(this);
	// }

	public PagerDataSet pop() {
		PagerDataSet obj = (PagerDataSet) stack.pop();
		HttpServletRequest request = this.getHttpServletRequest();
		if (stack.size() == 0)
			request.removeAttribute(PAGERDATASET_STACK);
		return obj;

	}

	/**
	 * 恢复父dataSet变量
	 * 
	 */
	protected void recoverParentDataSet() {
		if (stack.size() > 0) {
			PagerDataSet dataSet = (PagerDataSet) stack.peek();
			this.pageContext.setAttribute(dataSet.getDataSetName(), dataSet);
			this.pageContext.setAttribute(dataSet.getRowidName(), dataSet.getRowid() + "");
		}
	}

	protected int index() {
		return index;
	}

	/**
	 * 根据index获取相应位置上的
	 * 
	 * @param index
	 * @return PagerDataSet
	 */
	public PagerDataSet getPagerDataSet(int index) {
		HttpServletRequest request = this.getHttpServletRequest();
		java.util.Stack stack = (java.util.Stack) request
				.getAttribute(PagerDataSet.PAGERDATASET_STACK);
		return (PagerDataSet) stack.elementAt(index);
	}

	/**
	 * 搜索页面上的数据集，如果指定了索引则获取索引所对应的数据集，否则返回离标签obj最近的数据集
	 * 
	 * @param obj
	 * @param clazz
	 * @return PagerDataSet
	 */
	protected PagerDataSet searchDataSet(Tag obj, Class clazz) {
		PagerDataSet dataSet = null;
		if (this.getIndex() < 0) {
			dataSet = (PagerDataSet) findAncestorWithClass(obj, clazz);
		} else {
			dataSet = getPagerDataSet(getIndex());
			// java.util.Stack stack =
			// (java.util.Stack) request.getAttribute(
			// PagerDataSet.PAGERDATASET_STACK);
			// dataSet = (PagerDataSet) stack.elementAt(getIndex());
		}
		return dataSet;
	}
    public static int consumeCookie(String cookieid,int defaultsize,HttpServletRequest request,PagerContext pagerContext) {
    	if(pagerContext != null && pagerContext.getId() != null)
    		return RequestContext.consumeCookie(  cookieid,  defaultsize,  request,pagerContext.getId());//return RequestContext.isCookieForThisPagerTag(cookie, cookieid, pagerContext.getId());
		else
		{
			return RequestContext.consumeCookie(  cookieid,  defaultsize,  request,null);
		}
		
	}
	
	
	
	
	
    public static  Cookie[] getPageCookies(HttpServletRequest request) {
//		HttpServletRequest request = this.getHttpServletRequest();
//        HttpSession session = request.getSession(false);
		 
		return RequestContext.getPageCookies(request);
	}
	
	public static  boolean isPagerCookie(final Cookie cookie) {
		return RequestContext.isPagerCookie(cookie)	;
	}
	
	public static  boolean isCookieForThisPagerTag(final Cookie cookie,String cookieid,PagerContext pagerContext) {
		
		if(pagerContext != null && pagerContext.getId() != null)
			return RequestContext.isCookieForThisPagerTag(cookie, cookieid, pagerContext.getId());
		else
		{
			return RequestContext.isCookieForThisPagerTag(cookie, cookieid, null);
		}
	}
	

	/**
	 * 初始化，pagerContext
	 * 
	 * @throws LoadDataException
	 */
	public void init() throws LoadDataException {
		// 初始化页面上下文信息
		// if(this.origineTag == null)
		HttpServletRequest request = this.getHttpServletRequest();
		HttpServletResponse response = this.getHttpServletResponse();
		this.pagerContext = new PagerContext(request, response,
				this.pageContext, this);
		// else
		// this.pagerContext = new PagerContext(this.request, this.response,
		// this.pageContext,this.origineTag);

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
		pagerContext.setMoreQuery(moreQuery);
		pagerContext.setIsList(this.isList);
		pagerContext.setField(this.field);
		pagerContext.setForm(this.form);
		pagerContext.setId(this.getId());
		pagerContext.setNavindex(this.navindex);
		pagerContext.setPromotion(this.promotion);
		pagerContext.setScope(this.scope);
		pagerContext.setTitle(this.title);
		pagerContext.setMaxIndexPages(this.maxIndexPages);
		pagerContext.setMaxItems(this.maxItems);
		
		
//		Object temp = null;
//		if (requestKey != null) {			
//				temp = request.getAttribute(requestKey);
//		}
//
//		else if (sessionKey != null)
//		{
//			temp = session.getAttribute(sessionKey);
//		}
//		else if (pageContextKey != null) {
//			temp = pageContext.getAttribute(pageContextKey);
//		}

		String baseUri = request.getRequestURI();
		boolean isControllerPager = PagerContext.isPagerMehtod(request); 
		String cookieid = null;
		if(isControllerPager)
		{
//			baseUri = PagerContext.getPathwithinHandlerMapping(request);
			baseUri = PagerContext.getHandlerMappingRequestURI(request);
//			String mappingpath = PagerContext.getHandlerMappingPath(request); 
//			cookieid = this.pagerContext.getId() == null ?COOKIE_PREFIX + mappingpath :COOKIE_PREFIX + mappingpath + "|" +this.pagerContext.getId();
			cookieid = PagerContext.getControllerCookieID(request);
			pagerContext.setUrl(baseUri);
//			ListInfo mvcinfo = (ListInfo)temp;
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
		
			int defaultSize = consumeCookie(cookieid,maxPageItems,request,pagerContext);
			pagerContext.setMaxPageItems(defaultSize);
			pagerContext.setCustomMaxPageItems(maxPageItems);
		}
		pagerContext.setCookieid(cookieid);
		
		pagerContext.setWapflag(this.wapflag);
		pagerContext.setWidth(this.width);
		pagerContext.setIsOffset(this.isOffset);
		pagerContext.setDbname(this.dbname);
		pagerContext.setStatement(this.statement);
		SQLExecutor sqlExecutor = (SQLExecutor)request.getAttribute(sqlparamskey);
		pagerContext.setSQLExecutor(sqlExecutor);
//		pagerContext.setPretoken(pretoken);
//		pagerContext.setEndtoken(endtoken);
		
		pagerContext.setData(this.data);
		
		pagerContext.setIndex(this.index);
		pagerContext.setColName(this.colName);
		pagerContext.setProperty(this.property);
		pagerContext.setRequestKey(this.requestKey);
		pagerContext.setSessionKey(this.sessionKey);
		pagerContext.setPageContextKey(this.pageContextKey);
		

		pagerContext.setUri();
		pagerContext.setContainerid(this.getContainerid());
        pagerContext.setSelector(this.getSelector());
		// params = 0;
		// offset = 0;
		// itemCount = 0;

		// 设置是否是升序还是降序
		String desc_key = pagerContext.getKey("desc");

		String t_desc = request.getParameter(desc_key);
		boolean desc = false;
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

		// pagerContext.setDataInfo();
		pagerContext.setActual(this.actual);
		pagerContext.init();
		// if(!this.isList)
		// {
		// this.request.setAttribute(this.getId(),pagerContext);
		// }
	}

	public int doStartTag() throws JspException {
		super.doStartTag();
		push();
		setVariable();
		this.formulas = new HashMap();
		/**
		 * 支持内容管理系统得发布的需要
		 */
		if (this.pagerContext == null) {
			try {
				init();
				
			} catch (LoadDataException e) {
				if(e.getCause() == null)
					log.debug(e.getMessage());
				else
					log.debug(e.getCause().getMessage());
				return SKIP_BODY;
			}
			catch (Throwable e) {
				if(e.getCause() == null)
					log.debug(e.getMessage());
				else
					log.debug(e.getCause().getMessage());
				return SKIP_BODY;
			}
		}
		
		
		
//		flag = true;
		// setMeta();

		// else //如果不是嵌套列表，则是普通的分页/列表显示
		// {

		initField(pagerContext.getFields());

		DataInfo dataInfo = pagerContext.getDataInfo();
		if (dataInfo == null)
			return SKIP_BODY;
		doDataLoading(false);
		int size = this.size();
		if(size > 0)
		{
			if(start > 0)
			{
				if(start < size)
				{
					this.rowid = start;
				}
				else
				{
					return SKIP_BODY;
				}
			}
			initCurrentValueObject();
			this.putVarValue();
			this.putListScoptVarValue();
			return EVAL_BODY_INCLUDE;
			
		}
		else
			return SKIP_BODY;

	}
	public void initCurrentValueObject()
	{
		this.currentValueObject = this.getClassDataValue(rowid);
	}
	public void doDataLoading()
	{
		doDataLoading(true);
	}
	/**
	 * cms文档发布时，需要在doDataLoading中初始化当前记录对象，否则会报异常
	 * @param initcurrentObject
	 */
	public void doDataLoading(boolean initcurrentObject) {
		/**
		 * 得到页面上要显示的值对象中字段
		 * 
		 */
		DataInfo dataInfo = this.pagerContext.getDataInfo();
		try {
			if (dataInfo instanceof ObjectDataInfoImpl) {
				Object data = dataInfo.getObjectData();
				if (data instanceof Collection)
					loadClassData((Collection) data);
				else if (data instanceof Iterator)
					loadClassData((Iterator) data);
				else if (data instanceof Map) {
					if(!(this instanceof MapTag))
					{
						this.loadClassData((Map) data, null);
					}
					else
					{
						loadMapClassData((Map) data,(MapTag)this);
					}
				} 
				else if (data instanceof Map[]) // 如果集合是一个对象数组，则调用加载对象数组的方法
				{
					loadClassData((Map[]) data, null, false);
				} 
				else if (data instanceof Object[]) // 如果集合是一个对象数组，则调用加载对象数组的方法
				{
					loadClassData((Object[]) data);
				} else {
					loadClassData(data, data.getClass());
				}
			}
			// else if(dataInfo instanceof DefaultDataInfoImpl)
			// {
			// loadClassData(dataInfo);
			// }

			else if (dataInfo instanceof DataInfo)
				loadClassData(dataInfo, pagerContext.ListMode());
			// /**
			// * 以下的代码对取到的数据（及当前页面数据）进行排序
			// */
			// sortKey = pagerContext.getSortKey();

		}  catch (LoadDataException e) {
			if(e.getCause() == null)
				log.warn(e.getMessage());
			else
				log.warn(e.getCause().getMessage());
//			return SKIP_BODY;
		}
		

		if (size() > 0) {
			
			/**
			 * 以下的代码对取到的数据进行排序
			 */
			// 获取标签本身设置的排序码
			boolean t_desc = true;
			if (desc == null && pagerContext != null) // 如果标签本身没有设置排序顺序并且存在pager标签，则从pager标签中获取排序顺序
				t_desc = pagerContext.getDesc();
			if (desc != null) {
				t_desc = new Boolean(desc).booleanValue();
			}
			if (sortKey == null && pagerContext != null)
				sortKey = pagerContext.getSortKey();
			if (sortKey != null && autosort  )
			{
				sortBy(sortKey.trim(), t_desc);
			}
			
			if(initcurrentObject)
			{
				this.initCurrentValueObject();
			}
//			return EVAL_BODY_INCLUDE;
		} 
		else
			rowid = -1;
	}
	
	private void putVarValue()
	{
		if(currentValueObject != null)
		{
			if(this.var != null)
				request.setAttribute(var, currentValueObject.getValueObject());
			if(this.loopvar != null)
				request.setAttribute(loopvar, this.getRowid());
			if(this.mapkeyvar != null)
				request.setAttribute(mapkeyvar, this.getMapKey());
			
		}
	}
	
	private void putListScoptVarValue()
	{	
		if(pagesizevar != null)
			request.setAttribute(this.pagesizevar, this.getPageSize());
		if(offsetvar != null)
			request.setAttribute(this.offsetvar, this.getOffset());
		if(rowcountvar != null)
			request.setAttribute(this.rowcountvar, this.getRowcount());
		
	}
	private void removeVarValue()
	{
		if(this.var != null )
		{
			request.removeAttribute(var);
		}
		if(this.loopvar != null)
		{
			request.removeAttribute(loopvar);
		}
		if(this.pagesizevar != null)
		{
			request.removeAttribute(pagesizevar);
		}
		if(this.rowcountvar != null)
		{
			request.removeAttribute(rowcountvar);
		}
		if(this.offsetvar != null)
		{
			request.removeAttribute(offsetvar);
		}
		if(this.mapkeyvar != null)
		{
			request.removeAttribute(mapkeyvar);
		}
		 
	}

	/**
	 * 动态够建该dataSet的元模型
	 */
	protected void setMeta() {
		if (isExportMeta()) {
			if (dataModel == null) {
				// 保存元数据信息
				dataModel = new DataModel();
				dataModel.setField(this.getColName());
				dataModel.setIndex(this.getIndex());
				dataModel.setProperty(this.getProperty());
			}
			// /**
			// * 保存dataModel
			// */
			// if(!dataModel.isHasAdded())
			// {
			// //如果没有最上层的dataSet元数据则在pagerContext中添加添加，
			// //否则直接搜索父dataSet元数据，将dataModel添加到其中
			// if(pagerContext != null &&
			// !pagerContext.getMetaDatas().hasDataModel())
			// {
			// pagerContext.getMetaDatas().addDataModel(dataModel);
			// dataModel.setHasAdded(true);
			// }
			// else
			// {
			// //直接保存到上一级dataSet的元模型中
			// PagerDataSet data_father =
			// (PagerDataSet)findAncestorWithClass(this, PagerDataSet.class);
			// data_father.getDataModel().getMetaDatas().addDataModel(dataModel);
			// dataModel.setHasAdded(true);
			// }
			//
			// }

		}
	}

	public void setVariable() {
		pageContext.setAttribute(this.getDataSetName(), this);
		pageContext.setAttribute(this.getRowidName(), rowid + "");
		
	}

	

	public int doAfterBody() {
		if (this.rowid < this.size() - 1) {
			rowid++;
//			pageContext.setAttribute(this.getDataSetName(), this);
			pageContext.setAttribute(this.getRowidName(), rowid + "");
//			this.currentValueObject = this.getClassDataValue(rowid);
			this.initCurrentValueObject();
			putVarValue();
			return EVAL_BODY_AGAIN;
		} else {
			this.currentValueObject = null;
			removeVarValue();
			return SKIP_BODY;
		}
	}

	/**
	 * @return 排序关键字
	 */
	public String getSortKey() {

		return sortKey;
	}

	/**
	 * @param string
	 */
	public void setSortKey(String string) {
		// sortKey = request.getParameter("sortKey");
		// if (sortKey == null && string != null)
		sortKey = string;
	}

	/**
	 * @return 数据集的排序顺序，desc降序，asc升序
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param string
	 */
	public void setDesc(String string) {
		desc = string;
	}

	/**
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @return 返回属性名称
	 */
	public String getColName() {
		return colName;
	}

	// /**
	// * @return
	// */
	// public String getProperty() {
	// return property;
	// }

	/**
	 * @param string
	 */
	public void setColName(String string) {

		colName = string;
	}

	// /**
	// * @param string
	// */
	// public void setProperty(String string) {
	// property = string;
	// }

	/**
	 * @return 数据集对应的子属性名称
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param string
	 */
	public void setProperty(String string) {
		property = string;
	}
	
	public int doEndTag() throws JspException {
//		System.out.println("this=" + this);
//		System.out.println("dataSet doendtag();");
//		System.out.println("flag=" + flag);
		
		return super.doEndTag();

	}

	public void removeVariable() {
		// if(id != null && !id.trim().equals(""))
		// {
		// pageContext.removeAttribute("dataSet_" + id);
		// pageContext.removeAttribute("rowid_" + id);
		// }
		// else
		{
			pageContext.removeAttribute(this.getDataSetName(),PageContext.PAGE_SCOPE);
			pageContext.removeAttribute(this.getRowidName(),PageContext.PAGE_SCOPE);
		}
	}
	
	/**
	 * 内容管理时使用的方法，释放环境变量和还原容器的内容
	 */
	public void cmsClear()
	{
		removeVariable();
		theClassDataList = null;
		// if(index == null || index.trim().equals(""))

		rowid = 0;
	}

	/**
	 * 
	 * Description:重置对象theClassDataList和行号rowid 否则嵌套使用情况下会出错 void
	 */
	public void clear() {
		theClassDataList = null;
		// if(index == null || index.trim().equals(""))
		position = -1;
		rowid = 0;
		start = 0;
//		if(flag)
		pop();
		HttpServletRequest request = this.getHttpServletRequest();
		HttpSession session = request.getSession(false);
		
		// 清除缓冲
		if (getNeedClear()) {
			if (requestKey != null)
				request.removeAttribute(requestKey);
			else if (session != null && sessionKey != null)
				session.removeAttribute(sessionKey);
			else if (pageContextKey != null)
				pageContext.removeAttribute(pageContextKey,PageContext.APPLICATION_SCOPE);

			/**
			 * 这个条件可能不成立，因为web容器可能对request做了相应的wraper封装，
			 * 要想得到实际的request需要通过特定的方法进行查找
			 */
			if (!(request instanceof CMSServletRequest))
				this.pagerContext = null;
		}

	}

	/**
	 * 获取数据集索引
	 * 
	 * @return 数据集索引
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param string
	 */
	public void setIndex(int string) {
		index = string;
	}

	/**
	 * 数据集在pageContext中的缓冲名称
	 * 
	 * @return String
	 */
	public String getPageContextKey() {
		return pageContextKey;
	}

	/**
	 * 数据集在request中的缓冲名称
	 * 
	 * @return String
	 */
	public String getRequestKey() {
		return requestKey;
	}

	/**
	 * 数据集在session中的缓冲名称
	 * 
	 * @return String
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * @param string
	 */
	public void setPageContextKey(String string) {
		pageContextKey = string;
	}

	/**
	 * @param string
	 */
	public void setRequestKey(String string) {
		requestKey = string;
	}

	/**
	 * @param string
	 */
	public void setSessionKey(String string) {
		sessionKey = string;
	}

	/**
	 * 获取是否需要自动清楚数据集缓冲
	 * 
	 * @return boolean false不需要，true需要
	 */
	public boolean getNeedClear() {

		return needClear;
	}

	/**
	 * @param string
	 */
	public void setNeedClear(boolean string) {
		needClear = string;
	}

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
	 * @return Returns the dataModel.
	 */
	public DataModel getDataModel() {
		return dataModel;
	}

	/**
	 * @param dataModel
	 *            The dataModel to set.
	 */
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**
	 * 求和函数
	 * 
	 * @param colName
	 * @return Object 结果
	 */
	public Object sum(String colName) throws FormulaException {
		int value_i = 0;
		short value_s = 0;
		long value_l = 0;
		double value_d = 0;
		float value_f = 0;
		Object left = null;
		// 数据类型：0－－整形
		// 数据类型：1－－浮点形
		int type = 0;
		boolean start = false;
		for (int i = 0; i < this.size(); i++) {
			left = this.getValue(i, colName);
			if (left == null)
				throw new FormulaException("attribute '" + colName
						+ "' is null!");
			if (!start) {
				if (int.class.isInstance(left)
						|| Integer.class.isInstance(left)) {
					type = 0;
					value_i += ((Integer) left).intValue();
				} else if (short.class.isInstance(left)
						|| Short.class.isInstance(left)) {
					type = 1;
					value_s += ((Short) left).shortValue();
				} else if (long.class.isInstance(left)
						|| Long.class.isInstance(left)) {
					type = 2;
					value_l += ((Long) left).longValue();
				} else if (double.class.isInstance(left)
						|| Double.class.isInstance(left)) {
					type = 3;
					value_d += ((Double) left).doubleValue();
				} else if (float.class.isInstance(left)
						|| Float.class.isInstance(left)) {
					type = 4;
					value_f += ((Float) left).floatValue();
				}
				start = true;
			}
			switch (type) {
			case 0:
				value_i += ((Integer) left).intValue();
				break;
			case 1:
				value_s += ((Short) left).shortValue();
				break;
			case 2:
				value_l += ((Long) left).longValue();
				break;
			case 3:
				value_d += ((Double) left).doubleValue();
				break;
			case 4:
				value_f += ((Float) left).floatValue();
				break;
			}
		}

		switch (type) {
		case 0:
			return new Integer(value_i);
		case 1:
			return new Short(value_s);
		case 2:
			return new Long(value_l);
		case 3:
			return new Double(value_d);
		case 4:
			return new Float(value_f);
		default:
			throw new FormulaException("attribute '" + colName
					+ "' must be a number!");
		}
	}

	/**
	 * 求和函数
	 * 
	 * @param colName
	 * @param property
	 * @return Object 结果
	 */
	public Object sum(String colName, String property) throws FormulaException {
		int value_i = 0;
		short value_s = 0;
		long value_l = 0;
		double value_d = 0;
		float value_f = 0;
		Object left = null;
		// 数据类型：0－－整形
		// 数据类型：1－－浮点形
		int type = 0;
		boolean start = false;
		for (int i = 0; i < this.size(); i++) {
			left = this.getValue(i, colName, property);

			if (!start) {
				if (int.class.isInstance(left)
						|| Integer.class.isInstance(left)) {
					type = 0;
					value_i += ((Integer) left).intValue();
				} else if (short.class.isInstance(left)
						|| Short.class.isInstance(left)) {
					type = 1;
					value_s += ((Short) left).shortValue();
				} else if (long.class.isInstance(left)
						|| Long.class.isInstance(left)) {
					type = 2;
					value_l += ((Long) left).longValue();
				} else if (double.class.isInstance(left)
						|| Double.class.isInstance(left)) {
					type = 3;
					value_d += ((Double) left).doubleValue();
				} else if (float.class.isInstance(left)
						|| Float.class.isInstance(left)) {
					type = 4;
					value_f += ((Float) left).floatValue();
				}
				start = true;
			}
			switch (type) {
			case 0:
				value_i += ((Integer) left).intValue();
				break;
			case 1:
				value_s += ((Short) left).shortValue();
				break;
			case 2:
				value_l += ((Long) left).longValue();
				break;
			case 3:
				value_d += ((Double) left).doubleValue();
				break;
			case 4:
				value_f += ((Float) left).floatValue();
				break;
			}
		}

		switch (type) {
		case 0:
			return new Integer(value_i);
		case 1:
			return new Short(value_s);
		case 2:
			return new Long(value_l);
		case 3:
			return new Double(value_d);
		case 4:
			return new Float(value_f);
		default:
			throw new FormulaException("attribute '" + colName + "." + property
					+ "' must be a number!");
		}
	}

	/**
	 * 求平均值
	 * 
	 * @param colName
	 * @return float
	 */
	public float avg(String colName) throws FormulaException {
		Object left = this.sum(colName);
		float count = (float) this.count(colName);
		if (int.class.isInstance(left) || Integer.class.isInstance(left)) {
			return ((Integer) left).floatValue() / count;
		} else if (short.class.isInstance(left) || Short.class.isInstance(left)) {
			return ((Short) left).floatValue() / count;
		} else if (long.class.isInstance(left) || Long.class.isInstance(left)) {
			return ((Long) left).floatValue() / count;
		} else if (double.class.isInstance(left)
				|| Double.class.isInstance(left)) {

			return ((Double) left).floatValue() / count;
		} else if (float.class.isInstance(left) || Float.class.isInstance(left)) {
			return ((Float) left).floatValue() / count;
		}
		return 0.0f;

	}

	/**
	 * 求平均值
	 * 
	 * @param colName
	 * @param property
	 * @return float
	 */

	public float avg(String colName, String property) throws FormulaException {
		Object left = this.sum(colName, property);
		float count = (float) this.count(colName, property);
		if (int.class.isInstance(left) || Integer.class.isInstance(left)) {
			return ((Integer) left).floatValue() / count;
		} else if (short.class.isInstance(left) || Short.class.isInstance(left)) {
			return ((Short) left).floatValue() / count;
		} else if (long.class.isInstance(left) || Long.class.isInstance(left)) {
			return ((Long) left).floatValue() / count;
		} else if (double.class.isInstance(left)
				|| Double.class.isInstance(left)) {

			return ((Double) left).floatValue() / count;
		} else if (float.class.isInstance(left) || Float.class.isInstance(left)) {
			return ((Float) left).floatValue() / count;
		}
		return 0.0f;

	}

	/**
	 * 计数功能
	 * 
	 * @param colName
	 * @return int
	 */
	public int count(String colName) {
		return this.size();
	}

	/**
	 * 技术功能
	 * 
	 * @param colName
	 * @param property
	 * @return int
	 */
	public int count(String colName, String property) {
		return this.size();
	}

	/**
	 * 获取formula所对应的 表达式对象
	 * 
	 * @param formula
	 * @return Formula
	 */
	public Formula getFormula(String formula) {
		if (formula == null)
			return null;
		
		if(this.formulas == null)
			this.formulas = new HashMap();
		Formula f = (Formula) formulas.get(formula);
		if (f != null)
		{
//			f.setDataSet(this);
			return f;
		}
		f = new Formula(this, formula);
		formulas.put(formula, f);
		return f;
		
	}

	public boolean isDeclare() {
		return declare;
	}

	public void setDeclare(boolean declare) {
		this.declare = declare;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getNavindex() {
		return navindex;
	}

	public void setNavindex(String navindex) {
		this.navindex = navindex;
	}

	public boolean isOffset() {
		return isOffset;
	}

	public void setOffset(boolean isOffset) {
		this.isOffset = isOffset;
	}

	public int getItems() {
		return items;
	}

	public void setItems(int items) {
		this.items = items;
	}

	public int getMaxIndexPages() {
		return maxIndexPages;
	}

	public void setMaxIndexPages(int maxIndexPages) {
		this.maxIndexPages = maxIndexPages;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isWapflag() {
		return wapflag;
	}

	public void setWapflag(boolean wapflag) {
		this.wapflag = wapflag;
	}

	public void setIsList(boolean isList) {
		this.isList = isList;
	}

	

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		if(!dataSetName.equals("") && !dataSetName.equals("null"))
			this.dataSetName = dataSetName;
	}

	public String getRowidName() {
		return rowidName;
	}

	public void setRowidName(String rowidName) {
		if(!rowidName.equals("") && !rowidName.equals("null"))
			this.rowidName = rowidName;
	}
	
	/**
	 * 获取外部展示的行号
	 * @param offset 是否使用偏移量，如果使用则从当前页面的起始位置开始，否则从0开始
	 * @param increament 偏移量的自增值
	 * @return
	 */
	public int getOuterRowid(boolean offset,int increament)
	{
		
		PagerContext pagerContext = getPagerContext();
		if(pagerContext == null)
			return -1;
		if(offset)
			return getRowid() + increament;
		else
			return  (int)(pagerContext.getOffset() + getRowid() + increament);
	}
	
	/**
	 * 获取当前行行号，每页都从零开始
	 * @return
	 */
	public int getRowid() {
//		PagerContext pagerContext = getPagerContext();
		if(this.pagerContext == null)
		{
			if(stack.size() == 0)
				return -1;
			PagerDataSet dataSet = (PagerDataSet)stack.peek();
			return dataSet.getRowid();
			
		}
		return this.rowid;
	}
	/**
	 * 获取嵌套list索引号对应的list的外部展示行号
	 * @param index 嵌套list索引 号	
	 * @return
	 */
	public int getRowidByIndex(int index) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return -1;
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);
		return dataSet.getRowid();
	}
	
	/**
	 * 获取嵌套list索引号对应的list的外部展示行号
	 * @param index 嵌套list索引 号
	 * @param offset 是否使用偏移量，如果使用则从当前页面的起始位置开始，否则从0开始
	 * @param increament 偏移量的自增值
	 * @return
	 */
	public int getOutRowidByIndex(int index,boolean offset,int increament) {
		if (index >= stack.size() || index < 0) {
			log.debug("Get [" + colName + "] error: Out of index[" + index
					+ "],stack size is " + stack.size());
			return -1;
		}
		// 根据索引获取字段的值
		PagerDataSet dataSet = (PagerDataSet) this.stack.elementAt(index);		
		return dataSet.getOuterRowid( offset, increament);
	}
	
	
	/**
	 * 获取页面的记录起始地址
	 * @return
	 */
	public int getOffset()
	{
		PagerContext pagerContext = getPagerContext();
		if(pagerContext == null)
			return -1;
		return (int)this.pagerContext.getOffset();
	}

	public boolean isPromotion() {
		return promotion;
	}

	public void setPromotion(boolean promotion) {
		this.promotion = promotion;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public boolean isAutosort() {
		return autosort;
	}

	public void setAutosort(boolean autosort) {
		this.autosort = autosort;
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

	
	public Object getActual()
	{
	
		return actual;
	}

	
	public void setActual(Object actual)
	{
	
		this.actual = actual;
	}

	public boolean isSoftparser() {
		return softparser;
	}

	public void setSoftparser(boolean softparser) {
		this.softparser = softparser;
	}
    
	
	public boolean usedwithpagerTag()
	{
		return this.requestKey == null && this.pageContextKey == null && this.sessionKey == null && this.statement == null
				&& this.colName == null /*begin added by biaoping.yin on 2015.3.9 because list data can be setted by actual*/&& this.actual == null/*end added by biaoping.yin on 2015.3.9*/;
	}

	public boolean isMoreQuery() {
		return moreQuery;
	}

	public void setMoreQuery(boolean moreQuery) {
		this.moreQuery = moreQuery;
	}

	@Override
	public void doFinally() {
		try
		{
			this.removeVariable();
			clear();
	//		if(flag)
			recoverParentDataSet();
	//		flag = false;
			//if(declare)
			
			this.declare = true;
	
			this.formulas = null;
			this.sortKey = null;
			this.desc = null;
	//		pretoken= null;
	//        endtoken= null;
			this.sqlparamskey = "sql.params.key";
			/**
			 * added by biaoping.yin on 20080912 start.
			 */
			this.autosort = false;
			this.needClear = false;
			sessionKey = null;
			this.actual = null;
	
			requestKey = null;
	
			pageContextKey = null;
			this.containerid = null;
	        this.selector = null;
	        this.moreQuery = false;
	        
	        //begin clear some field by biaoping.yin on 2015.3.8
	        this.colName = null;
	        this.property = null;
	        this.softparser = true;
	        this.type = "";
	        rowidName = "rowid";
	    	dataSetName = "dataSet";
	    	this.index = -1;
	    	this.statement = null;
	    	this.dbname = null;
	    	this.currentValueObject = null;
	        //end clear some field by biaoping.yin on 2015.3.8
			/**
			 * added by biaoping.yin on 20080912 end.
			 */		
			if(formulas != null)
			{
				this.formulas.clear();
				formulas = null;
			}
			this.removeVarValue();
			this.var = null;
			this.loopvar = null;
			this.pagesizevar = null;
			this.rowcountvar = null;
			this.offsetvar = null;
			this.mapkeyvar = null;
			
		}
		catch(Exception e)
		{
			
		}
		
		
		super.doFinally();
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}


	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getMapkeyvar() {
		return mapkeyvar;
	}

	public void setMapkeyvar(String mapkeyvar) {
		this.mapkeyvar = mapkeyvar;
	}

    

}
