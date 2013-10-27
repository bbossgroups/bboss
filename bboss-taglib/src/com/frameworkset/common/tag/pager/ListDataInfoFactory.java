/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author biaoping.yin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ListDataInfoFactory {
	
	
	private static final Logger log = Logger.getLogger(ListDataInfoFactory.class);
	/**
	 * Describe what the  constructor does
	 * 
	 * 2004-6-22
	 * 
	 */
	private static HashMap map;
	static {
		ClassLoader classLoader = ListDataInfoFactory.class.getClassLoader();
		try 
		{
			URL url = classLoader.getResource("listdata.properties");
			Properties listProps = new Properties();
			if(map == null)
				map = new HashMap();
			if (url != null) 
			{
				InputStream is = url.openStream();
				listProps.load(is);
				is.close();
				Enumeration keys = listProps.keys();
				while(keys != null && keys.hasMoreElements())
				{
					String key = (String)keys.nextElement();
					String className = listProps.getProperty(key);
					map.put(key.trim(),className.trim());					
				}
			}
		}
		catch (Exception e) 
		{
			//:log
			log.info("not found resources file listdata.properties");			
			//e.printStackTrace();
		}			
	}

	public static DataInfo getDataInfo(String type)
	{
		DataInfo data = null;
		type = type.trim();
		try
		{
			data = (DataInfo)Class.forName(type).newInstance();
			return data;
			
		}
		catch(Exception e)
		{
			//:log
			log.info("class " + type + " not found in class path!!!");
			
		}
		if(map == null)
		{
			return null;
		}
		else
		{
			Object obj = map.get(type);
			/**
			 * 如果对应的类型没有加载进来，则可执行以下的代码加载，
			 * 如果类型没有在属性文件中配置，则提示到treedata.properties中进行配置
			 */
			if(obj == null)
			{
				try
				{
					ClassLoader classLoader = ListDataInfoFactory.class.getClassLoader();
					URL url = classLoader.getResource("listdata.properties");
					Properties listProps = new Properties();					
					if (url != null) 
					{
						InputStream is = url.openStream();
						listProps.load(is);
						is.close();					
						obj = listProps.getProperty(type);
						if(obj != null)		
						{
							map.put(type,obj);
						}
						
					}
					else
					{
						log.info("not found resources file listdata.properties");
						return null;
					}
				}				
				catch (Exception e) 
				{
					
					log.info("not found resources file listdata.properties");
					
					return null;
				}
			}
			if(obj != null)
			{
				
				String className = ((String)obj).trim();
				try
				{
					data = (DataInfo)Class.forName(className).newInstance();
				}
				catch(Exception e)
				{	
					log.info("class " + className + " not found in class path!!!");
					return null;
				}
			}
			if(data == null)
				log.info("type " + type + " not exist please check listdata.properties"); 
			return data;
		}
			
	}
}
