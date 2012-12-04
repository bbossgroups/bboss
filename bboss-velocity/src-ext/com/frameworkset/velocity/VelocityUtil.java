/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.velocity;



import java.io.StringWriter;

import org.apache.log4j.Logger;

import bboss.org.apache.velocity.VelocityContext;
import bboss.org.apache.velocity.app.Velocity;
import bboss.org.apache.velocity.exception.MethodInvocationException;
import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;

import com.frameworkset.util.SimpleStringUtil;


/**
 * <p> VelocityUtil.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-12-4 下午5:29:40
 * @author biaoping.yin
 * @version 1.0
 */
public class VelocityUtil {
	private static final Logger log = Logger.getLogger(VelocityUtil.class);
	private static boolean inited;
	public VelocityUtil() {
		// TODO Auto-generated constructor stub
	}
	public static  void init()
    {
    	if(inited)
    		return ;
    	inited = true;
    	 {
    		 
	    	 try {
	    		
	    	        
	    	       java.util.Properties pros =SimpleStringUtil.getProperties("/bboss-velocity.properties", VelocityUtil.class);

	             
	
	             //初始化velocity
	             try { 
	
	            	 Velocity.init(pros);
	                
	
	             } catch (Exception ex) {
	             	try {
	             	
	 					Velocity.init("bboss-velocity.properties");
	 					
	 					log.error("Init velocity failed velocity.properties："+ ex.getMessage(),ex) ;
	 				} catch (Exception e) {
	 				
	 					log.error("Init velocity failed velocity.properties "+ ex.getMessage(),e);
	 				}
	                
	             }
	            
	         } catch (Exception e) {
	        	
	             log.error("Init velocity failed:velocity.properties :"+ e.getMessage(),e);
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
    public static String evaluate(VelocityContext context, String template)
    {
    	init();
    	StringWriter out = new StringWriter();
    	try {
			Velocity.evaluate(context, out, template);
		} catch (ParseErrorException e) {
			e.printStackTrace();
			return template;
		} catch (MethodInvocationException e) {
			e.printStackTrace();
			return template;
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return template;
		}  catch (Exception e) {
			e.printStackTrace();
			return template;
		}
    	return out.toString();
    	
    }

}
