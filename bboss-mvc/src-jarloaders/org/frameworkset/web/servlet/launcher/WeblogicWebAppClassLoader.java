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
package org.frameworkset.web.servlet.launcher;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//import weblogic.utils.classloaders.ChangeAwareClassLoader;
//import weblogic.utils.classloaders.ClassFinder;
//import weblogic.utils.classloaders.URLClassFinder;
//import weblogic.utils.classloaders.ZipClassFinder;

/**
 * <p>
 * Title: TomcatWebAppClassLoader.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-11-19
 * @author biaoping.yin
 * @version 1.0
 */
public class WeblogicWebAppClassLoader extends BaseAppServerClassLoader {
//	public void addRepository(String[] repositories) {
//		if (repositories == null || repositories.length == 0)
//			return;
//		WebappClassLoader classLoader_ = (WebappClassLoader) super.classLoader;
//		for (String reppath : repositories) {
//			loadJars(classLoader_, reppath);
//			System.out.println("add lib[" + reppath + "] to ClassLoader["
//					+ classLoader + "]");
//		}
//	}
//
//	private void loadJars(WebappClassLoader classLoader, String file) {
//		File jar = new File(file);
//		if (!jar.exists())
//			return;
//		if (jar.isFile())
//			try {
//				classLoader.addRepository("file:///" + jar.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else {
//			File[] jarfiles = jar.listFiles(new FileFilter() {
//				public boolean accept(File pathname) {
//					if (pathname.isFile()) {
//						String name = pathname.getName();
//						return ListenerUtil.isJarFile(name);
//					}
//					else
//					{
//						String name = pathname.getName();
//						return !ListenerUtil.isExecludeJarDir(name) ;
//					}
//				}
//			});
//
//			if (jarfiles == null || jarfiles.length == 0)
//				return;
//			for (File jarfile : jarfiles) {
//
//				if (jarfile.isFile()) {
//					try {
//						classLoader.addRepository("file:///"
//								+ jarfile.getCanonicalPath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					try {
//						loadJars(classLoader, jarfile.getCanonicalPath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		System.out.println("add lib[" + file + "] to ClassLoader["
//				+ classLoader + "]");
//	}
	
	protected Object buildZipClassFinder(File jar) throws IllegalArgumentException, SecurityException, InstantiationException, 
		IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Object zipClassFinder = Class.forName("weblogic.utils.classloaders.ZipClassFinder").getConstructor(File.class).newInstance(jar);
		return zipClassFinder;
	}
	
	protected Object buildURLClassFinder(String jar) throws IllegalArgumentException, SecurityException, InstantiationException, 
	IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException
	{
		Object urlClassFinder = Class.forName("weblogic.utils.classloaders.URLClassFinder").getConstructor(String.class).newInstance(jar);
		return urlClassFinder;
	}
	protected void addClassFinder(ClassLoader classLoader,Object classFinder) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Class findClass = Class.forName("weblogic.utils.classloaders.ClassFinder");
		Method addClassFinder = classLoader.getClass().getDeclaredMethod("addClassFinder", findClass);
		addClassFinder.invoke(classLoader, classFinder);
	}
	
	protected  void addRepository(ClassLoader classLoader,String jarfile) throws Exception
	{
	
////		weblogic.utils.classloaders.CodeGenClassFinder finder = null;
//		try
//		{
//			ClassFinder s;
////			ZipClassFinder urlClassFinder = new ZipClassFinder(new File(jarfile.substring("file:///".length())));
//			ChangeAwareClassLoader classLoader_ = (ChangeAwareClassLoader) classLoader;
//			if(jarfile.endsWith(".jar" ) || jarfile.endsWith(".zip" ))
//			{
//				ZipClassFinder urlClassFinder = new ZipClassFinder(new File(jarfile.substring("file:///".length())));
//				classLoader_.addClassFinder(urlClassFinder);
//			}
//			else//".dll",".lib",".so"
//			{
//				String file = jarfile.substring("file:///".length());
//				File file_ = new File(file);
//				
//				
//				URLClassFinder urlClassFinder = new URLClassFinder(file_.getParent());
//				classLoader_.addClassFinder(urlClassFinder);
//			}
//				
////			classLoader_.addClassFinder(urlClassFinder);
////			classLoader_.
//			
////			classLoader_.
//	//		classLoader_.addRepository(jarfile);
//			System.out.println("load custom Jars from Location:" + jarfile);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		if(jarfile.endsWith(".jar" ) || jarfile.endsWith(".zip" ))
		{

			Object zipClassFinder = buildZipClassFinder(new File(jarfile.substring("file:///".length())));
			addClassFinder(classLoader,zipClassFinder);
		}
		else//".dll",".lib",".so"
		{

			String file = jarfile.substring("file:///".length());
			File file_ = new File(file);
			Object urlClassFinder = buildURLClassFinder(file_.getParent());
			addClassFinder(classLoader,urlClassFinder);
		}			

		System.out.println("load custom Jars from Location:" + jarfile);
	}

	public boolean validate(ClassLoader classLoader) {
		try {
			if(classLoader.getClass().getName().equals("weblogic.utils.classloaders.ChangeAwareClassLoader"))
				return true;
			return false;
//			weblogic.utils.classloaders.ChangeAwareClassLoader classLoader_ = (weblogic.utils.classloaders.ChangeAwareClassLoader) classLoader;
//			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
