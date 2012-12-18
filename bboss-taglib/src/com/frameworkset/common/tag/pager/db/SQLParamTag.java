package com.frameworkset.common.tag.pager.db;

import com.frameworkset.common.poolman.SetSQLParamException;
import com.frameworkset.common.tag.BaseTag;

import javax.servlet.jsp.JspException;

public class SQLParamTag extends BaseTag{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5205212913867669568L;
	/**
     * 变量名称
     */
    private String name;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Object getValue()
    {
        return value;
    }
    public void setValue(Object value)
    {
        this.value = value;
    }
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    /**
     * 变量值
     */
    private Object value;
    /**
     * 变量类型
     */
    private String type;  
    
    private String dataformat = null;
    private String charset = null;
    
    public int doEndTag()throws JspException 
    {
        this.name = null;
        this.value = null;
        this.type = null;
        this.dataformat = null;
        this.charset = null;
        return EVAL_PAGE;
    }
    
    public int doStartTag() throws JspException {
        
    	SQLParamsContext paramsContext = (SQLParamsContext)findAncestorWithClass(this, SQLParamsContext.class);
        if(paramsContext != null)
        {
            try
            {
            	paramsContext.getSQLExecutor().addSQLParam(name, value,type,dataformat,charset);
                return EVAL_BODY_INCLUDE; 
            }
            catch (SetSQLParamException e)
            {
                throw new JspException(e);
            }
        }
        else
        {
        	throw new JspException("sqlparam tag must be contained in sqlparams tag or dbutil tag or statement tag or batch tag.");
        }
            
//        return EVAL_BODY_INCLUDE;
        
        
    }
	public String getDataformat() {
		return dataformat;
	}
	public void setDataformat(String dataformat) {
		this.dataformat = dataformat;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

}
