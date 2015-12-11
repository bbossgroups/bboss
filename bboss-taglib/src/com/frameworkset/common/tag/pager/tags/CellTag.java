/**
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
package com.frameworkset.common.tag.pager.tags;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.model.Field;
import com.frameworkset.common.tag.pager.model.Formula;
import com.frameworkset.common.util.ValueObjectUtil;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessorInf;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessorUtil;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.util.RegexUtil;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;
/**
 * @author biaoping.yin
 * 显示一个数据的tag
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CellTag  extends PagerTagSupport {
	protected Object actual;
	protected boolean actualseted = false;
	protected String requestKey;
	protected String sessionKey;
	protected String pageContextKey;
	protected String parameter;
	private boolean currentcelltoColName=false;
	private String usecurrentCellValuetoCellName;
	private String locale;
	private String timeZone = null;
	private boolean userRequestLocale;
	
	public String getRequestKey()
	{
	
		return requestKey;
	}

	
	public void setRequestKey(String requestKey)
	{
	
		this.requestKey = requestKey;
	}

	
	public String getSessionKey()
	{
	
		return sessionKey;
	}

	
	public void setSessionKey(String sessionKey)
	{
	
		this.sessionKey = sessionKey;
	}

	
	public String getPageContextKey()
	{
	
		return pageContextKey;
	}

	
	public void setPageContextKey(String pageContextKey)
	{
	
		this.pageContextKey = pageContextKey;
	}

	
	public String getParameter()
	{
	
		return parameter;
	}

	
	public void setParameter(String parameter)
	{
	
		this.parameter = parameter;
	}
    private static Logger log = Logger.getLogger (CellTag.class);
    /**
     * 记录cell的元模型元素
     */
    private Field metafield;

	/**
	 * 列名
	 */
	private String colName;

	/**
	 * 祖先索引
	 */
	protected int index = -1;

	/**
	 * 输出日期格式
	 */
	private String dateformat;

	/**
	 * 输出数字格式
	 */
	private String numerformat;
	/**
	 * 属性名称
	 */
	private String property;
	/**
	 * 指定属性输出内容
	 */
	private String content;
	/**
	 * 列的索引
	 */
	private int colid = -1;

	protected Object defaultValue;

	/**
	 * 计算公式
	 */
	private String expression;

	protected Formula t_formula;

	/**
	 * 是否进行url编码
	 */
	private String encode;
	
	/**
	 * 编码次数，连续编码次数
	 */
	private int encodecount = 1;
	

	/**
	 * 是否进行url解码
	 */
	private String decode;
	
	protected int maxlength = -1;
	
	protected String replace ;
	
	/**
	 * true
	 * false
	 */
	protected boolean trim ;
	/**
	 * 包含cell标签的dataSet
	 */
	protected PagerDataSet dataSet;
	protected PagerDataSet currentDataSet;
	
	protected boolean htmlEncode = false;
	
	protected boolean htmlDecode = false;
	
	
	/**
	 * 标识是否对标签属性对应的值进行发布，内容管理系统中使用的标签
	 * true-发布
	 * false-不发布
	 * 主要是针对内容中对应的链接和附件的发布和路径修改
	 */
	protected boolean publish = false;
	

	//protected boolean isLogicoperation = false;

	protected void init()
	{
	    dataSet = searchDataSet(this,PagerDataSet.class);
        if(dataSet != null)
        {
        	try{
        		t_formula = dataSet.getFormula(getExpression());
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        	}
//            //设置报表元模型
//            setMeta();
        }
        else
        {
        	try
        	{
        		HttpServletRequest request = getHttpServletRequest();
//        		HttpSession session = request.getSession(false) ;
	        	CMSServletRequest cmsRequest = InternalImplConverter.getInternalRequest(request);
	        	if( cmsRequest != null)
	        	{
//		        	CMSServletRequest internalRequest = (CMSServletRequest)request;
		        	dataSet = (PagerDataSet)cmsRequest.getAttribute("dataset." + cmsRequest.getContext().getID());
	        	}
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();
        		
        	}
        }
       
	}
	

	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#generateContent()
	 */
	public String generateContent() {

		return null;
	}
	/* (non-Javadoc)
	 * @see com.frameworkset.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output) {

	}
	protected String getStringValue()
	{
		String outStr =   this.getOutStr(); 
		return outStr;
	}
	public int doStartTag() throws JspException {
		//super.doStartTag();
	    //初始化dataSet
		
	    init();
	   
		String outStr =  this.getOutStr();
		if(outStr != null)
		{
			if(getEncode() != null && getEncode().equals("true"))
			{
//				if(this.encodecount == 1)
//					try {
//						outStr = URLEncoder.encode(outStr,"UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				else if(this.encodecount == 2)
//					try {
//						outStr = URLEncoder.encode(URLEncoder.encode(outStr,"UTF-8"),"UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				else if(this.encodecount == 3)
//					try {
//						outStr = URLEncoder.encode(URLEncoder.encode(URLEncoder.encode(outStr,"UTF-8"),"UTF-8"),"UTF-8");
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				outStr = SimpleStringUtil.urlencode(outStr, "UTF-8",encodecount);
			}
			
			if(getDecode() != null && getDecode().equals("true"))
				outStr = URLDecoder.decode(outStr);
		}
		try { 
			/**
			 * 如果需要对输出的内容进行发布则执行发布的过程
			 */
			if(this.isPublish())
			{
				
				String encoding = "";
//				CmsLinkProcessorInf processor = new CmsLinkProcessor(null,
//						CmsLinkProcessorInf.REPLACE_LINKS,
//						  encoding);
				CmsLinkProcessorInf processor = CmsLinkProcessorUtil.getCmsLinkProcessor(null,
						CmsLinkProcessorInf.REPLACE_LINKS,
						  encoding);
				
				processor.setHandletype(CmsLinkProcessorInf.PROCESS_CONTENT);
				content = processor.process(content,encoding);
			}
			
				
			outStr = StringUtil.getHandleString(maxlength,replace,htmlEncode,htmlDecode,outStr);
//			if(this.maxlength > 0 && outStr != null && outStr.length() > maxlength)
//			{
//				outStr = outStr.substring(0,this.maxlength);
//				if(replace != null)
//					outStr += replace;
//			}
//			if(this.htmlEncode)
//			{
//				out.print(StringUtil.HTMLEncode(outStr));
//			}
//			else if(this.htmlDecode)
//			{
//				out.print(StringUtil.HTMLEncodej(outStr));
//			}
//			else
//			{
//				out.print(outStr);
//			}
			if(outStr != null)
				this.getJspWriter().print(outStr);
			else
				this.getJspWriter().print("");
			

		} catch (Exception e) {
			throw new JspException(e.getMessage());
		}
		return SKIP_BODY;
	}
	
	
	
	

	protected void setMeta()
	{
	    if(isExportMeta())
		{
		    if(metafield == null)
		    {

		        metafield = new Field();
		        //设置字段名称，如果指定的是列索引，通过索引查找字段的名称
		        if(this.getColid() == -1)
		            metafield.setField(this.getColName());
		        else
		            metafield.setField(fieldHelper.getFields()[getColid()]);
		        //设置字段id
		        metafield.setColid(getColid());
		        //设置字段的显示标题
		        metafield.setTitle(this.fieldHelper.getTitles()[getColid()]);
		        //设置统计公式
		        if(t_formula != null)
		        {

		            //Formula t_formula = searchDataSet(this,PagerDataSet.class).getFormula(getExpression());
		            metafield.setFormula(t_formula);
		        }

		        metafield.setNumerFormat(this.getNumerformat());
		        metafield.setDateFormat(this.getDateformat());

		        PagerDataSet data_father = (PagerDataSet)findAncestorWithClass(this, PagerDataSet.class);
	            data_father.getDataModel().getMetaDatas().addMetaField(metafield);

		        metafield.setHasAdded(true);
		    }
		    else if(!metafield.isHasAdded())
		    {
		        PagerDataSet data_father = (PagerDataSet)findAncestorWithClass(this, PagerDataSet.class);
	            data_father.getDataModel().getMetaDatas().addMetaField(metafield);
		        metafield.setHasAdded(true);
		    }



		}
	}


	private String getValue(PagerDataSet dataSet, int rowid, int colid) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(rowid, colid, getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				colid,
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		return dataSet.getString(rowid, colid);

	}
	
	private Object getObjectValue(PagerDataSet dataSet, int rowid, int colid) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(rowid, colid, getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				colid,
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		return dataSet.getValue(rowid, colid);

	}
	
	private String getValue(PagerDataSet dataSet, int rowid) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(rowid, getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		Object value = dataSet.getObject(rowid);
		if(value != null)
			return String.valueOf(value);
		return null;

	}
	
	private Object getObjectValue(PagerDataSet dataSet, int rowid) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(rowid, getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		return dataSet.getObject(rowid);

	}

	/**
	 * 缺省值为对象时调用以下方法，获取输出
	 * @param obj
	 * @return String
	 */
	private String getValue(Object obj) {
	    try
	    {
		    if (this.getNumerformat() != null) {

			    NumberFormat numerFormat = DataFormatUtil.getDecimalFormat(getNumerformat());
				if (obj == null)
					return null;
				return numerFormat.format(obj);
			}

			if (this.getDateformat() != null) {
			    SimpleDateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(getDateformat());
				if (obj == null)
					return null;
				return dateFormat.format(obj);
			}
	    }
	    catch(Exception e)
	    {

	    }

		return obj.toString();

	}
	
	/**
	 * 缺省值为对象时调用以下方法，获取输出
	 * @param obj
	 * @return String
	 */
	private Object getObjectValue(Object obj) {
	    try
	    {
		    if (this.getNumerformat() != null) {

			    NumberFormat numerFormat = DataFormatUtil.getDecimalFormat(getNumerformat());
				if (obj == null)
					return null;
				return numerFormat.format(obj);
			}

			if (this.getDateformat() != null) {
			    SimpleDateFormat dateFormat = DataFormatUtil.getSimpleDateFormat(getDateformat());
				if (obj == null)
					return null;
				return dateFormat.format(obj);
			}
	    }
	    catch(Exception e)
	    {

	    }

		return obj;

	}

	private String getValue(
		PagerDataSet dataSet,
		int rowid,
		int colid,
		String property) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(
				rowid,
				colid,
				property,
				getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				colid,
				property,
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		return dataSet.getString(rowid, colid, property);

	}
	
	private Object getObjectValue(
			PagerDataSet dataSet,
			int rowid,
			int colid,
			String property) {
			if (this.getNumerformat() != null) {
				return dataSet.getFormatData(
					rowid,
					colid,
					property,
					getNumerformat());
			}

			if (this.getDateformat() != null) {
				return dataSet.getFormatDate(
					rowid,
					colid,
					property,
					getDateformat(),  locale,  userRequestLocale,  timeZone);

			}
			return dataSet.getObject(rowid, colid, property);

		}

	private String getValue(
		PagerDataSet dataSet,
		int rowid,
		String colName,
		String Property) {
		if (this.getNumerformat() != null) {
			return dataSet.getFormatData(
				rowid,
				colName,
				property,
				getNumerformat());
		}

		if (this.getDateformat() != null) {
			return dataSet.getFormatDate(
				rowid,
				colName,
				property,
				getDateformat(),  locale,  userRequestLocale,  timeZone);

		}
		return dataSet.getString(rowid, colName, property);
	}
	
	private Object getObjectValue(
			PagerDataSet dataSet,
			int rowid,
			String colName,
			String Property) {
			if (this.getNumerformat() != null) {
				return dataSet.getFormatData(
					rowid,
					colName,
					property,
					getNumerformat());
			}

			if (this.getDateformat() != null) {
				return dataSet.getFormatDate(
					rowid,
					colName,
					property,
					getDateformat(),  locale,  userRequestLocale,  timeZone);

			}
			return dataSet.getObject(rowid, colName, property);
		}

	private String getValue(PagerDataSet dataSet, int rowid, String colName) {
		if (getNumerformat() != null) {
			return dataSet.getFormatData(rowid, colName, getNumerformat());
		}

		if (getDateformat() != null) {
			return dataSet.getFormatDate(rowid, colName, getDateformat(),  locale,  userRequestLocale,  timeZone);
		}
		return dataSet.getString(rowid, colName);
	}
	
	private Object getObjectValue(PagerDataSet dataSet, int rowid, String colName) {
		if (getNumerformat() != null) {
			return dataSet.getFormatData(rowid, colName, getNumerformat());
		}

		if (getDateformat() != null) {
			return dataSet.getFormatDate(rowid, colName, getDateformat(),  locale,  userRequestLocale,  timeZone);
		}
		return dataSet.getObject(rowid, colName);
	}

	protected int index() {
		return index;
	}

	protected PagerDataSet searchDataSet(Tag obj,Class clazz) {
		PagerDataSet dataSet = null;
		if (this.getIndex() < 0) {
			dataSet =
				(PagerDataSet) findAncestorWithClass(obj, clazz);
//			if(this.currentcelltoColName || this.usecurrentCellValuetoCellName != null)
//				this.currentDataSet = dataSet;
		} else {
			//int idx = index();
			HttpServletRequest request = getHttpServletRequest();
//			HttpSession session = request.getSession(false) ;
			java.util.Stack stack =
				(java.util.Stack) request.getAttribute(
					PagerDataSet.PAGERDATASET_STACK);
			
			dataSet = (PagerDataSet) stack.elementAt(this.getIndex());
			
			if(this.currentcelltoColName || this.usecurrentCellValuetoCellName != null)
				currentDataSet =  (PagerDataSet) findAncestorWithClass(obj, clazz);
		}
		
		return dataSet;
	}

	/**
	 * 包含标签库属性变量的表达式匹配公式，其他的表达式将不能使用本表达式
	 */
	public static final String EXPRESSION_PATTERN = "\\s*\\{[^\\}]+\\}\\s*";
	/**
	 * 求一般表达式的值
	 * @param formula
	 * @return Object
	 * @throws FormulaException
	 */
	protected Object getFormulaValue(String formula) throws FormulaException
	{
		
		return dataSet.getFormula(formula).getValue();
			
		
	}
	
	
	/**
	 * 求一定包含对象属性表达式的值
	 * @param formula
	 * @return Object
	 * added on 2007.11.5 
	 */
	protected Object getFormulaValueWithAttribute(String formula) 
	{
		if(RegexUtil.isContain(formula,EXPRESSION_PATTERN))
		{
			try
			{
				return dataSet.getFormula(formula).getValue();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return formula;
			}
		}
		else
		{
			return formula;
		}			
		
	}
	
	/**
	 * 求一定包含对象属性表达式的值,首先判断formula是不是表达式,如果不是则直接获取属性formula（将其当作属性来使用）
	 * @param formula
	 * @return Object
	 * added on 2007.11.5 
	 */
	public static Object getCommonFormulaValue(PagerDataSet dataSet,int rowid,String formula) 
	{
		if(RegexUtil.isContain(formula,EXPRESSION_PATTERN))
		{
			try
			{
				return dataSet.getFormula(formula).getValue();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return dataSet.getValue(rowid,formula);
			}
		}
		else
		{
			return dataSet.getValue(rowid,formula);
		}			
		
	}
	
	
	

	/**
	 * 获取单元格的输出
	 * @return String
	 */

	protected String getOutStr() {
		String outStr = getOut();
		if(trim && outStr != null)
		{
			outStr = outStr.trim();
		}
		return outStr;
//		//PagerDataSet dataSet = this.searchDataSet(this,PagerDataSet.class);
//		//在setMeta方法中初始化t_formula对象
//		//如果设置了表达式，则通过表达式求解返回的值
//		if(this.t_formula != null)
//		{
//		    try {
//		        Object data = t_formula.getValue();
//		        if(getNumerformat() != null)
//		        {
//		            NumberFormat numerFormat = new DecimalFormat(getNumerformat());
//		            outStr = numerFormat.format(data);
//		        }
//		        outStr = data.toString();
//            } catch (FormulaException e) {
//                //System.out.println(e.getMessage());
//                log.error(e);
//            }
//		}
//		else if (this.getColid() != -1) {
//			if (getProperty() == null)
//			{
//				//outStr = dataSet.getString(dataSet.getRowid(), this.getColId());
//			    outStr = getValue(dataSet, dataSet.getRowid(), getColid());
//			}
//			else
//			    outStr =
//						getValue(
//							dataSet,
//							dataSet.getRowid(),
//							getColid(),
//							getProperty());
//		} else if (getColName() != null) {
//			if (getProperty() == null)
//			    outStr =
//						getValue(dataSet, dataSet.getRowid(), getColName());
//			else
//			    outStr =
//						getValue(
//							dataSet,
//							dataSet.getRowid(),
//							this.getColName(),
//							getProperty());
//			//outStr = dataSet.getString(dataSet.getRowid(), this.getColName());
//		} else
//			outStr = getContent();
//		if(outStr == null && getDefaultValue() != null)
//			outStr = getValue(getDefaultValue());
//		if(outStr == null)
//		    outStr = "";
//		return outStr;
	}
	private void evalColName()
	{
		if(this.index >= 0 && colName == null)
		{
			if(this.currentcelltoColName)
			{
				this.colName = String.valueOf(this.currentDataSet.getObject());
			}
			else if(this.usecurrentCellValuetoCellName != null)
			{
				this.colName = String.valueOf(this.currentDataSet.getObject(currentDataSet.getRowid(), usecurrentCellValuetoCellName));
			}
		}
	}
	protected Object getObjectValue()
	{
		return _getObjectValue(true);
	}
	/**
	 * 
	 * @param returncurrentObject 是否返回当前记录 true返回，false不返回
	 * @return
	 */
	protected Object _getObjectValue(boolean returncurrentObject)
	{
		try
		{
			
			if(this.actualseted)
			{
				Object data = (this.actual == null?getDefaultValue():this.actual);
				if (this.getNumerformat() != null) {
					return PagerDataSet.formatData(request,data,  getNumerformat());
				}
				else if (this.getDateformat() != null) {
					return PagerDataSet.formatDate(request,data, getDateformat(),  locale,  userRequestLocale,  timeZone);
				}
				else
				{
					return data;
				}
			}
			evalColName();
			if(this.requestKey == null && this.sessionKey == null && this.pageContextKey == null && parameter == null)
			{
				
				if(this.dataSet == null)
//					return getDefaultValue() != null?getObjectValue(getDefaultValue()) :null;
					return null;
				Object outStr = null;
				//		在setMeta方法中初始化t_formula对象
						//如果设置了表达式，则通过表达式求解返回的值
						if(this.t_formula != null)
						{
						    try {
						        Object data = t_formula.getValue();
						        if(data != null )
						        {
						        	if(getNumerformat() != null)
						        	{
							            
							            outStr = PagerDataSet.formatData(request,data,  getNumerformat());
						        	}
						        	else if(this.getDateformat() != null)
						        	{
						        		outStr = PagerDataSet.formatDate(request,data, getDateformat(),  locale,  userRequestLocale,  timeZone);
						        	}
						        	else
						        	{
						        		outStr = data;
						        	}
						        		
						        }
						        else
						        {
						        	
						        	outStr = data;
						        }
				            } catch (FormulaException e) {
				                //System.out.println(e.getMessage());
				                log.debug(e.getMessage());
				            }
						}
						else if (this.getColid() != -1) {
							if (getProperty() == null)
							{
								//outStr = dataSet.getString(dataSet.getRowid(), this.getColId());
							    outStr = getObjectValue(dataSet, dataSet.getRowid(), getColid());
							}
							else
							    outStr =
							    	getObjectValue(
											dataSet,
											dataSet.getRowid(),
											getColid(),
											getProperty());
						} else if (getColName() != null) {
							if (getProperty() == null)
							    outStr =
							    	getObjectValue(dataSet, dataSet.getRowid(), getColName());
							else
							    outStr =
							    	getObjectValue(
											dataSet,
											dataSet.getRowid(),
											this.getColName(),
											getProperty());
							//outStr = dataSet.getString(dataSet.getRowid(), this.getColName());
						} 
						else if(this.content != null)				
							outStr = getContent();
						else if(returncurrentObject)
							outStr = getObjectValue(dataSet, dataSet.getRowid());
						if(outStr == null && getDefaultValue() != null)
							outStr = getObjectValue(getDefaultValue());
						
						return outStr;
			}	
			else 
			{
				Object outStr = null;
//				Object temp = null;
				if(this.requestKey != null)
				{
					outStr = request.getAttribute(requestKey);
					
				}
				else if(this.sessionKey != null)
				{
					outStr = session.getAttribute(sessionKey);
					
				}
				else if(this.pageContextKey != null)
				{
					outStr = this.pageContext.getAttribute(pageContextKey);
					
				}
				else if(this.parameter != null)
				{
					outStr = this.request.getParameter(parameter);
					
				}
				if(outStr != null)
				{
					if(this.getProperty() != null)
					{
						outStr = ValueObjectUtil.getValue(outStr,this.getProperty());
					}
				}
				if(outStr == null && getDefaultValue() != null)
					outStr = getObjectValue(getDefaultValue());
				
				return outStr;

			}
			
			
			
		}
		catch(Exception e)
		{
			
			log.debug("eval cell value error:" ,e);
			return null;
		}
	}
	protected String getOut()
	{
//		try
//		{
//			if(this.dataSet == null)
//				return null;
//			String outStr = null;
//	//		在setMeta方法中初始化t_formula对象
//			//如果设置了表达式，则通过表达式求解返回的值
//			if(this.t_formula != null)
//			{
//			    try {
//			        Object data = t_formula.getValue();
//			        if(data != null) 
//			        {
//				        if(getNumerformat() != null)
//				        {
//				            NumberFormat numerFormat = new DecimalFormat(getNumerformat());
//				            outStr = numerFormat.format(data);
//				        }
//				        outStr = data.toString();
//			        }
//	            } catch (FormulaException e) {
//	                //System.out.println(e.getMessage());
//	                log.error(e);
//	            }
//			}
//			else if (this.getColid() != -1) {
//				if (getProperty() == null)
//				{
//					//outStr = dataSet.getString(dataSet.getRowid(), this.getColId());
//				    outStr = getValue(dataSet, dataSet.getRowid(), getColid());
//				}
//				else
//				    outStr =
//							getValue(
//								dataSet,
//								dataSet.getRowid(),
//								getColid(),
//								getProperty());
//			} else if (getColName() != null) {
//				if (getProperty() == null)
//				    outStr =
//							getValue(dataSet, dataSet.getRowid(), getColName());
//				else
//				    outStr =
//							getValue(
//								dataSet,
//								dataSet.getRowid(),
//								this.getColName(),
//								getProperty());
//				//outStr = dataSet.getString(dataSet.getRowid(), this.getColName());
//			} 
//			else if(this.content != null)
//				
//				outStr = getContent();
//			else
//				outStr = getValue(dataSet, dataSet.getRowid());
//			if(outStr == null && getDefaultValue() != null)
//				outStr = getValue(getDefaultValue());
//			if(outStr == null)
//			    outStr = null;
//			return outStr;
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
////			log.debug("error:" ,e);
//			return null;
//		}
		Object ret = getObjectValue();
		if(ret == null)
			return null;
		return String.valueOf(ret);
	}
	/**
	 * @return 返回属性索引
	 */
	public int getColid() {
		return this.colid;
	}
	/**
	 * @param colid
	 */
	public void setColid(int colid) {
		this.colid = colid;
	}
	/**
	 * @return String 返回属性名称
	 */
	public String getColName() {
		return colName;
	}
	/**
	 * @param string
	 */
	public void setColName(String string) {
		colName = string;
	}
	/**
	 * @return String
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param string
	 */
	public void setContent(String string) {
		content = string;
	}
	/**
	 * @return String
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

	/**
	 * Description:
	 * @return String

	 */
	public String getDateformat() {
		return dateformat;
	}

	/**
	 * Description:
	 * @return String

	 */
	public String getNumerformat() {
		return numerformat;
	}

	/**
	 * Description:
	 * @return void

	 */
	public void setDateformat(String string) {
		dateformat = string;
	}

	/**
	 * Description:
	 * @return void

	 */
	public void setNumerformat(String string) {
		numerformat = string;
	}

	//	protected Object getObject(PagerDataSet dataSet, int rowid, int colid) {
	//
	//		return dataSet.getValue(rowid, colid);
	//
	//	}
	//
	//	protected Object getObject(
	//		PagerDataSet dataSet,
	//		int rowid,
	//		int colid,
	//		String property) {
	//
	//			return dataSet.getValue(
	//				rowid,
	//				colid,
	//				property);
	//	}
	//
	//	protected Object getObject(
	//		PagerDataSet dataSet,
	//		int rowid,
	//		String colName,
	//		String Property) {
	//			return dataSet.getValue(
	//				rowid,
	//				colName,
	//				property);
	//	}
	//
	//	protected Object getObject(
	//		PagerDataSet dataSet,
	//		int rowid,
	//		String colName) {
	//			return dataSet.getValue(rowid, colName);
	//	}

	/**
	 * @return int
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
	 * Description:
	 * @return
	 * String
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setDefaultValue(Object string) {
		defaultValue = string;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public String getDecode() {
		return decode;
	}

	/**
	 * Description:
	 * @return
	 * String
	 */
	public String getEncode() {
		return encode;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setDecode(String string) {
		decode = string;
	}

	/**
	 * Description:
	 * @param string
	 * void
	 */
	public void setEncode(String string) {
		encode = string;
	}

    /**
     * @return Returns the expression.
     */
    public String getExpression() {
        return expression;
    }
    /**
     * @param expression The expression to set.
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }


	public int getMaxlength()
	{
		return maxlength;
	}


	public void setMaxlength(int maxlength)
	{
		this.maxlength = maxlength;
	}


	public String getReplace()
	{
		return replace;
	}


	public void setReplace(String replace)
	{
		this.replace = replace;
	}
	@Override
	public void doFinally()
	{
		this.pagerContext = null;
		this.t_formula = null;
		this.expression = null;
		this.htmlDecode = false;
		this.htmlEncode = false;
		this.publish = false;
		
		this.colid = -1;
		this.colName = null;
		this.content = null;
		this.dataSet = null;
		this.dateformat = null;
		this.decode = null;
		this.defaultValue = null;
		this.encode = null;
		this.index = -1;
		this.maxlength = -1;
		this.metafield = null;
		this.numerformat = null;
		this.property = null;
		this.replace = null;
		this.actual = null;
		this.actualseted = false;
		this.requestKey = null ;
		this.sessionKey= null ;
		this.pageContextKey= null ;
		this.parameter= null ;
		this.trim = false;
		this.usecurrentCellValuetoCellName = null;
		this.currentcelltoColName = false;
		this.currentDataSet = null;
		timeZone = null;
		this.locale = null;
		this.userRequestLocale = false;
		super.doFinally();
	}
	public int doEndTag() throws JspException
	{
		
		
		
		int ret = super.doEndTag();
		return ret;
		
	}


	public boolean isHtmlDecode() {
		return htmlDecode;
	}


	public void setHtmlDecode(boolean htmlDecode) {
		this.htmlDecode = htmlDecode;
	}


	public boolean isHtmlEncode() {
		return htmlEncode;
	}


	public void setHtmlEncode(boolean htmlEncode) {
		this.htmlEncode = htmlEncode;
	}
	
	public boolean isPublish() {
		return publish;
	}


	public void setPublish(boolean publish) {
		this.publish = publish;
	}
	public Object getActual()
	{
	
		return actual;
	}


	
	public void setActual(Object actual)
	{
		actualseted = true;
		this.actual = actual;
	}


	public int getEncodecount() {
		return encodecount;
	}


	public void setEncodecount(int encodecount) {
		this.encodecount = encodecount;
	}


	public boolean getTrim() {
		return trim;
	}


	public void setTrim(boolean trim) {
		this.trim = trim;
	}


	

	public boolean isCurrentcelltoColName() {
		return currentcelltoColName;
	}


	public void setCurrentcelltoColName(boolean currentcelltoColName) {
		this.currentcelltoColName = currentcelltoColName;
	}


	public String getUsecurrentCellValuetoCellName() {
		return usecurrentCellValuetoCellName;
	}


	public void setUsecurrentCellValuetoCellName(
			String usecurrentCellValuetoCellName) {
		this.usecurrentCellValuetoCellName = usecurrentCellValuetoCellName;
	}


	public String getLocale() {
		return locale;
	}


	public void setLocale(String locale) {
		this.locale = locale;
	}


	public boolean isUserRequestLocale() {
		return userRequestLocale;
	}


	public void setUserRequestLocale(boolean userRequestLocale) {
		this.userRequestLocale = userRequestLocale;
	}


	public String getTimeZone() {
		return timeZone;
	}


	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
