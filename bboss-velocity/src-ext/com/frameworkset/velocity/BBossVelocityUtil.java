package com.frameworkset.velocity;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.app.Velocity;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VelocityUtil;

public class BBossVelocityUtil {
	private static Logger log = Logger.getLogger(VelocityUtil.class);
	public static VelocityContext buildVelocityContext(Map<String,Object> context)
    {
    	VelocityContext context_ = new VelocityContext();
    	if(context != null && context.size() > 0)
    	{
    		Iterator<String> it = context.keySet().iterator();
    		while(it.hasNext())
    		{
    			String key = it.next();
    			context_.put(key, context.get(key));
    		}
    		
    	}
    	return context_;
    	
    }
	 /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(Map context,  String templateName, String template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return template;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return template;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			return template;
		}
    	return out.toString();
    	
    }
    private static boolean inited = false;
	private static Object lock = new Object();
    public static  void init(String approot)
    {
    	 if(inited)
    		 return;
    	 synchronized(lock)
    	 {
    		 if(inited)
        		 return;
	    	 try {
	    		 
	    	       java.util.Properties pros =SimpleStringUtil.getProperties("/bboss-velocity.properties", VelocityUtil.class);
	    	       
//	    	      
	             
	
	             //初始化velocity
	             try { 
	
	            	 Velocity.init(pros);
	                 inited = true;
	
//	                 BaseApplicationContext.addShutdownHook(new Runnable() {
//						
//						public void run()
//						{
//					
//							inited = false;
//							
//						}
//					});
	             } catch (Exception ex) {
	             	try {
	 					Velocity.init("bboss-velocity.properties");
	 					inited = true;
	 					log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),ex) ;
	 				} catch (Exception e) {
	 					inited = true;
	 					log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),e);
	 				}
	                
	             }
	            
	         } catch (Exception e) {
	        	 inited = true;
	             log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ e.getMessage(),e);
	         }
    	 }
    	 
    	 
    }
}
