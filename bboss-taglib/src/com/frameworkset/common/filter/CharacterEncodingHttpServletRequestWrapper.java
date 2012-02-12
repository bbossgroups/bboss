package com.frameworkset.common.filter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CharacterEncodingHttpServletRequestWrapper
    extends HttpServletRequestWrapper{
	private Map<String,String[]> parameters = null;
	private Object lock = new Object();
    private String newecoding = null;
    private String oldEncoding = null;
    
    private static final String system_encoding = System.getProperty("sun.jnu.encoding");
    public static final String USE_MVC_DENCODE_KEY = "org.frameworkset.web.servlet.handler.HandlerUtils.USE_MVC_DENCODE_KEY";
    

    public CharacterEncodingHttpServletRequestWrapper(HttpServletRequest request, String encoding) {
        super(request);
        this.newecoding = encoding != null ? encoding:system_encoding;
        this.oldEncoding = request.getCharacterEncoding();
        parameters = new HashMap<String,String[]>();
        
    }

    public String getParameter(String name) {
    	
    	
    	
//    	String values[] =getParameterValues(name);
//    	if(values != null && values.length > 0)
//    		return values[0]; 
//    	else
//    		return null;
//    	String value_ = super.getParameter(name);  
//    	if(value_ == null)
//			return null;
//        try {
//            if ( (oldEncoding == null || isIOS88591(oldEncoding)) && value_ != null) {
//                return new String(value_.getBytes("iso-8859-1"), newecoding);
//            }
//            else
//            {
//                return value_;
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return value_;
//        }
    	String[] values = getParameterValues(name);
    	if(values == null || values.length == 0)
    		return null;
    	return values[0];
    }

    public String[] getParameterValues(String name) {
    	
    	  
        try {
        	
            
            String[] tempArray = parameters.get(name);
            if(tempArray != null)
            	return tempArray; 
            tempArray = super.getParameterValues(name);
            if ( tempArray == null  || tempArray.length == 0) {
                return tempArray;
            }
            
            if ( (oldEncoding == null || isIOS88591(oldEncoding)) )
            {
            	String[] clone = new String[tempArray.length];
            	Boolean userdecode = (Boolean)this.getAttribute(USE_MVC_DENCODE_KEY);
                for (int i = 0; i < tempArray.length; i++) {
                    if ( tempArray[i]!= null) {
                    	clone[i] = new String(tempArray[i].getBytes("iso-8859-1"), newecoding);
                    }
                    else
                    {
                    	clone[i] = tempArray[i];
                    }
                }
                parameters.put(name,clone);
                return clone;
            }
            else
            {
            	parameters.put(name,tempArray);
            	return tempArray;
            }
            
        }
        catch (Exception e) {
            e.printStackTrace();
            return super.getParameterValues(name) ;
        }
    }
    
    
//    public String[] getParameterValues(String name) {
//    	
//  	  
//        try {
//        	
//            
//            if(parameters == null)
//        	{
//        		synchronized(lock)
//        		{
//        			if(parameters == null)
//        			{
//        				parameters = new HashMap<String,String[]>();
//        			}
//        		}
//        	}
//            String[] tempArray = parameters.get(name);
//            if(tempArray != null)
//            	return tempArray; 
//            tempArray = super.getParameterValues(name);
//            if ( tempArray == null  || tempArray.length == 0) {
//                return tempArray;
//            }
//            
//            if ( (oldEncoding == null || isIOS88591(oldEncoding)) )
//            {
//            	String[] clone = new String[tempArray.length];
//            	Boolean userdecode = (Boolean)this.getAttribute(USE_MVC_DENCODE_KEY);
//                for (int i = 0; i < tempArray.length; i++) {
//                    if ( tempArray[i]!= null) {
//                    	if(userdecode == null || !userdecode.booleanValue())
//                    	{
//                    		clone[i] = new String(tempArray[i].getBytes("iso-8859-1"), newecoding);
//                    	}
//                    	else
//                    	{
//                    		clone[i] = new String(tempArray[i].getBytes("iso-8859-1"), "UTF-8");
////                    		clone[i] = URLEncoder.encode(tempArray[i],"UTF-8") ;
//                    		clone[i] = URLEncoder.encode(clone[i], "UTF-8") ;
//                    	}
//                    }
//                    else
//                    {
//                    	clone[i] = tempArray[i];
//                    }
//                }
//                parameters.put(name,clone);
//                return clone;
//            }
//            else
//            {
//            	parameters.put(name,tempArray);
//            	return tempArray;
//            }
//            
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return super.getParameterValues(name) ;
//        }
//    }

    private boolean isIOS88591(String endcoding) {
        endcoding = endcoding.toLowerCase();
        return endcoding.startsWith("iso") && (endcoding.indexOf("8859") != -1) && endcoding.endsWith("1");
    }
}
