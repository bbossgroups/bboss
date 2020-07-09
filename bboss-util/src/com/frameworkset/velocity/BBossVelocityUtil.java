package com.frameworkset.velocity;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.app.VelocityEngine;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.runtime.resource.Resource;
import com.frameworkset.util.VelocityUtil;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


public class BBossVelocityUtil {
	private static VelocityEngine dbEngine;

	private static VelocityEngine elasticEngine;
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

    	return VelocityUtil.evaluate(context, templateName, template);
    	
    }

    public static  void init(String approot)
    {

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

	public static void initDBTemplate(Resource template)
	{
		initDBEngine();
		dbEngine.initTemplate(template);
	}

	public static void initDBTemplate(Resource template,String encoding)
	{
		initDBEngine();
		dbEngine.initTemplate(template,encoding);
	}

	public static void initElasticTemplate(Resource template)
	{
		initElasticEngine();
		elasticEngine.initTemplate(template);
	}

	public static void initElasticTemplate(Resource template,String encoding)
	{
		initElasticEngine();
		elasticEngine.initTemplate(template,encoding);
	}
	private static Map<String,VelocityEngine> velocityEngineMap = new HashMap<String, VelocityEngine>();
	public static VelocityEngine getVelocityEngine(String configFile){
		VelocityEngine velocityEngine = velocityEngineMap.get(configFile);
		if(velocityEngine != null){
			return velocityEngine;
		}
		else{
			synchronized (velocityEngineMap) {
				velocityEngine = velocityEngineMap.get(configFile);
				if (velocityEngine == null) {
					velocityEngine = VelocityUtil.initVelocityEngine(configFile);
					velocityEngineMap.put(configFile,velocityEngine);
				}
			}
			return velocityEngine;
		}
	}

	private static void initElasticEngine(){
    	if(elasticEngine == null){
    		synchronized (BBossVelocityUtil.class){
				if(elasticEngine == null) {
					elasticEngine = VelocityUtil.initVelocityEngine("/bboss-elasticvelocity.properties");
				}
			}
		}
	}
	private static void initDBEngine(){
		if(dbEngine == null){
			synchronized (BBossVelocityUtil.class){
				if(dbEngine == null) {
					dbEngine = VelocityUtil.initVelocityEngine("/bboss-dbvelocity.properties");
				}
			}
		}
	}

	/**
	 * 字符串模板解析
	 * @param context
	 * @param templateName
	 * @param template
	 * @return
	 */
	public static String evaluateDB(Map context,  String templateName, String template)
	{
		initDBEngine();
		StringWriter out = new StringWriter();
		try {
			dbEngine.evaluate(buildVelocityContext( context), out, templateName, template);
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

	/**
	 * 字符串模板解析
	 * @param context
	 * @param templateName
	 * @param template
	 * @return
	 */
	public static String evaluateElastic(Map context,  String templateName, String template)
	{
		initElasticEngine();
		StringWriter out = new StringWriter();
		try {
			elasticEngine.evaluate(buildVelocityContext( context), out, templateName, template);
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


}
