/*
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

package org.frameworkset.soa;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Title: SerialFactory.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年5月23日 上午11:39:27
 * @author biaoping.yin
 * @version 1.0
 */
public class SerialFactory {
	private static Logger log = LoggerFactory.getLogger(SerialFactory.class);
	private Map<String,MagicClass> magicclassesByName = new HashMap<String,MagicClass>();
	private Map<String,MagicClass> magicclassesByMagicNumber = new HashMap<String,MagicClass>();
	/**
	 * 默认序列化插件
	 */
	private Map<String,MagicClass> defaultPlugins = new HashMap<String,MagicClass>();
	private Map<String,MagicClass> defaultmagicclassesByMagicNumber = new HashMap<String,MagicClass>();
	private static PluginFactory hibernatePluginFactory;
	static {
		try {
			Class<PluginFactory> hibernatePluginFactoryClass = (Class<PluginFactory>) Class.forName("org.frameworkset.hibernate.serial.HibernatePluginFactory");
			hibernatePluginFactory = hibernatePluginFactoryClass.newInstance();
		} catch (ClassNotFoundException e) {
		}
		catch (Throwable e) {
			log.debug("Init HibernatePluginFactory failed:",e);
		}
	}
	private static String defaultPluginNames[] = new String[]{
		"org.frameworkset.soa.plugin.UnmodifiableRandomAccessListPreSerial",
		"org.frameworkset.soa.plugin.SublistPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableCollectionPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableListPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableMapPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableSetPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableSortedMapPreSerial",
		"org.frameworkset.soa.plugin.UnmodifiableSortedSetPreSerial"
	};
	/**
	 * 在序列化对象的过程中默认忽略的异常清单，碰到异常就忽略并继续进行后续的序列化操作
	 */
	private   String defaultIgnoreExceptionNames[] = new String[]{
			"org.hibernate.LazyInitializationException"			 
		};
	
	private   String[] ignoreExceptionNames;
	
	private static SerialFactory serialFactory;
	public static SerialFactory getSerialFactory()
	{
		if(serialFactory != null)
			return serialFactory;
		synchronized(SerialFactory.class)
		{
			if(serialFactory == null)
			{
				SerialFactory serialFactory = new SerialFactory();
				serialFactory.initDefaultPlugins();
				serialFactory.init();
				SerialFactory.serialFactory = serialFactory;
			}
		}
		return serialFactory;
	}
	
	
	public static class MagicClass
	{
		private String magicnumber;

		private String magicclass;
		private String serial;
		private Serial serailObject;
		private String preserial;
		private PreSerial preserialObject;
		public Serial getSerailObject() {
			if(serial == null)
				return null;
			if(serailObject == null)
			{
				try {
					serailObject = (Serial)Class.forName(serial).newInstance();
				} catch (InstantiationException e) {
					log.warn("",e);
				} catch (IllegalAccessException e) {
					log.warn("",e);
				} catch (ClassNotFoundException e) {
					log.warn("",e);
				}
			}
			return serailObject;
		}
		
		public String getMagicnumber() {
			return magicnumber;
		}
		public void setMagicnumber(String magicnumber) {
			this.magicnumber = magicnumber;
		}
		public String getMagicclass() {
			return magicclass;
		}
		public void setMagicclass(String magicclass) {
			this.magicclass = magicclass;
		}
		public String getSerial() {
			return serial;
		}
		public void setSerial(String serial) {
			this.serial = serial;
		}

		public String getPreserial() {
			return preserial;
		}

		public PreSerial getPreserialObject() {
			if(preserial == null)
				return null;
			if(this.preserialObject == null)
			{
				try {
					preserialObject = (PreSerial)Class.forName(preserial).newInstance();
				} catch (InstantiationException e) {
					log.warn("",e);
				} catch (IllegalAccessException e) {
					log.warn("",e);
				} catch (ClassNotFoundException e) {
					log.warn("",e);
				}
			}
			return preserialObject;
		}

		public void setPreserial(String preserial) {
			this.preserial = preserial;
		}

		public void setPreserialObject(PreSerial preserialObject) {
			this.preserialObject = preserialObject;
		}
		
	}
	
	public SerialFactory() {
		// TODO Auto-generated constructor stub
	}
	public void init()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("org/frameworkset/soa/serialconf.xml");
		Iterator<String> it = context.getPropertyKeys().iterator();
		while(it.hasNext())
		{
			
			String magicClasss = it.next();
			if(!magicClasss.equals("ignoreExceptions"))
			{
				Pro magic = context.getProBean(magicClasss);
				String magicNumber = magic.getStringExtendAttribute("magic");
				String serial = magic.getStringExtendAttribute("serial");
				String preserial = magic.getStringExtendAttribute("preserial");
				MagicClass MagicClass = new MagicClass();
				MagicClass.setMagicclass(magicClasss);
				MagicClass.setSerial(serial);
				MagicClass.setMagicnumber(magicNumber);
				MagicClass.setPreserial(preserial);
				magicclassesByName.put(magicClasss, MagicClass);
				this.magicclassesByMagicNumber.put(magicNumber, MagicClass);
			}
			else
			{
				Pro magic = context.getProBean(magicClasss);
				String ignoreExceptions = (String)magic.getValue();
				
				if(ignoreExceptions != null && ignoreExceptions.trim().length() > 0)
				{
					ignoreExceptions= ignoreExceptions.trim();
					this.ignoreExceptionNames = ignoreExceptions.split("\n");
					for(int i =0 ; i < ignoreExceptionNames.length; i ++)
					{
						ignoreExceptionNames[i] = ignoreExceptionNames[i].trim();
					}
				}
			}
			
			
			
		}
	}

	private MagicClass buildMagicClass(String preclazz ,boolean debugInfo)
	{
		MagicClass magicClass = null;
//		magicClass.setPreserial("org.frameworkset.soa.plugin.UnmodifiableRandomAccessListPreSerial");
		
		try {
			
			PreSerial preSerial = (PreSerial)Class.forName(preclazz).newInstance();
			 magicClass = new MagicClass();
			 magicClass.setPreserial(preclazz);
			magicClass.setPreserialObject(preSerial);
			magicClass.setMagicclass(preSerial.getClazz());
			magicClass.setMagicnumber(preSerial.getClazz());
			this.defaultmagicclassesByMagicNumber.put(preSerial.getClazz(), magicClass);
			this.defaultPlugins.put(preSerial.getClazz(), magicClass);
		} catch (InstantiationException e) {
			if(debugInfo && log.isDebugEnabled())
			 log.debug("buildMagicClass ["+preclazz+"] InstantiationException:"+e.getMessage());
		} catch (IllegalAccessException e) {
			if(debugInfo && log.isDebugEnabled())
				log.debug("buildMagicClass ["+preclazz+"] IllegalAccessException:"+e.getMessage());
		} catch (ClassNotFoundException e) {
			if(debugInfo && log.isDebugEnabled())
				log.debug("buildMagicClass ["+preclazz+"] ClassNotFoundException:"+e.getMessage());
		}
		return magicClass;
	}
	/**
	 * 
	 */
	public void initDefaultPlugins()
	{
		for(String clazz:defaultPluginNames)
		{
			this.buildMagicClass(clazz,true);
		}
		if(hibernatePluginFactory != null && hibernatePluginFactory.getPlugins() != null){
			for(String clazz:hibernatePluginFactory.getPlugins())
			{
				this.buildMagicClass(clazz,false);
			}
		}
	}
	
	public String getMagicNumber(String className)
	{
		MagicClass magicClass = this.magicclassesByName.get(className);
		return magicClass.magicnumber;
	}
	
	public MagicClass getMagicClassByMagicNumber(String magicnumber)
	{
		MagicClass magicClass = this.magicclassesByMagicNumber.get(magicnumber);
		if(magicClass == null)
			magicClass = this.defaultmagicclassesByMagicNumber.get(magicnumber);
		return magicClass;
	}
	
	public MagicClass getMagicClass(String magicclassName)
	{
		MagicClass magicClass = this.magicclassesByName.get(magicclassName);
		if(magicClass == null)
			magicClass = this.defaultPlugins.get(magicclassName);
		return magicClass;
	}
	
	public MagicClass getMagicClass(Class magicclass)
	{
		String magicclassName = magicclass.getName();
		return getMagicClass( magicclassName);
	}
	
	public boolean isIgnoreException(Throwable e)
	{
		String name = e.getClass().getName();
		for(String iname : defaultIgnoreExceptionNames)
		{
			if(name.equals(iname))
				return true;
				
		}
		if(ignoreExceptionNames!= null )
		{
			for(String iname : ignoreExceptionNames)
			{
				if(name.equals(iname))
					return true;
					
			}
		}
		return false;
		
	}

}
