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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;

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
	private Map<String,MagicClass> magicclassesByName = new HashMap<String,MagicClass>();
	private Map<String,MagicClass> magicclassesByMagicNumber = new HashMap<String,MagicClass>();
	/**
	 * 默认序列化插件
	 */
	private Map<String,MagicClass> defaultPlugins = new HashMap<String,MagicClass>();
	private Map<String,MagicClass> defaultmagicclassesByMagicNumber = new HashMap<String,MagicClass>();
	
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		Iterator it = context.getPropertyKeys().iterator();
		while(it.hasNext())
		{
			String magicClasss = (String)it.next();
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
	}

	private MagicClass buildMagicClass(String preclazz )
	{
		MagicClass magicClass = new MagicClass();
		magicClass.setPreserial("org.frameworkset.soa.plugin.UnmodifiableRandomAccessListPreSerial");
		try {
			PreSerial preSerial = (PreSerial)Class.forName(preclazz).newInstance();
			 
			magicClass.setPreserialObject(preSerial);
			magicClass.setMagicclass(preSerial.getClazz());
			magicClass.setMagicnumber(preSerial.getClazz());
			this.defaultmagicclassesByMagicNumber.put(preSerial.getClazz(), magicClass);
			this.defaultPlugins.put(preSerial.getClazz(), magicClass);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			this.buildMagicClass(clazz);
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

}
