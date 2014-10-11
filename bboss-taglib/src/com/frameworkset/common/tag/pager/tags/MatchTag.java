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

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.util.StringUtil;

/**
 * 所有用于分页/列表/详细信息标签逻辑判断的标签基类，
 * 具体的逻辑标签只需实现其中的抽象方法match()即可
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class MatchTag extends BaseValueTag {
	/**
	 * 检测值得类型是否与typeof对应的类型或者接口的类型一致，或者是其子类
	 * typeof可以是一个表示类路径的String，或者直接是一个Class对象
	 */
	protected Object typeof;
    /**实际值*/
	protected Object actualValue;
	
	/**期望值*/
	protected Object value;
	
	/**表达式，通过计算表达式的结果获取期望值*/
	protected String expressionValue;
	
	/**
	 * 控制是否将真值添加到该字段的枚举值中（扩展字段，生成报表时有用）
	 */
	protected boolean addTruevalue = false; 
	
	protected String pattern;
	
	/**
	 * in，notin标签匹配的范围枚举值
	 * 可以是变量表达式来指定，例如：{xxxscope}
	 */
	
	protected String scope;
	
	protected String[] scopes;
	
	/**
	 * 用于设置获取集合，字符串长度的变量名称
	 * <pg:notequal length="cell|request|parameter|session|pagecontext:rejectList" value="1">
	 * length属性值带有前缀cell|request|session|pagecontext:
	 * cell 从对象属性中获取属性值得长度
	 * request 从request属性中获取属性值得长度
	 * parameter 从request参数中获取属性值得长度
	 * session 从session属性中获取属性值得长度
	 * pagecontext 从pagecontext属性中获取属性值得长度
	 * 默认从cell中获取对象属性
	 * length属性适用于equal,notequal,upper,lower,upperequal,lowerequal,
	 * in,notin标签
	 */
	protected String length;

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	protected void evalLengthInfo()
	{
		
		String[] infos = length.split(":");
		if(infos.length == 1)
		{
			this.setColName(length);
			
		}
		else
		{
			String prefix = infos[0];
			if(prefix.equals("cell"))
			{
				this.setColName(infos[1]);
			}
			else if(prefix.equals("request"))
			{
				this.setRequestKey(infos[1]);
			}
			else if(prefix.equals("parameter"))
			{
				this.setParameter(infos[1]);
			}
			else if(prefix.equals("session"))
			{
				this.setSessionKey(infos[1]);
			}
			else if(prefix.equals("pagecontext"))
			{
				this.setPageContextKey(infos[1]);
			}
		}
		Object _actualValue = evaluateActualValue();
		actualValue = length(_actualValue);
		
	}
	
	
	/**
	 * @return String
	 */
	public Object getValue()
	{
		return value;
	}

	/**
	 * @param string
	 */
	public void setValue(Object string)
	{
		value = string;
	}
	
	public int doStartTag() throws JspException {
	    init();
//	    dataSet = searchDataSet(this,PagerDataSet.class);
//	    t_formula = dataSet.getFormula(getExpression());
	    if(StringUtil.isEmpty(length))
	    	actualValue = evaluateActualValue();
	    else
	    	evalLengthInfo();
	    
//		actualValue = getOutStr();
//		setMeta();
		//如果期望值为表达式，则求解表达式的值来得到期望值
		if(this.getExpressionValue() != null)
            try {
            	Object value_ = getFormulaValue(getExpressionValue());
            	if(value_ != null)
            	{
//            		value = String.valueOf(value_);
            		value = value_;
            	}
            	else
            	{
            		value = null;
            	}
            } catch (FormulaException e) {
                e.printStackTrace();
                return SKIP_BODY;                
            }
		if(match())		
			return EVAL_BODY_INCLUDE;
		else
			return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException 
	{
		int ret = super.doEndTag();
		this.actualValue = null;
		this.addTruevalue = false; 
		this.value = null;
		this.expressionValue = null;
		this.pattern = null;
		this.scope = null;
		this.scopes = null;
//		this.requestKey = null ;
//		this.sessionKey= null ;
//		this.pageContextKey= null ;
//		this.parameter= null ;
		
		return ret;
		
	}
	
	protected abstract boolean match();

    /**
     * @return Returns the addTruevalue.
     */
    public boolean isAddTruevalue() {
        return addTruevalue;
    }
    /**
     * @param addTruevalue The addTruevalue to set.
     */
    public void setAddTruevalue(boolean addTruevalue) {
        this.addTruevalue = addTruevalue;
    }
    /**
     * @return Returns the expressionValue.
     */
    public String getExpressionValue() {
        return expressionValue;
    }
    /**
     * @param expressionValue The expressionValue to set.
     */
    public void setExpressionValue(String expressionValue) {
        this.expressionValue = expressionValue;
    }

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String[] getScopes() {
		if(scopes == null)
			calculatescope();
		return scopes;
	}

	private void calculatescope() {
		Object temp = super.getFormulaValueWithAttribute(scope);
		if(temp != null)
		{
			this.scopes = StringUtil.split(temp.toString(),"\\,");
		}
		else
		{
			this.scopes = StringUtil.split(scope,"\\,");
		}
			
		
		
	}

	
	public Object getActualValue()
	{
	
		return actualValue;
	}

	
	public void setActualValue(Object actualValue)
	{
	
		this.actualValue = actualValue;
	}

	
	protected boolean istypeof()
	{
		if(typeof instanceof String)
		{
			try {
				Class clazz = Class.forName((String)typeof);
				if(clazz.isAssignableFrom(this.actualValue.getClass()))
				{
					return true;
				}
				else
					return false;
			} catch (Exception e) {
				throw new java.lang.IllegalArgumentException(typeof +"不存在或者不正确",e);
			}
		}
		else //if(typeof instanceof Class)
		{
			try {
				Class clazz =(Class)typeof;
				if(clazz.isAssignableFrom(this.actualValue.getClass()))
				{
					return true;
				}
				else
					return false;
			} catch (Exception e) {
				throw new java.lang.IllegalArgumentException(typeof +"不存在或者不正确,或者必须是Class类型的对象",e);
			}
		}
	}
	public Object getTypeof() {
		return typeof;
	}
	public void setTypeof(Object typeof) {
		this.typeof = typeof;
	}
	public void doFinally() {
		this.typeof = null;
		super.doFinally();
	}
}
