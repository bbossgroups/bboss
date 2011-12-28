/*
 * Created on 2004-7-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.common.bean;

/**
 * @author biaoping.yin	
 *2004-7-12
 */
public class QueryVOException extends Exception
{
    public QueryVOException(String msg)
    {
        super(msg);
    }
    
    public QueryVOException()
    {
        super();
    }
    
    public QueryVOException(String msg,Throwable throwable)
    {
        //super(msg,throwable);
    }
}
