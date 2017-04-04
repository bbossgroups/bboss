package org.frameworkset.spi.assemble;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.frameworkset.spi.CallContext;
/**
 * 对应java.util.Properties类型的容器
 * @author yinbp
 *
 * @param <K>
 * @param <V>
 */
public class ProProperties<K,V extends Pro> extends HashMap<K,V>{
	private boolean isfreeze = false;
    
    
    
    public void freeze()
    {
        this.isfreeze = true;
    }
    private boolean isFreeze()
    {
        
        return this.isfreeze;
    }
    
    private void modify() 
    {
        if(this.isFreeze())
            throw new CannotModifyException();
    }
 
  
    private java.util.Properties properties;
    private Object lock = new Object();
    private Properties _getProperties()
    {
    	Properties componentMap = new Properties();
    	
    	return componentMap;
    }
    public Properties getProperties(CallContext callcontext)
    {
    	 
    	if(properties == null)
    	{
    		synchronized(lock)
    		{
    			if(properties == null)
    			{
//    				
						properties = _getProperties( );
    					
    					Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
    					Iterator<java.util.Map.Entry<K, V>> iter = this.entrySet().iterator();
    					while(iter.hasNext())
    					{
    						Pro pro = iter.next().getValue();
    						try{	    							
    							properties.put(pro.getName(), pro.getProxyBean(callcontext));
    						}
    						finally
    						{
    							if(callcontext != null)
    								callcontext.setLoopContext(currentLoopContext);
    						}
    						
    					}
    					
    				}
    		}
 
    		
    	}
    	
    	return properties;
    }
    
   
    
	
}
