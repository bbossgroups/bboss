package com.frameworkset.spi.assemble;

import java.lang.reflect.InvocationTargetException;


/**
 * 
 * <p>Title: BeanInstanceException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-9-17 下午04:04:28
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanInstanceException extends RuntimeException
{

    public BeanInstanceException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public BeanInstanceException(String message, Throwable cause)
    {	
        super(message, handleThrowable(cause) );
        // TODO Auto-generated constructor stub
    }
    public static Throwable handleThrowable(Throwable retval)  
	{
		if(retval == null)
			return null;
		if(retval instanceof java.lang.reflect.InvocationTargetException)
		{
			Throwable ret = ((java.lang.reflect.InvocationTargetException)retval).getTargetException();
			if(ret == null)
				return null;
			if(ret instanceof InvocationTargetException)
			{
				return handleThrowable(ret);
			}
			return ret;
		}
		
		return (Throwable)retval;
		
		
	}

    public BeanInstanceException(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public BeanInstanceException(Throwable cause)
    {
        super(handleThrowable(cause));
        // TODO Auto-generated constructor stub
    }

}
