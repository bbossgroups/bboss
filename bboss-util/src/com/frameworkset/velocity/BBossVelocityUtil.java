package com.frameworkset.velocity;

import java.util.Map;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.runtime.resource.Resource;

import com.frameworkset.util.VelocityUtil;


public class BBossVelocityUtil {
	public static VelocityContext buildVelocityContext(Map<String,Object> context)
    {
    	return VelocityUtil.buildVelocityContext(context);
    	
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
//    	init(null);
//    	StringWriter out = new StringWriter();
//    	try {
//			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
//		} catch (ParseErrorException e) {
//			e.printStackTrace();
//			return template;
//		} catch (MethodInvocationException e) {
//			e.printStackTrace();
//			return template;
//		} catch (ResourceNotFoundException e) {
//			e.printStackTrace();
//			return template;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return template;
//		}
//    	return out.toString();
    	return VelocityUtil.evaluate(context, templateName, template);
    	
    }

    public static  void init(String approot)
    {
//    	 if(inited)
//    		 return;
//    	 synchronized(lock)
//    	 {
//    		 if(inited)
//        		 return;
//	    	 try {
//	    		 
//	    	       java.util.Properties pros =SimpleStringUtil.getProperties("/bboss-velocity.properties", BBossVelocityUtil.class);
//	    	       
////	    	      
//	             
//	
//	             //初始化velocity
//	             try { 
//	
//	            	 Velocity.init(pros);
//	                 inited = true;
//	
////	                 BaseApplicationContext.addShutdownHook(new Runnable() {
////						
////						public void run()
////						{
////					
////							inited = false;
////							
////						}
////					});
//	             } catch (Exception ex) {
////	            	 ex.printStackTrace();
//	            	 log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),ex) ;
//	             	try {
//	 					Velocity.init("bboss-velocity.properties");
//	 					inited = true;
//	 					
//	 				} catch (Exception e) {
//	 					inited = true;
//	 					log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),e);
//	 				}
//	                
//	             }
//	            
//	         } catch (Exception e) {
//	        	 inited = true;
//	             log.info("Init velocity failed from bboss-velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ e.getMessage(),e);
//	         }
//    	 }
    	VelocityUtil.init(approot);
    	 
    	 
    }
    public static void initTemplate(Resource template)
    {
//    	init(null);
//    	Velocity.initTemplate(template);
    	VelocityUtil.initTemplate(template);
    }
    
    public static void initTemplate(Resource template,String encoding)
    {
//    	init(null);
//    	Velocity.initTemplate(template, encoding);
    	VelocityUtil.initTemplate(template,encoding);
    }
}
