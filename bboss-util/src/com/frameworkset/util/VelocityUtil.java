package com.frameworkset.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
//import org.frameworkset.spi.BaseApplicationContext;


//
/**
 * <p>Title: </p> 
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: chinacreator</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class VelocityUtil implements Serializable{
	private static Logger log = Logger.getLogger(VelocityUtil.class);
	private static boolean inited = false;

//    static {
//    	init(null);
//    }
//    
	private static File computeApplicationDir(URL location, File defaultDir)
    {
        if (location == null)
        {
            System.out.println("Warning: Cannot locate the program directory. Assuming default.");
            return defaultDir;
        }
        if (!"file".equalsIgnoreCase(location.getProtocol()))
        {
            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(".");
        }
        String file = location.getFile();
        if (!file.endsWith(".jar") && !file.endsWith(".zip"))
        {
            try
            {
                return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getParentFile().getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {

            }

            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(location.getFile());
        }
        else
        {
            
            try
            {
                File path = null;//new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
                if(!isLinux())
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile().getParentFile();
                }
                else
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(5), "UTF-8")).getParentFile().getParentFile();
                }
//                System.out.println("path: " + path.getAbsolutePath());
//                System.out.println("location: " + location.getPath());
//                System.out.println("external from location: " + URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8"));
//                System.out.println("external from location + 6: " + URLDecoder.decode(location.toExternalForm(), "UTF-8"));
                
                return path;
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Warning: Unrecognized location type. Assuming default.");
        return new File(location.getFile());
    }
	/**
	 * determine the OS name
	 * 
	 * @return The name of the OS
	 */
	public static final String getOS() {
		return System.getProperty("os.name");
	}

	/**
	 * @return True if the OS is a Windows derivate.
	 */
	public static final boolean isWindows() {
		return getOS().startsWith("Windows");
	}

	/**
	 * @return True if the OS is a Linux derivate.
	 */
	public static final boolean isLinux() {
		return getOS().startsWith("Linux");
	}
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
	    		 if(approot == null)
	    		 {
	    			 Properties aop = SimpleStringUtil.getProperties(SimpleStringUtil.AOP_PROPERTIES_PATH, VelocityUtil.class);
	    			 if(aop != null)
	    			 {
	    				 String approot_ = aop.getProperty("approot");
	    				 if(approot_ != null && !approot_.equals(""))
	    				 {
	    					 approot = approot_;
	    				 }
	    			 }
	    		 }
	    		 else
	    		 {
	    			 File f = new File(approot);
	    			 if(!f.exists())
	    			 {
	    				 Properties aop = SimpleStringUtil.getProperties(SimpleStringUtil.AOP_PROPERTIES_PATH, VelocityUtil.class);
		    			 if(aop != null)
		    			 {
		    				 String approot_ = aop.getProperty("approot");
		    				 if(approot_ != null && !approot_.equals(""))
		    				 {
		    					 approot = approot_;
		    				 }
		    			 }
	    			 }
	    		 }
	//    		 URL location = (VelocityUtil.class).getProtectionDomain().getCodeSource().getLocation();
	//    	        File appDir = computeApplicationDir(location, new File("."));
	    		    String appDir = FileUtil.apppath + "/WEB-INF";
	    		    log.debug("FileUtil.apppath:"+FileUtil.apppath);
	    		    if(approot != null && !new File(appDir).exists())
	    		    {
	    		    	appDir = approot + "/WEB-INF";
	    		    }
	    		    
	    	        String templatePath = appDir + "/templates";
	    	        File templatePathFile = new File(templatePath);
	    	        if(!templatePathFile.exists())
	    	        {
	    	        	 Properties aop = SimpleStringUtil.getProperties(SimpleStringUtil.AOP_PROPERTIES_PATH, VelocityUtil.class);
		    			 if(aop != null)
		    			 {
		    				 String approot_ = aop.getProperty("approot");
		    				 if(approot_ != null && !approot_.equals(""))
		    				 {
		    					 templatePath = SimpleStringUtil.getRealPath(approot_, "/WEB-INF/templates");
		    				 }
		    			 }
	    	        }
//	    	        File configurationFile = new File(appDir, "/classes/velocity.properties");
//	    	        log.debug("configurationFile.getAbsolutePath():"+configurationFile.getAbsolutePath());
	    	        log.debug("velocity.properties:"+ VelocityUtil.class.getResource("/velocity.properties"));
	    	        log.debug("file.resource.loader.path:"+ templatePath);
	    	       java.util.Properties pros =SimpleStringUtil.getProperties("/velocity.properties", VelocityUtil.class);
	    	       
//	    	       pros.load(new java.io.FileInputStream(configurationFile));
	    	       pros.setProperty("file.resource.loader.path", templatePath);
	             
	
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
	 					Velocity.init("velocity.properties");
	 					inited = true;
	 					log.error("Init velocity failed:velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),ex) ;
	 				} catch (Exception e) {
	 					inited = true;
	 					log.error("Init velocity failed:velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ ex.getMessage(),e);
	 				}
	                
	             }
	            
	         } catch (Exception e) {
	        	 inited = true;
	             log.error("Init velocity failed:velocity.properties (系统找不到指定的文件。) 检查classpath中是否配置正确:"+ e.getMessage(),e);
	         }
    	 }
    	 
    	 
    }

    //private static Map templates;

    public static Template getTemplate(String templateName) {
        init(null);
        Template template = null;
        try {

            template = Velocity.getTemplate(templateName);

        } catch (ResourceNotFoundException rnfe) {
            //rnfe.printStackTrace();
           log.error("Example : error : cannot find template " +
                               templateName,rnfe);
        } catch (ParseErrorException pee) {
           // pee.printStackTrace();
            log.error("Example : Syntax error in template " +
                               templateName + ":" + pee,pee);
        } catch (Exception ex) {
            log.error("Example : Syntax error in template " +
                               templateName + ":" ,ex);
        }

        return template;
    }
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(VelocityContext context,  String templateName, String template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return template;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return template;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return template;
		} catch (IOException e) {
			e.printStackTrace();
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			return template;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(VelocityContext context,  Writer out,String templateName, String template)
    {
    	init(null);
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(VelocityContext context,  String templateName, InputStream template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return null;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return null;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(VelocityContext context,  Writer out,String templateName, InputStream template)
    {
    	init(null);
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(VelocityContext context,  String templateName, Reader template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return null;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return null;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(VelocityContext context,  Writer out,String templateName, Reader template)
    {
    	init(null);
    	try {
			Velocity.evaluate(context, out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
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
		} catch (IOException e) {
			e.printStackTrace();
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			return template;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(Map context,  Writer out,String templateName, String template)
    {
    	init(null);
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(Map context,  String templateName, InputStream template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return null;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return null;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(Map context,  Writer out,String templateName, InputStream template)
    {
    	init(null);
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static String evaluate(Map context,  String templateName, Reader template)
    {
    	init(null);
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return null;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return null;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    	return out.toString();
    	
    }
    
    
    /**
     * 字符串模板解析
     * @param context
     * @param templateName
     * @param template
     * @return
     */
    public static void evaluate(Map context,  Writer out,String templateName, Reader template)
    {
    	init(null);
    	try {
			Velocity.evaluate(buildVelocityContext( context), out, templateName, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
    	
    	
    }
    
    

    public static void main(String[] args) {
        VelocityUtil velocityutil = new VelocityUtil();
        VelocityContext fcontext = new VelocityContext();
     
      
    }
}
