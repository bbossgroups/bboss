/*
 * Created on 2004-7-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.common.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:存放组合查询的条件
 * @author biaoping.yin
 * 2004-7-12
 */
public abstract class QueryVO implements Serializable 
{
    private Map conditions = new HashMap();
    private boolean builded = false;
    protected String queryString = "";
	public static final int EQUAL = 0;
	public static final int BIGTHAN = 1;
	public static final int SMALLTHAN= 2;
	public static final int LIKE = 3;

    public void clear()
    {
        if(!conditions.isEmpty())
            conditions.clear();
    }
    
    public void setCondition(String key,String value,int operation)
    	throws QueryVOException
    {
        if(key == null)
            throw new QueryVOException("setCondition Exception:key is null");
        conditions.put(key,value);
    }
    
    public String getCondition(String key)
		throws QueryVOException
	{
	    if(key == null)
	        throw new QueryVOException("getCondition Exception:key is null");
	    return (String)conditions.get(key);	    
	}
    
    /**
     *  构造查询串的抽象方法
     */
    protected abstract String buildQuery()
    	throws QueryVOException;    
    
    /**
     * 获取查询串  
     */
    public String getQueryString()
    	throws QueryVOException
    {
        if(!builded)
        {
            try
            {
               queryString =  buildQuery();
                builded = true;                
            }
            catch(QueryVOException e)
            {
                throw new QueryVOException("build query string Exception:" + e.getMessage());
            }            
        }        
        return queryString;        
    }
    
}
